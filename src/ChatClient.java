
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ChatClient {
	
	// �����Ա����
	/**
	 * �ͻ���ͨ���߳�
	 */
	private ClientThread clientThread;
	 
	JFrame frmtcp;
	private JTextField textServerIP;
	private JTextField textServerPort;
	private JTextArea textAreaRecord;
	private JButton btnSend;
	private JButton btnConnect;
	private JTextField textUsername;
	private JList<String> listUsers;
	private JLabel lblRoomInfo; 
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnBrocast;
	private JRadioButton rdbtnPrivateChat;
	private JTextArea textAreaMsg;
	private DefaultListModel<String> modelUsers;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatClient window = new ChatClient();
					window.frmtcp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatClient() {
		initialize();
		// ��ʼ����Ա����������û���
		modelUsers = new DefaultListModel<String>();
		listUsers.setModel(modelUsers);
		// ��������û���
		Random rand = new Random();
		textUsername.setText("�û�" + rand.nextInt(100));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmtcp = new JFrame();
		frmtcp.setResizable(false);
		frmtcp.setTitle("TCP\u804A\u5929\u5BA4\u5BA2\u6237\u7AEF");
		frmtcp.setBounds(100, 100, 715, 476);
		frmtcp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmtcp.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u670D\u52A1\u5668IP\u5730\u5740:");
		lblNewLabel.setBounds(14, 24, 121, 18);
		frmtcp.getContentPane().add(lblNewLabel);
		
		textServerIP = new JTextField();
		textServerIP.setText("127.0.0.1");
		textServerIP.setBounds(121, 21, 121, 24);
		frmtcp.getContentPane().add(textServerIP);
		textServerIP.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u7AEF\u53E3\u53F7:");
		lblNewLabel_1.setBounds(256, 24, 58, 18);
		frmtcp.getContentPane().add(lblNewLabel_1);
		
		textServerPort = new JTextField();
		textServerPort.setText("8000");
		textServerPort.setBounds(313, 21, 48, 24);
		frmtcp.getContentPane().add(textServerPort);
		textServerPort.setColumns(10);
		
		btnConnect = new JButton("\u767B\u5F55");
		btnConnect.setBounds(596, 20, 95, 27);
		frmtcp.getContentPane().add(btnConnect);
		// ���Ӱ�ť�¼��������
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// �жϵ�ǰ��¼״̬
				if (btnConnect.getText().equals("��¼")) {
					// �����ͻ���ͨ���߳�
					clientThread = new ClientThread();
					clientThread.start();
				} else {
					clientThread.logout();
				}
				
			}
		});
		
		JLabel lblNewLabel_2 = new JLabel("\u6D88\u606F:");
		lblNewLabel_2.setBounds(14, 313, 72, 18);
		frmtcp.getContentPane().add(lblNewLabel_2);
		
		btnSend = new JButton("\u53D1\u9001");
		btnSend.setEnabled(false);
		btnSend.setBounds(444, 409, 88, 27);
		frmtcp.getContentPane().add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(null!=clientThread)
					clientThread.sendChatMag();
			}
		});
		JLabel label = new JLabel("\u6D88\u606F\u8BB0\u5F55");
		label.setBounds(14, 55, 72, 18);
		frmtcp.getContentPane().add(label);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 72, 518, 228);
		frmtcp.getContentPane().add(scrollPane);
		
		textAreaRecord = new JTextArea();
		textAreaRecord.setEditable(false);
		scrollPane.setViewportView(textAreaRecord);
		
		JLabel label_1 = new JLabel("\u7528\u6237\u540D:");
		label_1.setBounds(375, 24, 58, 18);
		frmtcp.getContentPane().add(label_1);

		textUsername = new JTextField();
		textUsername.setBounds(433, 21, 95, 24);
		frmtcp.getContentPane().add(textUsername);
		textUsername.setColumns(10);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 332, 518, 73);
		frmtcp.getContentPane().add(scrollPane_1);
		
		textAreaMsg = new JTextArea();
		scrollPane_1.setViewportView(textAreaMsg);
		
		JLabel label_2 = new JLabel("\u5728\u7EBF\u7528\u6237");
		label_2.setBounds(543, 55, 72, 18);
		frmtcp.getContentPane().add(label_2);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(546, 72, 149, 334);
		frmtcp.getContentPane().add(scrollPane_2);
		
		listUsers = new JList<String>();
		listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_2.setViewportView(listUsers);
		lblRoomInfo = new JLabel("\u5171\u4EBA");
		lblRoomInfo.setBounds(546, 413, 145, 18);
		frmtcp.getContentPane().add(lblRoomInfo);
		rdbtnBrocast = new JRadioButton("\u53D1\u9001\u7ED9\u6240\u6709\u4EBA");
		buttonGroup.add(rdbtnBrocast);
		rdbtnBrocast.setBounds(106, 309, 132, 22);
		frmtcp.getContentPane().add(rdbtnBrocast);
		rdbtnPrivateChat = new JRadioButton("\u79C1\u804A");
	    buttonGroup.add(rdbtnPrivateChat);
		rdbtnPrivateChat.setSelected(true);
		rdbtnPrivateChat.setBounds(256, 309, 157, 22);
		frmtcp.getContentPane().add(rdbtnPrivateChat);
		// ������ʽ
		try {
			String style = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			UIManager.setLookAndFeel(style);
		// ���´�����ʽ
			SwingUtilities.updateComponentTreeUI(this.frmtcp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class ClientThread extends Thread {
		/**
		 * ͨ���׽���
		 */
		private  Socket socket;
		
		/**
		 * ��������������
		 */
		private DataInputStream dis;
		
		/**
		 * �������������
		 */
		private DataOutputStream dos;
		
		/**
		 * �Ƿ��¼
		 */
		private boolean isLogged;
		
		/**
		 * ���ӷ���������¼
		 */
		void sendChatMag()
		{
			String msgChat=null;
			if(rdbtnBrocast.isSelected()){
				msgChat="TALKTO_ALL#"+textAreaMsg.getText();
				
			}
			if(rdbtnPrivateChat.isSelected()){
				String toUsername=(String)listUsers.getSelectedValue();
				msgChat="TALKTO#"+toUsername+"#"+textAreaMsg.getText();
			}
			if(null!=msgChat){
				try {
					dos.writeUTF(msgChat);
					dos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		void logout()
		{
			try {
				String msgLogout="LOGOUT";
				dos.writeUTF(msgLogout);
				dos.flush();
				isLogged=false;
				socket.close();
				modelUsers.clear();
				btnSend.setEnabled(false);
				btnConnect.setText("��¼");
				addMsg("�Ѿ��˳�������");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private void login() throws IOException{
			
			// ��ȡ������IP��ַ�Ͷ˿�
			String serverIp = textServerIP.getText();
			int serverPort = Integer.parseInt(textServerPort.getText());
			// ���ӷ���������ȡ�׽���IO��
			socket = new Socket(serverIp, serverPort);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			// ��ȡ�û��������������͵�¼����
			String username = textUsername.getText();
			String msgLogin = "LOGIN#" + username;
			dos.writeUTF(msgLogin);
			dos.flush();
			// ��ȡ���������ص���Ϣ���ж��Ƿ��¼�ɹ�
			String response = dis.readUTF();
			// ��¼ʧ��
			if(response.equals("FAIL")) {
				addMsg("��¼������ʧ��");
				// ��¼ʧ�ܣ��Ͽ����ӣ������ͻ����߳�
				socket.close();
				return;
			}
			// ��¼�ɹ�
			if(response.equals("SUCCESS")) {
				addMsg("��¼�������ɹ�");
				isLogged = true;
				btnConnect.setText("�˳�");
				btnSend.setEnabled(true);
			}
		}

		@Override
		public void run() {
			// ���ӷ���������¼
			try {
				login();
			} catch (IOException e) {
				addMsg("���ӵ�¼������ʱ�����쳣");
				e.printStackTrace();
				return;
			}
			while(isLogged) {
				try {
					String msg = dis.readUTF();
					String[] parts = msg.split("#");
					switch (parts[0]) {
					// ����������������û��б���
					case "USERLIST":
						for(int i = 1; i< parts.length; i++) {
							modelUsers.addElement(parts[i]);
						}
						lblRoomInfo.setText("��"+listUsers.getModel().getSize());
						
						break;
					// ������������������û���¼����
					case "LOGIN":
						modelUsers.addElement(parts[1]);
						lblRoomInfo.setText("��"+listUsers.getModel().getSize());
						break;
					case "LOGOUT":
						modelUsers.removeElement(parts[1]);
						lblRoomInfo.setText("��"+listUsers.getModel().getSize());
						break;
					case "TALKTO_ALL":
						addMsg(parts[1]+"��������˵:"+parts[2]);
						
						break;
					case "TALKTO":
						
						addMsg(parts[1]+"����˵:"+parts[2]);
						break;
					default:
						break;
					}
				} catch (IOException e) {
					// TODO �����쳣
					isLogged = false;
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * �����Ϣ���ı���textAreaRecord
	 * @param msg��Ҫ��ӵ���Ϣ
	 */
	private void addMsg(String msg) {
		// ���ı��������һ����Ϣ�������ϻ���
		textAreaRecord.append(msg + "\n");
		// �Զ��������ı��������һ��
		textAreaRecord.setCaretPosition(textAreaRecord.getText().length());
	}

}
