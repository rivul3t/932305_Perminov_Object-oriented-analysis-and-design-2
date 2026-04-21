#pragma once
#include "FileSystemItem.h"
#include <string>

class File : public FileSystemItem {
    int sizeKb;
    std::string extension;
    std::string content;

public:
    File(std::string name, int sizeKb, std::string extension, std::string content = "");

    int getSizeKb() const;
    const std::string& getExtension() const;
    const std::string& getContent() const;

    void accept(Visitor& visitor) override;
};