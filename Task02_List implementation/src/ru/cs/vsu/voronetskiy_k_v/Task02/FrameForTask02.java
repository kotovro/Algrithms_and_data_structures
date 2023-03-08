package ru.cs.vsu.voronetskiy_k_v.Task02;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class FrameForTask02 extends JFrame {
    private final JMenuBar menuBarMain;
    private final JMenu menuLookAndFeel;
    private JPanel panelMain;
    private JTable tableForInputArr;
    private JButton createUnrepeatableArrayButton;
    private JTable table1;
    private JScrollPane paneForResult;
    private JScrollPane paneForInput;
    private JPanel buttonsPanel;
    private JButton buttonSave;
    private JButton buttonOpen;

    private int[] inArr;
    private JFileChooser fileChooserOpen;
    private JFileChooser fileChooserSave;
    private int[] outArr;


    public FrameForTask02(String inFile, String outFile) throws IOException {
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Task09 Reverse list elements");
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

        JTableUtils.initJTableForArray(table1, 40, false, false, false, false);

        if (inFile != null && inFile.length() > 0) {
            JTableUtils.initJTableForArray(tableForInputArr, 40, false, false, false, false);
            inArr = ArrayUtils.readIntArrayFromFile(inFile);
            JTableUtils.writeArrayToJTable(tableForInputArr, inArr);
        } else {
            JTableUtils.initJTableForArray(tableForInputArr, 40, false, false, false, true);
        }
        tableForInputArr.setRowHeight(25);
        table1.setRowHeight(25);
        createUnrepeatableArrayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inFile == null || inFile.length() == 0) {
                    try {
                        inArr = JTableUtils.readIntArrayFromJTable(tableForInputArr);
                    } catch (Exception ex) {
                    }
                }

                outArr = RunSolution.runSolution(inArr, outFile);
                JTableUtils.writeArrayToJTable(table1, outArr);
            }
        });
        this.pack();
//        chooseInputFile.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                try {
////                    if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
////                        JTableUtils.initJTableForArray(tableForInputArr, 40, false, false, false, false);
////                        inArr = ArrayUtils.readIntArray2FromFile(fileChooserOpen.getSelectedFile().getPath());
////                        JTableUtils.writeArrayToJTable(tableForInputArr, inArr);
////                    }
////                } catch (Exception ex1) {
////                    System.err.println("Error");
////                }
//            }
//        });
//        chooseOutputFileButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
        buttonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                        JTableUtils.initJTableForArray(tableForInputArr, 40, false, false, false, false);
                        inArr = ArrayUtils.readIntArrayFromFile(fileChooserOpen.getSelectedFile().getPath());
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
                        outArr = JTableUtils.readIntArrayFromJTable(table1);
                        ArrayUtils.writeArrayToFile(fileChooserSave.getSelectedFile().getPath(), outArr);
                    }
                } catch (Exception ex) {};
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
