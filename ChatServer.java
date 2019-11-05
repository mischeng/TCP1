package exercise7.step0;
import java.awt.EventQueue;

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
		
		btnStop = new JButton("\u505C\u6B62");
		btnStop.setEnabled(false);
		btnStop.setBounds(588, 23, 95, 27);
		frmTcp.getContentPane().add(btnStop);
		
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

}
