package ru.vsu.cs.cousre1.voronetskiy_k_v;

/**
 * Класс для хранения параметров игры
 */
public class GameParams {
    private int rowCount;
    private int colCount;
    private int colorCount;
    private int nextBallsCount;
    private int cellSize;

    public GameParams(int rowCount, int colCount, int colorCount, int nextBallsCount, int cellSize) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.colorCount = colorCount;
        this.nextBallsCount = nextBallsCount;
        this.cellSize = cellSize;
    }

    public GameParams() {
        this(7, 7, 7, 3, 90);
    }

    /**
     * @return the colCount
     */
    public int getColCount() {
        return colCount;
    }

    /**
     * @param colCount the colCount to set
     */
    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    /**
     * @return the rowCount
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * @param rowCount the rowCount to set
     */
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    /**
     * @return the colorCount
     */
    public int getColorCount() {
        return colorCount;
    }

    /**
     * @param colorCount the colorCount to set
     */
    public void setColorCount(int colorCount) {
        this.colorCount = colorCount;
    }
    public int getNextBallsCount() {return nextBallsCount;}
    public void setNextBallsCount(int nextBallsCount) {
        this.nextBallsCount = nextBallsCount;
    }
    public int getCellSize() {return cellSize;}
    public void setCellsSize(int newCellSize) {cellSize = newCellSize;}
}
