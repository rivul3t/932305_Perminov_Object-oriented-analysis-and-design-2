#include "../model/Directory.h"
#include "../model/File.h"
#include <iostream>

int main() {
    auto root = std::make_shared<Directory>("root");

    auto docs = std::make_shared<Directory>("docs");
    docs->add(std::make_shared<File>("report", 40, "md"));
    docs->add(std::make_shared<File>("notes", 10, "txt"));

    root->add(docs);

    std::vector<std::string> results;
    root->search("report", results);

    std::cout << "Search:\n";
    for (auto& r : results) {
        std::cout << r << "\n";
    }

    int count = 0, size = 0;
    root->collectStats(count, size);

    std::cout << "\nStats:\n";
    std::cout << "Files: " << count << "\n";
    std::cout << "Size: " << size << "\n";

    return 0;
}