#pragma once
#include <string>
#include <memory>
#include <chrono>

class Visitor;
class Directory;

class FileSystemItem {
protected:
    std::string name;
    std::chrono::system_clock::time_point createdAt;
    std::chrono::system_clock::time_point modifiedAt;
    Directory* parent = nullptr;

public:
    explicit FileSystemItem(std::string name);
    virtual ~FileSystemItem() = default;

    const std::string& getName() const;
    Directory* getParent() const;
    void setParent(Directory* p);

    std::string getPath() const;

    virtual void accept(Visitor& visitor) = 0;
};