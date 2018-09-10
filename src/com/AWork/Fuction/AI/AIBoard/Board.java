package com.AWork.Fuction.AI.AIBoard;

import com.AWork.Piece.Class.*;
import com.TeamWork.Data.AI_Info.BaseBoard_Data;
import com.TeamWork.Data.AI_Info.Direction;
import com.TeamWork.Data.AI_Info.Level;
import com.TeamWork.Data.Global.Data;

import java.awt.Point;
import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这是代表AI视角的面板类
 * @author A
 * @version 1.6
 * @since 1.0
 *
 */

public class Board implements Data, BaseBoard_Data {

    private int minx, maxx, miny, maxy;
    private int currentPlayer = 0;
    private Stack<Point> history;
    private Piece[][] data;
    private Piece[] sorted;

    /**
     * 面板类的无参构造函数
     */
    public Board() {
        data = new Piece[BT][BT];
        for (int i = 0; i < BT; i++)
            for (int j = 0; j < BT; j++) {
                data[i][j] = new Piece(i, j);
                if (i == 0 || i == BT - 1 || j == 0 || j == BT - 1)
                    data[i][j].setColorPi(borderPiece);
            }

        history = new Stack<Point>();
    }

    /**
     * 用于AI算法中的深度拷贝
     * @param b 传入被拷贝的面板实例
     * @exception NullPointerException 可能出现传入空棋盘
     */

    public Board(Board b) {
        Piece[][] b_data = b.getData();
        Piece[] b_sorted = b.getSorted();
        data = new Piece[BT][BT];
        for (int i = 0; i < BT; i++)
            for (int j = 0; j < BT; j++) {
                data[i][j] = new Piece(i, j);
                Piece c = b_data[i][j];
                data[i][j].setSum(c.getSum());
                data[i][j].setDefence(c.getDefence());
                data[i][j].setOffense(c.getOffense());
                data[i][j].setColorPi(c.getColorPi());
            }
        sorted = new Piece[b_sorted.length];
        for (int i = 0; i < sorted.length; i++) {
            Piece c = b_sorted[i];
            sorted[i] = new Piece(c.getX(), c.getY());
            sorted[i].setSum(c.getSum());
            sorted[i].setOffense(c.getOffense());
            sorted[i].setDefence(c.getDefence());
            sorted[i].setColorPi(c.getColorPi());
        }
        currentPlayer = b.getPlayer();
        minx = b.minx;
        maxx = b.maxx;
        miny = b.miny;
        maxy = b.maxy;
        history = new Stack<Point>();
    }

    /**
     * 使AI开始记录棋局的方法，第一步为AI下，且默认黑棋，位置为最中间
     */

    public void start() {
        currentPlayer = blackPiece;
        putChess(CENTER, CENTER);
        minx = maxx = miny = maxy = CENTER;
    }

    /**
     * 重置整个面板的方法
     */

    public void reset() {
        for (int i = 1; i < BT - 1; i++)
            for (int j = 1; j < BT - 1; j++) {
                data[i][j].reset();
            }
        history.clear();
    }

    /**
     * 在面板上悔棋的方法，一次会移除两个棋子
     * @return 悔棋后下的那个棋子的坐标，为Point类型
     */

    public Point undo() {
        if (!history.isEmpty()) {
            Point p1 = history.pop();
            Point p2 = history.pop();
            data[p1.x][p1.y].setColorPi(emptyPiece);
            data[p2.x][p2.y].setColorPi(emptyPiece);
            return history.peek();
        }
        return null;
    }

    /**
     * 判断并在当前位置放置棋子
     * @param x 放置的X坐标
     * @param y 放置的Y坐标
     * @return 布尔值，代表是否可以放置棋子
     */

    public boolean putChess(int x, int y) {
        if (data[x][y].isEmpty()) {
            minx = Math.min(minx, x);
            maxx = Math.max(maxx, x);
            miny = Math.min(miny, y);
            maxy = Math.max(maxy, y);
            data[x][y].setColorPi(currentPlayer);
            history.push(new Point(x, y));
            trogglePlayer();
            sorted = getSortedChess(currentPlayer);
            System.out.printf("[" + (char) (64 + x) + (16 - y) + "]");
            return true;
        }
        return false;
    }

    /**
     * 切换当前棋子颜色参数的方法
     */

    private void trogglePlayer() {
        currentPlayer = 3 - currentPlayer;
    }

    /**
     * 用于计算同色棋子连接数量的方法
     * @param x 判断位置的X坐标
     * @param y 判断位置的Y坐标
     * @param dx 延伸的X轴上的方向
     * @param dy 延伸的Y轴上的方向
     * @param chess 检测的棋子颜色
     * @return 同方向连接的棋子总数
     */

    private int check(int x, int y, int dx, int dy, int chess) {
        int sum = 0;
        for (int i = 0; i < 4; ++i) {
            x += dx;
            y += dy;
            if (x < 1 || x > RC || y < 1 || y > RC) {
                break;
            }
            if (data[x][y].getColorPi() == chess) {
                sum++;
            } else {
                break;
            }
        }
        return sum;
    }

    /**
     * 判断当前棋局情况的方法
     * @return 胜利的棋子，有两种棋子，Gaming(0),Tied(3),.
     */

    public int isGameOver() {
        if (!history.isEmpty()) {
            int chess = (history.size() % 2 == 1) ? blackPiece : whitePiece;
            Point lastStep = history.peek();
            int x = (int) lastStep.getX();
            int y = (int) lastStep.getY();
            if (check(x, y, 1, 0, chess) + check(x, y, -1, 0, chess) >= 4) {

                return chess;
            }
            if (check(x, y, 0, 1, chess) + check(x, y, 0, -1, chess) >= 4) {

                return chess;
            }
            if (check(x, y, 1, 1, chess) + check(x, y, -1, -1, chess) >= 4) {

                return chess;
            }
            if (check(x, y, 1, -1, chess) + check(x, y, -1, 1, chess) >= 4) {

                return chess;
            }
        }

        for (int i = 0; i < RC; ++i) {
            for (int j = 0; j < RC; ++j)
                if (data[i][j].isEmpty()) {
                    return Gaming;
                }
        }

        return Tied;
    }

    /**
     * 对当前的棋子的落子估值进行排序的方法
     * @param player 传入棋子
     * @return 按从大到小排序的估值棋子类数组
     */

    public Piece[] getSortedChess(int player) {
        // 限制范围
        int px = Math.max(minx - 5, 1);
        int py = Math.max(miny - 5, 1);
        int qx = Math.min(maxx + 5, Board.BT - 1);
        int qy = Math.min(maxy + 5, Board.BT - 1);
        Piece[] temp = new Piece[(qx - px + 1) * (qy - py + 1)];
        int count = 0;
        for (int x = px; x <= qx; x++) {
            for (int y = py; y <= qy; y++) {
                temp[count] = new Piece(x, y);
                if (data[x][y].isEmpty()) {
                    data[x][y].clearDetail();
                    data[x][y].append("______________________________\n");
                    int o = getScore(x, y, player) + 1;
                    data[x][y].append("\n");
                    int d = getScore(x, y, 3 - player);
                    data[x][y].append("\n");
                    String cs = "[" + (char) (64 + x) + (16 - y) + "] ";
                    data[x][y].append(cs).append(" 攻击:" + o).append(" 防守:" + d)
                            .append("\n\n");
                    data[x][y].setOffense(o);
                    temp[count].setOffense(o);
                    data[x][y].setDefence(d);
                    temp[count].setDefence(d);
                    data[x][y].setSum(o + d);
                    temp[count].setSum(o + d);
                }
                count++;
            }
        }
        Arrays.sort(temp);
        return temp;
    }

    /**
     *在某点落下棋子的估值
     * @param x 落子的X坐标
     * @param y 落子的Y坐标
     * @param chess 落子的颜色
     * @return 估值
     */

    public int getScore(int x, int y, int chess) {
        data[x][y].append("-");
        Level l1 = getLevel(x, y, Direction.HENG, chess);
        data[x][y].append("|");
        Level l2 = getLevel(x, y, Direction.SHU, chess);
        data[x][y].append("/");
        Level l3 = getLevel(x, y, Direction.PIE, chess);
        data[x][y].append("\\");
        Level l4 = getLevel(x, y, Direction.NA, chess);
        return level2Score(l1, l2, l3, l4) + position[x - 1][y - 1];
    }

    /**
     * 在某点落下棋子后，方向形成的棋子形态
     * @param x 落子的X坐标
     * @param y 落子的Y坐标
     * @param direction 传入的代表方向的枚举类
     * @param chess 落子的颜色
     * @return 代表棋子形态的枚举类
     */

    public Level getLevel(int x, int y, Direction direction, int chess) {
        String seq, left = "", right = "";
        if (direction == Direction.HENG) {
            left = getHalfSeq(x, y, -1, 0, chess);
            right = getHalfSeq(x, y, 1, 0, chess);
        } else if (direction == Direction.SHU) {
            left = getHalfSeq(x, y, 0, -1, chess);
            right = getHalfSeq(x, y, 0, 1, chess);
        } else if (direction == Direction.PIE) {
            left = getHalfSeq(x, y, -1, 1, chess);
            right = getHalfSeq(x, y, 1, -1, chess);
        } else if (direction == Direction.NA) {
            left = getHalfSeq(x, y, -1, -1, chess);
            right = getHalfSeq(x, y, 1, 1, chess);
        }
        seq = left + chess + right;
        String rseq = new StringBuilder(seq).reverse().toString();
        data[x][y].append("\t" + seq + "\t");
        // seq2Level

        for (Level level : Level.values()) {
            Pattern pat = Pattern.compile(level.getRegex()[chess - 1]);
            Matcher mat = pat.matcher(seq);
            boolean rs1 = mat.find();
            mat = pat.matcher(rseq);
            boolean rs2 = mat.find();
            if (rs1 || rs2) {
                data[x][y].append(level.toString()).append("\n");
                return level;
            }
        }
        return Level.NULL;
    }

    /**
     * 返回棋局的形态的方法
     * @param x 当前查找的X坐标
     * @param y 当前查找的Y坐标
     * @param dx 延伸的X轴上的方向
     * @param dy 延伸的Y轴上的方向
     * @param chess 棋子的颜色
     * @return 代表棋子的形态字符串
     */

    private String getHalfSeq(int x, int y, int dx, int dy, int chess) {
        String sum = "";
        boolean isR = false;
        if (dx < 0 || (dx == 0 && dy == -1))
            isR = true;
        for (int i = 0; i < 5; ++i) {
            x += dx;
            y += dy;
            if (x < 1 || x > RC || y < 1 || y > RC) {
                break;
            }
            if (isR) {
                sum = data[x][y].getColorPi() + sum;
            } else
                sum = sum + data[x][y].getColorPi();

        }
        return sum;
    }

    /**
     * 将各个方向的棋子形态统计并初步估分
     * @param l1 方向一
     * @param l2 方向二
     * @param l3 方向三
     * @param l4 方向四
     * @return 估值
     */

    public int level2Score(Level l1, Level l2, Level l3, Level l4) {
        int size = Level.values().length;
        int[] levelCount = new int[size];
        for (int i = 0; i < size; i++) {
            levelCount[i] = 0;
        }
        levelCount[l1.getIndex()]++;
        levelCount[l2.getIndex()]++;
        levelCount[l3.getIndex()]++;
        levelCount[l4.getIndex()]++;
        int score = 0;
        if (levelCount[Level.GO_4.getIndex()] >= 2
                || levelCount[Level.GO_4.getIndex()] >= 1
                && levelCount[Level.ALIVE_3.getIndex()] >= 1)
            score = 10000;
        else if (levelCount[Level.ALIVE_3.getIndex()] >= 2)
            score = 5000;
        else if (levelCount[Level.SLEEP_3.getIndex()] >= 1
                && levelCount[Level.ALIVE_3.getIndex()] >= 1)
            score = 1000;
        else if (levelCount[Level.ALIVE_2.getIndex()] >= 2)
            score = 100;
        else if (levelCount[Level.SLEEP_2.getIndex()] >= 1
                && levelCount[Level.ALIVE_2.getIndex()] >= 1)
            score = 10;

        score = Math.max(
                score,
                Math.max(Math.max(l1.getScore(), l2.getScore()),
                        Math.max(l3.getScore(), l4.getScore())));
        return score;
    }

    /**
     * 返回棋盘情况的棋子二维数组的方法
     * @return 返回棋盘情况
     */

    public Piece[][] getData() {
        return data;
    }

    /**
     * 返回当前的排序数组的方法
     * @return 从大到小的排序数组的方法
     */

    public Piece[] getSorted() {
        return sorted;
    }

    /**
     * 返回当前玩家的方法
     * @return 代表当前玩家的参数
     */

    public int getPlayer() {
        return currentPlayer;
    }

    /**
     * 返回代表落子历史的二维数组的方法，0代表X坐标，1代表Y坐标
     * @return 代表落子历史的二维数组
     */

    public int[][] getHistory() {
        int length = history.size();
        int[][] array = new int[length][2];
        for (int i = 0; i < length; i++)
            for (int j = 0; j < 2; j++) {
                Point p = history.get(i);
                array[i][0] = (int) p.getX();
                array[i][1] = (int) p.getY();
            }
        return array;
    }
}
