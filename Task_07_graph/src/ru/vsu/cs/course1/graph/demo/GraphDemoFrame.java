package ru.vsu.cs.course1.graph.demo;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import ru.vsu.cs.course1.graph.*;
import ru.vsu.cs.course1.graph.GraphUtils;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphDemoFrame extends JFrame {
    private JTabbedPane tabbedPaneMain;
    private JPanel panelMain;
    private JPanel panelGraphTab;
    private JPanel panelGraphvizTab;
    private JPanel panelDotPainterContainer;
    private JButton buttonLoadDotFile;
    private JButton buttonDotPaint;
    private JTextArea textAreaDotFile;
    private JTextArea textAreaSystemOut;
    private JPanel panelGraphPainterContainer;
    private JButton buttonLoadGraphFromFile;
    private JTextArea textAreaGraphFile;
    private JComboBox comboBoxGraphType;
    private JButton buttonCreateGraph;
    private JSplitPane splitPaneGraphTab1;
    private JSplitPane splitPaneGraphTab2;
    private JSplitPane splitPaneGraphvizTab1;
    private JButton buttonSaveGraphToFile;
    private JButton buttonSaveDotFile;

    private JButton buttonSaveGraphSvgToFile;
    private JButton buttonSaveDotSvgToFile;
    private JComboBox comboBoxExample;
    private JButton buttonExampleExec;
    private JButton buttonSplitTeams;
    private JTextField textFieldTeams;
    private JTextField textFieldMaxDif;
    private JCheckBox checkBoxRelations;
    private JLabel labelMaxDif;
    private JLabel labelTeamAmount;
    private JTextField textFieldVertexNum;
    private JTextField textFieldProbability;
    private JLabel lblVertexes;
    private JLabel lblProbability;
    private JButton buttonGenerate;

    private JFileChooser fileChooserTxtOpen;
    private JFileChooser fileChooserDotOpen;
    private JFileChooser fileChooserTxtSave;
    private JFileChooser fileChooserDotSave;
    private JFileChooser fileChooserImgSave;

    private Graph graph = null;

    private SvgPanel panelGraphPainter;
    private SvgPanel panelGraphvizPainter;


    private static class SvgPanel extends JPanel {
        private String svg = null;
        private GraphicsNode svgGraphicsNode = null;

        public void paint(String svg) throws IOException {
            String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
            SVGDocument doc = df.createSVGDocument(null, new StringReader(svg));
            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GVTBuilder builder = new GVTBuilder();
            svgGraphicsNode = builder.build(ctx, doc);

            this.svg = svg;
            repaint();
        }

        @Override
        public void paintComponent(Graphics gr) {
            super.paintComponent(gr);

            if (svgGraphicsNode == null) {
                return;
            }

            double scaleX = this.getWidth() / svgGraphicsNode.getPrimitiveBounds().getWidth();
            double scaleY = this.getHeight() / svgGraphicsNode.getPrimitiveBounds().getHeight();
            double scale = Math.min(scaleX, scaleY);
            AffineTransform transform = new AffineTransform(scale, 0, 0, scale, 0, 0);
            svgGraphicsNode.setTransform(transform);
            Graphics2D g2d = (Graphics2D) gr;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            svgGraphicsNode.paint(g2d);
        }
    }


    public GraphDemoFrame() {
        this.setTitle("Графы");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        splitPaneGraphTab1.setBorder(null);
        splitPaneGraphTab2.setBorder(null);
        splitPaneGraphvizTab1.setBorder(null);

        fileChooserTxtOpen = new JFileChooser();
        fileChooserDotOpen = new JFileChooser();
        fileChooserTxtSave = new JFileChooser();
        fileChooserDotSave = new JFileChooser();
        fileChooserImgSave = new JFileChooser();
        fileChooserTxtOpen.setCurrentDirectory(new File("./files/input"));
        fileChooserDotOpen.setCurrentDirectory(new File("./files/input"));
        fileChooserTxtSave.setCurrentDirectory(new File("./files/input"));
        fileChooserDotSave.setCurrentDirectory(new File("./files/input"));
        fileChooserImgSave.setCurrentDirectory(new File("./files/output"));
        FileFilter txtFilter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
        FileFilter dotFilter = new FileNameExtensionFilter("DOT files (*.dot)", "dot");
        FileFilter svgFilter = new FileNameExtensionFilter("SVG images (*.svg)", "svg");
        //FileFilter pngFilter = new FileNameExtensionFilter("PNG images (*.png)", "png");

        fileChooserTxtOpen.addChoosableFileFilter(txtFilter);
        fileChooserDotOpen.addChoosableFileFilter(dotFilter);
        fileChooserTxtSave.addChoosableFileFilter(txtFilter);
        fileChooserDotSave.addChoosableFileFilter(dotFilter);
        fileChooserImgSave.addChoosableFileFilter(svgFilter);
        //fileChooserImgSave.addChoosableFileFilter(pngFilter);

        fileChooserTxtSave.setAcceptAllFileFilterUsed(false);
        fileChooserTxtSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserTxtSave.setApproveButtonText("Save");
        fileChooserDotSave.setAcceptAllFileFilterUsed(false);
        fileChooserDotSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserDotSave.setApproveButtonText("Save");
        fileChooserImgSave.setAcceptAllFileFilterUsed(false);
        fileChooserImgSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserImgSave.setApproveButtonText("Save");

        panelGraphPainterContainer.setLayout(new BorderLayout());
        panelGraphPainter = new SvgPanel();
        panelGraphPainterContainer.add(new JScrollPane(panelGraphPainter));

        panelDotPainterContainer.setLayout(new BorderLayout());
        panelGraphvizPainter = new SvgPanel();
        panelDotPainterContainer.add(new JScrollPane(panelGraphvizPainter));

        Method[] methods = GraphvizExamples.class.getMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.getReturnType() == String.class && method.getParameterCount() == 0) {
                comboBoxExample.addItem(method.getName() + "()");
            }
        }

        buttonLoadGraphFromFile.addActionListener(e -> {
            if (fileChooserTxtOpen.showOpenDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                try (Scanner sc = new Scanner(fileChooserTxtOpen.getSelectedFile())) {
                    sc.useDelimiter("\\Z");
                    textAreaGraphFile.setText(sc.next());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonSaveGraphToFile.addActionListener(e -> {
            if (fileChooserTxtSave.showSaveDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserTxtSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".txt")) {
                    filename += ".txt";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(textAreaGraphFile.getText());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonCreateGraph.addActionListener(e -> {
            try {
                String name = comboBoxGraphType.getSelectedItem().toString();
                Matcher matcher = Pattern.compile(".*\\W(\\w+)\\s*\\)\\s*$").matcher(name);
                matcher.find();
                String className = matcher.group(1);
                Class clz = Class.forName("ru.vsu.cs.course1.graph." + className);
                Graph graph = GraphUtils.fromStr(textAreaGraphFile.getText(), clz);
                System.out.println(textAreaGraphFile.getText());
                GraphDemoFrame.this.graph = graph;
                //panelGraphPainter.paint(dotToSvg(GraphUtils.toDot(graph)));
                panelGraphPainter.paint(dotToSvg(((AdjMatrixGraphExt) graph).toDot()));
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });

        buttonSaveGraphSvgToFile.addActionListener(e -> {
            if (panelGraphPainter.svg == null) {
                return;
            }
            if (fileChooserImgSave.showSaveDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserImgSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".svg")) {
                    filename += ".svg";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(panelGraphPainter.svg);
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonLoadDotFile.addActionListener(e -> {
            if (fileChooserDotOpen.showOpenDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                try (Scanner sc = new Scanner(fileChooserDotOpen.getSelectedFile())) {
                    sc.useDelimiter("\\Z");
                    textAreaDotFile.setText(sc.next());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonSaveDotFile.addActionListener(e -> {
            if (fileChooserDotSave.showSaveDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserDotSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".dot")) {
                    filename += ".dot";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(textAreaDotFile.getText());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonDotPaint.addActionListener(e -> {
            try {
                panelGraphvizPainter.paint(dotToSvg(textAreaDotFile.getText()));
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonExampleExec.addActionListener(e -> {
            try {
                String name = comboBoxExample.getSelectedItem().toString();
                if (name.endsWith("()")) {
                    name = name.substring(0, name.length() - 2);
                }
                Method method = GraphvizExamples.class.getMethod(name);
                String svg = (String) method.invoke(null);
                panelGraphvizPainter.paint(svg);
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonSaveDotSvgToFile.addActionListener(e -> {
            if (panelGraphvizPainter.svg == null) {
                return;
            }
            if (fileChooserImgSave.showSaveDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserImgSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".svg")) {
                    filename += ".svg";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(panelGraphvizPainter.svg);
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonSplitTeams.addActionListener(e -> {
            int teams = Integer.parseInt(textFieldTeams.getText());
            int difTimes = Integer.parseInt(textFieldMaxDif.getText());
            boolean isKnown = checkBoxRelations.isSelected();
            if (GraphSplitter.splitter(teams, difTimes, isKnown, (AdjMatrixGraphExt) graph, 10000, -1, true)) {
                try {
                    panelGraphPainter.paint(dotToSvg(((AdjMatrixGraphExt) graph).toDot()));
                } catch (Exception ex) {
                }
            } else {
                SwingUtils.showInfoMessageBox("Такое разбиение невозможно.");
            }

        });
        buttonGenerate.addActionListener(e -> {
            double probability = Double.parseDouble(textFieldProbability.getText());
            int vertexNum = Integer.parseInt(textFieldVertexNum.getText());
            graph = RandomGraphGenerator.generateGraph(vertexNum, probability);
            textAreaGraphFile.setText("");
            textAreaGraphFile.setText(((AdjMatrixGraphExt) graph).toStr());
            try {
                panelGraphPainter.paint(dotToSvg(((AdjMatrixGraphExt) graph).toDot()));
            } catch (Exception ex) {
            }
        });
    }

    /**
     * Преобразование dot-записи в svg-изображение (с помощью Graphviz)
     *
     * @param dotSrc dot-запись
     * @return svg
     * @throws IOException
     */
    private static String dotToSvg(String dotSrc) throws IOException {
        MutableGraph g = new Parser().read(dotSrc);
        return Graphviz.fromGraph(g).render(Format.SVG).toString();
    }

    /**
     * Выполнение действия с выводом стандартного вывода в окне (textAreaSystemOut)
     *
     * @param action Выполняемое действие
     */
    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, "UTF-8"));

            action.run();

            textAreaSystemOut.setText(baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            SwingUtils.showErrorMessageBox(e);
        }
        System.setOut(oldOut);
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), 10, 10));
        panelMain.setInheritsPopupMenu(true);
        tabbedPaneMain = new JTabbedPane();
        tabbedPaneMain.setName("");
        panelMain.add(tabbedPaneMain, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        panelGraphTab = new JPanel();
        panelGraphTab.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), 10, 10));
        tabbedPaneMain.addTab("Граф", null, panelGraphTab, "Демонстрация работы с графами");
        splitPaneGraphTab1 = new JSplitPane();
        splitPaneGraphTab1.setOrientation(0);
        splitPaneGraphTab1.setResizeWeight(0.75);
        panelGraphTab.add(splitPaneGraphTab1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        splitPaneGraphTab1.setRightComponent(scrollPane1);
        textAreaSystemOut = new JTextArea();
        scrollPane1.setViewportView(textAreaSystemOut);
        splitPaneGraphTab2 = new JSplitPane();
        splitPaneGraphTab2.setResizeWeight(0.0);
        splitPaneGraphTab1.setLeftComponent(splitPaneGraphTab2);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 8, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneGraphTab2.setLeftComponent(panel1);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel1.add(scrollPane2, new GridConstraints(0, 0, 1, 8, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaGraphFile = new JTextArea();
        textAreaGraphFile.setText("13\n13\n0 5\n4 3\n0 1\n9 12\n6 4\n5 4\n0 2\n11 12\n9 10\n0 6\n7 8\n9 11\n5 3\n");
        scrollPane2.setViewportView(textAreaGraphFile);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(2, 0, 1, 8, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonLoadGraphFromFile = new JButton();
        buttonLoadGraphFromFile.setText("Загрузить из файла");
        panel2.add(buttonLoadGraphFromFile, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSaveGraphToFile = new JButton();
        buttonSaveGraphToFile.setText("Сохранить в файл");
        panel2.add(buttonSaveGraphToFile, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonGenerate = new JButton();
        buttonGenerate.setText("Случайный граф");
        panel2.add(buttonGenerate, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(3, 0, 1, 8, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        comboBoxGraphType = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Н-граф (AdjMatrixGraphExt)");
        comboBoxGraphType.setModel(defaultComboBoxModel1);
        panel3.add(comboBoxGraphType, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCreateGraph = new JButton();
        buttonCreateGraph.setText("Построить граф");
        panel3.add(buttonCreateGraph, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(4, 0, 1, 8, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(null, "Разбиения", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(1, 2, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldTeams = new JTextField();
        textFieldTeams.setText("5");
        panel5.add(textFieldTeams, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        labelTeamAmount = new JLabel();
        labelTeamAmount.setText("Количество команд");
        panel5.add(labelTeamAmount, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldMaxDif = new JTextField();
        textFieldMaxDif.setText("2");
        panel4.add(textFieldMaxDif, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonSplitTeams = new JButton();
        buttonSplitTeams.setText("Разделить");
        panel4.add(buttonSplitTeams, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxRelations = new JCheckBox();
        checkBoxRelations.setText("Учёт опосредованного занкомства");
        panel4.add(checkBoxRelations, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelMaxDif = new JLabel();
        labelMaxDif.setText("Максимальная разница");
        panel4.add(labelMaxDif, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel4.add(spacer3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblVertexes = new JLabel();
        lblVertexes.setText("Число вершин:");
        panel1.add(lblVertexes, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblProbability = new JLabel();
        lblProbability.setText("Вероятность связи:");
        panel1.add(lblProbability, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldVertexNum = new JTextField();
        textFieldVertexNum.setText("20");
        panel1.add(textFieldVertexNum, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textFieldProbability = new JTextField();
        textFieldProbability.setText("0.5");
        panel1.add(textFieldProbability, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneGraphTab2.setRightComponent(panel6);
        buttonSaveGraphSvgToFile = new JButton();
        buttonSaveGraphSvgToFile.setText("Сохранить в файл");
        panel6.add(buttonSaveGraphSvgToFile, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelGraphPainterContainer = new JPanel();
        panelGraphPainterContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panelGraphPainterContainer, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel6.add(spacer5, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        panelGraphvizTab = new JPanel();
        panelGraphvizTab.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), 10, 10));
        tabbedPaneMain.addTab("Graphviz", null, panelGraphvizTab, "Демонстрация возможностей GraphViz");
        splitPaneGraphvizTab1 = new JSplitPane();
        splitPaneGraphvizTab1.setResizeWeight(0.0);
        panelGraphvizTab.add(splitPaneGraphvizTab1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneGraphvizTab1.setRightComponent(panel7);
        buttonSaveDotSvgToFile = new JButton();
        buttonSaveDotSvgToFile.setText("Сохранить в файл");
        panel7.add(buttonSaveDotSvgToFile, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelDotPainterContainer = new JPanel();
        panelDotPainterContainer.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panelDotPainterContainer, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panelDotPainterContainer.add(spacer6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panelDotPainterContainer.add(spacer7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel7.add(spacer8, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneGraphvizTab1.setLeftComponent(panel8);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel8.add(scrollPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaDotFile = new JTextArea();
        textAreaDotFile.setText("graph {\n    2 -- { 3 -- 4 }\n    3 -- 6 -- { 2 4 }\n    7 -- 8 -- 7\n    9\n}\n");
        scrollPane3.setViewportView(textAreaDotFile);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonLoadDotFile = new JButton();
        buttonLoadDotFile.setText("Загрузить из файла");
        panel9.add(buttonLoadDotFile, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel9.add(spacer9, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonSaveDotFile = new JButton();
        buttonSaveDotFile.setText("Сохранить в файл");
        panel9.add(buttonSaveDotFile, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel10, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonDotPaint = new JButton();
        buttonDotPaint.setText("Отобразить");
        panel10.add(buttonDotPaint, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel11, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Пример:");
        panel11.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxExample = new JComboBox();
        panel11.add(comboBoxExample, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonExampleExec = new JButton();
        buttonExampleExec.setText("Выполнить");
        panel11.add(buttonExampleExec, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel11.add(spacer10, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
