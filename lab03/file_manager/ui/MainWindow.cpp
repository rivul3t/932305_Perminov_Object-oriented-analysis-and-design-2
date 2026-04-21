#include "MainWindow.h"

#include <QTreeWidget>
#include <QInputDialog>
#include <QTextEdit>
#include <QLineEdit>
#include <QPushButton>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QWidget>
#include <QLabel>
#include <QGroupBox>

#include "../models/Directory.h"
#include "../models/File.h"
#include "../visitors/TreePrintVisitor.h"
#include "../visitors/FileStatisticsVisitor.h"
#include "../visitors/SearchVisitor.h"
//#include "../visitors/LargeFilesVisitor.h"

MainWindow::MainWindow(QWidget* parent)
    : QMainWindow(parent) {
    setWindowTitle("Virtual File Manager");
    resize(1100, 700);

    setStyleSheet(R"(
        QWidget {
            background-color: #1e1e1e;
            color: #dddddd;
            font-size: 14px;
        }

        QTreeWidget {
            background-color: #252526;
            border: 1px solid #333;
        }

        QTextEdit {
            background-color: #1e1e1e;
            border: 1px solid #333;
        }

        QPushButton {
            background-color: #3a3d41;
            border-radius: 6px;
            padding: 6px;
        }

        QPushButton:hover {
            background-color: #505357;
        }

        QLineEdit {
            background-color: #2d2d2d;
            border: 1px solid #444;
            padding: 4px;
        }
    )");

    buildDemoFs();

    auto* central = new QWidget(this);
    setCentralWidget(central);

    auto* mainLayout = new QHBoxLayout(central);

    treeWidget = new QTreeWidget(this);
    treeWidget->setHeaderLabels({"Name", "Type", "Path"});
    mainLayout->addWidget(treeWidget, 1);

    auto* rightLayout = new QVBoxLayout();
    mainLayout->addLayout(rightLayout, 2);

    auto* buttonsBox = new QGroupBox("Actions", this);
    auto* buttonsLayout = new QVBoxLayout(buttonsBox);

    auto* btnTree = new QPushButton("Show tree", this);
    auto* btnStats = new QPushButton("Statistics", this);
    auto* btnSearch = new QPushButton("Search", this);
    //auto* btnLarge = new QPushButton("Large files", this);

    auto* btnAddFile = new QPushButton("Add File", this);
    auto* btnAddDir = new QPushButton("Add Folder", this);
    auto* btnDelete = new QPushButton("Delete", this);


    searchInput = new QLineEdit(this);
    searchInput->setPlaceholderText("search query");

    sizeInput = new QLineEdit(this);
    sizeInput->setPlaceholderText("min size KB");
    sizeInput->setText("50");

    buttonsLayout->addWidget(btnTree);
    buttonsLayout->addWidget(btnStats);
    buttonsLayout->addWidget(searchInput);
    buttonsLayout->addWidget(btnSearch);
    buttonsLayout->addWidget(sizeInput);
    //buttonsLayout->addWidget(btnLarge);
        
    buttonsLayout->addWidget(btnAddFile);
    buttonsLayout->addWidget(btnAddDir);
    buttonsLayout->addWidget(btnDelete);

    rightLayout->addWidget(buttonsBox);

    output = new QTextEdit(this);
    output->setReadOnly(true);
    rightLayout->addWidget(output, 1);

    connect(btnTree, &QPushButton::clicked, this, &MainWindow::showTree);
    connect(btnStats, &QPushButton::clicked, this, &MainWindow::showStats);
    connect(btnSearch, &QPushButton::clicked, this, &MainWindow::searchFiles);
    //connect(btnLarge, &QPushButton::clicked, this, &MainWindow::showLargeFiles);

    connect(btnAddFile, &QPushButton::clicked, this, &MainWindow::addFile);
    connect(btnAddDir, &QPushButton::clicked, this, &MainWindow::addDirectory);
    connect(btnDelete, &QPushButton::clicked, this, &MainWindow::deleteItem);

    refreshTree();
    showTree();
}

void MainWindow::buildDemoFs() {
    rootDir = std::make_shared<Directory>("workspace");

    auto docs = std::make_shared<Directory>("docs");
    docs->add(std::make_shared<File>("report", 42, "md", "Lab report about Visitor"));
    docs->add(std::make_shared<File>("notes", 18, "txt", "Some notes"));

    auto src = std::make_shared<Directory>("src");
    src->add(std::make_shared<File>("main", 12, "cpp", "int main() {}"));
    src->add(std::make_shared<File>("model", 24, "hpp", "class FileSystemItem"));

    auto assets = std::make_shared<Directory>("assets");
    assets->add(std::make_shared<File>("logo", 120, "png", "binary"));

    rootDir->add(docs);
    rootDir->add(src);
    rootDir->add(assets);
}

void MainWindow::refreshTree() {
    treeWidget->clear();
    fillTreeItem(nullptr, rootDir);
    treeWidget->expandAll();
}

void MainWindow::fillTreeItem(QTreeWidgetItem* parentItem, const std::shared_ptr<FileSystemItem>& item) {
    auto* node = new QTreeWidgetItem();

    if (auto dir = std::dynamic_pointer_cast<Directory>(item)) {
        node->setText(0, QString::fromStdString(dir->getName() + "/"));
        node->setText(1, "Directory");
        node->setText(2, QString::fromStdString(dir->getPath()));
        if (parentItem) parentItem->addChild(node);
        else treeWidget->addTopLevelItem(node);

        for (const auto& child : dir->getChildren()) {
            fillTreeItem(node, child);
        }
    } else if (auto file = std::dynamic_pointer_cast<File>(item)) {
        node->setText(0, QString::fromStdString(file->getName() + "." + file->getExtension()));
        node->setText(1, "File");
        node->setText(2, QString::fromStdString(file->getPath()));
        if (parentItem) parentItem->addChild(node);
        else treeWidget->addTopLevelItem(node);
    }
}

void MainWindow::showTree() {
    TreePrintVisitor visitor;
    rootDir->accept(visitor);
    output->setPlainText(QString::fromStdString(visitor.getResult()));
}

void MainWindow::showStats() {
    FileStatisticsVisitor visitor;
    rootDir->accept(visitor);

    QString text;
    text += "Folders: " + QString::number(visitor.directoryCount) + "\n";
    text += "Files: " + QString::number(visitor.fileCount) + "\n";
    text += "Total size: " + QString::number(visitor.totalSize) + " KB\n\n";
    text += "Extensions:\n";

    for (const auto& [ext, count] : visitor.extensions) {
        text += QString::fromStdString("." + ext) + ": " + QString::number(count) + "\n";
    }

    output->setPlainText(text);
}

void MainWindow::searchFiles() {
    SearchVisitor visitor(searchInput->text().toStdString());
    rootDir->accept(visitor);

    QString text = "Search results:\n";
    for (const auto& s : visitor.results) {
        text += QString::fromStdString(s) + "\n";
    }
    output->setPlainText(text);
}

//void MainWindow::showLargeFiles() {
//    bool ok = false;
//    int minSize = sizeInput->text().toInt(&ok);
//    if (!ok) {
//        output->setPlainText("Invalid size value");
//        return;
//    }
//
//    LargeFilesVisitor visitor(minSize);
//    rootDir->accept(visitor);
//
//    QString text = "Large files:\n";
//    for (const auto& s : visitor.results) {
//        text += QString::fromStdString(s) + "\n";
//    }
//    output->setPlainText(text);
//}

void MainWindow::addFile() {
    auto parentItem = getSelectedItem(treeWidget, rootDir);

    auto dir = std::dynamic_pointer_cast<Directory>(parentItem);
    if (!dir) {
        if (parentItem->getParent())
            dir = std::shared_ptr<Directory>(parentItem->getParent(), [](Directory*){});
        else
            return;
    }

    bool ok;
    QString name = QInputDialog::getText(this, "File name", "Enter name:", QLineEdit::Normal, "", &ok);
    if (!ok || name.isEmpty()) return;

    int size = QInputDialog::getInt(this, "Size", "Enter size (KB):", 10);

    auto file = std::make_shared<File>(name.toStdString(), size, "txt");
    dir->add(file);

    refreshTree();
}

void MainWindow::addDirectory() {
    auto parentItem = getSelectedItem(treeWidget, rootDir);

    auto dir = std::dynamic_pointer_cast<Directory>(parentItem);
    if (!dir) return;

    bool ok;
    QString name = QInputDialog::getText(this, "Folder name", "Enter name:", QLineEdit::Normal, "", &ok);
    if (!ok || name.isEmpty()) return;

    dir->add(std::make_shared<Directory>(name.toStdString()));

    refreshTree();
}

void MainWindow::deleteItem() {
    auto item = getSelectedItem(treeWidget, rootDir);

    if (item == rootDir) return;

    auto parent = item->getParent();
    if (!parent) return;

    parent->remove(item);

    refreshTree();
}

std::shared_ptr<FileSystemItem>
MainWindow::getSelectedItem(QTreeWidget* tree,
                            const std::shared_ptr<Directory>& root)
{
    auto items = tree->selectedItems();
    if (items.empty()) return root;

    QString path = items[0]->text(2);
    std::string target = path.toStdString();

    std::function<std::shared_ptr<FileSystemItem>(std::shared_ptr<FileSystemItem>)> find;

    find = [&](std::shared_ptr<FileSystemItem> item)
        -> std::shared_ptr<FileSystemItem>
    {
        if (item->getPath() == target)
            return item;

        if (auto dir = std::dynamic_pointer_cast<Directory>(item)) {
            for (auto& child : dir->getChildren()) {
                auto res = find(child);
                if (res) return res;
            }
        }
        return nullptr;
    };

    return find(root);
}