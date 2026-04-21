#include "TreePrintVisitor.h"

void TreePrintVisitor::visitFile(File& file) {
    result += indent() + "- " + file.getName() + "." + file.getExtension()
           + " (" + std::to_string(file.getSizeKb()) + " KB)\n";
}

void TreePrintVisitor::visitDirectory(Directory& directory) {
    result += indent() + "[" + directory.getName() + "]\n";
    depth++;
    for (auto& child : directory.getChildren()) {
        child->accept(*this);
    }
    depth--;
}

const std::string& TreePrintVisitor::getResult() const {
    return result;
}