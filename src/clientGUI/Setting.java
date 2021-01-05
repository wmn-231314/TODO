/* Name: Setting.java 
 * Description: 设置界面
 * Author: 19301050-wumengning 
 * Date: 12-11-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import elements.Account;
import net.ClientNet;

public class Setting extends JPanel {
	//declare private attributes
	private JPanel iconPanel,enterPanel, buttonPanel;
	private JPasswordField originPassword,enterPassword, confirmPassword;
	private JLabel icon,origin, password, passwordAgain;
	private JButton enter,choose;
	private Account account;
	private MainGUI mainframe;
	private File file;
	private ImageIcon image;
	private ClientNet connection = ClientNet.getNet();
	
	//constructor,initialize the attributes
	public Setting(MainGUI mainframe) {
		//initialize the attributes
		this.mainframe = mainframe;
		account = mainframe.getAccount();
		iconPanel = new JPanel();
		enterPanel = new JPanel();
		file = new File("userIcons\\"+account.getID()+"icon.png");
		if(file.exists()) {
			image = new ImageIcon(file.getPath());
		}else {
			image = new ImageIcon("images\\pic.jpg");
		}
		icon = new JLabel();
		origin = new JLabel("请输入原密码：");
		password = new JLabel("请输入新密码：");
		originPassword = new JPasswordField();
		enterPassword = new JPasswordField();
		passwordAgain = new JLabel("请再次确认密码：");
		confirmPassword = new JPasswordField();
		buttonPanel = new JPanel();
		enter = new JButton("\u786E\u5B9A");
		choose = new JButton("修改头像");
		
		//set the panel
		setBounds(new Rectangle(0, 0, 180, 180));
		setMinimumSize(new Dimension(700, 800));
		setPreferredSize(new Dimension(700, 800));
		setBorder(new EmptyBorder(30, 5, 5, 5));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false);
		
		//set label and add to infoPanel
		image.setImage(image.getImage().getScaledInstance(150, 150, Image.SCALE_AREA_AVERAGING));
		icon.setIcon(image);
		//icon.setBorder(new EmptyBorder(50, 50, 0, 50));
		icon.setBorder(new CompoundBorder(new LineBorder(new Color(255, 255, 255), 1, true), new EmptyBorder(25, 25, 25, 25)));
		icon.setPreferredSize(new Dimension(200, 200));
		choose.setFont(new Font("宋体", Font.BOLD, 20));
		iconPanel.setBorder(new EmptyBorder(50,0,0,0));
		iconPanel.add(icon);
		iconPanel.add(choose);
		iconPanel.setOpaque(false);
		
		//set the enterPanel and add to contentPane
		enterPanel.setBorder(new EmptyBorder(20, 0, 0, 180));
		enterPanel.setLayout(new GridLayout(3, 2, 0, 30));
		
		//set several components and add to enterPanel
		origin.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		enterPanel.add(origin);
		enterPanel.add(originPassword);
		
		password.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		enterPanel.add(password);
		enterPanel.add(enterPassword);
		
		passwordAgain.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		enterPanel.add(passwordAgain);
		enterPanel.add(confirmPassword);
		
		//set buttonPanel and add to contentPane
		buttonPanel.setBorder(new EmptyBorder(40, 0, 0, 0));
		
		//add components to buttonPanel
		enter.setFont(new Font("宋体", Font.BOLD, 20));
		buttonPanel.add(enter);
		
		//add the components
		add(iconPanel);
		add(enterPanel);
		add(buttonPanel);
		
		//add action listeners
		enter.addActionListener(new EnterHandler());
		choose.addActionListener(new ChooseHandler());
	}
	
	//inner class, enter button handler
	private class EnterHandler implements ActionListener{
		private String originP,pass,again;
		@Override
		public void actionPerformed(ActionEvent e) {
			//declare variables
			account = mainframe.getAccount();
			originP = new String(originPassword.getPassword());
			//if the user input id
			if(originP.equals(account.getPassword())) {
				//get password
				pass = new String(enterPassword.getPassword());
				again = new String(confirmPassword.getPassword());
				//check if two password are consistent
				if(pass.equals(again)) {
					account.setPass(pass);
					connection.uploadData("changePass", account);
					originPassword.setText("");
					enterPassword.setText("");
					confirmPassword.setText("");
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"密码修改成功 ! !","设置成功",JOptionPane.INFORMATION_MESSAGE);
				}else { // show the hint
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"两次密码需相同 ! !","设置失败",JOptionPane.WARNING_MESSAGE);
				}
			}else {
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"原密码输入错误 ! !","设置失败",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	//refresh the icon
	public void refresh() {
		image = new ImageIcon(file.getPath());
		image.setImage(image.getImage().getScaledInstance(150, 150, Image.SCALE_AREA_AVERAGING));
		icon.setIcon(image);
		this.repaint();
		this.revalidate();
	}
	
	//show a file chooser to chose file
	public void showIconFileDialog() {
		JFileChooser chooser = new JFileChooser();
		//chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png)", "jpg", "png"));
		int result = chooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			try {
				InputStream in = new FileInputStream(chooser.getSelectedFile());
				OutputStream out = new FileOutputStream(file);
				byte[] photo = new byte[in.available()];
				in.read(photo);
				out.write(photo);
				mainframe.refreshIcon();
				refresh();
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	//inner class, choose button handler
	private class ChooseHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			showIconFileDialog();
		}
	}
}
