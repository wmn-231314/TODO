/* Name: TodoRight.java 
 * Description: 待办事项右侧界面
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import elements.Account;
import elements.LocalTodo;
import exception.ListFullException;
import net.ClientNet;

public class TodoRight extends JPanel {
	//declare private attributes
	private Account account;
	private JScrollPane scrollPane;
	private Box verticalBox;
	private JButton newTodo;
	private TodoCenter centerframe = null;
	private MainGUI mainframe;
	private ClientNet connection = ClientNet.getNet();
	
	//constructor, initialize the attributes and set the panel
	public TodoRight(MainGUI mainframe) {
		//initialize the attributes
		this.mainframe = mainframe;
		newTodo = new JButton("新增待办");
		scrollPane = new JScrollPane();
		verticalBox = Box.createVerticalBox();
		
		//set the panel
		setPreferredSize(new Dimension(300, 800));
		setLayout(new BorderLayout());
		
		//set the scrollPane and add to panel
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(300,750));
		add(scrollPane, BorderLayout.CENTER);
		
		//set the verticalBox
		verticalBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		scrollPane.setViewportView(verticalBox);
		
		//set the newTodo and add to panel
		newTodo.setPreferredSize(new Dimension(300, 50));
		newTodo.setFont(new Font("宋体", Font.BOLD, 20));
		newTodo.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(newTodo, BorderLayout.SOUTH);
		
		//add action listener
		newTodo.addActionListener(new AddHandler());
	}
	
	//initialize the right frame
	public void initTodo() {
		//get the latest account
		account = mainframe.getAccount();
		
		//get the iterator and use while loop to initialize
		Iterator<LocalTodo> it = account.getTodos();
		while(it.hasNext()) {
			LocalTodo todo = it.next();
			TodoGUI todoGUI = new TodoGUI(mainframe,this,todo);
			todoGUI.addActionListener(new DisplayHandler(todo));
			verticalBox.add(todoGUI);
		}
	}
	
	//set the center frame
	public void setCenter(TodoCenter centerframe) {
		this.centerframe = centerframe;
	}
	
	//add a TODO
	public void addTodo(LocalTodo todo) {
		//get the latest account
		account = mainframe.getAccount();
		try {
			//add TODO to right frame and repaint the panel
			account.addToDo(todo);
			TodoGUI todoGUI = new TodoGUI(mainframe,this,todo);
			todoGUI.addActionListener(new DisplayHandler(todo));
			verticalBox.add(todoGUI);
			this.revalidate();
			this.repaint();
		} catch (ListFullException e) {
			JOptionPane.showMessageDialog(new JFrame().getContentPane(),e.getMessage()+"\n列表已满(请联系管理员充钱获得更大容量) ! !","创建失败",JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	//delete a TODO
	public void deleteTodo(TodoGUI todoGUI) {
		//get the latest account
		account = mainframe.getAccount();
		account.deleteTodo(todoGUI.todo);
		
		//upload data
		connection.uploadData("deleteLocal", account);
		
		//repaint the frame
		verticalBox.remove(todoGUI);
		mainframe.remove(centerframe);
		this.revalidate();
		this.repaint();
		mainframe.revalidate();
		mainframe.repaint();
	}
	
	//inner class, add button handler
	private class AddHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//show the center frame
			centerframe.createTodo();
		}
		
	}
	
	//inner class, display button handler
	private class DisplayHandler implements ActionListener{
		private LocalTodo todo;
		//constructor
		public DisplayHandler(LocalTodo todo) {
			this.todo = todo;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//show the center frame
			centerframe.showTodo(todo);
		}
	}

}
