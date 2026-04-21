#include "FileStatisticsVisitor.h"

void FileStatisticsVisitor::visitFile(File& file) {
    fileCount++;
    totalSize += file.getSizeKb();
    extensions[file.getExtension()]++;
}

void FileStatisticsVisitor::visitDirectory(Directory& directory) {
    directoryCount++;
    for (auto& child : directory.getChildren()) {
        child->accept(*this);
    }
}