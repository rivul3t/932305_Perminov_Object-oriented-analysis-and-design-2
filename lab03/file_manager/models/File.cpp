#include "File.h"
#include "Visitor.h"
#include <utility>

File::File(std::string name, int sizeKb, std::string extension, std::string content)
    : FileSystemItem(std::move(name)),
      sizeKb(sizeKb),
      extension(std::move(extension)),
      content(std::move(content)) {}

int File::getSizeKb() const {
    return sizeKb;
}

const std::string& File::getExtension() const {
    return extension;
}

const std::string& File::getContent() const {
    return content;
}

void File::accept(Visitor& visitor) {
    visitor.visitFile(*this);
}