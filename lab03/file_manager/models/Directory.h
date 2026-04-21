#pragma once
#include "FileSystemItem.h"
#include <vector>
#include <memory>

class Directory : public FileSystemItem {
    std::vector<std::shared_ptr<FileSystemItem>> children;

public:
    explicit Directory(std::string name);

    void add(const std::shared_ptr<FileSystemItem>& item);
    void remove(const std::shared_ptr<FileSystemItem>& item);
    const std::vector<std::shared_ptr<FileSystemItem>>& getChildren() const;

    void accept(Visitor& visitor) override;
};