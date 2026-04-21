#include "MainWindow.h"

#include <QTreeWidget>
#include <QTextEdit>
#include <QLineEdit>
#include <QPushButton>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QWidget>
#include <QGroupBox>

#include "../model/Directory.h"
#include "../model/File.h"

MainWindow::MainWindow(QWidget* parent)
    : QMainWindow(parent)
{
    setWindowTitle("File Manager (NO VISITOR)");
    resize(1000, 600);

    buildDemoFs();

    auto* central = new QWidget(this);
    setCentralWidget(central);

    auto* layout = new QHBoxLayout(central);

    treeWidget = new QTreeWidget(this);
    treeWidget->setHeaderLabels({"Name"});
    layout->addWidget(treeWidget, 1);

    auto* right = new QVBoxLayout();
    layout->addLayout(right, 2);

    auto* box = new QGroupBox("Actions");

    auto* vbox = new QVBoxLayout(box);

    auto* btnTree = new QPushButton("Tree");
    auto* btnStats = new QPushButton("Stats");
    auto* btnSearch = new QPushButton("Search");
    auto* btnLarge = new QPushButton("Large Files");

    searchInput = new QLineEdit();
    searchInput->setPlaceholderText("search...");

    sizeInput = new QLineEdit("50");

    vbox->addWidget(btnTree);
    vbox->addWidget(btnStats);
    vbox->addWidget(searchInput);
    vbox->addWidget(btnSearch);
    vbox->addWidget(sizeInput);
    vbox->addWidget(btnLarge);

    right->addWidget(box);

    output = new QTextEdit();
    right->addWidget(output);

    connect(btnTree, &QPushButton::clicked, this, &MainWindow::showTree);
    connect(btnStats, &QPushButton::clicked, this, &MainWindow::showStats);
    connect(btnSearch, &QPushButton::clicked, this, &MainWindow::searchFiles);
    connect(btnLarge, &QPushButton::clicked, this, &MainWindow::showLargeFiles);

    refreshTree();
}

void MainWindow::refreshTree() {
    treeWidget->clear();
    fillTreeItem(nullptr, rootDir);
    treeWidget->expandAll();
}

void MainWindow::fillTreeItem(QTreeWidgetItem* parent,
    const std::shared_ptr<FileSystemItem>& item)
{
    auto* node = new QTreeWidgetItem();

    if (auto dir = std::dynamic_pointer_cast<Directory>(item)) {
        node->setText(0, QString::fromStdString(dir->getName() + "/"));

        if (parent) parent->addChild(node);
        else treeWidget->addTopLevelItem(node);

        for (auto& child : dir->getChildren()) {
            fillTreeItem(node, child);
        }
    }
    else if (auto file = std::dynamic_pointer_cast<File>(item)) {
        node->setText(0, QString::fromStdString(file->getFullName()));

        if (parent) parent->addChild(node);
        else treeWidget->addTopLevelItem(node);
    }
}

void MainWindow::buildDemoFs() {
    rootDir = std::make_shared<Directory>("root");

    auto docs = std::make_shared<Directory>("docs");
    docs->add(std::make_shared<File>("report", 40, "md"));
    docs->add(std::make_shared<File>("notes", 10, "txt"));

    auto src = std::make_shared<Directory>("src");
    src->add(std::make_shared<File>("main", 20, "cpp"));

    rootDir->add(docs);
    rootDir->add(src);
}

void MainWindow::searchFiles() {
    std::vector<std::string> results;

    rootDir->search(searchInput->text().toStdString(), results);

    QString text;
    for (auto& r : results) {
        text += QString::fromStdString(r) + "\n";
    }

    output->setText(text);
}

void MainWindow::showStats() {
    int count = 0, size = 0;

    rootDir->collectStats(count, size);

    QString text;
    text += "Files: " + QString::number(count) + "\n";
    text += "Size: " + QString::number(size);

    output->setText(text);
}

void MainWindow::showTree() {
    std::string result;

    buildTreeString(rootDir, result);

    output->setText(QString::fromStdString(result));
}

static void buildTreeString(const std::shared_ptr<FileSystemItem>& item,
                            std::string& result,
                            int depth = 0)
{
    std::string indent(depth * 2, ' ');

    if (auto dir = std::dynamic_pointer_cast<Directory>(item)) {
        result += indent + "[" + dir->getName() + "]\n";

        for (auto& child : dir->getChildren()) {
            buildTreeString(child, result, depth + 1);
        }
    }
    else if (auto file = std::dynamic_pointer_cast<File>(item)) {
        result += indent + "- " + file->getFullName() + "\n";
    }
}