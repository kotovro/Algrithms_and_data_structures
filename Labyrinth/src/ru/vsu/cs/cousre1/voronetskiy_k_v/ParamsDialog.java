package ru.vsu.cs.cousre1.voronetskiy_k_v;

import utils.JTableUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ParamsDialog extends JDialog {
    private JPanel panelMain;
    private JSpinner spinnerColCount;
    private JSpinner spinnerRowCount;
    private JSpinner wallDensity;
    private JSlider sliderCellSize;
    private JButton buttonCancel;
    private JButton buttonNewGame;
    private JButton buttonOk;
    private JLabel labelWallDensity;




    private MazeParams params;
    private JTable gameFieldJTable;
    private ActionListener newGameAction;

    private int oldCellSize;

    private void resizeCells(JTable field, int height, int width) {
        field.setRowHeight(height);
        field.setRowHeight(0, 2);
        field.setRowHeight(field.getRowCount() - 1, 2);
        for (int i = 1; i < field.getColumnCount() - 1; i++) {
            field.getColumnModel().getColumn(i).setMinWidth(width);
            field.getColumnModel().getColumn(i).setMaxWidth(width);
        }
        int cellSize = Math.min((field.getWidth() - 100) / params.getWallDensity(), params.getCellSize());
    }
    public ParamsDialog(MazeParams params, JTable gameFieldJTable, ActionListener newGameAction) {
        this.setTitle("Параметры");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.pack();

        this.setResizable(false);

        this.params = params;
        this.gameFieldJTable = gameFieldJTable;
        this.newGameAction = newGameAction;
        this.oldCellSize = gameFieldJTable.getRowHeight();
        sliderCellSize.addChangeListener(e -> {
            int value = sliderCellSize.getValue();
            params.setCellsSize(value);
            resizeCells(gameFieldJTable, value, value);
        });
        buttonCancel.addActionListener(e -> {
            JTableUtils.resizeJTableCells(gameFieldJTable, oldCellSize, oldCellSize);
            this.setVisible(false);
        });
        buttonNewGame.addActionListener(e -> {
            buttonOk.doClick();
            if (newGameAction != null) {
                newGameAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "newGame"));
            }
        });
        buttonOk.addActionListener(e -> {
            params.setRowCount((int) spinnerRowCount.getValue());
            params.setColCount((int) spinnerColCount.getValue());
            params.setWallDensity((int) wallDensity.getValue());
            oldCellSize = gameFieldJTable.getRowHeight();
            this.setVisible(false);
        });
    }


    public void updateView() {
        spinnerRowCount.setValue(params.getRowCount());
        spinnerColCount.setValue(params.getColCount());
        sliderCellSize.setValue(gameFieldJTable.getRowHeight());
        wallDensity.setValue(params.getWallDensity());
    }

}
