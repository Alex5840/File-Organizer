# Smart File Organizer (Hybrid Utility)

## 1. Problem Statement
Manual file management is time-consuming. This utility provides a "one-click" solution to organize messy folders into categorized sub-folders (Images, Documents, Code, etc.) based on file extensions.

## 2. How the Program Works
* **Scan:** The program reads all files in the target directory using Java's `Files.list()` stream.
* **Identify:** It extracts the extension of each file.
* **Map:** It looks up the extension in a pre-defined `HashMap` to find the correct destination category.
* **Move:** It creates the destination folder (if missing) and moves the file using an atomic `Files.move()` operation.

## 3. Design Choices & Assumptions
* **Hybrid Mode:** I implemented a dual-mode system. If the user provides a path in the terminal, it runs as a CLI. If no path is provided, it launches a Swing GUI.
* **Standard Library:** Built entirely with JDK standard libraries (`javax.swing` and `java.nio`), requiring no external dependencies.
* **Safety:** The program only processes regular files and ignores existing directories to prevent data loss or accidental restructuring.

## 4. How to Run
1. Compile: `javac FileOrganizer.java`
2. Launch GUI: `java FileOrganizer`
3. Launch CLI: `java FileOrganizer "yourName/folder/path"`