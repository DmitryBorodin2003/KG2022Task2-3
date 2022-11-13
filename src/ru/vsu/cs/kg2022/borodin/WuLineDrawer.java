package ru.vsu.cs.kg2022.borodin;

public class WuLineDrawer implements LineDrawer{
    private PixelDrawer pd;

    public WuLineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        if (x2 < x1){
            x1 += x2;
            x2 = x1 - x2;
            x1 -= x2;
            y1 += y2;
            y2 = y1 - y2;
            y1 -= y2;
        }
        int dx = x2 - x1;
        int dy = y2 - y1;
        if (dx == 0 || dy == 0){
            //Не сглаживаем ровно вертикальные и ровно горизонтальные линии
            return;
        }
        float gradient = 0;
        if (dx > dy){
            gradient = (float) dy / dx;
            float intery = y1 + gradient;
            pd.drawPixel(x1, y1);
            for (int i = x1; i < x2; ++i) {
                pd.drawPixel(i, (int) intery);
                pd.drawPixel(i, (int) intery + 1);
                intery+=gradient;
            }
            pd.drawPixel(x2, y2);
        } else {
            gradient = (float) dx/dy;
            float interx = x1 + gradient;
            pd.drawPixel(x1, y1);
            for (int i = y1; i < y2; ++i) {
                pd.drawPixel((int)interx, i);
                pd.drawPixel((int)interx+1, i);
                interx+=gradient;
            }
            pd.drawPixel(x2, y2);
        }
    }
}
