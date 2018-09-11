package com.LNWork.Addition;

import com.LCWork.NetWork.Client;
import com.LCWork.NetWork.Server;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import com.AWork.Piece.Class.Piece;
import com.TeamWork.Data.AI_Info.BaseBoard_Data;
import com.TeamWork.Data.Global.Data;
import com.AWork.Fuction.AI.AIBoard.Board;
import com.AWork.Fuction.AI.AIBrain.Brain;
import com.AWork.Fuction.IsWin.IsWin;

/**
 * ������ĺ�̨��
 *
 * @author LN
 * @version 2.0
 * @since 1.0
 */

public class GoBang implements Data, BaseBoard_Data {
    public int x;
    public int y;

    private boolean IsConnect = false;
    public int Type = 0;
    private double temX = -1;
    private double temY = -1;
    public boolean judge = true;
    public int totalgonumber = 0;
    public boolean thatstart = false;
    public boolean IsAI = false;
    public boolean IsEm = true;
    Piece[][] boardPi = new Piece[BT][BT];// 11
    int[][] piece = new int[RC][RC];// 00
    Piece[] golist = new Piece[250];
    static coordinate[][] pos = new coordinate[15][15];
    Canvas c = new Canvas(891, 846);
    GraphicsContext g = c.getGraphicsContext2D();

    public Board bd;
    public Brain br;

    public Server server;
    public Client client;

    /**
     * ��̨��Ĺ��췽�����������������������������ȣ����������������UI
     */

    public GoBang() {
        DrawPan();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                pos[i][j] = new coordinate(150 + i * 50.0, 75 + j * 50.0);
            }
        }
        g.restore();

        c.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (IsConnect) {
                    if (Type == 1)
                        if (server.getTurn())
                            for (int i = 0; i < 15; i++)
                                for (int j = 0; j < 15; j++) {
                                    if ((event.getX() - pos[i][j].x) * (event.getX() - pos[i][j].x)
                                            + (event.getY() - pos[i][j].y) * (event.getY() - pos[i][j].y) < 625) {
                                        temX = pos[i][j].x;
                                        temY = pos[i][j].y;
                                        x = i;
                                        y = j;

                                    }
                                }
                    if (Type == 2)
                        if (client.getTurn()) {
                            for (int i = 0; i < 15; i++) {
                                for (int j = 0; j < 15; j++) {
                                    if ((event.getX() - pos[i][j].x) * (event.getX() - pos[i][j].x)
                                            + (event.getY() - pos[i][j].y) * (event.getY() - pos[i][j].y) < 625) {
                                        temX = pos[i][j].x;
                                        temY = pos[i][j].y;
                                        x = i;
                                        y = j;

                                    }
                                }
                            }
                        }
                } else if (!IsConnect) {
                    for (int i = 0; i < 15; i++)
                        for (int j = 0; j < 15; j++) {
                            if ((event.getX() - pos[i][j].x) * (event.getX() - pos[i][j].x)
                                    + (event.getY() - pos[i][j].y) * (event.getY() - pos[i][j].y) < 625) {
                                temX = pos[i][j].x;
                                temY = pos[i][j].y;
                                x = i;
                                y = j;

                            }
                        }
                }
                if (Type == 1)
                    server.setLocation(x, y);
                if (Type == 2)
                    client.setLocation(x, y);
                if (IsEm)
                    AddPiece(x, y);
            }

        });

    }

    /**
     * ��̨�������ӵķ���
     *
     * @param x �������ӵ�X����
     * @param y �������ӵ�Y����
     */

    public void AddPiece(int x, int y) {
        System.out.println("�ɹ�����");
        if (!IsConnect)
            for (int i = 0; i < totalgonumber; i++) {
                if (temX == changeX(golist[i].getX()) && temY == changeY(golist[i].getY()))
                    judge = false;
                else {
                    judge = true;
                }
            }

        if (judge && thatstart && putChess(x + 1, y + 1)) {
            golist[totalgonumber] = new Piece(x + 1, y + 1, totalgonumber % 2 + 1);

            piece[x][y] = totalgonumber % 2 + 1;
            if (golist[totalgonumber].getColorPi() == whitePiece)
                g.setFill(Color.WHITE);
            else
                g.setFill(Color.BLACK);
            g.fillOval(changeX(golist[totalgonumber].getX()) - 20, changeY(golist[totalgonumber].getY()) - 20, 40, 40);
            System.out.println((changeX(golist[totalgonumber].getX()) - 20) + " " + (changeY(golist[totalgonumber].getY()) - 20));
            totalgonumber += 1;
            System.out.println(x + " " + y);
            if (IsAI) {
                int[] best = br.AIMove(Hard);
                putChess(best[0], best[1]);
                golist[totalgonumber] = new Piece(best[0], best[1], totalgonumber % 2 + 1);
                piece[best[0] - 1][best[1] - 1] = totalgonumber % 2 + 1;
                if (golist[totalgonumber].getColorPi() == 2)
                    g.setFill(Color.WHITE);
                else
                    g.setFill(Color.BLACK);
                g.fillOval(changeX(golist[totalgonumber].getX()) - 20, changeY(golist[totalgonumber].getY()) - 20, 40,
                        40);

                totalgonumber += 1;
            }

        }

        if (IsAI) {
            if (bd.isGameOver() == whitePiece || bd.isGameOver() == blackPiece) {
                if (totalgonumber % 2 == 0) {
                    // ��
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Black Win");
                    alert.setHeaderText("�ֳ�ʤ��");
                    alert.setContentText("����ʤ");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                } else {
                    // ��
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("White Win");
                    alert.setHeaderText("�ֳ�ʤ��");
                    alert.setContentText("����ʤ");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                }
                bd.reset();

            }
        } else {

            if (IsWin.Checkborad(5, piece)) {

                if (totalgonumber % 2 == 0) {
                    // ��
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("White Win");
                    alert.setHeaderText("�ֳ�ʤ��");
                    alert.setContentText("����ʤ");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                    bd.reset();
                } else {
                    // ��
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Black Win");
                    alert.setHeaderText("�ֳ�ʤ��");
                    alert.setContentText("����ʤ");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                    bd.reset();
                }

            }
        }
    }

    /**
     * ��ȡ����ķ���
     *
     * @return ��̨�Ļ���
     */

    public Canvas getC() {
        return c;
    }

    /**
     * ��ȡ��Ӧ���ʵķ���
     *
     * @return ��̨�Ļ���
     */

    public GraphicsContext getG() {
        return g;
    }

    /**
     * ��ȡ����������ʷ������ķ���
     *
     * @return ����(Piece)���һά���飬���ڴ���������ʷ
     */

    public Piece[] getGolist() {
        return golist;
    }

    /**
     * ��ȡ��ǰ��������ķ���
     *
     * @return ����(Piece)���һά���飬���ڴ��浱ǰ�������
     */

    public int[][] getPiece() {
        return piece;
    }

    /**
     * �����ж�����λ�õĺϷ��Ե�����
     * @param x ���ӵ�X����
     * @param y ���ӵ�Y����
     * @return ����ֵ����ʾ�Ƿ��������
     */

    private boolean putChess(int x, int y) {
        if (IsAI) {
            if (bd.putChess(x, y)) {
                return true;
            }
            return false;
        } else {
            if (piece[x - 1][y - 1] == 0) {
                return true;
            }
            return false;
        }
    }

    /**
     * ���ڽ����Ե�X����ת��Ϊ�������
     * @param x ����ľ���X����
     * @return �����X������������
     */

    public int changeX(int x) {
        return (x - 1) * 50 + 150;
    }

    /**
     * ���ڽ����Ե�X����ת��Ϊ�������
     * @param y ����ľ���Y����
     * @return �����Y������������
     */

    public int changeY(int y) {
        return (y - 1) * 50 + 75;
    }

    /**
     * ����ȫ�ֱ����������̵ķ���
     */

    public void DrawPan() {
        g.save();
        g.setFill(Color.BURLYWOOD);
        g.fillRect(91, 50, 800, 750);

        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                if ((i + 1) % 2 != 0 && (j + 1) % 2 != 0)
                    g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                else if ((i + 1) % 2 == 0 && (j + 1) % 2 == 0)
                    g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                else
                    g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
            }
        }
    }

    /**
     * ����������ݵķ���
     */

    public void clearlist() {
        for (int i = 1; i < RC; i++)
            for (int j = 1; j < RC; j++)
                piece[i][j] = emptyPiece;
        for (int i = 0; i < totalgonumber; i++) {
            golist[i].setX(-1);
            golist[i].setY(-1);
            golist[i].setColorPi(emptyPiece);
        }
    }

    /**
     * �����ж�����״̬�ķ���
     * @return ����ֵ���Ƿ�����
     */

    public boolean isConnect() {
        return IsConnect;
    }

    /**
     * �����޸�����״̬�ķ�����
     * @param connect ���벼��ֵ��ΪҪ�޸ĵ�����״̬
     */

    public void setConnect(boolean connect) {
        IsConnect = connect;
    }

    /**
     * �������ڴ������Ӿ����������
     */
    public class coordinate {
        public double x;
        public double y;

        public coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

}
