#include "FileSystemItem.h"
#include "Directory.h"

FileSystemItem::FileSystemItem(std::string name)
    : name(std::move(name)),
      createdAt(std::chrono::system_clock::now()),
      modifiedAt(createdAt) {}

const std::string& FileSystemItem::getName() const {
    return name;
}

Directory* FileSystemItem::getParent() const {
    return parent;
}

void FileSystemItem::setParent(Directory* p) {
    parent = p;
}

std::string FileSystemItem::getPath() const {
    if (!parent) return "/" + name;
    return parent->getPath() + "/" + name;
}