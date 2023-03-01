package ru.vsu.cs.cousre1.voronetskiy_k_v;
import java.util.Random;

/**
 * Класс, реализующий логику лабирнта
 */
/**
 0 - в ячейке нет стен
 1 - стена сврху
 2 - стена снизу
 3 - стена сверху и снизу
 4 - стена справа
 5 - стена справа и сверху
 6 - стена справа и снизу
 7 - стена справа, сверху и снизу
 8 - стена слева
 9 - стена слева и сверху
 10 - стена слева и снизу
 11 - стена слева, снизу и сверху
 12 - стена слева и справа
 13 - стенае слева, спрвав и свеху
 14 - стена слева, справа и снизу
 15 - непроходимая ячейка, стены со всех сторон
 16+ - уже посещённая ячейка
 32+ - найденный путь
*/
public class Maze {
    /**
     * объект Random для генерации случайных чисел
     * (можно было бы объявить как static)
     */
    private final Random rnd = new Random();

    /**
     * двумерный массив для хранения поля лабиринта
     * (в данном случае цветов, 0 - пусто; создается / пересоздается при старте игры)
     */
    private boolean pathFound = false;
    private int startPositionX = -1;
    private int startPositionY = -1;
    private int finalPositionX = 0;
    private int finalPositionY = 0;
    private int wallDensity = 1;
    private int[][] field = null;

    public Maze() {
    }


    public void newMaze(int rowCount, int colCount, int wallDensity) {
        // создаем поле
        this.wallDensity = wallDensity;
        field = new int[rowCount + 2][colCount + 2];
        for (int i = 0; i < rowCount + 2; i++) {
            field[i][0] = 15;
            field[i][colCount + 1] = 15;
        }
        for (int i = 1; i < colCount + 1; i++) {
            field[0][i] = 15;
            field[rowCount + 1][i] = 15;
        }
        for (int i = 1; i < rowCount + 1; i++) {
            for (int j = 1; j < colCount + 1; j++) {
                createNewCell(i, j);
            }
        }
        startPositionX = -1;
        startPositionY = -1;
        finalPositionX = 0;
        finalPositionY = 0;
        pathFound = false;
    }
    private void createNewCell(int row, int col) {
        int isWallLeft = (field[row][col - 1] & 4) * 2;
        int isWallAbove = (field[row - 1][col] & 2) / 2;
        int isWallBelow = (row == field.length - 2) ? 2 : isWallCreated() * 2;
        int isWallRight = (col == field[0].length - 2) ? 4 : isWallCreated() * 4;
        field[row][col] = isWallBelow + isWallAbove  + isWallLeft + isWallRight;
    }
    private int isWallCreated(){
        int random = rnd.nextInt(wallDensity + 1);
        return random == 0 ? 1 : 0;
    }
    public void selectCell(int x, int y) {
        if (startPositionX < 0 || startPositionY < 0) {
            startPositionX = x;
            startPositionY = y;
            finalPositionX = 0;
            finalPositionY = 0;
            field[startPositionX][startPositionY] += 16;
        } else if (startPositionX == x && startPositionY == y) {
            field[startPositionX][startPositionY] -= 16;
            startPositionX = -1;
            startPositionY = -1;
            finalPositionX = 0;
            finalPositionY = 0;
            clearPreviousPaths(false);
        } else {
            finalPositionX = x;
            finalPositionY = y;
            clearPreviousPaths(false);
            pathFound = isPathFound(startPositionX, startPositionY);
            if (pathFound){
                field[startPositionX][startPositionY] += 16;
            }
            clearPreviousPaths(true);

        }
    }

    private boolean isPathFound(int startRow, int startColumn) {
        if (startRow == finalPositionX && startColumn == finalPositionY) {
            field[startRow][startColumn] += 32;
            return true;
        }
        boolean isFreeAbove = !isWallAbove(startRow, startColumn);
        boolean isFreeBelow = !isWallBelow(startRow, startColumn);
        boolean isFreeLeft = !isWallLeft(startRow, startColumn);
        boolean isFreeRight = !isWallRight(startRow, startColumn);

        if (field[startRow][startColumn] >= 16) {
            return false;
        }

        field[startRow][startColumn] |= 16;
        boolean isPathFound =
                isFreeBelow && isPathFound(startRow + 1, startColumn) ||
                isFreeAbove && isPathFound(startRow - 1, startColumn) ||
                isFreeRight && isPathFound(startRow, startColumn + 1) ||
                isFreeLeft && isPathFound(startRow, startColumn - 1);
        if (isPathFound) {
            field[startRow][startColumn] &= 15;
            field[startRow][startColumn] |= 32;
        }

        return isPathFound;
    }

    public void clearPreviousPaths(boolean isTemp) {
        for (int i = 1; i < field.length - 1; i++) {
            for (int j = 1; j < field[0].length - 1; j++) {
                field[i][j] &= (isTemp ? 47 : 15);
            }
        }
        if (isTemp){
            finalPositionX = 0;
            finalPositionY = 0;
        }
    }

    public boolean getPathNotFound() {
        boolean pathNotFound = startPositionX >= 0 && startPositionY >= 0 && (field[startPositionX][startPositionY] < 16);
        if (pathNotFound) {
            startPositionX = -1;
            startPositionY = -1;
        }
        return pathNotFound;
    }
    public boolean isSelected(int x, int y) {
        return x == startPositionX && y == startPositionY;
    }
    public void leftMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        if (row < 1 || row > rowCount - 2 || col < 1 || col > colCount - 2) {
            return;
        }
        selectCell(row, col);
    }

    public int getRowCount() {
        return field == null ? 0 : field.length;
    }

    public int getColCount() {
        return field == null ? 0 : field[0].length;
    }

    public boolean isWallAbove(int row, int column) {
        return (field[row][column] & 1) > 0;
    }
    public boolean isWallBelow(int row, int column) {
        return (field[row][column] & 2) > 0;
    }
    public boolean isWallRight(int row, int column) {
        return (field[row][column] & 4) > 0;
    }
    public boolean isWallLeft(int row, int column) {
        return (field[row][column] & 8) > 0;
    }

    public boolean isPath(int x, int y) {
        return field[x][y] > 15;
    }
}
