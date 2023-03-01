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
    private int startPositionX = 0;
    private int startPositionY = 0;
    private int finalPositionX = 0;
    private int finalPositionY = 0;
    private int[][] field = null;

    public Maze() {
    }


    public void newMaze(int rowCount, int colCount) {
        // создаем поле
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
        startPositionX = 0;
        startPositionY = 0;
        finalPositionX = 0;
        finalPositionY = 0;
        pathFound = false;
    }
    private void createNewCell(int row, int col) {
        int isWallLeft = (field[row][col - 1] & 4) * 2;
        int isWallAbove = (field[row - 1][col] & 2) / 2;
        int isWallBelow = (row == field.length - 2) ? 2 : rnd.nextInt(2) * 2;
        int isWallRight = (col == field[0].length - 2) ? 4 : rnd.nextInt(2) * 4;
        field[row][col] = isWallBelow + isWallAbove  + isWallLeft + isWallRight;
    }
    public void selectCell(int x, int y) {
        if (startPositionX < 1 && startPositionY < 1) {
            startPositionX = x;
            startPositionY = y;
            finalPositionX = 0;
            finalPositionY = 0;
            field[startPositionX][startPositionY] += 16;
        } else {
            finalPositionX = x;
            finalPositionY = y;
            if (startPositionX > 0 && startPositionY > 0) {
                if (!isPathFound(startPositionX, startPositionY)) {
                     clearPreviousPaths();
                }
            }
        }
    }

    private boolean isPathFound(int startX, int startY) {
        if (startX == finalPositionX && startY == finalPositionY) {
            field[startX][startY] += 32;
            pathFound = true;
            return true;
        }
        boolean isFreeAbove = (field[startX][startY] & 1) == 0;
        boolean isFreeBelow = (field[startX][startY] & 2) == 0;
        boolean isFreeLeft = (field[startX][startY] & 8) == 0;
        boolean isFreeRight = (field[startX][startY] & 4) == 0;
        if (startX != startPositionX || startY != startPositionY) {
            if (field[startX][startY] >= 16) {
                return false;
            }
            field[startX][startY] += 16;
        }
        boolean isPathFound = isFreeBelow && isPathFound(startX + 1, startY) ||
                isFreeAbove && isPathFound(startX - 1, startY) ||
                isFreeRight && isPathFound(startX, startY + 1) ||
                isFreeLeft && isPathFound(startX, startY - 1);
        if (isPathFound) {
            field[startX][startY] += 32;
        }
        return isPathFound;
    }

    public void clearPreviousPaths() {
        for (int i = 1; i < field.length - 1; i++) {
            for (int j = 1; j < field[0].length - 1; j++) {
                field[i][j] &= 15;
            }
        }
        startPositionX = 0;
        startPositionY = 0;
        finalPositionX = 0;
        finalPositionY = 0;
        pathFound = false;
    }

    public boolean getPathNotFound() {
        return (startPositionX == 0 && startPositionY == 0 && finalPositionX == 0 && finalPositionY == 0);
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

    public int getCell(int row, int col) {
        return (row < 0 || row >= getRowCount() || col < 0 || col >= getColCount()) ? 0 : field[row][col];
    }


}
