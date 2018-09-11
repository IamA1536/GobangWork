package com.LCWork.NetWork;

import com.LNWork.Addition.GoBang;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ��������ս�ķ������
 * @author LC
 * @version 1.1
 * @version 1.0
 */

public class Server implements Runnable {

    public static final int ROWS = 15;//����
    public static final int COLUMNS = 15;//����
    static int[][] chesses = new int[ROWS][COLUMNS];
    private InputStream is;
    private OutputStream os;
    private boolean clicked = false;//�����һ�κ�Ϊtrue,����һ������,�ٱ�Ϊfalse
    private boolean turn = false;////�ֵ��÷�����Ϊtrue��Ĭ�Ϸ���˺���
    private int row;//�������ӵ��У�ÿ�ε����ı�
    private int col;//���ӵ���
    private GoBang gobang;

    /**
     * ����˵Ĺ��췽��
     * @param gobang �����̨��
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
     * �����ж��Ƿ�Ϊ������ӵĲ���
     *
     * @return ����ֵ��Ϊ�Ƿ�
     */

    public boolean getTurn() {
        return turn;
    }

    /**
     * ���ڴ�����������
     * @param row ���ӵ�X����
     * @param col ���ӵ�Y����
     */

    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
        clicked = true;
    }

    /**
     * ��д�ӿ�Runnable�ķ������ڲ�Ϊ���Ӽ���������
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
                        int c = Integer.parseInt(split[1].trim());//trim()����ȥ����β�ո�
                        System.out.println("���ܳɹ���������������Ϊ:" + r + "," + c);
                        gobang.AddPiece(r, c);
                        chesses[r][c] = 1;
                        ;//��������
                        turn = !turn;
                        break;
                    } catch (Exception ex) {
                        System.out.println("������Ϣ����" + ex.getMessage());
                        ex.printStackTrace();
                        continue;
                    }
                }
                while (true) {
                    if (!clicked || !turn) {
                        System.out.print("��ͣ");
                        continue;
                    }
                    System.out.print("����");
                    String location = row + "," + col;
                    if (row >= 0 && row < ROWS && col >= 0 && col <= COLUMNS && chesses[row][col] == 0) {
                        chesses[row][col] = -1;
                        PrintWriter pw = new PrintWriter(os);
                        os.write(location.getBytes());
                        os.flush();
                        System.out.println("���ͳɹ�:" + location);
                        pw.flush();
                        clicked = false;
                        turn = !turn;
                        break;
                    } else {
                        System.out.println("�����������������!");
                        continue;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("����ʧ��");
        }
    }

}