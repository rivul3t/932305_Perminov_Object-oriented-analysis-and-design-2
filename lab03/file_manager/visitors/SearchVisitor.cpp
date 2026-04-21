#include "SearchVisitor.h"
#include <algorithm>

static std::string toLowerCopy(std::string s) {
    std::transform(s.begin(), s.end(), s.begin(), [](unsigned char c) {
        return static_cast<char>(std::tolower(c));
    });
    return s;
}

SearchVisitor::SearchVisitor(std::string query)
    : query(toLowerCopy(std::move(query))) {}

void SearchVisitor::visitFile(File& file) {
    auto name = toLowerCopy(file.getName());
    auto ext = toLowerCopy(file.getExtension());
    auto content = toLowerCopy(file.getContent());

    if (name.find(query) != std::string::npos ||
        ext.find(query) != std::string::npos ||
        content.find(query) != std::string::npos) {
        results.push_back(
            file.getPath() + "." + file.getExtension()
        );
    }
}

void SearchVisitor::visitDirectory(Directory& directory) {
    auto name = toLowerCopy(directory.getName());
    if (name.find(query) != std::string::npos) {
        results.push_back(directory.getPath());
    }

    for (auto& child : directory.getChildren()) {
        child->accept(*this);
    }
}