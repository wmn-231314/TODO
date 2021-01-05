/* Name: FutureRight.java 
 * Description: 未来目标右侧界面
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import elements.Account;
import elements.FutureInfo;
import exception.ListFullException;
import net.ClientNet;

public class FutureRight extends JPanel{
	//declare private attributes
	private Account account;
	private JScrollPane scrollPane;
	private Box verticalBox;
	private JButton newTodo;
	private FutureCenter centerframe = null;
	private MainGUI mainframe;
	private ClientNet connection = ClientNet.getNet();
	
	//constructor, initialize the attributes and set the panel
	public FutureRight(MainGUI mainframe) {
		//initialize the attributes
		this.mainframe = mainframe;
		newTodo = new JButton("新增计划");
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
	public void initFuture() {
		//get the latest account
		account = mainframe.getAccount();
		
		//get the iterator and use while loop to initialize
		Iterator<FutureInfo> it = account.getFutures();
		while(it.hasNext()) {
			FutureInfo goal = it.next();
			FutureGUI futureGUI = new FutureGUI(this,goal);
			futureGUI.addActionListener(new DisplayHandler(goal));
			verticalBox.add(futureGUI);
		}
	}
	
	//set the center frame
	public void setCenter(FutureCenter centerframe) {
		this.centerframe = centerframe;
	}
	
	//add a future goal
	public void addFuture(FutureInfo goal) {
		//get the latest account
		account = mainframe.getAccount();
		//add TODO to right frame and repaint the panel
		try {
			account.addGoals(goal);
			FutureGUI futureGUI = new FutureGUI(this,goal);
			futureGUI.addActionListener(new DisplayHandler(goal));
			verticalBox.add(futureGUI);
			this.revalidate();
			this.repaint();
		} catch (ListFullException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//delete a future goal
	public void deleteFuture(FutureGUI futureGUI) {
		//get the latest account
		account = mainframe.getAccount();
		account.deleteFuture(futureGUI.goal);
		
		//upload data
		connection.uploadData("deleteGoal", account);
		
		//repaint the frame
		verticalBox.remove(futureGUI);
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
			centerframe.createFuture();
		}
		
	}
	
	//inner class, display button handler
	private class DisplayHandler implements ActionListener{
		private FutureInfo goal;
		//constructor
		public DisplayHandler(FutureInfo goal) {
			this.goal = goal;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//show the center frame
			centerframe.showFuture(goal);
		}
	}
}
