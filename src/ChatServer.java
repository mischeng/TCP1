
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class ChatServer {
	
	// 添加用于功能实现的成员变量
	/**
	 * 服务器套接字
	 */
	private ServerSocket server;
	
	/**
	 * 判断服务器是否在运行
	 */
	private boolean isRunning;
	
	/**
	 * 客户端映射，key -> String：客户端名称； value -> ClientHandler： 客户端处理线程
	 */
	private HashMap<String, ClientHandler> clientHandlerMap = new HashMap<String, ClientHandler>();
	

	private JFrame frmTcp;
	private JTextField textServerIP;
	private JTextField textPort;
	private JTable tblUsers;
	private JButton btnStart;
	private JButton btnStop;
	private JTextArea textAreaRecord;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatServer window = new ChatServer();
					window.frmTcp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTcp = new JFrame();
		frmTcp.setResizable(false);
		frmTcp.setTitle("TCP\u804A\u5929\u5BA4\u670D\u52A1\u5668");
		frmTcp.setBounds(100, 100, 700, 509);
		frmTcp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTcp.getContentPane().setLayout(null);
		
		JLabel lblip = new JLabel("\u670D\u52A1\u5668IP\u5730\u5740:");
		lblip.setBounds(10, 27, 105, 18);
		frmTcp.getContentPane().add(lblip);
		textServerIP = new JTextField();
		textServerIP.setText("0.0.0.0");
		textServerIP.setBounds(114, 24, 141, 24);
		frmTcp.getContentPane().add(textServerIP);
		textServerIP.setColumns(10);
		JLabel label = new JLabel("\u7AEF\u53E3\u53F7:");
		label.setBounds(293, 27, 72, 18);
		frmTcp.getContentPane().add(label);
		textPort = new JTextField();
		textPort.setText("8000");
		textPort.setBounds(356, 24, 86, 24);
		frmTcp.getContentPane().add(textPort);
		textPort.setColumns(10);
		
		btnStart = new JButton("\u542F\u52A8");
		btnStart.setBounds(479, 23, 95, 27);
		frmTcp.getContentPane().add(btnStart);
		// 启动按钮事件监听处理
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// 创建、启动服务器通信线程
				Thread serverThread = new Thread(new ServerThread());
				serverThread.start();
			}
		});
		
		btnStop = new JButton("\u505C\u6B62");
		btnStop.setEnabled(false);
		btnStop.setBounds(588, 23, 95, 27);
		frmTcp.getContentPane().add(btnStop);
		// 停止按钮事件监听处理
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					isRunning = false;
					// 关闭服务器套接字，清空客户端映射
					server.close();
					clientHandlerMap.clear();
					// 修改按钮状态
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					addMsg("服务器关闭成功");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		JLabel label_1 = new JLabel("\u6D88\u606F\u8BB0\u5F55");
		label_1.setBounds(10, 58, 72, 18);
		frmTcp.getContentPane().add(label_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 78, 673, 159);
		frmTcp.getContentPane().add(scrollPane);
		
		textAreaRecord = new JTextArea();
		textAreaRecord.setEditable(false);
		scrollPane.setViewportView(textAreaRecord);

		JLabel lblNewLabel = new JLabel("\u5728\u7EBF\u7528\u6237\u5217\u8868");
		lblNewLabel.setBounds(10, 250, 105, 18);
		frmTcp.getContentPane().add(lblNewLabel);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 273, 673, 191);
		frmTcp.getContentPane().add(scrollPane_1);
		
		tblUsers = new JTable();
		tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblUsers.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "\u5E8F\u53F7", "\u7528\u6237\u540D",
						"IP\u5730\u5740", "\u7AEF\u53E3\u53F7",
						"\u767B\u5F55\u65F6\u95F4" }) {
			boolean[] columnEditables = new boolean[] { true, true, true,
					false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblUsers.getColumnModel().getColumn(0).setPreferredWidth(56);
		tblUsers.getColumnModel().getColumn(1).setPreferredWidth(118);
		tblUsers.getColumnModel().getColumn(2).setPreferredWidth(126);
		tblUsers.getColumnModel().getColumn(3).setResizable(false);
		tblUsers.getColumnModel().getColumn(3).setPreferredWidth(61);
		tblUsers.getColumnModel().getColumn(4).setPreferredWidth(122);
		scrollPane_1.setViewportView(tblUsers);
		// 更换样式
		try {
			String style = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			UIManager.setLookAndFeel(style);
			// 更新窗体样式
			SwingUtilities.updateComponentTreeUI(this.frmTcp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	class ServerThread implements Runnable {
		
		/**
		 * 启动服务
		 */
		private void startServer() {
			try {
				// 获取serverIp 和 serverPort
				String serverIp = textServerIP.getText();
				int serverPort = Integer.parseInt(textPort.getText());
				// 创建套接字地址
				SocketAddress socketAddress = new InetSocketAddress(serverIp, serverPort);
				// 创建ServerSocket，绑定套接字地址
				server = new ServerSocket();
				server.bind(socketAddress);
				// 修改判断服务器是否运行的标识变量
				isRunning = true;
				// 修改启动和停止按钮状态
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				addMsg("服务器启动成功");
			} catch (IOException e) {
				addMsg("服务器启动失败，请检查端口是否被占用");
				e.printStackTrace();
				isRunning = false;
			}
		}

		/**
		 * 线程体
		 */
		@Override
		public void run() {
			startServer();
			// 当服务器处于运行状态时，循环监听客户端的连接请求
			while(isRunning) {
				try {
					Socket socket = server.accept();
					// 创建与客户端交互的线程
					Thread thread = new Thread(new ClientHandler(socket));
					thread.start();
				} catch (IOException e) {
					System.out.println("还没连接");
				}
			}
		}
		
	}
	
	class ClientHandler implements Runnable {
		private Socket socket;
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isConnected;
		private String username;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
			try {
			
				this.dis = new DataInputStream(socket.getInputStream());
				this.dos = new DataOutputStream(socket.getOutputStream());
				isConnected = true;
			} catch (IOException e) {
				isConnected = false;
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while(isRunning && isConnected) {
				try {
					// 读取客户端发送的报文
					String msg = dis.readUTF();
					String[] parts = msg.split("#");
					switch (parts[0]) {
					// 处理登录报文
					case "LOGIN":
						String loginUsername = parts[1];
						// 如果该用户名已登录，则返回失败报文，否则返回成功报文
						if(clientHandlerMap.containsKey(loginUsername)) {
							dos.writeUTF("FAIL");
						} else {
							dos.writeUTF("SUCCESS");
							// 将此客户端处理线程的信息添加到clientHandlerMap中
							clientHandlerMap.put(loginUsername, this);
							// 将现有用户的信息发给新用户
							StringBuffer msgUserList = new StringBuffer();
							msgUserList.append("USERLIST#");
							for(String username : clientHandlerMap.keySet()) {

								msgUserList.append(username + "#");
							}
							dos.writeUTF(msgUserList.toString());
							// 将新登录的用户信息广播给其他用户
							String msgLogin = "LOGIN#" + loginUsername;
							broadcastMsg(loginUsername, msgLogin);
							// 存储登录的用户名
							this.username = loginUsername;
						}
						break;
					case "LOGOUT":
						clientHandlerMap.remove(username);
						String msgLogout="LOGOUT#"+username;
						broadcastMsg(username, msgLogout);
						isConnected=false;
						socket.close();
						break;
					case "TALKTO_ALL":
						String msgTalkToAll="TALKTO_ALL#"+username+"#"+parts[1];
						broadcastMsg(username, msgTalkToAll);
						break;
					case "TALKTO":
						
						ClientHandler clientHandler=clientHandlerMap.get(parts[1]);
						if(null!=clientHandler){
							String msgTalkTo="TALKTO#"+username+"#"+parts[2];
							clientHandler.dos.writeUTF(msgTalkTo);
							clientHandler.dos.flush();
						}
						break;

					default:
						break;
					}
				} catch (IOException e) {
					isConnected = false;
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * 将某个用户发来的消息广播给其它用户
		 * @param fromUsername 发来消息的用户
		 * @param msg 需要广播的消息
		 */
		private void broadcastMsg(String fromUsername, String msg) throws IOException{
			for(String toUserName : clientHandlerMap.keySet()) {
				if(fromUsername.equals(toUserName) == false) {
					DataOutputStream dos = clientHandlerMap.get(toUserName).dos;
					dos.writeUTF(msg);
					dos.flush();
				}
			}
		}
	}
	
	/**
	 * 添加消息到文本框textAreaRecord
	 * @param msg，要添加的消息
	 */
	private void addMsg(String msg) {
		// 在文本区中添加一条消息，并加上换行
		textAreaRecord.append(msg + "\n");
		// 自动滚动到文本区的最后一行
		textAreaRecord.setCaretPosition(textAreaRecord.getText().length());
	}

}
