/* Name: MainGUI.java 
 * Description: 主界面
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import elements.Account;
import net.ClientNet;

public class MainGUI extends JFrame {
	//declare private attributes
	private MainPanel todoPane,groupPane,futurePane,statisticPane,settingPane;
	private Account account;
	private ClientNet connection = ClientNet.getNet();
	private JPanel leftPanel, infoPanel;
	private TodoRight todoRight;
	private TodoCenter todoCenter;
	private GroupCenter groupCenter;
	private GroupRight groupRight;
	private FutureCenter futureCenter;
	private FutureRight futureRight;
	private Setting set;
	private JLabel userIcon;
	private Statistic statistic;
	private JButton todo, todoTeam, goals, diagram, setting;
	private ImageIcon icon;
	private File file;
	
	//constructor, initialize the attributes
	public MainGUI(Account account) {
		initLookAndFeel();
		this.account = account;
		todoCenter = new TodoCenter(this);
		todoRight = new TodoRight(this);
		futureCenter = new FutureCenter(this);
		futureRight = new FutureRight(this);
		groupCenter = new GroupCenter(this);
		groupRight = new GroupRight(this);
		statistic = new Statistic(this);
		set = new Setting(this);
		todoPane = new MainPanel("images\\background.png");
		futurePane = new MainPanel("images\\background1.png");
		groupPane = new MainPanel("images\\background2.png");
		settingPane = new MainPanel("images\\background3.png");
		statisticPane = new MainPanel("images\\background4.png");
		leftPanel = new JPanel();
		infoPanel = new JPanel();
		todo = new JButton("\u4EE3\u529E\u4E8B\u9879");
		todoTeam = new JButton("\u5C0F\u7EC4\u4EE3\u529E");
		goals = new JButton("\u672A\u6765\u76EE\u6807");
		diagram = new JButton("\u7EDF\u8BA1\u56FE\u8868");
		setting = new JButton("\u8BBE\u7F6E");
		file = new File("userIcons\\"+account.getID()+"icon.png");
		
		//initialize the icon
		if(file.exists()) {
			icon = new ImageIcon(file.getPath());
		}else {
			icon = new ImageIcon("images\\pic.jpg");
		}
		
		//set each panel
		todoCenter.setRight(todoRight);
		todoRight.setCenter(todoCenter);
		futureCenter.setRight(futureRight);
		futureRight.setCenter(futureCenter);
		groupCenter.setRight(groupRight);
		groupRight.setCenter(groupCenter);
		
		//set the frame
		setTitle("ToDo-List");
		setBounds(50, 50, 1050, 800);
		
		//add window listener
		addWindowListener(new CloseWidgt());
		todo.addActionListener(new TodoHandler());
		todoTeam.addActionListener(new TeamHandler());
		goals.addActionListener(new GoalsHandler());
		diagram.addActionListener(new DiagramHandler());
		setting.addActionListener(new SetHandler());
	}
	
	//get the account
	public Account getAccount() {
		return account;
	}
	
	//refresh the icon
	public void refreshIcon() {
		if(file.exists()) {
			icon = new ImageIcon(file.getPath());
		}else {
			icon = new ImageIcon("images\\pic.jpg");
		}
		//refresh
		icon.setImage(icon.getImage().getScaledInstance(70, 70, Image.SCALE_AREA_AVERAGING));
		userIcon.setIcon(icon);
	}
	
	//set look and feel
	private void initLookAndFeel() {
		//use try-catch 
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // set
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//initialize the todo panel
	public void initTodoPane() {
		setContentPane(todoPane);
	}
	
	//set the diagram panel
	public void setDiagramPane() {
		setContentPane(statisticPane);
		statistic.launchImage();
		statisticPane.add(leftPanel,BorderLayout.WEST);
		statisticPane.add(statistic,BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}
	
	//set the todo panel
	public void setTodoPane() {
		initTodoPane();
		todoPane.add(leftPanel, BorderLayout.WEST);
		todoPane.add(todoRight,BorderLayout.EAST);
		this.revalidate();
		this.repaint();
	}
	
	//set the setting panel
	public void setSettingPane() {
		setContentPane(settingPane);
		settingPane.add(leftPanel,BorderLayout.WEST);
		settingPane.add(set,BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}
	
	//set the group panel
	public void setGroupPane() {
		setContentPane(groupPane);
		groupPane.add(leftPanel,BorderLayout.WEST);
		if(account.getGroup()==null) {
			groupRight.changeToOriginal();
			groupPane.add(groupRight,BorderLayout.CENTER);
		}else {
			groupRight.changeToAdded();
			groupPane.add(groupRight,BorderLayout.EAST);
		}
		this.revalidate();
		this.repaint();
	}
	
	//set the future panel
	public void setFuturePane() {
		setContentPane(futurePane);
		futurePane.add(leftPanel,BorderLayout.WEST);
		futurePane.add(futureRight,BorderLayout.EAST);
		this.revalidate();
		this.repaint();
	}
	
	//launch the frame
	public void launchFrame() {
		initTodoPane();
		
		//set the contentPane
		todoPane.setLayout(new BorderLayout());
		futurePane.setLayout(new BorderLayout());
		groupPane.setLayout(new BorderLayout());
		statisticPane.setLayout(new BorderLayout());
		settingPane.setLayout(new BorderLayout());
		
		//set leftPanel and add to contentPane
		leftPanel.setPreferredSize(new Dimension(300, 800));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(Color.WHITE);
		todoPane.add(leftPanel, BorderLayout.WEST);
		
		//set info panel and add to leftPanel
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setMaximumSize(new Dimension(350, 125));
		leftPanel.add(infoPanel);
		
		//set label and add to infoPanel
		icon.setImage(icon.getImage().getScaledInstance(70, 70, Image.SCALE_AREA_AVERAGING));
		userIcon = new JLabel(icon);
		userIcon.setBorder(new EmptyBorder(10, 10, 10, 10));
		userIcon.setPreferredSize(new Dimension(100, 100));
		infoPanel.add(userIcon);
		
		//set userName and add to infoPanel
		JLabel userName = new JLabel(account.getID());
		userName.setFont(new Font("宋体", Font.BOLD, 20));
		userName.setPreferredSize(new Dimension(150, 20));
		infoPanel.add(userName);
		
		//set several left choices and add to leftPanel
		todo.setFont(new Font("宋体", Font.BOLD, 20));
		todo.setAlignmentX(Component.CENTER_ALIGNMENT);
		todo.setMaximumSize(new Dimension(350, 100));
		leftPanel.add(todo);
		
		todoTeam.setFont(new Font("宋体", Font.BOLD, 20));
		todoTeam.setAlignmentX(Component.CENTER_ALIGNMENT);
		todoTeam.setMaximumSize(new Dimension(350, 100));
		leftPanel.add(todoTeam);
		
		goals.setFont(new Font("宋体", Font.BOLD, 20));
		goals.setMaximumSize(new Dimension(350, 100));
		goals.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftPanel.add(goals);
		
		diagram.setMaximumSize(new Dimension(350, 100));
		diagram.setFont(new Font("宋体", Font.BOLD, 20));
		diagram.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftPanel.add(diagram);
		
		setting.setMaximumSize(new Dimension(350, 100));
		setting.setFont(new Font("宋体", Font.BOLD, 20));
		setting.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftPanel.add(setting);
		
		//initialize the main frame and show the frame
		todoRight.initTodo();
		futureRight.initFuture();
		groupRight.initGroup();
		todoPane.add(todoRight,BorderLayout.EAST);
		setVisible(true);
	}
	
	//close window handler
	private class CloseWidgt extends WindowAdapter implements WindowListener{

		@Override
		public void windowClosing(WindowEvent e) {
			//upload data and quit
			connection.uploadData("quit", account);
			System.exit(0);
		}
	}
	
	//inner class, change to group panel
	private class TeamHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			setGroupPane();
		}
	}
	
	//inner class, change to setting panel
	private class SetHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setSettingPane();
		}
		
	}
	
	//inner class, change to future panel
	private class GoalsHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			setFuturePane();
		}
		
	}
	
	//inner class, change to diagram panel
	private class DiagramHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setDiagramPane();
		}
		
	}
	
	//inner class, change to todo panel
	private class TodoHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			setTodoPane();
		}
		
	}

}
