#include "Directory.h"
#include "File.h"
#include <algorithm>

Directory::Directory(std::string name)
    : FileSystemItem(std::move(name)) {}

void Directory::add(const std::shared_ptr<FileSystemItem>& item) {
    children.push_back(item);
}

void Directory::search(const std::string& query,
                       std::vector<std::string>& result)
{
    if (name.find(query) != std::string::npos) {
        result.push_back(name + "/");
    }

    for (auto& child : children) {
        if (auto file = std::dynamic_pointer_cast<File>(child)) {
            if (file->matchesSearch(query)) {
                result.push_back(file->getFullName());
            }
        }
        else if (auto dir = std::dynamic_pointer_cast<Directory>(child)) {
            dir->search(query, result);
        }
    }
}

void Directory::collectStats(int& fileCount, int& totalSize)
{
    for (auto& child : children) {
        if (auto file = std::dynamic_pointer_cast<File>(child)) {
            file->collectStats(fileCount, totalSize);
        }
        else if (auto dir = std::dynamic_pointer_cast<Directory>(child)) {
            dir->collectStats(fileCount, totalSize);
        }
    }
}