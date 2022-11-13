package ru.vsu.cs.kg2022.borodin;

import java.util.List;

public class Figure {
    boolean rounded = false;
    private java.util.List<Line> lines;

    public Figure(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public boolean isRounded() {
        return rounded;
    }

    public void setRounded(boolean rounded) {
        this.rounded = rounded;
    }
}
