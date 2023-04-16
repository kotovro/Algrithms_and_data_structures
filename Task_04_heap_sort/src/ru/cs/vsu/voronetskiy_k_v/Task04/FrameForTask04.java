package src.ru.cs.vsu.voronetskiy_k_v.Task04;

import src.ru.cs.vsu.voronetskiy_k_v.Task04.utils.ArrayUtils;
import src.ru.cs.vsu.voronetskiy_k_v.Task04.utils.JTableUtils;
import src.ru.cs.vsu.voronetskiy_k_v.Task04.utils.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class FrameForTask04 extends JFrame {
    private final JMenuBar menuBarMain;
    private final JMenu menuLookAndFeel;
    private JPanel panelMain;
    private JTable tableForInputArr;
    private JButton btnRunSolution;
    private JScrollPane paneForInput;
    private JPanel buttonsPanel;
    private JButton buttonSave;
    private JButton buttonOpen;
    private JScrollPane paneForResult;
    private JTable tableResult;
    private JTextField textFieldSortFrom;
    private JTextField textFieldSortTo;

    private JFileChooser fileChooserOpen;
    private JFileChooser fileChooserSave;


    public FrameForTask04(String inFile, String outFile) throws IOException {
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Task3 Queue implementation");
        fileChooserOpen = new JFileChooser();
        fileChooserSave = new JFileChooser();
        fileChooserOpen.setCurrentDirectory(new File("."));
        fileChooserSave.setCurrentDirectory(new File("."));
        FileFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooserOpen.addChoosableFileFilter(filter);
        fileChooserSave.addChoosableFileFilter(filter);

        fileChooserSave.setAcceptAllFileFilterUsed(false);
        fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserSave.setApproveButtonText("Save");

        menuBarMain = new JMenuBar();
        setJMenuBar(menuBarMain);

        menuLookAndFeel = new JMenu();
        menuLookAndFeel.setText("View");
        menuBarMain.add(menuLookAndFeel);
        SwingUtils.initLookAndFeelMenu(menuLookAndFeel);

        if (inFile != null && inFile.length() > 0) {
            JTableUtils.initJTableForArray(tableForInputArr, 40, false, false, false, true);
            int[] inArr = ArrayUtils.readIntArrayFromFile(inFile);
            JTableUtils.writeArrayToJTable(tableForInputArr, inArr);
        } else {
            JTableUtils.initJTableForArray(tableForInputArr, 40, false, false, false, true);
        }
        JTableUtils.initJTableForArray(tableResult, 40, false, false, false, false);
        paneForInput.setMinimumSize(new Dimension(100, 100));
        tableForInputArr.setRowHeight(50);
        tableResult.setRowHeight(50);
        btnRunSolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int[] inArr = JTableUtils.readIntArrayFromJTable(tableForInputArr);
                    int from = Integer.parseInt(textFieldSortFrom.getText());
                    int to = Integer.parseInt(textFieldSortTo.getText());
                    int[] res = RunSolution.runSolution(inArr, from, to);
                    JTableUtils.writeArrayToJTable(tableResult, res);
                } catch (Exception ex) {
                }
            }
        });
        buttonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                        int[] inArr = ArrayUtils.readIntArrayFromFile(fileChooserOpen.getSelectedFile().getPath());
                        JTableUtils.writeArrayToJTable(tableForInputArr, inArr);
                    }
                } catch (Exception ex1) {
                    System.err.println("Error");
                }
            }
        });
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                        int[] inArr = JTableUtils.readIntArrayFromJTable(tableResult);
                        ArrayUtils.writeArrayToFile(fileChooserSave.getSelectedFile().getPath(), inArr);
                    }
                } catch (Exception ex) {};
            }
        });
        this.pack();
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
