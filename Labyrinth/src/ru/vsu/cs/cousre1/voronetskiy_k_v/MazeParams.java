package ru.vsu.cs.cousre1.voronetskiy_k_v;

/**
 * Класс для хранения параметров игры
 */
public class MazeParams {
    private int rowCount;
    private int colCount;
    private int colorCount;
    private int wallDensity;
    private int cellSize;

    public MazeParams(int rowCount, int colCount, int wallDensity, int cellSize) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.wallDensity = wallDensity;
        this.cellSize = cellSize;
    }

    public MazeParams() {
        this(7, 7, 1,  90);
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
    public int getWallDensity() {return wallDensity;}
    public void setWallDensity(int nextBallsCount) {
        this.wallDensity = nextBallsCount;
    }
    public int getCellSize() {return cellSize;}
    public void setCellsSize(int newCellSize) {cellSize = newCellSize;}
}
