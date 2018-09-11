package com.LCWork.NetWork;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.LNWork.Addition.GoBang;
import javafx.scene.control.Alert;

/**
 * 局域网对战的客户端类
 * @author LC
 * @version 1.1
 * @since 1.0
 */

public class Client implements Runnable{
	public static final int ROWS = 15;//行数
	public static final int COLUMNS = 15;//列数
	static int[][] chesses = new int[ROWS][COLUMNS];
	private String ip;
	private int port = 8080;//端口
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private boolean clicked = false;//鼠标点击一次后为true,发送一次坐标,再变为false
	private boolean turn = true;//轮到该方下棋为true，默认客户端先手
	private int row;//储存棋子的行，每次点击后改变
	private int col;//棋子的列
	private GoBang gobang;

	/**
	 * 客户端类的构造函数
	 * @param ip 传入的IP地址
	 * @param gobang 传入的后台类
	 */

	public Client(String ip, GoBang gobang){
		//UI省略
		this.ip = ip;
		this.gobang = gobang;
		//连接服务器，启动接受线程
	}

	/**
	 * 返回判断是否为玩家落子的参数
	 * @return 布尔值，为是否
	 */

	public boolean getTurn(){
		return turn;
	}

	/**
	 * 初始化客户端连接
	 * @return 布尔值，代表是否连接成功
	 */

	public boolean init(){
		try {
			socket = new Socket(ip,port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			System.out.println("连接成功");
			return true;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("警告");
			alert.setHeaderText("连接错误");
			alert.setContentText("可能是连接被拒绝或者超时");
			alert.showAndWait();
			return false;
		}
	}

	/**
	 * 用于传输落子坐标
	 * @param row 落子的X坐标
	 * @param col 落子的Y坐标
	 */

	public void setLocation(int row,int col){
		this.row = row;
		this.col = col;
		clicked = true;
	}

	/**
	 * 重写接口Runnable的方法，内部为连接及传输数据
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
						System.out.println("发送成功:"+location);
						pw.flush();
						clicked = false;
						turn = !turn;
						break;
					}else{
						System.out.println("坐标错误，请重新输入!");
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
						int c = Integer.parseInt(split[1].trim());//trim()函数去除首尾空格
						System.out.println("接受成功，对手下棋坐标为:"+r+","+c);
						gobang.AddPiece(r,c);
						chesses[r][c] = 1;;//存入棋子
						turn = !turn;
						break;
					}catch (Exception ex) {
						System.out.println("接受消息出错："+ex.getMessage());
						ex.printStackTrace();
						continue;
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("发送失败");
		}
	}
}
