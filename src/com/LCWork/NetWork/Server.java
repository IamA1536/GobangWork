package com.LCWork.NetWork;

import com.LNWork.Addition.GoBang;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 局域网对战的服务端类
 * @author LC
 * @version 1.1
 * @version 1.0
 */

public class Server implements Runnable {

    public static final int ROWS = 15;//行数
    public static final int COLUMNS = 15;//列数
    static int[][] chesses = new int[ROWS][COLUMNS];
    private InputStream is;
    private OutputStream os;
    private boolean clicked = false;//鼠标点击一次后为true,发送一次坐标,再变为false
    private boolean turn = false;////轮到该方下棋为true，默认服务端后手
    private int row;//储存棋子的行，每次点击后改变
    private int col;//棋子的列
    private GoBang gobang;

    /**
     * 服务端的构造方法
     * @param gobang 传入后台类
     */

    public Server(GoBang gobang) {
        this.gobang = gobang;
        try {
            System.out.println("Open Port");
            ServerSocket ss = new ServerSocket(8080);
            System.out.println("Set Port");
            Socket socket = ss.accept();
//			System.out.println("Open Complete");
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 返回判断是否为玩家落子的参数
     *
     * @return 布尔值，为是否
     */

    public boolean getTurn() {
        return turn;
    }

    /**
     * 用于传输落子坐标
     * @param row 落子的X坐标
     * @param col 落子的Y坐标
     */

    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
        clicked = true;
    }

    /**
     * 重写接口Runnable的方法，内部为连接及传输数据
     */

    public void run() {
        try {
            Thread.sleep(1000);
            while (true) {
                while (true) {
                    try {
                        byte[] bytes = new byte[1024];
                        is.read(bytes);
                        String s = new String(bytes);
                        String[] split = s.split(",");
                        int r = Integer.parseInt(split[0]);
                        int c = Integer.parseInt(split[1].trim());//trim()函数去除首尾空格
                        System.out.println("接受成功，对手下棋坐标为:" + r + "," + c);
                        gobang.AddPiece(r, c);
                        chesses[r][c] = 1;
                        ;//存入棋子
                        turn = !turn;
                        break;
                    } catch (Exception ex) {
                        System.out.println("接受消息出错：" + ex.getMessage());
                        ex.printStackTrace();
                        continue;
                    }
                }
                while (true) {
                    if (!clicked || !turn) {
                        System.out.print("暂停");
                        continue;
                    }
                    System.out.print("继续");
                    String location = row + "," + col;
                    if (row >= 0 && row < ROWS && col >= 0 && col <= COLUMNS && chesses[row][col] == 0) {
                        chesses[row][col] = -1;
                        PrintWriter pw = new PrintWriter(os);
                        os.write(location.getBytes());
                        os.flush();
                        System.out.println("发送成功:" + location);
                        pw.flush();
                        clicked = false;
                        turn = !turn;
                        break;
                    } else {
                        System.out.println("坐标错误，请重新输入!");
                        continue;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("发送失败");
        }
    }

}