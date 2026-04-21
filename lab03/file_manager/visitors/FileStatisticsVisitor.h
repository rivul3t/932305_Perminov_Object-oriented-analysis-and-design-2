#pragma once
#include "../models/Visitor.h"
#include "../models/File.h"
#include "../models/Directory.h"
#include <map>

class FileStatisticsVisitor : public Visitor {
public:
    int fileCount = 0;
    int directoryCount = 0;
    int totalSize = 0;
    std::map<std::string, int> extensions;

    void visitFile(File& file) override;
    void visitDirectory(Directory& directory) override;
};