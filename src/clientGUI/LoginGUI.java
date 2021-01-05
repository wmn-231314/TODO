/* Name: LoginGUI.java 
 * Description: 用户登录界面
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.Box;
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


public class LoginGUI {
	//declare private attributes
	private JFrame signFrame;
	private JPanel numberPanel,passwordPanel,buttonPanel,choicePanel;
	private JLabel userID,password;
	private JTextField userIDT;
	private JPasswordField passwordT;
	private JButton enter,cancel,signIn,signUp;
	private Box box;
	private ClientNet connection;
	private Account account;
	
	//constructor,initialize the GUI components
	public LoginGUI() {
		connection = ClientNet.getNet();
		signFrame = new JFrame("用户登录");
		userID = new JLabel("  用户名: ");
		password = new JLabel("     密码: ");
		userIDT = new JTextField(20);
		passwordT = new JPasswordField(10);
		numberPanel = new JPanel();
		passwordPanel = new JPanel();
		buttonPanel = new JPanel();
		choicePanel = new JPanel();
		enter = new JButton("确定");
		cancel = new JButton("取消");
		signUp = new JButton("注册新用户");
		signIn = new JButton("登录");
		box = Box.createVerticalBox();
	}
	
	//launch the Choice frame
	public void launchChoice() {
		//set the choice panel and add to frame
		choicePanel.add(signIn);
		choicePanel.add(signUp);
		choicePanel.setBorder(new EmptyBorder(20,0,20,0));
		signFrame.add(choicePanel);
		
		//add listeners
		signIn.addActionListener(new SignInHandler());
		signUp.addActionListener(new RegisterHandler());
		
		//set the frame
		signFrame.setBounds(600, 300, 300, 150);
		signFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//show the frame
		signFrame.setVisible(true);
	}
	
	//launch the Login frame
	public void launchFrame() {
		//clean old panel
		signFrame.remove(choicePanel);
		
		//add components to different Panels
		numberPanel.add(userID);
		numberPanel.add(userIDT);
		
		passwordPanel.add(password);
		passwordPanel.add(passwordT);
		
		buttonPanel.add(enter);
		buttonPanel.add(cancel);
		
		//add panels to the box
		box.add(numberPanel);
		box.add(passwordPanel);
		box.add(buttonPanel);
		
		//set form of password field
		passwordT.setEchoChar('*');
		passwordT.setColumns(20);
		
		//add box to the frame
		signFrame.add(box);
		
		//set the frame
		signFrame.setBounds(600, 300, 100, 100);
		signFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		signFrame.pack();
		
		//add action listeners
		cancel.addActionListener(new CancelHandler());
		enter.addActionListener(new EnterHandler());
		passwordT.addKeyListener(new EnterKeyHandler());
		
		//repaint and show the frame
		signFrame.revalidate();
		signFrame.repaint();
	}
	
	//signIn button Handler
	private class SignInHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				connection.serverObo.writeUTF("SignIn");
				connection.serverObo.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//launch the frame
			launchFrame();
		}
	}
	
	//sign up button handler
	private class RegisterHandler implements ActionListener {
		private RegisterGUI register;
		//constructor
		public RegisterHandler() {
			register = new RegisterGUI();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//send order to server
				connection.serverObo.writeUTF("SignUp");
				connection.serverObo.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//launch the register frame
			signFrame.dispose();
			register.launchFrame();
		}
		
	}
	
	//inner class enter key handler
	private class EnterKeyHandler extends KeyAdapter implements KeyListener{
		//declare attribute
		public EnterKeyHandler() {
		}
		
		@Override
		//when enter pressed
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				//get the password and send to the server
				String password = new String(passwordT.getPassword());
				try {
					//send user name and order to server
					connection.serverObo.writeUTF(userIDT.getText());
					connection.serverObo.writeUTF(password);
					connection.serverObo.flush();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				try {
					//get the order from server
					String message = connection.serverObi.readUTF();
					
					//if the password is correct
					if(message.equals("Correct")) {
						try {
							account = (Account) connection.serverObi.readObject();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
						
						//launch the main frame
						MainGUI mainframe = new MainGUI(account);
						//mainframe.setAccount(account);
						mainframe.launchFrame();
						signFrame.dispose();
					}else if(message.equals("Wrong")) {// if the password is wrong
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"错误的用户名或密码 ! !","登陆失败",JOptionPane.WARNING_MESSAGE);
					}else if(message.equals("Same")) { // if the id has been logged in
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"该用户已在异地登录 ! !","登陆失败",JOptionPane.WARNING_MESSAGE);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}
	
	//enter button handler
	private class EnterHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//get the password and send to the server
			String password = new String(passwordT.getPassword());
			try {
				//send user name and order to server
				connection.serverObo.writeUTF(userIDT.getText());
				connection.serverObo.writeUTF(password);
				connection.serverObo.flush();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
	
			try {
				//get the order from server
				String message = connection.serverObi.readUTF();
				
				//if the password is correct
				if(message.equals("Correct")) {
					try {
						account = (Account) connection.serverObi.readObject();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					
					//launch the main frame
					MainGUI mainframe = new MainGUI(account);
					mainframe.launchFrame();
					signFrame.dispose();
				}else if(message.equals("Wrong")) {// if the password is wrong
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"错误的用户名或密码 ! !","登陆失败",JOptionPane.WARNING_MESSAGE);
				}else if(message.equals("Same")) { // if the id has been logged in
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"该用户已在异地登录 ! !","登陆失败",JOptionPane.WARNING_MESSAGE);
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
	
	//main method
	public static void main(String[] args) {
		LoginGUI client = new LoginGUI();
		javax.swing.SwingUtilities.invokeLater(new Runnable() { // thread protection
			public void run() {
				//launch the choice frame
				client.launchChoice();
			}
		});
	}
}
