package ru.vsu.cs.kg2022.borodin;

public class Line {
    private RealPoint p1, p2;
    boolean usedInFigure = false;

    public Line(RealPoint p1, RealPoint p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public RealPoint getP1() {
        return p1;
    }

    public void setP1(RealPoint p1) {
        this.p1 = p1;
    }

    public RealPoint getP2() {
        return p2;
    }

    public void setP2(RealPoint p2) {
        this.p2 = p2;
    }

    public boolean isUsedInFigure() {
        return usedInFigure;
    }

    public void setUsedInFigure(boolean usedInFigure) {
        this.usedInFigure = usedInFigure;
    }

}
