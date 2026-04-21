cmake_minimum_required(VERSION 3.16)
project(FileManagerVisitor LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(Qt6 REQUIRED COMPONENTS Widgets)

qt_standard_project_setup()

add_executable(FileManagerVisitor
    main.cpp
    models/FileSystemItem.cpp
    models/File.cpp
    models/Directory.cpp
    visitors/TreePrintVisitor.cpp
    visitors/FileStatisticsVisitor.cpp
    visitors/SearchVisitor.cpp
    ui/MainWindow.cpp
)

target_include_directories(FileManagerVisitor PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
)

target_link_libraries(FileManagerVisitor PRIVATE Qt6::Widgets)