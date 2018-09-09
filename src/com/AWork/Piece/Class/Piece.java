package com.AWork.Piece.Class;

import com.TeamWork.Data.Global.Data;

/**
 * 这是棋子类
 * @author A
 * @version 1.1
 * @since 1.0
 */

public class Piece implements Comparable<Piece>, Data {


    private int x;
    private int y;
    private int colorPi;
    private int offense;
    private int defence;
    private int sum;
    private StringBuilder detail;

    /**
     * 棋子类的无参构造函数
     */

    public Piece() {

    }

    /**
     * 棋子类的构造函数
     *
     * @param x 棋子的X坐标
     * @param y 棋子的Y坐标
     */

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
        detail = new StringBuilder();
    }

    /**
     * 棋子类的构造函数
     *
     * @param x       棋子的X坐标
     * @param y       棋子的Y坐标
     * @param colorPi 棋子的颜色
     */

    public Piece(int x, int y, int colorPi) {
        this.x = x;
        this.y = y;
        this.colorPi = colorPi;
        detail = new StringBuilder();
    }

    /**
     * 获取棋子X坐标的方法
     *
     * @return 棋子的X坐标
     */

    public int getX() {
        return x;
    }

    /**
     * 设置棋子的X坐标
     *
     * @param x 传入X坐标
     */

    public void setX(int x) {
        this.x = x;
    }

    /**
     * 获取棋子Y坐标的方法
     *
     * @return 棋子的Y坐标
     */

    public int getY() {
        return y;
    }

    /**
     * 设置棋子的Y坐标
     *
     * @param y 传入Y坐标
     */

    public void setY(int y) {
        this.y = y;
    }

    /**
     * 获取棋子颜色的方法
     *
     * @return 棋子的颜色
     */

    public int getColorPi() {
        return colorPi;
    }

    /**
     * 设置棋子的颜色
     *
     * @param colorPi 传入棋子的颜色
     */

    public void setColorPi(int colorPi) {
        this.colorPi = colorPi;
    }

    /**
     * 获取棋子进攻分的方法
     *
     * @return 棋子的进攻分
     */

    public int getOffense() {
        return offense;
    }

    /**
     * 设置棋子的进攻分
     *
     * @param offense 传入棋子的进攻分
     */

    public void setOffense(int offense) {
        this.offense = offense;
    }

    /**
     * 获取棋子防守分的方法
     *
     * @return 棋子的防守分
     */

    public int getDefence() {
        return defence;
    }

    /**
     * 设置棋子的防守分
     *
     * @param defence 传入棋子的防守分
     */

    public void setDefence(int defence) {
        this.defence = defence;
    }

    /**
     * 获取棋子综合分的方法
     *
     * @return 棋子的综合分
     */

    public int getSum() {
        return sum;
    }

    /**
     * 设置棋子的综合分
     *
     * @param sum 传入棋子的综合分
     */

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

    /**
     * 重置棋子的方法
     */

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

    /**
     * 判断棋子是否为空棋子的方法
     *
     * @return 布尔值，是否为空棋子
     */

    public boolean isEmpty() {
        return colorPi == emptyPiece ? true : false;
    }

    /**
     * 比较函数
     *
     * @param o 传入要比较的棋子
     * @return 比较结果
     */

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
