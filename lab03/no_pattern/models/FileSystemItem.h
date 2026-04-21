#pragma once
#include <string>

class FileSystemItem {
protected:
    std::string name;

public:
    explicit FileSystemItem(std::string name)
        : name(std::move(name)) {}

    virtual ~FileSystemItem() = default;

    const std::string& getName() const {
        return name;
    }
};