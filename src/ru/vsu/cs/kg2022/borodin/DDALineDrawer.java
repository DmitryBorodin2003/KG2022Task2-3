package ru.vsu.cs.kg2022.borodin;

public class DDALineDrawer implements LineDrawer {
    private PixelDrawer pd;

    public DDALineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        double dx,dy,steps,x,y;
        double xc,yc;

        dx = x2 - x1;
        dy = y2 - y1;
        steps = Math.max(Math.abs(dx), Math.abs(dy));
        xc = dx/steps;
        yc = dy/steps;
        x = x1;
        y = y1;
        pd.drawPixel((int)x, (int)y);
        for (int i = 1; i <= steps; i++) {
            x += xc;
            y += yc;
            pd.drawPixel((int)x, (int)y);
        }
    }
}
