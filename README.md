# OS Project

A simple mock operating system built to simulate basic desktop functionalities such as file management, window handling, and terminal interaction.

## Overview

This project is a lightweight desktop environment simulation developed using Java and JavaFX. It demonstrates core operating system concepts in a visual and interactive way, including managing files, opening windows, and navigating a desktop interface.

## Features

- Desktop interface with icons
- File and folder management (create, open, navigate)
- Window system (multiple draggable/resizable windows)
- Basic file explorer
- Terminal interface
- Context menus for user interaction
- Persistent file structure using JSON

## Tech Stack

- Java
- JavaFX
- FXML (for UI layout)
- CSS (for styling)

## Project Structure
```│
src/main/java/com/gummybear/
    ├── desktop/ # Core desktop components (windows, icons, explorer)
    ├── data/ # File system data handling
    ├── controllers/ # UI controllers
    └── App.java # Entry point
```

## How to Run

1. Clone the repository
2. Open the project in your IDE (e.g., IntelliJ or VS Code)
3. Make sure JavaFX is properly configured
4. Run `App.java`

## Notes

- This is a simulation project and does not interact with the real file system.
- Some features are simplified to focus on demonstrating concepts rather than full functionality.

## Future Improvements

- Improved UI/UX
- More terminal commands
- Better file handling and validation
- Drag-and-drop support

---
