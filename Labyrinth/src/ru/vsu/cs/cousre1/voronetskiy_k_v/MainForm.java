package ru.vsu.cs.cousre1.voronetskiy_k_v;

import utils.JTableUtils;
import utils.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainForm extends JFrame {
    private JPanel panelMain;
    private JTable tableMazeField;
    private JLabel labelScore;
    private JTable tableNextTurnBalls;

    private static final int DEFAULT_WALLS_DENSITY = 3;
    private static final int DEFAULT_COL_COUNT = 9;
    private static final int DEFAULT_ROW_COUNT = 9;
    private static final int DEFAULT_GAP = 10;
    private static final int DEFAULT_CELL_SIZE = 50;

    private static final Color[] COLORS = {
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.MAGENTA,
            Color.CYAN,
            Color.ORANGE,
            Color.PINK,
            Color.WHITE,
            Color.GRAY
    };

    private MazeParams params = new MazeParams(DEFAULT_ROW_COUNT, DEFAULT_COL_COUNT, DEFAULT_WALLS_DENSITY, DEFAULT_CELL_SIZE);
    private Maze maze = new Maze();
    private ParamsDialog dialogParams;


    public MainForm() {
        this.setTitle("Maze");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        setJMenuBar(createMenuBar());
        this.pack();

        SwingUtils.setShowMessageDefaultErrorHandler();

        tableMazeField.setRowHeight(DEFAULT_CELL_SIZE);
        tableNextTurnBalls.setRowHeight(DEFAULT_CELL_SIZE);
        JTableUtils.initJTableForArray(tableMazeField, DEFAULT_CELL_SIZE, false, false, false, false);
        JTableUtils.initJTableForArray(tableNextTurnBalls, DEFAULT_CELL_SIZE, false, false, false, false);
        tableMazeField.setIntercellSpacing(new Dimension(0, 0));
        tableNextTurnBalls.setIntercellSpacing(new Dimension(0, 0));
        tableMazeField.setEnabled(false);
        tableNextTurnBalls.setEnabled(false);



        tableMazeField.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            final class DrawComponent extends Component {
                private int row = 0, column = 0;


                @Override
                public void paint(Graphics gr) {
                    Graphics2D g2d = (Graphics2D) gr;
                    int width = getWidth() - 2;
                    int height = getHeight() - 2;
                    paintCell(row, column, g2d, width, height);
                }
            }

            DrawComponent comp = new DrawComponent();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                comp.row = row;
                comp.column = column;
                return comp;
            }
        });

        newMaze();

        updateWindowSize();
        updateView();

        dialogParams = new ParamsDialog(params, tableMazeField, tableNextTurnBalls, e -> newMaze());

        tableMazeField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = tableMazeField.rowAtPoint(e.getPoint());
                int col = tableMazeField.columnAtPoint(e.getPoint());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    maze.leftMouseClick(row, col);
                    updateView();
                    if (maze.getPathNotFound()) {
                        SwingUtils.showInfoMessageBox(
                                "Путь не найден"
                        );
                    }
                }

            }
        });
    }

    private JMenuItem createMenuItem(String text, String shortcut, Character mnemonic, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        if (shortcut != null) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut.replace('+', ' ')));
        }
        if (mnemonic != null) {
            menuItem.setMnemonic(mnemonic);
        }
        return menuItem;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBarMain = new JMenuBar();

        JMenu menuGame = new JMenu("Настройки");
        menuBarMain.add(menuGame);
        menuGame.add(createMenuItem("Новый", "ctrl+N", null, e -> {
            newMaze();
        }));
        menuGame.add(createMenuItem("Параметры", "ctrl+P", null, e -> {
            dialogParams.updateView();
            dialogParams.setVisible(true);
        }));
        menuGame.addSeparator();
        menuGame.add(createMenuItem("Выход", "ctrl+X", null, e -> {
            System.exit(0);
        }));

        JMenu menuView = new JMenu("Вид");
        menuBarMain.add(menuView);
        menuView.add(createMenuItem("Подогнать размер окна", null, null, e -> {
            updateWindowSize();
        }));
        menuView.addSeparator();
        SwingUtils.initLookAndFeelMenu(menuView);

        JMenu menuHelp = new JMenu("Справка");
        menuBarMain.add(menuHelp);
        menuHelp.add(createMenuItem("Справка", "ctrl+R", null, e -> {
            SwingUtils.showInfoMessageBox("""
                            Лабиринт и поиск путей в нём из\040
                            произвольной клетки в произвольную другую."""
                    , "Справка");
        }));
        menuHelp.add(createMenuItem("О программе", "ctrl+A", null, e -> {
            SwingUtils.showInfoMessageBox(
                    "Лабиринт" +
                            "\n\nАвтор: Воронецкий К.В." +
                            "\nE-mail: kootvoro@gmail.com",
                    "О программе"
            );
        }));

        return menuBarMain;
    }

    public void updateWindowSize() {
        int menuSize = this.getJMenuBar() != null ? this.getJMenuBar().getHeight() : 0;
        SwingUtils.setFixedSize(
                this,
                tableMazeField.getWidth() + 2 * DEFAULT_GAP + 20,
                tableMazeField.getHeight() + tableNextTurnBalls.getHeight() + labelScore.getHeight() +
                        panelMain.getY() + menuSize + 1 * DEFAULT_GAP + 2 * DEFAULT_GAP + 50
        );
        this.setMaximumSize(null);
        this.setMinimumSize(null);
    }

    private void updateView() {
        tableMazeField.repaint();
    }


    private Font font = null;

    private Font getFont(int size) {
        if (font == null || font.getSize() != size) {
            font = new Font("Comic Sans MS", Font.BOLD, size);
        }
        return font;
    }

    private void paintCell(int row, int column, Graphics2D g2d, int cellWidth, int cellHeight) {
        if (row == 0 || row == maze.getRowCount() - 1 || column == 0 || column == maze.getColCount() - 1){
            return;
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(cellWidth, cellHeight);
        if (maze.isWallAbove(row, column)) {
            g2d.drawLine(0, 0, size, 0);
        }
        if (maze.isWallBelow(row, column)) {
            g2d.drawLine(0, size, size, size);
        }
        if (maze.isWallRight(row, column)) {
            g2d.drawLine(size, 0, size, size);
        }
        if (maze.isWallLeft(row, column)) {
            g2d.drawLine(0, 0, 0, size);
        }

        if (!maze.isPath(row, column)) {
            return;
        }
        Color color = maze.isSelected(row, column) ? Color.DARK_GRAY : Color.RED;


        int bound = (int) Math.round(size * 0.4);

        g2d.setColor(color);
        g2d.fillRoundRect(bound, bound, size - 2 * bound, size - 2 * bound, size, size);
        g2d.setColor(maze.isSelected(row, column) ? Color.DARK_GRAY : Color.YELLOW);
        g2d.drawRoundRect(bound, bound, size - 2 * bound, size - 2 * bound, size, size);
    }
    private void validateParams() {
        if (params.getWallDensity() < 1) {
            params.setWallDensity(1);
        }
        if (params.getColorCount() < 1) {
            params.setColorCount(1);
        }
        if (params.getColorCount() > COLORS.length) {
            params.setColorCount(COLORS.length);
        }
        if (params.getColCount() < 1) {
            params.setColCount(1);
        }
        if (params.getRowCount() < 1) {
            params.setRowCount(1);
        }

    }
    private void newMaze() {
        validateParams();
        maze.newMaze(params.getRowCount(), params.getColCount(), params.getWallDensity());
        JTableUtils.resizeJTable(tableMazeField,
                maze.getRowCount(), maze.getColCount(),
                tableMazeField.getRowHeight(), tableMazeField.getRowHeight()
        );
        tableMazeField.setRowHeight(0, 2);
        tableMazeField.setRowHeight(params.getRowCount() + 1, 2);
        tableMazeField.getColumnModel().getColumn(0).setMinWidth(2);
        tableMazeField.getColumnModel().getColumn(tableMazeField.getColumnCount() - 1).setMinWidth(2);
        tableMazeField.getColumnModel().getColumn(0).setMaxWidth(2);
        tableMazeField.getColumnModel().getColumn(tableMazeField.getColumnCount() - 1).setMaxWidth(2);
        for (int i = 1; i < tableMazeField.getColumnCount() - 1; i++) {
            tableMazeField.getColumnModel().getColumn(i).setMinWidth(params.getCellSize());
            tableMazeField.getColumnModel().getColumn(i).setMaxWidth(params.getCellSize());
        }
        JTableUtils.recalcJTableSize(tableMazeField);
        updateView();
    }
}
