package Test;

import com.AWork.Fuction.AI.AIBoard.Board;
import com.AWork.Fuction.AI.AIBrain.Brain;
import com.TeamWork.Data.Global.Data;

import java.util.Scanner;

/**
 * 测试用，硬核五子棋类
 */

public class Test0909 implements Data {
    private Board bd;
    private Brain br;

    public static void main(String[] args) {
        new Test0909().work();
    }

    private void work() {
        Scanner input = new Scanner(System.in);
        bd = new Board();
        br = new Brain(bd, 3, 5);
        bd.start();
        
        while (true) {
            System.out.println("Put a piece(player): ");
            int x = input.nextInt();
            int y = input.nextInt();
            if (x == -1 || y == -1) break;
            System.out.println("AIMove: ");
            if (putChess(x, y)) {
                System.out.println("Player DONE________________________________");
                int[] best = br.AIMove(Hard);
                putChess(best[0], best[1]);
                System.out.println("AI DONE___________________________");
            }
        }
    }

    private boolean putChess(int x, int y) {
        if (bd.putChess(x, y)) {
            int winSide = bd.isGameOver();
            if (winSide > 0) {
                bd.reset();
                return false;
            }
            return true;
        }
        return false;
    }
}