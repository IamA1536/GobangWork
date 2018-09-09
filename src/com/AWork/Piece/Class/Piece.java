package com.AWork.Piece.Class;


import com.TeamWork.Data.Global.Data;


public class Piece implements Comparable<Piece>, Data {

    public Piece() {

    }

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
        detail = new StringBuilder();
    }

    private int x;
    private int y;
    private int colorPi;
    private int offense;
    private int defence;
    private int sum;
    private StringBuilder detail;



    public int getX() {
        return x;
    }



    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }


    public void setY(int y) {
        this.y = y;
    }


    public int getColorPi() {
        return colorPi;
    }


    public void setColorPi(int colorPi) {
        this.colorPi = colorPi;
    }


    public int getOffense() {
        return offense;
    }


    public void setOffense(int offense) {
        this.offense = offense;
    }


    public int getDefence() {
        return defence;
    }


    public void setDefence(int defence) {
        this.defence = defence;
    }


    public int getSum() {
        return sum;
    }


    public void setSum(int sum) {
        this.sum = sum;
    }


    public String getDetail() {
        return detail.toString();
    }

    public StringBuilder append(String more) {
        return this.detail.append(more);
    }

    public void clearDetail() {
        detail = new StringBuilder();
    }


    public void reset() {
        clearDetail();
        offense = 0;
        defence = 0;
        sum = 0;
        colorPi = emptyPiece;
    }

    public String toString() {
        return String.format(x + "," + y + "-(" + (char) (64 + x) + ","
                + (16 - y) + ") " + offense + "," + defence + "," + sum);
    }

    public boolean isEmpty() {
        return colorPi == emptyPiece ? true : false;
    }


    @Override
    public int compareTo(Piece o) {
        if (o == null || sum == o.getSum()) {
            return 0;
        } else if (sum < o.getSum()) {
            return 1;
        } else if (sum > o.getSum()) {
            return -1;
        }
        return 0;
    }
}
