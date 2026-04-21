#include "Directory.h"
#include "Visitor.h"
#include <utility>
#include <algorithm>

Directory::Directory(std::string name)
    : FileSystemItem(std::move(name)) {}

void Directory::add(const std::shared_ptr<FileSystemItem>& item) {
    item->setParent(this);
    children.push_back(item);
}

void Directory::remove(const std::shared_ptr<FileSystemItem>& item) {
    children.erase(
        std::remove(children.begin(), children.end(), item),
        children.end()
    );
}

const std::vector<std::shared_ptr<FileSystemItem>>& Directory::getChildren() const {
    return children;
}

void Directory::accept(Visitor& visitor) {
    visitor.visitDirectory(*this);
}