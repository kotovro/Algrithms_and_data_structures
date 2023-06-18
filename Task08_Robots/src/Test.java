import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Test extends JFrame {
    private int numberOfNodes = 25;
    private int nodeRadius = 15;

    private int selectedNodeIndex = -1;
    private Point2D[] nodePositions = new Point2D[numberOfNodes];
    //private Nodes ;
    private static final long serialVersionUID = 1L;
    private int width = 600;
    private int height = 600;
    private int padding = 50;
    private BufferedImage graphicsContext;
    private JPanel contentPanel = new JPanel();
    private JLabel contextRender;
    private Stroke dashedStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 2f, new float[] {3f, 3f}, 0f);
    private Stroke solidStroke = new BasicStroke(3.0f);
    private RenderingHints antialiasing;
    private Random random = new Random();

    public static void main(String[] args) {
        //you should always use the SwingUtilities.invodeLater() method
        //to perform actions on swing elements to make certain everything
        //is happening on the correct swing thread
        Runnable swingStarter = new Runnable()
        {
            @Override
            public void run(){
                new Test();
            }
        };

        SwingUtilities.invokeLater(swingStarter);
    }

    public Test() {
        antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphicsContext = new BufferedImage(width + (2 * padding), width + (2 * padding), BufferedImage.TYPE_INT_RGB);
        contextRender = new JLabel(new ImageIcon(graphicsContext));

        contentPanel.add(contextRender);
        contentPanel.setSize(width + padding * 2, height + padding * 2);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setContentPane(contentPanel);
        //take advantage of auto-sizing the window based on the size of its contents
        this.pack();
        this.setLocationRelativeTo(null);

        Point2D areaCenter = new Point2D.Double((double)width / 2 + padding, (double)height / 2 + padding);
        int radius = Math.min(height / 2, width / 2) / 3;
        int nodesCount = Math.max(3, Math.ceilDiv(numberOfNodes, 5));
        generateNodesCircle(areaCenter, radius, nodesCount, 0);
        generateNodesCircle(areaCenter, radius * 2, Math.min(nodesCount * 2, numberOfNodes - nodesCount), nodesCount);
        generateNodesCircle(areaCenter, radius * 3, Math.min(nodesCount * 3, numberOfNodes - 3 * nodesCount),nodesCount * 3);

        contextRender.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectedNodeIndex = -1;
                    for (int i = 0; i < nodePositions.length; i++) {
                        double difX = point.getX() - nodePositions[i].getX();
                        double difY = point.getY() - nodePositions[i].getY();
                        double distance = Math.sqrt(difX * difX + difY * difY);
                        if (distance <= nodeRadius) {
                            selectedNodeIndex = i;
                            break;
                        }
                    }
                    updateView();

                } else if (SwingUtilities.isRightMouseButton(e)) {

                }

            }
        });
        this.paint();
        setVisible(true);
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

        drawNodesCircle(nodePositions, nodeRadius, g2d, fontMetrics, selectedNodeIndex);

        g2d.dispose();
        //force the container for the context to re-paint itself
        contextRender.repaint();

    }
    private void generateNodesCircle(Point2D centerPoint, int radius,  int nodesCount, int startIndex) {
        int i = 0;

        Random rnd = new Random();
        double nodeAngle = rnd.nextInt(360);
        while (i < nodesCount){
            Line2D vectorToPersonCenter =
                    getVector(centerPoint, nodeAngle, radius);
            Point2D personCircleCenter = vectorToPersonCenter.getP2();
            nodePositions[startIndex + i] = personCircleCenter;
            nodeAngle += 360.0d / nodesCount;
            i++;
        }
    }
    private static void drawNodesCircle(Point2D[] nodePositions, int nodeRadius, Graphics2D g2d, FontMetrics fontMetrics, int selectedNode) {
        int i = 0;


        while (i < nodePositions.length){
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
            g2d.drawString("" + i, (float)textX, (float)textY);
            i++;
        }
    }
    private static Line2D getVector(Point2D start, double degrees, double length){
        //we just multiply the unit vector in the direction we want by the length
        //we want to get a vector of correct direction and magnitute
        double endX = start.getX() + (length * Math.sin(Math.PI * degrees/ 180.0d));
        double endY = start.getY() + (length * Math.cos(Math.PI * degrees/ 180.0d));
        Point2D end = new Point2D.Double(endX, endY);
        Line2D vector = new Line2D.Double(start, end);
        return vector;
    }

    private static Ellipse2D getCircleByCenter(Point2D center, double radius)
    {
        Ellipse2D.Double myCircle = new Ellipse2D.Double(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);
        return myCircle;
    }

}

