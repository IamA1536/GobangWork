package com.LCWork.NetWork;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.LNWork.Addition.GoBang;
import javafx.scene.control.Alert;

/**
 * ��������ս�Ŀͻ�����
 * @author LC
 * @version 1.1
 * @since 1.0
 */

public class Client implements Runnable{
	public static final int ROWS = 15;//����
	public static final int COLUMNS = 15;//����
	static int[][] chesses = new int[ROWS][COLUMNS];
	private String ip;
	private int port = 8080;//�˿�
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private boolean clicked = false;//�����һ�κ�Ϊtrue,����һ������,�ٱ�Ϊfalse
	private boolean turn = true;//�ֵ��÷�����Ϊtrue��Ĭ�Ͽͻ�������
	private int row;//�������ӵ��У�ÿ�ε����ı�
	private int col;//���ӵ���
	private GoBang gobang;

	/**
	 * �ͻ�����Ĺ��캯��
	 * @param ip �����IP��ַ
	 * @param gobang ����ĺ�̨��
	 */

	public Client(String ip, GoBang gobang){
		//UIʡ��
		this.ip = ip;
		this.gobang = gobang;
		//���ӷ����������������߳�
	}

	/**
	 * �����ж��Ƿ�Ϊ������ӵĲ���
	 * @return ����ֵ��Ϊ�Ƿ�
	 */

	public boolean getTurn(){
		return turn;
	}

	/**
	 * ��ʼ���ͻ�������
	 * @return ����ֵ�������Ƿ����ӳɹ�
	 */

	public boolean init(){
		try {
			socket = new Socket(ip,port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			System.out.println("���ӳɹ�");
			return true;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("����");
			alert.setHeaderText("���Ӵ���");
			alert.setContentText("���������ӱ��ܾ����߳�ʱ");
			alert.showAndWait();
			return false;
		}
	}

	/**
	 * ���ڴ�����������
	 * @param row ���ӵ�X����
	 * @param col ���ӵ�Y����
	 */

	public void setLocation(int row,int col){
		this.row = row;
		this.col = col;
		clicked = true;
	}

	/**
	 * ��д�ӿ�Runnable�ķ������ڲ�Ϊ���Ӽ���������
	 */

	public void run(){
		try{
			Thread.sleep(1000);
			while(true){
				while(true){
					if(!clicked || !turn){
						System.out.print("");
						continue;
					}
					System.out.print("");
					String location = row+","+col;
					if(row >= 0 && row < ROWS && col >= 0 && col <= COLUMNS && chesses[row][col] == 0){
						chesses[row][col] = -1;
						PrintWriter pw = new PrintWriter(os);
						os.write(location.getBytes());
						os.flush();
						System.out.println("���ͳɹ�:"+location);
						pw.flush();
						clicked = false;
						turn = !turn;
						break;
					}else{
						System.out.println("�����������������!");
						continue;
					}
				}
				while(true){
					try{
						byte[] bytes = new byte[1024];
						is.read(bytes);
						String s = new String(bytes);
						String[] split = s.split(",");
						int r = Integer.parseInt(split[0]);
						int c = Integer.parseInt(split[1].trim());//trim()����ȥ����β�ո�
						System.out.println("���ܳɹ���������������Ϊ:"+r+","+c);
						gobang.AddPiece(r,c);
						chesses[r][c] = 1;;//��������
						turn = !turn;
						break;
					}catch (Exception ex) {
						System.out.println("������Ϣ����"+ex.getMessage());
						ex.printStackTrace();
						continue;
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("����ʧ��");
		}
	}
}
