#pragma once
#include "../models/Visitor.h"
#include "../models/File.h"
#include "../models/Directory.h"
#include <string>

class TreePrintVisitor : public Visitor {
    std::string result;
    int depth = 0;

    std::string indent() const {
        return std::string(depth * 2, ' ');
    }

public:
    void visitFile(File& file) override;
    void visitDirectory(Directory& directory) override;

    const std::string& getResult() const;
};