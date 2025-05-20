import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TetrisGame extends JPanel {



    private static final int CELL_SIZE = 30;
    private static final int ROWS = 25;
    private static final int COLS = 15;
    private static final int EMPTY_CELL = 0;

    private int[][] grid;
    private TetrisPiece currentPiece;
    private TetrisPiece nextPiece;



    private Timer timer;
    private int score;
    private int numCheckedRows;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private int level;
    private int speed;


    public TetrisGame() {
        grid = new int[ROWS][COLS];
        timer = new Timer(1000, new GameLoop());
        score = 0;

        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });

        startGame();
    }

    private void startGame() {
        level = 1;
        speed = 1000; // Initial speed in milliseconds
        clearGrid();
        spawnNewPiece();
        timer.start();
        setLayout(new BorderLayout());

        // Create and initialize the score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(scoreLabel, BorderLayout.NORTH );
        timer = new Timer(speed, new GameLoop());

        levelLabel = new JLabel("Level: " + level);
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(levelLabel, BorderLayout.SOUTH);    }

    private void clearGrid() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = EMPTY_CELL;
            }
        }

    }

    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                currentPiece.moveLeft(this);
                break;
            case KeyEvent.VK_RIGHT:
                currentPiece.moveRight(this);
                break;
            case KeyEvent.VK_DOWN:
                currentPiece.moveDown(this);
                break;
            case KeyEvent.VK_UP:
                currentPiece.rotate(this);
                break;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the next piece
        if (nextPiece != null) {
            int[][] nextShape = nextPiece.getShape();
            int nextType = nextPiece.getType();

            for (int row = 0; row < nextShape.length; row++) {
                for (int col = 0; col < nextShape[row].length; col++) {
                    if (nextShape[row][col] != 0) {
                        int cellCol = col + 1; // Offset to position the next piece
                        int cellRow = row + 1; // Offset to position the next piece
                        drawCell(g, cellCol, cellRow, nextType);
                    }
                }
            }
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int cellValue = grid[row][col];
                if (cellValue != EMPTY_CELL) {
                    drawCell(g, col, row, cellValue);
                }
            }
        }

        if (currentPiece != null) {
            int[][] pieceShape = currentPiece.getShape();
            int pieceRow = currentPiece.getRow();
            int pieceCol = currentPiece.getCol();

            for (int row = 0; row < pieceShape.length; row++) {
                for (int col = 0; col < pieceShape[row].length; col++) {
                    if (pieceShape[row][col] != 0) {
                        int cellRow = pieceRow + row;
                        int cellCol = pieceCol + col;
                        drawCell(g, cellCol, cellRow, currentPiece.getType());
                    }
                }
            }
        }
    }

    private void drawCell(Graphics g, int col, int row, int value) {
        Color color = getCellColor(value);

        g.setColor(color);
        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private Color getCellColor(int value) {
        switch (value) {
            case 1:
                return Color.CYAN;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.MAGENTA;
            case 4:
                return Color.ORANGE;
            case 5:
                return Color.BLUE;
            case 6:
                return Color.GREEN;
            case 7:
                return Color.RED;
            default:
                return Color.GRAY;
        }
    }

    private class GameLoop implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isGameOver()) {

                if (currentPiece.canMoveDown(TetrisGame.this)) {
                    currentPiece.moveDown(TetrisGame.this);
                }  else {
                        placePieceOnGrid();
                        clearLines();
                        spawnNewPiece();

                    }

                repaint();

                // Increase speed based on level and score
                if (score >= level * 50 || numCheckedRows >= level * 3) {
                    level++;
                    speed = speed * speed;
                    speed -= 100; // Decrease speed by 100 milliseconds
                    if (speed >= 0)
                        timer.setDelay(speed);
                    else {
                        timer.setDelay(-speed);
                    }
                    levelLabel.setText("Level: " + level); // Update the level label

                }
            } else {
                timer.stop();
                scoreLabel.setText("Game Over! Your score is: " + score); // Update the score label
                JOptionPane.showMessageDialog(TetrisGame.this, "Game Over! Your score is: " + score);
            }
        }

    }
    private void placePieceOnGrid() {
        int[][] pieceShape = currentPiece.getShape();
        int pieceRow = currentPiece.getRow();
        int pieceCol = currentPiece.getCol();

        for (int row = 0; row < pieceShape.length; row++) {
            for (int col = 0; col < pieceShape[row].length; col++) {
                if (pieceShape[row][col] != 0) {
                    int cellRow = pieceRow + row;
                    int cellCol = pieceCol + col;
                    grid[cellRow][cellCol] = currentPiece.getType();
                }
            }
        }
    }

    private void eraseCompletedLines() {
        int destRow = ROWS - 1;

        for (int srcRow = ROWS - 1; srcRow >= 0; srcRow--) {
            if (!isLineComplete(srcRow)) {
                copyRow(srcRow, destRow);
                destRow--;
            }
        }

        clearRemainingRows(destRow);
    }

    private void copyRow(int srcRow, int destRow) {
        System.arraycopy(grid[srcRow], 0, grid[destRow], 0, COLS);
    }

    private void clearRemainingRows(int startRow) {
        for (int row = startRow; row >= 0; row--) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = EMPTY_CELL;
            }
        }
    }
    private void clearLines() {
        numCheckedRows = 0;

        for (int row = ROWS - 1; row >= 0; row--) {
            if (isLineComplete(row)) {
                numCheckedRows++;
            }
        }
        if (numCheckedRows > 0) {
            eraseCompletedLines();
            int lineScore = 10 * numCheckedRows * level;
            score += lineScore;
            scoreLabel.setText("Score: " + score); // Update the score label
        }

    }

    private boolean isLineComplete(int row) {
        for (int col = 0; col < COLS; col++) {
            if (grid[row][col] == EMPTY_CELL) {
                return false;
            }
        }
        return true;
    }

    private void shiftRowsDown(int row) {
        for (int i = row; i >= 1; i--) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = grid[i - 1][j];
            }
        }
        for (int j = 0; j < COLS; j++) {
            grid[0][j] = EMPTY_CELL;
        }
    }

    private void spawnNewPiece() {
        Random random = new Random();
        int type = random.nextInt(7) + 1; // Generate a random type from 1 to 7
        int[][] shape = TetrisPiece.SHAPES[type - 1]; // Subtract 1 to match array index

        int initialCol = (COLS - shape[0].length) / 2;
        int initialRow = 0;

        currentPiece = new TetrisPiece(type, shape, initialRow, initialCol);
        // Generate the next piece
        int nextType = random.nextInt(7) + 1;
        int[][] nextShape = TetrisPiece.SHAPES[nextType - 1];
        nextPiece = new TetrisPiece(nextType, nextShape, 0, 0);
    }

    private boolean isGameOver() {
        return !currentPiece.canMoveDown(TetrisGame.this) && currentPiece.getRow() == 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tetris");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new TetrisGame(), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private class TetrisPiece {
        private static final int[][][] SHAPES = {
                // I
                {
                        {1, 1, 1, 1}
                },
                // O
                {
                        {2, 2},
                        {2, 2}
                },
                // T
                {
                        {0, 3, 0},
                        {3, 3, 3}
                },
                // L
                {
                        {4, 0, 0},
                        {4, 4, 4}
                },
                // J
                {
                        {0, 0, 5},
                        {5, 5, 5}
                },
                // S
                {
                        {0, 6, 6},
                        {6, 6, 0}
                },
                // Z
                {
                        {7, 7, 0},
                        {0, 7, 7}
                }
        };

        private int type;
        private int[][] shape;
        private int row;
        private int col;

        public TetrisPiece(int type, int[][] shape, int row, int col) {
            this.type = type;
            this.shape = shape;
            this.row = row;
            this.col = col;
        }

        public int getType() {
            return type;
        }

        public int[][] getShape() {
            return shape;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public void moveLeft(TetrisGame game) {
            if (canMoveLeft(game)) {
                col--;
            }
        }

        public void moveRight(TetrisGame game) {
            if (canMoveRight(game)) {
                col++;
            }
        }

        public void moveDown(TetrisGame game) {
            if (canMoveDown(game)) {
                row++;
            }
        }

        public void rotate(TetrisGame game) {
            int[][] newShape = rotateShape();

            if (canRotate(game, newShape)) {
                shape = newShape;
            }
        }

        private int[][] rotateShape() {
            int rows = shape.length;
            int cols = shape[0].length;
            int[][] newShape = new int[cols][rows];

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    newShape[col][rows - 1 - row] = shape[row][col];
                }
            }

            return newShape;
        }

        private boolean canMoveLeft(TetrisGame game) {
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0) {
                        int cellRow = this.row + row;
                        int cellCol = this.col + col - 1;

                        if (cellCol < 0 || game.grid[cellRow][cellCol] != EMPTY_CELL) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private boolean canMoveRight(TetrisGame game) {
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0) {
                        int cellRow = this.row + row;
                        int cellCol = this.col + col + 1;

                        if (cellCol >= COLS || game.grid[cellRow][cellCol] != EMPTY_CELL) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private boolean canMoveDown(TetrisGame game) {
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0) {
                        int cellRow = this.row + row + 1;
                        int cellCol = this.col + col;

                        if (cellRow >= TetrisGame.ROWS || game.grid[cellRow][cellCol] != TetrisGame.EMPTY_CELL) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private boolean canRotate(TetrisGame game, int[][] newShape) {
            int newRows = newShape.length;
            int newCols = newShape[0].length;

            for (int row = 0; row < newRows; row++) {
                for (int col = 0; col < newCols; col++) {
                    if (newShape[row][col] != 0) {
                        int cellRow = this.row + row;
                        int cellCol = this.col + col;

                        if (cellRow < 0 || cellRow >= ROWS || cellCol < 0 || cellCol >= COLS || game.grid[cellRow][cellCol] != EMPTY_CELL) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }
}