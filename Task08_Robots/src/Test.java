import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
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

    private int numberOfNodes = 30;
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

    public Test(){
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
        this.paint();
        setVisible(true);
    }

    public void paint() {

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

        //set up the large circle
        Point2D largeCircleCenter = new Point2D.Double((double)width / 2 + padding, (double)height / 2 + padding);
        double largeCircleRadius = (double)width / 2 + 15;
        Ellipse2D largeCircle = getCircleByCenter(largeCircleCenter, largeCircleRadius);

        //here we build the small circle
        Point2D smallCircleCenter = new Point2D.Double();
        double smallCircleRadius = 15;
        //we need to make certain it is confined inside the larger circle
        //so we choose the following values carefully

        //we want to go a random direction from the circle, so chose an
        //angle randomly in any direction
        double smallCenterVectorAngle = random.nextDouble() * 360.0d;
        //and we want to be a random distance from the center of the large circle, but
        //we limit the distance based on the radius of the small circle to prevent it
        //from appearing outside the large circle
        double smallCenterVectorLength = random.nextDouble() * (largeCircleRadius - smallCircleRadius);
        Line2D vectorToSmallCenter = getVector(largeCircleCenter, smallCenterVectorAngle, smallCenterVectorLength);
        //the resulting end point of the vector is a random distance from the center of the large circle
        //in a random direction, and guaranteed to not place the small circle outside the large
        smallCircleCenter.setLocation(vectorToSmallCenter.getP2());
        Ellipse2D smallCircle = getCircleByCenter(smallCircleCenter, smallCircleRadius);

        int personCount = 5;

        //we create a list of the people in the circle to
        //prevent overlap
        ArrayList<Shape> people = new ArrayList<Shape>();
        people.add(smallCircle);
        int radius = Math.min(height / 2, width / 2) / 3;
        int nodesCount = Math.max(3, Math.ceilDiv(numberOfNodes, 5));
        drawNodesCircle(largeCircleCenter, radius, nodesCount, g2d, fontMetrics, 0);
        drawNodesCircle(largeCircleCenter, radius * 2, Math.min(nodesCount * 2, numberOfNodes - nodesCount),
                g2d, fontMetrics, nodesCount);
        drawNodesCircle(largeCircleCenter, radius * 3, Math.min(nodesCount * 3, numberOfNodes - 3 * nodesCount),
                g2d, fontMetrics, nodesCount * 3);

        g2d.dispose();
        //force the container for the context to re-paint itself
        contextRender.repaint();

    }
    private static void drawNodesCircle(Point2D centerPoint, int radius,  int nodesCount,
                                        Graphics2D g2d, FontMetrics fontMetrics, int startNumber) {
        int i = 0;

        int smallCircleRadius = 15;
        Random rnd = new Random();
        double nodeAngle = rnd.nextInt(360);
        while (i < nodesCount){
            double personCenterVectorLength = radius;
            Line2D vectorToPersonCenter =
                    getVector(centerPoint, nodeAngle, personCenterVectorLength);
            Point2D personCircleCenter = vectorToPersonCenter.getP2();
            Ellipse2D personCircle = getCircleByCenter(personCircleCenter, smallCircleRadius);


            nodeAngle += 360.0d / nodesCount;

            g2d.setColor(Color.orange);
            g2d.fill(personCircle);
            g2d.setColor(Color.black);
            String itemString = "" + i;
            Rectangle2D itemStringBounds = fontMetrics.getStringBounds(itemString, g2d);
            double textX = personCircleCenter.getX() - (itemStringBounds.getWidth() / 2);
            double textY = personCircleCenter.getY() + (itemStringBounds.getHeight()/ 2);
            g2d.drawString("" + (i + startNumber), (float)textX, (float)textY);
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

