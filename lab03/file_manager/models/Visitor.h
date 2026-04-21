#pragma once

class File;
class Directory;

class Visitor {
public:
    virtual ~Visitor() = default;
    virtual void visitFile(File& file) = 0;
    virtual void visitDirectory(Directory& directory) = 0;
};