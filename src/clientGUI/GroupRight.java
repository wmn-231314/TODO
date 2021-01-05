/* Name: GroupGUI.java 
 * Description: 小组待办右侧的界面
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.RandomStringUtils;

import elements.Account;
import elements.GroupTodo;
import net.ClientNet;
import thread.GroupThread;

public class GroupRight extends JPanel {
	//declare private attributes
	private Account account;
	private JPanel idPanel,buttonPanel,titlePanel; 
	private JScrollPane scrollPane;
	private JButton enter,create,confirm,cancel,quit;
	private Box verticalBox;
	private JButton newTodo;
	private JLabel tip,gId;
	private JTextField groupId;
	private GroupThread groupthread;
	private GroupCenter centerframe = null;
	private MainGUI mainframe;
	private ClientNet connection = ClientNet.getNet();
	private boolean isCreate;

	//constructor, initialize the attributes and set the panel
	public GroupRight(MainGUI mainframe) {
		//initialize the attributes
		this.mainframe = mainframe;
		idPanel = new JPanel();
		buttonPanel = new JPanel();
		titlePanel = new JPanel(new BorderLayout());
		enter = new JButton("加入小组");
		create = new JButton("创建小组");
		newTodo = new JButton("新增待办");
		scrollPane = new JScrollPane();
		verticalBox = Box.createVerticalBox();
		tip = new JLabel();
		gId = new JLabel();
		confirm = new JButton("确认");
		cancel = new JButton("取消");
		quit = new JButton("退组");
		groupId = new JTextField(40);
		isCreate = true;
		
		//set the components and add to idPanel
		tip.setFont(new Font("宋体", Font.BOLD, 20));
		groupId.setFont(new Font("宋体", Font.BOLD, 15));
		
		//set the group id panel
		idPanel.add(tip);
		idPanel.add(groupId);
		idPanel.setOpaque(false);
		
		//set the gId and add to titlePanel
		gId.setFont(new Font("宋体", Font.BOLD, 14));
		gId.setAlignmentX(CENTER_ALIGNMENT);
		
		//set the title Panel
		titlePanel.add(gId,BorderLayout.CENTER);
		titlePanel.add(quit,BorderLayout.EAST);
		titlePanel.setBorder(new EmptyBorder(10,10,10,10));
		
		//set the components and add to buttonPanel
		confirm.setFont(new Font("宋体", Font.BOLD, 20));
		cancel.setFont(new Font("宋体", Font.BOLD, 20));
		
		//set the buttonPanel
		buttonPanel.add(confirm);
		buttonPanel.add(cancel);
		buttonPanel.setOpaque(false);
		
		//set the scrollPane and add to panel
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(300,750));
		
		//set the components
		enter.setFont(new Font("宋体", Font.BOLD, 20));
		create.setFont(new Font("宋体", Font.BOLD, 20));
		
		//set the verticalBox
		verticalBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		scrollPane.setViewportView(verticalBox);
		
		//set the newTodo and add to panel
		newTodo.setPreferredSize(new Dimension(300, 50));
		newTodo.setFont(new Font("宋体", Font.BOLD, 20));
		newTodo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//add action listener
		newTodo.addActionListener(new AddHandler());
		create.addActionListener(new CreateHandler());
		enter.addActionListener(new EnterHandler());
		confirm.addActionListener(new ConfirmHandler());
		cancel.addActionListener(new CancelHandler());
		quit.addActionListener(new QuitHandler());
	}
	
	//change the state of panel to origin
	public void changeToOriginal() {
		//reset the panel
		setOpaque(false);
		setLayout(new FlowLayout(FlowLayout.CENTER,50,0));
		setBorder(new EmptyBorder(50,0,0,0));
		setMinimumSize(new Dimension(700, 800));
		setPreferredSize(new Dimension(700, 800));
		
		//repaint the panel
		removeAll();
		add(enter);
		add(create);
		this.revalidate();
		this.repaint();
	}
	
	//change the state of panel to added
	public void changeToAdded() {
		//set the panel
		setOpaque(true);
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(0,0,0,0));
		setPreferredSize(new Dimension(300, 800));
		gId.setText("邀请码："+account.getGroup());
		
		//repaint the panel
		removeAll();
		add(titlePanel,BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(newTodo, BorderLayout.SOUTH);
		this.revalidate();
		this.repaint();
	}
	
	//initialize the right frame
	public void initGroup() {
		//get the latest account
		account = mainframe.getAccount();
		
		//set the panel base on its state
		if(account.getGroup() == null) {
			setOpaque(false);
			setLayout(new FlowLayout(FlowLayout.CENTER,50,0));
			setBorder(new EmptyBorder(50,0,0,0));
			setMinimumSize(new Dimension(700, 800));
			setPreferredSize(new Dimension(700, 800));
			add(enter);
			add(create);
		}else {
			//set the panel
			setOpaque(true);
			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(0,0,0,0));
			setPreferredSize(new Dimension(300, 800));
			setMinimumSize(new Dimension(300, 800));
			add(scrollPane, BorderLayout.CENTER);
			add(newTodo, BorderLayout.SOUTH);
			
			//get the iterator and use while loop to initialize
			Iterator<GroupTodo> it = account.getGroupTodos();
			while(it.hasNext()) {
				GroupTodo todo = it.next();
				GroupGUI todoGUI = new GroupGUI(mainframe,this,todo);
				todoGUI.addActionListener(new DisplayHandler(todo));
				verticalBox.add(todoGUI);
			}
			
			//start the thread to receive the information
			startThread();
		}
	}
	
	//show the id when create a group
	public void showGroupId() {
		//set the panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(60,0,0,0));
		
		//repaint the panel
		removeAll();
		add(idPanel);
		add(buttonPanel);
		this.revalidate();
		this.repaint();
	}
	
	//set the center frame
	public void setCenter(GroupCenter centerframe) {
		this.centerframe = centerframe;
	}
	
	//add a group TODO
	public void addGroupTodo(GroupTodo todo) {
		//get the latest account
		account = mainframe.getAccount();
		//add TODO to right frame and repaint the panel
		account.addGroupTodo(todo);
		GroupGUI todoGUI = new GroupGUI(mainframe,this,todo);
		todoGUI.addActionListener(new DisplayHandler(todo));
		verticalBox.add(todoGUI);
		this.revalidate();
		this.repaint();
		
	}
	
	//delete a group TODO
	public void deleteGroupTodo(GroupGUI todoGUI) {
		//get the latest account
		account = mainframe.getAccount();
		account.deleteGroupTodo(todoGUI.todo);
		
		//upload data
		connection.uploadData("deleteGroupTodo", account);
		
		//repaint the frame
		verticalBox.remove(todoGUI);
		mainframe.remove(centerframe);
		this.revalidate();
		this.repaint();
		mainframe.revalidate();
		mainframe.repaint();
	}
	
	//start the thread
	public void startThread(){
		groupthread = new GroupThread(this);
		groupthread.start();
	}
	
	//inner class, confirm handler
	private class ConfirmHandler implements ActionListener{
		//declare private attributes 
		private String gId;
		private boolean order;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//get group id
			gId = groupId.getText();
			//check the state
			if(isCreate) {
				account.setGroup(gId);
				connection.uploadData("createGroup", account);
				startThread();
				mainframe.setGroupPane();
			}else {
				try {
					//when add a group
					connection.serverObo.writeUTF("findGroup");
					connection.serverObo.writeUTF(gId);
					connection.serverObo.flush();
					order = connection.serverObi.readBoolean();
					//get the order
					if(order) {
						account.setGroup(gId);
						connection.uploadData("createGroup", account);
						//start the thread
						startThread();
						mainframe.setGroupPane();
					}else {
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"错误的小组邀请码 ! !","加入失败",JOptionPane.WARNING_MESSAGE);
						groupId.setText("");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	//inner class, create button handler
	private class CreateHandler implements ActionListener{
		private String gId;
		@Override
		public void actionPerformed(ActionEvent e) {
			//show the create gui
			isCreate = true;
			gId = RandomStringUtils.randomAlphanumeric(20);
			tip.setText("生成小组邀请码：");
			groupId.setText(gId);
			groupId.setEditable(false);
			showGroupId();
		}
		
	}
	
	//inner class, enter button handler
	private class EnterHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//show enter gui
			isCreate = false;
			groupId.setText("");
			tip.setText("请输入邀请码：");
			groupId.setEditable(true);
			showGroupId();
		}
		
	}
	
	//inner class, cancel button handler
	private class CancelHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//return to origin
			mainframe.setGroupPane();
		}
	}
	
	//inner class, quit button handler
	private class QuitHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//quit the group
			account = mainframe.getAccount();
			account.quitGroup();
			verticalBox.removeAll();
			groupthread.changeState();
			try {
				connection.serverObo.writeUTF("quitGroup");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//upload the data
			connection.uploadData("endGroup", account);
			mainframe.setGroupPane();
		}
		
	}
	
	//inner class, add button handler
	private class AddHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//show the center frame
			centerframe.createGroupTodo();
		}
		
	}
	
	//inner class, display button handler
	private class DisplayHandler implements ActionListener{
		private GroupTodo todo;
		//constructor
		public DisplayHandler(GroupTodo todo) {
			this.todo = todo;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//show the center frame
			centerframe.showGroupTodo(todo);
		}
	}

}
