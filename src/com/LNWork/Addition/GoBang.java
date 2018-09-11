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
 * 五子棋的后台类
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
     * 后台类的构造方法，内置鼠标监听器，按键监听器等，用于配合其他类与UI
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
     * 后台加入棋子的方法
     *
     * @param x 传入棋子的X坐标
     * @param y 传入棋子的Y坐标
     */

    public void AddPiece(int x, int y) {
        System.out.println("成功进入");
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
                    // 白
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Black Win");
                    alert.setHeaderText("分出胜负");
                    alert.setContentText("黑棋胜");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                } else {
                    // 黑
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("White Win");
                    alert.setHeaderText("分出胜负");
                    alert.setContentText("白棋胜");
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
                    // 白
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("White Win");
                    alert.setHeaderText("分出胜负");
                    alert.setContentText("白棋胜");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                    bd.reset();
                } else {
                    // 黑
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Black Win");
                    alert.setHeaderText("分出胜负");
                    alert.setContentText("黑棋胜");
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
     * 获取画板的方法
     *
     * @return 后台的画板
     */

    public Canvas getC() {
        return c;
    }

    /**
     * 获取对应画笔的方法
     *
     * @return 后台的画笔
     */

    public GraphicsContext getG() {
        return g;
    }

    /**
     * 获取储存下棋历史的数组的方法
     *
     * @return 棋子(Piece)类的一维数组，用于储存下棋历史
     */

    public Piece[] getGolist() {
        return golist;
    }

    /**
     * 获取当前棋盘情况的方法
     *
     * @return 棋子(Piece)类的一维数组，用于储存当前棋盘情况
     */

    public int[][] getPiece() {
        return piece;
    }

    /**
     * 用于判断落子位置的合法性的数组
     * @param x 落子的X坐标
     * @param y 落子的Y坐标
     * @return 布尔值，表示是否可以落子
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
     * 用于将绝对的X坐标转换为相对坐标
     * @param x 传入的绝对X坐标
     * @return 传入的X坐标的相对坐标
     */

    public int changeX(int x) {
        return (x - 1) * 50 + 150;
    }

    /**
     * 用于将绝对的X坐标转换为相对坐标
     * @param y 传入的绝对Y坐标
     * @return 传入的Y坐标的相对坐标
     */

    public int changeY(int y) {
        return (y - 1) * 50 + 75;
    }

    /**
     * 根据全局变量绘制棋盘的方法
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
     * 用于清空数据的方法
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
     * 用于判断联机状态的方法
     * @return 布尔值，是否联机
     */

    public boolean isConnect() {
        return IsConnect;
    }

    /**
     * 用于修改联机状态的方法，
     * @param connect 传入布尔值，为要修改的联机状态
     */

    public void setConnect(boolean connect) {
        IsConnect = connect;
    }

    /**
     * 这是用于储存棋子绝对坐标的类
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
