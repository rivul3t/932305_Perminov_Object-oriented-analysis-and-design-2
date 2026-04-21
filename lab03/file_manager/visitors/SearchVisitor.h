#pragma once
#include "../models/Visitor.h"
#include "../models/File.h"
#include "../models/Directory.h"
#include <vector>
#include <string>

class SearchVisitor : public Visitor {
    std::string query;

public:
    std::vector<std::string> results;

    explicit SearchVisitor(std::string query);

    void visitFile(File& file) override;
    void visitDirectory(Directory& directory) override;
};