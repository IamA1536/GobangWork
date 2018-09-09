package com.AWork.Fuction.AI.AIBrain;

import com.AWork.Fuction.AI.AIBoard.Board;
import com.AWork.Piece.Class.Piece;
import com.TeamWork.Data.AI_Info.BaseBoard_Data;
import com.TeamWork.Data.AI_Info.Level;
import com.TeamWork.Data.Global.Data;

/**
 * 代表AI操作的AI类
 * @author A
 * @version 1.3
 * @since 10.0
 */
public class Brain implements Data, BaseBoard_Data {
    private Board bd;
    //	private int INFINITY = 1000000;
    private int movex, movey;
    private int level;
    private int node;

    /**
     * AI类的无参构造函数
     */

    public Brain(){

    }

    /**
     *  AI类的构造函数
     * @param bd
     * @param level
     * @param node
     */

    public Brain(Board bd, int level, int node) {
        this.bd = bd;
        this.level = level;
        this.node = node;
    }

    public int[] AIMove(int i) {
        if (i == Easy) return findOneBestStep();
        else if (i == Hard) return findTreeBestStep();
        return null;
    }

    private int[] findOneBestStep() {
        Piece[] arr = bd.getSortedChess(bd.getPlayer());
        Piece c = bd.getData()[arr[0].getX()][arr[0].getY()];
        int[] result = {c.getX(), c.getY()};
        return result;
    }


    private int[] findTreeBestStep() {
        alpha_beta(0, bd, -INFINITY, INFINITY);
        int[] result = {movex, movey};
        return result;
    }


    public int alpha_beta(int depth, Board board, int alpha, int beta) {
        if (depth == level || board.isGameOver() != 0) {
            Piece[] sorted = board.getSorted();
            Piece move = board.getData()[sorted[0].getX()][sorted[0].getY()];

            System.out.println("\t- " + "[" + (char) (64 + move.getX())
                    + (16 - move.getY()) + "]," + move.getSum());
            return move.getSum();
        }

        Board temp = new Board(board);
        Piece[] sorted = temp.getSorted();
        int score;
        for (int i = 0; i < node; i++) {
            int x = sorted[i].getX();
            int y = sorted[i].getY();

            if (depth >= 1) {
                System.out.println();
                for (int k = 0; k < depth; k++)
                    System.out.printf("\t");
            }

            if (!temp.putChess(x, y))
                continue;
            if (sorted[i].getOffense() >= Level.ALIVE_4.getScore()) {
                System.out.println("Attend to win");
                score = INFINITY + 1;
            } else if (sorted[i].getDefence() >= Level.ALIVE_4.getScore()) {
                System.out.println("Attend to lose");
                score = INFINITY;
            } else {
                score = alpha_beta(depth + 1, temp, alpha, beta);
            }
            temp = new Board(board);
            if (depth % 2 == 0) {// MAX
                if (score > alpha) {
                    alpha = score;
                    if (depth == 0) {
                        movex = x;
                        movey = y;
                    }
                }
                if (alpha >= beta) {
                    score = alpha;
                    // System.out.println(" beta剪枝");
                    return score;
                }
            } else {// MIN
                if (score < beta) {
                    beta = score;
                }
                if (alpha >= beta) {
                    score = beta;
                    // System.out.println(" alpha剪枝");
                    return score;
                }
            }

        }
        return depth % 2 == 0 ? alpha : beta;
    }

}
