#pragma once
#include "FileSystemItem.h"
#include <vector>
#include <string>

class File : public FileSystemItem {
    std::string extension;
    int size;

public:
    File(std::string name, int size, std::string extension);

    bool matchesSearch(const std::string& query);
    void collectStats(int& fileCount, int& totalSize);

    std::string getFullName() const;
};