package com.AWork.Piece.Class;

import com.TeamWork.Data.Global.Data;

/**
 * ����������
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
     * ��������޲ι��캯��
     */

    public Piece() {

    }

    /**
     * ������Ĺ��캯��
     *
     * @param x ���ӵ�X����
     * @param y ���ӵ�Y����
     */

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
        detail = new StringBuilder();
    }

    /**
     * ������Ĺ��캯��
     *
     * @param x       ���ӵ�X����
     * @param y       ���ӵ�Y����
     * @param colorPi ���ӵ���ɫ
     */

    public Piece(int x, int y, int colorPi) {
        this.x = x;
        this.y = y;
        this.colorPi = colorPi;
        detail = new StringBuilder();
    }

    /**
     * ��ȡ����X����ķ���
     *
     * @return ���ӵ�X����
     */

    public int getX() {
        return x;
    }

    /**
     * �������ӵ�X����
     *
     * @param x ����X����
     */

    public void setX(int x) {
        this.x = x;
    }

    /**
     * ��ȡ����Y����ķ���
     *
     * @return ���ӵ�Y����
     */

    public int getY() {
        return y;
    }

    /**
     * �������ӵ�Y����
     *
     * @param y ����Y����
     */

    public void setY(int y) {
        this.y = y;
    }

    /**
     * ��ȡ������ɫ�ķ���
     *
     * @return ���ӵ���ɫ
     */

    public int getColorPi() {
        return colorPi;
    }

    /**
     * �������ӵ���ɫ
     *
     * @param colorPi �������ӵ���ɫ
     */

    public void setColorPi(int colorPi) {
        this.colorPi = colorPi;
    }

    /**
     * ��ȡ���ӽ����ֵķ���
     *
     * @return ���ӵĽ�����
     */

    public int getOffense() {
        return offense;
    }

    /**
     * �������ӵĽ�����
     *
     * @param offense �������ӵĽ�����
     */

    public void setOffense(int offense) {
        this.offense = offense;
    }

    /**
     * ��ȡ���ӷ��طֵķ���
     *
     * @return ���ӵķ��ط�
     */

    public int getDefence() {
        return defence;
    }

    /**
     * �������ӵķ��ط�
     *
     * @param defence �������ӵķ��ط�
     */

    public void setDefence(int defence) {
        this.defence = defence;
    }

    /**
     * ��ȡ�����ۺϷֵķ���
     *
     * @return ���ӵ��ۺϷ�
     */

    public int getSum() {
        return sum;
    }

    /**
     * �������ӵ��ۺϷ�
     *
     * @param sum �������ӵ��ۺϷ�
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
     * �������ӵķ���
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
     * �ж������Ƿ�Ϊ�����ӵķ���
     *
     * @return ����ֵ���Ƿ�Ϊ������
     */

    public boolean isEmpty() {
        return colorPi == emptyPiece ? true : false;
    }

    /**
     * �ȽϺ���
     *
     * @param o ����Ҫ�Ƚϵ�����
     * @return �ȽϽ��
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
