package exercise7.step0;
import java.awt.EventQueue;
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

	private JFrame frmtcp;
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
		// 初始化成员变量和随机用户名
		modelUsers = new DefaultListModel<String>();
		listUsers.setModel(modelUsers);
		// 随机生成用户名
		Random rand = new Random();
		textUsername.setText("用户" + rand.nextInt(100));
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
		
		JLabel lblNewLabel_2 = new JLabel("\u6D88\u606F:");
		lblNewLabel_2.setBounds(14, 313, 72, 18);
		frmtcp.getContentPane().add(lblNewLabel_2);
		
		btnSend = new JButton("\u53D1\u9001");
		btnSend.setEnabled(false);
		btnSend.setBounds(444, 409, 88, 27);
		frmtcp.getContentPane().add(btnSend);
		
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
		// 更换样式
		try {
			String style = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			UIManager.setLookAndFeel(style);
		// 更新窗体样式
			SwingUtilities.updateComponentTreeUI(this.frmtcp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
