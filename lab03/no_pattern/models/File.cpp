#include "File.h"

File::File(std::string name, int size, std::string extension)
    : FileSystemItem(std::move(name)),
      extension(std::move(extension)),
      size(size) {}

bool File::matchesSearch(const std::string& query) {
    return name.find(query) != std::string::npos ||
           extension.find(query) != std::string::npos;
}

void File::collectStats(int& fileCount, int& totalSize) {
    fileCount++;
    totalSize += size;
}

std::string File::getFullName() const {
    return name + "." + extension;
}