/* Name: RegisterGUI.java 
 * Description: 注册用户界面
 * Author: 19301050-wumengning 
 * Date: 12-11-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import elements.Account;
import net.ClientNet;

public class RegisterGUI extends JFrame {
	//declare private attributes
	private JPanel contentPane, enterPanel, buttonPanel;
	private JPasswordField enterPassword, confirmPassword;
	private JTextField userID;
	private JLabel userName, password, passwordAgain;
	private JButton enter, cancel;
	private ClientNet connection = ClientNet.getNet();
	
	//constructor,initialize the attributes
	public RegisterGUI() {
		super("注册新用户");
		contentPane = new JPanel();
		enterPanel = new JPanel();
		userName = new JLabel("\u8BF7\u8F93\u5165\u7528\u6237\u540D\uFF1A   ");
		userID = new JTextField();
		password = new JLabel("\u8BF7\u8F93\u5165\u5BC6\u7801\uFF1A   ");
		enterPassword = new JPasswordField();
		passwordAgain = new JLabel("\u8BF7\u91CD\u590D\u8F93\u5165\u5BC6\u7801\uFF1A   ");
		confirmPassword = new JPasswordField();
		buttonPanel = new JPanel();
		enter = new JButton("\u786E\u5B9A");
		cancel = new JButton("\u53D6\u6D88");
		
		//add action listeners
		enter.addActionListener(new EnterHandler(this));
		cancel.addActionListener(new CancelHandler());
	}
	
	//launch the frame
	public void launchFrame() {
		//set the frame
		setBounds(100, 100, 576, 435);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(contentPane);
		
		//set the panel
		contentPane.setBounds(new Rectangle(0, 0, 180, 180));
		contentPane.setPreferredSize(new Dimension(180, 180));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		//set the enterPanel and add to contentPane
		enterPanel.setBorder(new EmptyBorder(80, 20, 0, 100));
		enterPanel.setLayout(new GridLayout(3, 2, 0, 15));
		contentPane.add(enterPanel);
		
		//set serverl components and add to enterPanel
		userName.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		userName.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterPanel.add(userName);
		
		userID.setColumns(10);
		enterPanel.add(userID);
		
		password.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		enterPanel.add(password);
		enterPanel.add(enterPassword);
		
		passwordAgain.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		enterPanel.add(passwordAgain);
		enterPanel.add(confirmPassword);
		
		//set buttonPanel and add to contentPane
		buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
		contentPane.add(buttonPanel);
		
		//add components to buttonPanel
		buttonPanel.add(enter);
		buttonPanel.add(cancel);
		
		//show the frame
		setVisible(true);
	}
	
	//inner class, enter button handler
	private class EnterHandler implements ActionListener{
		RegisterGUI registerFrame;
		//constructor
		public EnterHandler(RegisterGUI registerFrame) {
			this.registerFrame = registerFrame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//declare variables
			String user = userID.getText();
			String order;
			try {
				//if the user input id
				if(!user.equals("")) {
					//get password
					String pass = new String(enterPassword.getPassword());
					String again = new String(confirmPassword.getPassword());
					//check if two password are consistent
					if(pass.equals(again)) {
						//send user information to server
						connection.serverObo.writeUTF(user);
						connection.serverObo.writeUTF(pass);
						connection.serverObo.flush();
						
						//get the order
						order = connection.serverObi.readUTF();
						try {
						//check the tag
						if(!order.equals("Same")) {
							Account acct = (Account) connection.serverObi.readObject();
							MainGUI mainframe = new MainGUI(acct);
							//mainframe.setAccount(acct);
							mainframe.launchFrame();
							registerFrame.dispose();
						}else { // show the hint
							JOptionPane.showMessageDialog(new JFrame().getContentPane(),"该用户名已经注册 ! !","注册失败",JOptionPane.WARNING_MESSAGE);
						}
						
						}catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					}else { // show the hint
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"两次密码需相同 ! !","注册失败",JOptionPane.WARNING_MESSAGE);
					}
				}else { // show the hint
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"请输入用户名 ! !","注册失败",JOptionPane.WARNING_MESSAGE);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	//inner class, cancel handler
	private class CancelHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//exit the program
			System.exit(0);
		}
		
	}
}

