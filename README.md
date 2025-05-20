# Tetris Game

A classic Tetris implementation in Java, featuring modern gameplay mechanics and a user-friendly interface.

## Features

- Classic Tetris gameplay
- Score tracking system
- Level progression
- Next piece preview
- Game over detection
- Modern UI design

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Visual Studio Code (recommended) or any Java IDE
- Java Extension Pack for VS Code (if using VS Code)

## Setup and Installation

1. Download the project files
2. Open the project in your preferred IDE

3. For VS Code users:
   - Install the "Extension Pack for Java" extension
   - The project includes launch configurations in `.vscode/launch.json`
   - Press F5 to run the game or use the Run and Debug panel

## How to Play

- Use arrow keys to control the falling pieces:
  - Left Arrow: Move piece left
  - Right Arrow: Move piece right
  - Down Arrow: Move piece down faster
  - Up Arrow: Rotate piece
- Complete lines to score points
- The game ends when pieces stack up to the top

## Project Structure

```
Tetris/
├── src/
│   └── TetrisGame.java    # Main game implementation
├── .vscode/
│   └── launch.json        # VS Code launch configurations
└── README.md             # This file
```

## Development

The project is built using Java and follows object-oriented programming principles. The main game logic is implemented in `TetrisGame.java`.

## License

This project is licensed under the MIT License.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request 