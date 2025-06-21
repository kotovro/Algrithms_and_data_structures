package ru.cs.vsu.voronetskiy_k_v.Task02;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;


public class FrameForTask02 extends JFrame {
    private final JMenuBar menuBarMain;
    private final JMenu menuLookAndFeel;
    private JPanel panelMain;
    private JTable tableForInputArr;
    private JButton runSolution;
    private JScrollPane paneForInput;
    private JPanel buttonsPanel;
    private JScrollPane paneForResult;
    private JButton buttonSave;
    private JButton buttonOpen;
    private JTable tableForOutArray;

    private double[] inArr;
    private double[] outArr;
    private JFileChooser fileChooserOpen;
    private JFileChooser fileChooserSave;


    public FrameForTask02(String inFile, String outFile) throws IOException {
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Task2 List implementation");
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

        JTableUtils.initJTableForArray(tableForInputArr, 40, false, false, false, true);
        JTableUtils.initJTableForArray(tableForOutArray, 40, false, false, false, false);
        if (inFile != null && inFile.length() > 0) {
            inArr = ArrayUtils.readDoubleArrayFromFile(inFile);
            JTableUtils.writeArrayToJTable(tableForInputArr, inArr);
        } else {
            }
        tableForInputArr.setRowHeight(25);
        runSolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    inArr = JTableUtils.readDoubleArrayFromJTable(tableForInputArr);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                SimpleLinkedList<Double> temp = new SimpleLinkedList<>(ArrayUtils.toGeneric(inArr));
                SimpleLinkedList<Double> res = temp.getSolution();
                outArr = ArrayUtils.toPrimitive(res.listToArray());
                JTableUtils.writeArrayToJTable(tableForOutArray, outArr);
            }
        });
        this.pack();
        buttonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                        inArr = ArrayUtils.readDoubleArrayFromFile(fileChooserOpen.getSelectedFile().getPath());
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
                        inArr = JTableUtils.readDoubleArrayFromJTable(tableForInputArr);
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
