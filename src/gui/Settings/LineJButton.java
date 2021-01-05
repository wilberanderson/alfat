package gui.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class LineJButton extends JButton  {


    private Color color;
    private int btnPosX, btnPosY,  btnW,  btnH;
    private int btnLocX, btnLocY;
    private int lineX1, lineY1, lineX2, lineY2, lineW;

    public LineJButton() {
        //Default to values
        btnPosX = 0;
        btnPosY =0;
        btnW = 10;
        btnH = 50;

        btnLocX = 100;
        btnLocY = 0;

        lineX1 = 0;
        lineY1 = 0;
        lineX2 =0;
        lineY2 = 30;
        lineW = 5;
        color = Color.RED;

        setBounds(btnPosX,btnPosY, btnW, btnH);
        setLocation(btnLocX, btnLocY);
    }


    public LineJButton(Color color, int lineX1, int lineY1, int lineX2, int lineY2, int lineW) {
        this.lineX1 = lineX1;
        this.lineY1 = lineY1;
        this.lineX2 = lineX2;
        this.lineY2 = lineY2;
        this.lineW = lineW;
        this.color = color;
    }




    public void drawGraphic() {
        repaint();
        setOpaque(true);
    }



    public void paintComponent(Graphics g) {
        setOpaque(true);
        setContentAreaFilled(false);
        setBorderPainted(false);
        super.paintComponent(g);
//        g.setColor(Color.RED);
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
//        g2.setStroke(new BasicStroke(5));
//        g2.drawLine(0,0,0,30);
        g2.setStroke(new BasicStroke(lineW));
        g2.drawLine(lineX1,lineY1,lineX2,lineY2);
    }

}