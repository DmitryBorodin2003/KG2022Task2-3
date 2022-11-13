package ru.vsu.cs.kg2022.borodin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private ScreenConverter sc;
    private Line ox, oy;
    private java.util.List<Line> allLines = new ArrayList<>();
    private java.util.List<Figure> allFigures = new ArrayList<>();
    private Line currentLine = null;
    private Line editingLine = null;

    public DrawPanel() {
        sc = new ScreenConverter(-20, 20, 40, 40, 800, 600);
        ox = new Line(new RealPoint(-15, 0), new RealPoint(15, 0));
        oy = new Line(new RealPoint(0, -15), new RealPoint(0, 15));
        allFigures.clear();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    protected void paintComponent(Graphics gr) {

        Graphics2D g = (Graphics2D) gr;
        sc.setSw(getWidth());
        sc.setSh(getHeight());

        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D biG = bi.createGraphics();

        biG.setColor(Color.WHITE);
        biG.fillRect(0,0, getWidth(), getHeight());

        LineDrawer ld = new GraphicsLineDrawer(biG);
        //LineDrawer ld = new DDALineDrawer(new GraphicsPixelDrawer(biG));
        //LineDrawer ld = new BresenhamLineDrawer(new GraphicsPixelDrawer(biG));
        //LineDrawer ld = new WuLineDrawer(new GraphicsPixelDrawer(biG));

        biG.setColor(Color.BLUE);
        drawLine(ld, sc, ox);
        drawLine(ld, sc, oy);
        drawLine(ld, sc, currentLine);
        biG.setColor(Color.GRAY);
        for (Line l: allLines){
            drawLine(ld, sc, l);
        }
        for (Figure f: allFigures){
            findPoint(ld,biG, sc, f, 2);
        }

        biG.setColor(Color.RED);
        if (currentLine != null){
            drawLine(ld, sc, currentLine);
        }
        g.drawImage(bi, 0, 0, null);
        biG.dispose();
    }

    private static void drawLine(LineDrawer ld, ScreenConverter sc, Line l){
        if (l != null){
            ScreenPoint p1 = sc.r2s(l.getP1());
            ScreenPoint p2 = sc.r2s(l.getP2());
            ld.drawLine(p1.getC(), p1.getR(), p2.getC(), p2.getR());
        }
    }
    private static void findPoint(LineDrawer ld, Graphics2D biG, ScreenConverter sc, Figure f, int r){
        RealPoint p1 = null;
        RealPoint p2 = null;
        RealPoint pp = null;
        ScreenPoint sp1;
        ScreenPoint sp2;
        ScreenPoint spp;
        for (int i = 0; i < f.getLines().size(); i++) {
            for (int j = i + 1; j < f.getLines().size(); j++) {
                if (((f.getLines().get(i).getP1().equals(f.getLines().get(j).getP1())) || (f.getLines().get(i).getP1().equals(f.getLines().get(j).getP2())))){
                    //Рассматриваемая первая точка совпала с первой или второй j-той
                    pp = f.getLines().get(i).getP1();
                    p1 = pointFinder(f.getLines().get(i), pp, r);
                    p2 = pointFinder(f.getLines().get(j), pp, r);
                }
                if (((f.getLines().get(i).getP2().equals(f.getLines().get(j).getP1())) || (f.getLines().get(i).getP2().equals(f.getLines().get(j).getP2())))){
                    //Рассматриваемая вторая точка совпала с первой или второй j-той
                    pp = f.getLines().get(i).getP2();
                    p1 = pointFinder(f.getLines().get(i), pp, r);
                    p2 = pointFinder(f.getLines().get(j), pp, r);
                }

                sp1 = sc.r2s(p1);
                sp2 = sc.r2s(p2);
                spp = sc.r2s(pp);

                Line l1 = new Line(new RealPoint(p1.getX(), p1.getY()), new RealPoint(pp.getX(), pp.getY()));
                Line l2 = new Line(new RealPoint(p2.getX(), p2.getY()), new RealPoint(pp.getX(), pp.getY()));

                biG.setColor(Color.WHITE);
                drawLine(ld, sc, l1);
                drawLine(ld, sc, l2);

                int a1 = spp.getR();
                int b1 = spp.getC();
                int a2 = sp1.getR();
                int b2 = sp1.getC();
//                int a3 = sp2.getR();
//                int b3 = sp2.getC();
//                int x1 = a2 - a1;
//                int x2 = a3 - a1;
//                int y1 = b2 - b1;
//                int y2 = b3 - b1;

                double h1 = Math.sqrt((a1 - a2)*(a1 - a2) + (b1 - b2)*(b1 - b2));
                double tga = h1 / r;
                double angleRad = Math.atan(tga);

                //double angleRad = (Math.abs(x1*x2 + y1*y2))/(Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2));

                //System.out.println((int) ((180*atga)/Math.PI));
                //System.out.println(2 *(int) ((180*Math.acos(angleRad))/Math.PI));
                System.out.println((int) ((180 * angleRad) / Math.PI));

                biG.setColor(Color.BLACK);

                int a = Math.min(sp1.getC(), sp2.getC());
                int b = Math.min(sp1.getR(), sp2.getR());
                int c = Math.max(sp1.getC(), sp2.getC());
                int d = Math.max(sp1.getR(), sp2.getR());
                //biG.drawOval(c-5, d-5, 5, 5);
                biG.drawArc(a, b,  (c-a),  (d-b), 90, (int) ((180 * angleRad) / Math.PI));
            }
        }

    }

    private static RealPoint pointFinder(Line l1, RealPoint p, int r) {
        double x1 = l1.getP1().getX();
        double y1 = l1.getP1().getY();
        double x2 = l1.getP2().getX();
        double y2 = l1.getP2().getY();
        double l = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        double v = Math.sqrt(l * l + h * h);
        double f = (l * (v - r)) / v;
        RealPoint p1 = null;
        if (p.equals(l1.getP1())) {
            if (x1 > x2) {
                if (y1 > y2) {
                    double k = h - (h * (v - r)) / v;
                    p1 = new RealPoint(x2 + f, y1 - k);
                } else {
                    double k = h * (v - r) / v;
                    p1 = new RealPoint(x2 + f, y2 - k);
                }
            } else {
                if (y1 > y2) {
                    double k = h - (h * (v - r)) / v;
                    p1 = new RealPoint(x2 - f, y1 - k);
                } else {
                    double k = h * (v - r) / v;
                    p1 = new RealPoint(x2 - f, y2 - k);
                }
            }
        }
        if (p.equals(l1.getP2())) {
            if (x1 > x2) {
                if (y1 > y2) {
                    double k = h * (v - r) / v;
                    p1 = new RealPoint(x1 - f, y1 - k);
                } else {
                    double k = h - (h * (v - r) / v);
                    p1 = new RealPoint(x1 - f, y2 - k);
                }
            } else {
                if (y1 > y2) {
                    double k = h * (v - r) / v;
                    p1 = new RealPoint(x1 + f, y1 - k);
                } else {
                    double k = h - (h * (v - r) / v);
                    p1 = new RealPoint(x1 + f, y2 - k);
                }
            }
        }
        return p1;
    }

    private static boolean isNear(ScreenPoint p1, ScreenPoint p2, double eps){
        int dx = p1.getC() - p2.getC();
        int dy = p1.getR() - p2.getR();
        int d2 = dx*dx + dy*dy;
        return d2 < eps;
    }
    private static double distanceToLine(ScreenPoint lp1, ScreenPoint lp2, ScreenPoint cp){
        double a = lp2.getR() - lp1.getR();
        double b = -(lp2.getC() - lp1.getC());
        double e = - cp.getC()*b + cp.getR() * a;
        double f = a * lp1.getC() - b * lp1.getR();
        double y = (a*e-b*f)/(a*a+b*b);
        double x = (a*y-e)/b;
        return Math.sqrt((cp.getC()-x)*(cp.getC()-x) + (cp.getR()-y)*(cp.getR()-y));
    }
    private static boolean isPointInRect(ScreenPoint pr1, ScreenPoint pr2, ScreenPoint cp){
        return cp.getC() >= Math.min(pr1.getC(), pr2.getC()) && cp.getC() <= Math.max(pr1.getC(), pr2.getC()) && cp.getR() >= Math.min(pr1.getR(), pr2.getR()) && cp.getR() <= Math.max(pr1.getR(), pr2.getR());
    }
    private static boolean closeToLine(ScreenConverter sc, Line l, ScreenPoint p, int eps){
        ScreenPoint a = sc.r2s(l.getP1());
        ScreenPoint b = sc.r2s(l.getP2());
        return isNear(a, p, eps) || isNear(b, p, eps) || (distanceToLine(a, b, p) < eps && isPointInRect(a, b, p));
    }
    private static Line findLine(ScreenConverter sc, java.util.List<Line> lines, ScreenPoint searchPoint, int eps){
        for (Line l: lines){
            if (closeToLine(sc, l, searchPoint, eps)){
                return l;
            }
        }
        return null;
    }
    private static void lineConnector(java.util.List<Line> allLines, Line currentLine){
        int curp1x = (int) currentLine.getP1().getX();
        int curp1y = (int) currentLine.getP1().getY();
        int curp2x = (int) currentLine.getP2().getX();
        int curp2y = (int) currentLine.getP2().getY();
        for (Line l: allLines){
            if ((curp1x >= l.getP1().getX() - 2) && (curp1x <= l.getP1().getX() + 2) && (curp1y >= l.getP1().getY() - 2) && (curp1y <= l.getP1().getY() + 2) && l.getP1().isAvailable){
                currentLine.setP1(l.getP1());
                l.getP1().setAvailable(false);
            }
            if ((curp1x >= l.getP2().getX() - 2) && (curp1x <= l.getP2().getX() + 2) && (curp1y >= l.getP2().getY() - 2) && (curp1y <= l.getP2().getY() + 2) && l.getP2().isAvailable){
                currentLine.setP1(l.getP2());
                l.getP2().setAvailable(false);
            }
            if ((curp2x >= l.getP2().getX() - 2) && (curp2x <= l.getP2().getX() + 2) && (curp2y >= l.getP2().getY() - 2) && (curp2y <= l.getP2().getY() + 2) && l.getP2().isAvailable){
                currentLine.setP2(l.getP2());
                l.getP2().setAvailable(false);
            }
            if ((curp2x >= l.getP1().getX() - 2) && (curp2x <= l.getP1().getX() + 2) && (curp2y >= l.getP1().getY() - 2) && (curp2y <= l.getP1().getY() + 2) && l.getP1().isAvailable){
                currentLine.setP2(l.getP1());
                l.getP1().setAvailable(false);
            }
        }
    }

    private static void figureChecker(List<Figure> allFigures, List<Line> allLines){
        java.util.List<Line> neededLines = new ArrayList<>();
        for (Line l: allLines){
            if (!l.isUsedInFigure() && ((!l.getP1().isAvailable() && l.getP2().isAvailable()) || (l.getP1().isAvailable() && !l.getP2().isAvailable()))){
                return;
            }
        }
        for (Line l: allLines){
            if (!l.isUsedInFigure() && !l.getP1().isAvailable() && !l.getP2().isAvailable()){
                neededLines.add(l);
                l.setUsedInFigure(true);
            }
        }
        if (neededLines.size() > 1){
            allFigures.add(new Figure(neededLines));
        }
        //System.out.println(allFigures.size());
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private ScreenPoint prevPoint = null;
    @Override
    public void mousePressed(MouseEvent e) {

        if (SwingUtilities.isRightMouseButton(e)) {
            prevPoint = new ScreenPoint(e.getX(), e.getY());
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (editingLine == null){
                Line x = findLine(sc, allLines, new ScreenPoint(e.getX(), e.getY()), 3);
                if (x != null){
                    editingLine = x;
                } else {
                    RealPoint p = sc.s2r(new ScreenPoint(e.getX(), e.getY()));
                    currentLine = new Line(p, p);
                }
            } else {
                if (closeToLine(sc, editingLine, new ScreenPoint(e.getX(), e.getY()), 3)){
                    //todo
                } else {
                    editingLine = null;
                }
            }
        } else if (SwingUtilities.isMiddleMouseButton(e)){
            //r++;
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            prevPoint = null;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentLine != null) {
                currentLine.setP2(sc.s2r(new ScreenPoint(e.getX(), e.getY())));
                lineConnector(allLines, currentLine);
                allLines.add(currentLine);
                currentLine = null;
                figureChecker(allFigures, allLines);
            }
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            if (editingLine != null){
                if (closeToLine(sc, editingLine, new ScreenPoint(e.getX(), e.getY()), 4)){
                    allLines.remove(editingLine);
                }
                editingLine = null;
            }
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            ScreenPoint curPoint = new ScreenPoint(e.getX(), e.getY());
            RealPoint p1 = sc.s2r(curPoint);
            RealPoint p2 = sc.s2r(prevPoint);
            RealPoint delta = p2.minus(p1);
            sc.moveCorner(delta);
            prevPoint = curPoint;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentLine != null){
                currentLine.setP2(sc.s2r(new ScreenPoint(e.getX(), e.getY())));
            }
            else  if (editingLine != null){
                //
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private static final double SCALE_STEP = 0.05;
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicks = e.getWheelRotation();
        double scale = 1;
        double coef = 1 + SCALE_STEP*(clicks < 0 ? -1 : 1);
        for (int i = Math.abs(clicks); i > 0; i--) {
            scale *= coef;
        }
        sc.changeScale(scale);
        repaint();
    }
}
