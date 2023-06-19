package task08;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import graph.SimpleWGraph;
import org.apache.xmlgraphics.util.dijkstra.Edge;
import util.DemoUtils;

import java.awt.*;
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
    private JButton button1;
    private JPanel contentPanel = new JPanel();
    private JPanel panelButtons;

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

        this.graph = DemoUtils.createTestGraph(numberOfNodes);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setContentPane(panelMain);
        //take advantage of auto-sizing the window based on the size of its contents
        this.pack();
        this.setLocationRelativeTo(null);

        this.graph = DemoUtils.createTestGraph(numberOfNodes);
        initNodesPositions();

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

                if (SwingUtilities.isLeftMouseButton(e)) {
                    int tmpNodeIndex = findNodeIndexAtPoint(point.x, point.y);
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
                        nodeDialog = new NodeParamsDialog((evt) -> {
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
//                    nodeDialogParams.updateView();
//                    nodeDialogParams.setVisible(true);
                }
            }
        });
        this.paint();
        setVisible(true);
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
        panelMain.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(contentPanel);
        contextRender.setText("");
        contentPanel.add(contextRender, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panelButtons);
        button1 = new JButton();
        button1.setText("Button");
        panelButtons.add(button1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panelButtons.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panelButtons.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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

