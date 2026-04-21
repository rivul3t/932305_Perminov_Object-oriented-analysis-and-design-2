#pragma once
#include "FileSystemItem.h"
#include <vector>
#include <memory>

class File;
class Directory;

class Directory : public FileSystemItem {
    std::vector<std::shared_ptr<FileSystemItem>> children;

public:
    explicit Directory(std::string name);

    void add(const std::shared_ptr<FileSystemItem>& item);

    void search(const std::string& query, std::vector<std::string>& result);
    void collectStats(int& fileCount, int& totalSize);
};