#pragma once
#include <QMainWindow>
#include <memory>

class QTreeWidget;
class QTextEdit;
class QLineEdit;

class Directory;

class MainWindow : public QMainWindow {
    Q_OBJECT

    QTreeWidget* treeWidget;
    QTextEdit* output;
    QLineEdit* searchInput;
    QLineEdit* sizeInput;

    std::shared_ptr<Directory> rootDir;

    void buildDemoFs();
    void refreshTree();
    void fillTreeItem(class QTreeWidgetItem* parentItem,
                      const std::shared_ptr<class FileSystemItem>& item);

public:
    explicit MainWindow(QWidget* parent = nullptr);

private slots:
    void showTree();
    void showStats();
    void searchFiles();
    void showLargeFiles();
};