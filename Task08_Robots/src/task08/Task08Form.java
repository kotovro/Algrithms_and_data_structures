package task08;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import graph.SimpleWGraph;
import util.DemoUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;


public class Task08Form extends JFrame {
    private int numberOfNodes = 12;
    private int nodeRadius = 15;

    private int selectedNodeIndex = -1;
    private int draggingNodeIndex = -1;
    private Point2D[] nodePositions;
    //private Nodes ;
    private static final long serialVersionUID = 1L;
    private int width = 600;
    private int height = 600;
    private int padding = 50;
    private BufferedImage graphicsContext;
    private JPanel panelMain;
    private JLabel contextRender;
    private Stroke dashedStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 2f, new float[]{3f, 3f}, 0f);
    private Stroke solidStroke = new BasicStroke(3.0f);
    private RenderingHints antialiasing;
    private Random random = new Random();
    private JPanel contentPanel = new JPanel();
    private JPanel panelButtons;
    private JPanel panelRobot1;
    private JComboBox comboRobot1Start;
    private JComboBox comboRobot1Speed;
    private JPanel panelRobot2;
    private JComboBox comboRobot2Start;
    private JComboBox comboRobot2Speed;
    private JPanel panelRobot3;
    private JComboBox comboRobot3Start;
    private JComboBox comboRobot3Speed;
    private JPanel panelAnimation;
    private JButton buttonRun;
    private JButton buttonGenerateGraph;
    private JPanel panelGraph;
    private JTextField textFieldProbability;
    private JTextField textFieldMaxWeight;
    private JTextField textFieldMinWeight;
    private JTextField textFieldVertexCount;

    private JScrollPane scrollPane;
    private SimpleWGraph graph;
    private SimpleWGraph.GraphEdge selectedEdge = null;
    private EdgeParamsDialog edgeDialog = null;
    private NodeParamsDialog nodeDialog = null;
    //private NodeParamsDialog nodeDialogParams = new NodeParamsDialog();

    public static void main(String[] args) {
        //you should always use the SwingUtilities.invodeLater() method
        //to perform actions on swing elements to make certain everything
        //is happening on the correct swing thread
        Runnable swingStarter = new Runnable() {
            @Override
            public void run() {
                new Task08Form();
            }
        };
        SwingUtilities.invokeLater(swingStarter);
    }

    public Task08Form() {
//        antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        graphicsContext = new BufferedImage(width + (2 * padding), width + (2 * padding), BufferedImage.TYPE_INT_RGB);
//        contextRender = new JLabel(new ImageIcon(graphicsContext));

        //contentPanel.remove(contextRender);
        //contentPanel.add(contextRender);
        $$$setupUI$$$();
        contentPanel.setSize(width + padding * 2, height + padding * 2);
        //panelButtons.setSize(300, 300);

        getRandomGraph();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setContentPane(panelMain);
        //take advantage of auto-sizing the window based on the size of its contents
        this.setLocationRelativeTo(null);

        DefaultComboBoxModel cmbR1StartModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel cmbR2StartModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel cmbR3StartModel = new DefaultComboBoxModel<>();


        cmbR3StartModel.addElement("-");

        for (int i = 0; i < graph.vertexCount(); i++) {
            cmbR1StartModel.addElement("" + i);
            cmbR2StartModel.addElement("" + i);
            cmbR3StartModel.addElement("" + i);
        }
        comboRobot1Start.setModel(cmbR1StartModel);
        comboRobot2Start.setModel(cmbR2StartModel);
        comboRobot3Start.setModel(cmbR3StartModel);
        initNodesPositions();
        this.pack();

        contextRender.addMouseMotionListener(new MouseInputAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (selectedNodeIndex > -1) {
                    draggingNodeIndex = selectedNodeIndex;
                    selectedNodeIndex = -1;
                }
                if (draggingNodeIndex > -1) {
                    if (e.getX() <= width + padding && e.getX() >= padding && e.getY() >= padding && e.getY() <= padding + height) {
                        nodePositions[draggingNodeIndex] = new Point2D.Double(e.getX(), e.getY());
                    } else {
                        draggingNodeIndex = -1;
                    }
                    updateView();
                }
            }
        });
        contextRender.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                safeRemoveDialog(edgeDialog);
                safeRemoveDialog(nodeDialog);
                int tmpNodeIndex = findNodeIndexAtPoint(point.x, point.y);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (selectedNodeIndex >= 0 && tmpNodeIndex >= 0 && tmpNodeIndex != selectedNodeIndex) {
                        // edit or add new edge between selected vertexes
                        Double weight = graph.getWeight(tmpNodeIndex, selectedNodeIndex);
                        selectedEdge = new SimpleWGraph.GraphEdge(selectedNodeIndex, tmpNodeIndex,
                                (weight == null ? 1 : weight));
                        selectedNodeIndex = -1;
                        edgeDialog = new EdgeParamsDialog(selectedEdge, weight != null, (evt) -> {
                            graph.setWeight(selectedEdge);
                            selectedEdge = null;
                            updateView();
                        });
                        edgeDialog.setVisible(true);
                    } else if (selectedNodeIndex >= 0 && tmpNodeIndex == selectedNodeIndex) {
                        // remove node
                        nodeDialog = new NodeParamsDialog(true, (evt) -> {
                            graph = DemoUtils.removeNode(graph, tmpNodeIndex);
                            removeNodeFromGraphics(tmpNodeIndex);
                            selectedNodeIndex = -1;
                            updateView();
                        });
                        nodeDialog.setVisible(true);
                    } else if (selectedNodeIndex < 0 && tmpNodeIndex >= 0) {
                        selectedNodeIndex = tmpNodeIndex;
                        selectedEdge = null;
                    }
                    if (tmpNodeIndex < 0) {
                        selectedEdge = findEdgeAtPoint(point.x, point.y);
                        draggingNodeIndex = -1;
                        selectedNodeIndex = -1;
                        if (selectedEdge != null) {
                            edgeDialog = new EdgeParamsDialog(selectedEdge, true, (evt) -> {
                                graph.setWeight(selectedEdge);
                                selectedEdge = null;
                                updateView();
                            });
                            edgeDialog.setVisible(true);
                        }
                    }
                    updateView();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    selectedNodeIndex = -1;
                    selectedEdge = null;
                    draggingNodeIndex = -1;
                    if (tmpNodeIndex < 0) {
                        // add node
                        nodeDialog = new NodeParamsDialog(false, (evt) -> {
                            graph = DemoUtils.addNode(graph);
                            addNodeToGraphics(point);
                            updateView();
                        });
                        nodeDialog.setVisible(true);
                    } else {
                        // remove node
                        nodeDialog = new NodeParamsDialog(true, (evt) -> {
                            graph = DemoUtils.removeNode(graph, tmpNodeIndex);
                            removeNodeFromGraphics(tmpNodeIndex);
                            updateView();
                        });
                        nodeDialog.setVisible(true);
                    }
                    updateView();
                }
            }
        });
        this.paint();
        setVisible(true);
        buttonGenerateGraph.addActionListener(e -> {
            getRandomGraph();
            updateView();
        });
    }

    private void getRandomGraph() {
        int vertexCount = Integer.parseInt(textFieldVertexCount.getText());
        double probability = Double.parseDouble(textFieldProbability.getText());
        int minWeight = Integer.parseInt(textFieldMinWeight.getText());
        int maxWeight = Integer.parseInt(textFieldMaxWeight.getText());
        graph = DemoUtils.createTestGraph(vertexCount, probability, minWeight, maxWeight);
    }

    private void safeRemoveDialog(JDialog dialog) {
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
        }
    }

    private int findNodeIndexAtPoint(int x, int y) {
        int res = -1;
        for (int i = 0; i < nodePositions.length; i++) {
            double distance = getDistance(nodePositions[i], new Point2D.Double(x, y));
            if (distance <= nodeRadius) {
                res = i;
                break;
            }
        }
        return res;
    }

    private SimpleWGraph.GraphEdge findEdgeAtPoint(int x, int y) {
        for (int i = 0; i < graph.vertexCount() - 1; i++) {
            for (int j = i + 1; j < graph.vertexCount(); j++) {
                if (graph.getWeight(i, j) != null) {
                    Point2D middlePoint = new Point2D.Double((nodePositions[i].getX() + nodePositions[j].getX()) / 2,
                            (nodePositions[i].getY() + nodePositions[j].getY()) / 2);
                    double distance = getDistance(middlePoint, new Point2D.Double(x, y));
                    if (distance < 10) {
                        return new SimpleWGraph.GraphEdge(i, j, graph.getWeight(i, j));
                    }
                }
            }
        }
        return null;
    }

    private double getDistance(Point2D p1, Point2D p2) {
        double difX = p1.getX() - p2.getX();
        double difY = p1.getY() - p2.getY();
        return Math.sqrt(difX * difX + difY * difY);
    }

    private void updateView() {
        this.paint();
    }

    private void paint() {

        Graphics2D g2d = graphicsContext.createGraphics();
        g2d.setRenderingHints(antialiasing);

        //Set up the font to print on the circles
        Font font = g2d.getFont();
        font = font.deriveFont(Font.BOLD, 14f);
        g2d.setFont(font);

        FontMetrics fontMetrics = g2d.getFontMetrics();

        //clear the background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, graphicsContext.getWidth(), graphicsContext.getHeight());

        drawEdges(nodePositions, selectedEdge, graph, g2d, fontMetrics);
        drawNodesCircle(nodePositions, nodeRadius, g2d, fontMetrics, selectedNodeIndex);

        g2d.dispose();
        //force the container for the context to re-paint itself
        contextRender.repaint();

    }

    private void generateNodesCircle(Point2D centerPoint, int radius, int nodesCount, int startIndex) {
        int i = 0;

        Random rnd = new Random();
        double nodeAngle = rnd.nextInt(360);
        while (i < nodesCount) {
            Line2D vectorToPersonCenter =
                    getVector(centerPoint, nodeAngle, radius);
            Point2D personCircleCenter = vectorToPersonCenter.getP2();
            nodePositions[startIndex + i] = personCircleCenter;
            nodeAngle += 360.0d / nodesCount;
            i++;
        }
    }

    private static void drawEdges(Point2D[] nodePositions, SimpleWGraph.GraphEdge selectedEdge, SimpleWGraph graph, Graphics2D g2d, FontMetrics fontMetrics) {
        for (int i = 0; i < graph.vertexCount() - 1; i++) {
            for (int j = i + 1; j < graph.vertexCount(); j++) {
                if (graph.getWeight(i, j) != null) {
                    Line2D edge = new Line2D.Double(nodePositions[i], nodePositions[j]);
                    String itemString = "" + Math.round(graph.getWeight(i, j));
                    Rectangle2D itemStringBounds = fontMetrics.getStringBounds(itemString, g2d);
                    if (selectedEdge != null && (selectedEdge.to() == j && selectedEdge.from() == i || selectedEdge.to() == i && selectedEdge.from() == j)) {
                        g2d.setColor(Color.gray);
                    } else {
                        g2d.setColor(Color.red);
                    }
                    double textX = (nodePositions[i].getX() + nodePositions[j].getX()) / 2;// - itemStringBounds.getWidth();
                    double textY = (nodePositions[i].getY() + nodePositions[j].getY()) / 2;// - itemStringBounds.getHeight();
                    g2d.drawString("" + Math.round(graph.getWeight(i, j)), (float) textX, (float) textY);
                    g2d.draw(edge);
                }
            }
        }
    }

    private static void drawNodesCircle(Point2D[] nodePositions, int nodeRadius, Graphics2D g2d, FontMetrics fontMetrics, int selectedNode) {
        int i = 0;


        while (i < nodePositions.length) {
            Ellipse2D nodeCircle = getCircleByCenter(nodePositions[i], nodeRadius);

            if (i == selectedNode) {
                g2d.setColor(Color.gray);
            } else {
                g2d.setColor(Color.orange);
            }
            g2d.fill(nodeCircle);
            g2d.setColor(Color.black);
            String itemString = "" + i;
            Rectangle2D itemStringBounds = fontMetrics.getStringBounds(itemString, g2d);
            double textX = nodePositions[i].getX() - (itemStringBounds.getWidth() / 2);
            double textY = nodePositions[i].getY() + (itemStringBounds.getHeight() / 4);
            g2d.drawString("" + i, (float) textX, (float) textY);
            i++;
        }
    }

    private static Line2D getVector(Point2D start, double degrees, double length) {
        //we just multiply the unit vector in the direction we want by the length
        //we want to get a vector of correct direction and magnitute
        double endX = start.getX() + (length * Math.sin(Math.PI * degrees / 180.0d));
        double endY = start.getY() + (length * Math.cos(Math.PI * degrees / 180.0d));
        Point2D end = new Point2D.Double(endX, endY);
        Line2D vector = new Line2D.Double(start, end);
        return vector;
    }

    private static Ellipse2D getCircleByCenter(Point2D center, double radius) {
        Ellipse2D.Double myCircle = new Ellipse2D.Double(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);
        return myCircle;
    }

    private void initNodesPositions() {
        Point2D areaCenter = new Point2D.Double((double) width / 2 + padding, (double) height / 2 + padding);

        int radius = Math.min(height / 2, width / 2) / 3;
        int nodesCount = Math.max(3, Math.ceilDiv(graph.vertexCount(), 5));
        nodePositions = new Point2D[graph.vertexCount()];
        generateNodesCircle(areaCenter, radius, nodesCount, 0);
        generateNodesCircle(areaCenter, radius * 2, Math.min(nodesCount * 2, graph.vertexCount() - nodesCount), nodesCount);
        generateNodesCircle(areaCenter, radius * 3, Math.min(nodesCount * 3, graph.vertexCount() - 3 * nodesCount), nodesCount * 3);
    }

    private void removeNodeFromGraphics(int removeNodeIndex) {
        Point2D[] tmp = new Point2D[graph.vertexCount()];
        int i = 0;
        int newI = 0;
        while (i < graph.vertexCount() + 1) {
            if (i != removeNodeIndex) {
                tmp[newI] = nodePositions[i];
                newI++;
            }
            i++;
        }
        nodePositions = tmp;
    }

    private void addNodeToGraphics(Point2D newNodePosition) {
        Point2D[] tmp = new Point2D[graph.vertexCount()];
        for (int i = 0; i < graph.vertexCount() - 1; i++) {
            tmp[i] = nodePositions[i];
        }
        tmp[graph.vertexCount() - 1] = newNodePosition;
        nodePositions = tmp;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JSplitPane splitPane1 = new JSplitPane();
        panelMain.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(contentPanel);
        contextRender.setText("");
        contentPanel.add(contextRender, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panelButtons);
        panelRobot3 = new JPanel();
        panelRobot3.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        panelButtons.add(panelRobot3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        comboRobot3Start = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("0");
        comboRobot3Start.setModel(defaultComboBoxModel1);
        panelRobot3.add(comboRobot3Start, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboRobot3Speed = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("1");
        defaultComboBoxModel2.addElement("2");
        comboRobot3Speed.setModel(defaultComboBoxModel2);
        panelRobot3.add(comboRobot3Speed, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Старт в:");
        panelRobot3.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Скорость:");
        panelRobot3.add(label2, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Робот 3");
        panelRobot3.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelRobot2 = new JPanel();
        panelRobot2.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        panelButtons.add(panelRobot2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        comboRobot2Start = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("0");
        comboRobot2Start.setModel(defaultComboBoxModel3);
        panelRobot2.add(comboRobot2Start, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboRobot2Speed = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("1");
        defaultComboBoxModel4.addElement("2");
        comboRobot2Speed.setModel(defaultComboBoxModel4);
        panelRobot2.add(comboRobot2Speed, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Старт в:");
        panelRobot2.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Скорость:");
        panelRobot2.add(label5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Робот 2");
        panelRobot2.add(label6, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelRobot1 = new JPanel();
        panelRobot1.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        panelButtons.add(panelRobot1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        comboRobot1Start = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel5 = new DefaultComboBoxModel();
        defaultComboBoxModel5.addElement("0");
        comboRobot1Start.setModel(defaultComboBoxModel5);
        panelRobot1.add(comboRobot1Start, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboRobot1Speed = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel6 = new DefaultComboBoxModel();
        defaultComboBoxModel6.addElement("1");
        defaultComboBoxModel6.addElement("2");
        comboRobot1Speed.setModel(defaultComboBoxModel6);
        panelRobot1.add(comboRobot1Speed, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Старт в:");
        panelRobot1.add(label7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Скорость:");
        panelRobot1.add(label8, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Робот 1");
        panelRobot1.add(label9, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panelButtons.add(separator1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panelAnimation = new JPanel();
        panelAnimation.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelButtons.add(panelAnimation, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonRun = new JButton();
        buttonRun.setText("Запустить");
        panelAnimation.add(buttonRun, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panelAnimation.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panelAnimation.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panelGraph = new JPanel();
        panelGraph.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelButtons.add(panelGraph, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldVertexCount = new JTextField();
        textFieldVertexCount.setText("12");
        panelGraph.add(textFieldVertexCount, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Количество вершин:");
        panelGraph.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldProbability = new JTextField();
        textFieldProbability.setText("0.4");
        panelGraph.add(textFieldProbability, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonGenerateGraph = new JButton();
        buttonGenerateGraph.setText("Сгенерировать случайный граф");
        panelGraph.add(buttonGenerateGraph, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldMinWeight = new JTextField();
        textFieldMinWeight.setText("1");
        panelGraph.add(textFieldMinWeight, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textFieldMaxWeight = new JTextField();
        textFieldMaxWeight.setText("1");
        panelGraph.add(textFieldMaxWeight, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Вероятность связи:");
        panelGraph.add(label11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Минимальный вес:");
        panelGraph.add(label12, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Максимальный вес");
        panelGraph.add(label13, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panelGraph.add(spacer4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panelGraph.add(spacer5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panelGraph.add(spacer6, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panelGraph.add(spacer7, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

    private void createUIComponents() {
        antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphicsContext = new BufferedImage(width + (2 * padding), width + (2 * padding), BufferedImage.TYPE_INT_RGB);
        contextRender = new JLabel(new ImageIcon(graphicsContext));
    }
}

