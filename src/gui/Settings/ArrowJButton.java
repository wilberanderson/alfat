package gui.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * Simple button that looks like a arrow head
 *
 * */
public class ArrowJButton extends JButton {
    private Color color;
    private int  lineW;

    private int polyX[];
    private int polyY[];

    public static int UP = 1;
    public static int DOWN = 2;
    public static int LEFT = 3;
    public static int RIGHT = 4;

    private void setArrowOrientation(int ori) {
        if(ori == UP) {
            polyX = new int[]{5,0,10};
            polyY = new int[]{0,5,5};
        } else if (ori == DOWN) {
            polyX = new int[]{0,5,10};
            polyY = new int[]{0,5,0};
        }else if (ori == LEFT) {
            polyX = new int[]{0,5,5};
            polyY = new int[]{5,10,0};
        }else if (ori == RIGHT) {
            polyX = new int[]{0,0,5};
            polyY = new int[]{0,10,5};
        } else {
            ///
        }
    }


    //Arrow orientation
    public ArrowJButton(Color color, int orientation) {
        setArrowOrientation(orientation);
        this.color = color;
        this.lineW = 0;
    }

    public void drawGraphic() {
        repaint();
        setOpaque(false);
    }

    public void paintComponent(Graphics g) {
        //setOpaque(true); show button bounds
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        super.paintComponent(g);
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(lineW));
        Polygon p = new Polygon(polyX,polyY,3);
        g.fillPolygon(p);
        g.drawPolygon(p);

    }

}
