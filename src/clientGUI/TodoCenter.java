/* Name: TodoCenter.java 
 * Description: 待办事项中间界面
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXDatePicker;

import elements.Account;
import elements.LocalTodo;
import net.ClientNet;

public class TodoCenter extends JPanel {
	//declare private attributes
	private JPanel namePanel, timePanel, buttonPanel,taskPanel;
	private JLabel name, time,tasknum,tasktime;
	private JTextField nameText,taskNumber,taskTime;
	private JXDatePicker timePick;
	private JButton enter,task,begin;
	private TodoRight rightframe = null;
	private MainGUI mainframe;
	private Account account;
	private LocalTodo taskTodo;
	private boolean setStudy = false;
	private ClientNet connection = ClientNet.getNet();
	
	//constructor,initialize the attributes and set the panel
	public TodoCenter(MainGUI mainframe) {
		this.mainframe = mainframe;
		namePanel = new JPanel();
		timePanel = new JPanel();
		buttonPanel = new JPanel();
		taskPanel = new JPanel(new GridLayout(2, 2, 0, 15));
		name = new JLabel("\u5F85\u529E\u4E8B\u9879\uFF1A");
		time = new JLabel("\u622A\u6B62\u65E5\u671F\uFF1A");
		tasknum = new JLabel("任务次数：");
		tasktime = new JLabel("单次任务时长：");
		enter = new JButton("\u786E\u5B9A");
		task = new JButton("添加番茄钟");
		begin = new JButton("开始一次任务");
		nameText = new JTextField();
		taskNumber = new JTextField();
		taskTime = new JTextField();
		timePick = new JXDatePicker();
		
		//set the panel
		setBorder(new EmptyBorder(20, 0, 0, 0));
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setPreferredSize(new Dimension(400, 800));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//set namePanel and add to Panel
		namePanel.setBorder(new EmptyBorder(30, 25, 30, 25));
		namePanel.setMaximumSize(new Dimension(32767, 100));
		namePanel.setPreferredSize(new Dimension(10, 100));
		namePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(namePanel);
		
		//set name, nameText and add to namePanel
		name.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		namePanel.add(name);
		
		nameText.setPreferredSize(new Dimension(40, 30));
		nameText.setColumns(40);
		namePanel.add(nameText);
		
		taskNumber.setPreferredSize(new Dimension(40, 30));
		taskNumber.setColumns(20);
		taskTime.setPreferredSize(new Dimension(40, 30));
		taskTime.setColumns(20);
		taskTime.setToolTipText("以分钟为单位");
		
		taskPanel.setPreferredSize(new Dimension(10, 150));
		taskPanel.setMaximumSize(new Dimension(32767, 150));
		taskPanel.setBorder(new EmptyBorder(30, 25, 30, 25));
		
		taskPanel.add(tasktime);
		taskPanel.add(taskTime);
		taskPanel.add(tasknum);
		taskPanel.add(taskNumber);
				
		//set timePanel and add to Panel
		timePanel.setPreferredSize(new Dimension(10, 100));
		timePanel.setMaximumSize(new Dimension(32767, 100));
		timePanel.setBorder(new EmptyBorder(30, 25, 30, 25));
		timePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(timePanel);
		
		//set time, timeText and add to timePanel
		time.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		timePanel.add(time);
		
		timePick.setPreferredSize(new Dimension(120, 30));
		timePanel.add(timePick);
		
		//set buttonPanel and add to Panel
		buttonPanel.setPreferredSize(new Dimension(10, 100));
		buttonPanel.setMaximumSize(new Dimension(32767, 100));
		buttonPanel.setBorder(new EmptyBorder(30, 25, 30, 25));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(buttonPanel);
		
		//add actionListener
		enter.addActionListener(new EnterHandler(this));
		task.addActionListener(new TaskHandler());
		taskNumber.addKeyListener(new NumberHandler());
		taskTime.addKeyListener(new NumberHandler());
		begin.addActionListener(new BeginHandler());
	}
	
	//set the right frame
	public void setRight(TodoRight rightframe) {
		this.rightframe = rightframe;
	}
	
	//create a TODO
	public void createTodo() {
		setStudy = false;
		//set the component of the Panel
		removeAll();
		nameText.setEditable(true);
		timePick.setEditable(true);
		taskTime.setEditable(true);
		taskNumber.setEditable(true);
		nameText.setText("");
		timePick.setDate(new Date());
		taskTime.setText("");
		taskNumber.setText("");
		tasknum.setText("任务次数：");
		buttonPanel.removeAll();
		buttonPanel.add(task);
		buttonPanel.add(enter);
		add(namePanel);
		add(timePanel);
		add(buttonPanel);
		this.revalidate();
		this.repaint();
		
		//set the main frame and repaint the frame
		mainframe.remove(rightframe);
		mainframe.add(this,BorderLayout.CENTER);
		mainframe.add(rightframe,BorderLayout.EAST);
		mainframe.revalidate();
		mainframe.repaint();
	}
	
	//set task todo
	public void setTaskTodo(LocalTodo taskTodo) {
		this.taskTodo = taskTodo;
	}
	
	//get task todo
	public LocalTodo getTaskTodo() {
		return taskTodo;
	}
	
	//show a TODO
	public void showTodo(LocalTodo todo) {
		setTaskTodo(todo);
		if(todo.isStudy()) {
			removeAll();
			//set the component of the Panel
			nameText.setEditable(false);
			timePick.setEditable(false);
			taskTime.setEditable(false);
			taskNumber.setEditable(false);
			nameText.setText(todo.getTitle());
			timePick.setDate(todo.getDate());
			taskTime.setText(String.valueOf(todo.getPerTime()));
			taskNumber.setText(String.valueOf(todo.getTimes()));
			tasknum.setText("剩余任务次数：");
			buttonPanel.removeAll();
			buttonPanel.add(begin);
			add(namePanel);
			add(timePanel);
			add(taskPanel);
			add(buttonPanel);
			
			//set the main frame and repaint the frame
			mainframe.remove(this);
			mainframe.add(this,BorderLayout.CENTER);
			mainframe.revalidate();
			mainframe.repaint();
		}else {
			removeAll();
			//set the component of the Panel
			nameText.setEditable(false);
			timePick.setEditable(false);
			nameText.setText(todo.getTitle());
			timePick.setDate(todo.getDate());
			buttonPanel.removeAll();
			add(namePanel);
			add(timePanel);
			add(buttonPanel);
			
			//set the main frame and repaint the frame
			mainframe.remove(this);
			mainframe.add(this,BorderLayout.CENTER);
			mainframe.revalidate();
			mainframe.repaint();
		}
	}
	
	//show the panel with task
	public void showTask() {
		remove(buttonPanel);
		buttonPanel.removeAll();
		buttonPanel.add(enter);
		add(taskPanel);
		add(buttonPanel);
		this.revalidate();
		this.repaint();
	}
	
	//inner class, pervent user from input other characters
	private class NumberHandler extends KeyAdapter implements KeyListener{
		public void keyTyped(KeyEvent e) {
			String key="0123456789";
			if(key.indexOf(e.getKeyChar())<0){
				e.consume();   
			}
		}
	}
	
	//inner class, task button handler
	private class TaskHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			setStudy = true;
			showTask();
		}
	}
	
	//inner class, begin button handler
	private class BeginHandler implements ActionListener{
		private LocalTodo taskTodo;
		@Override
		public void actionPerformed(ActionEvent e) {
			taskTodo = getTaskTodo();
			if(taskTodo.getTimes() > 0) {
				new Clock(taskTodo);
				showTodo(taskTodo);
			}else {
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"所有任务已完成 ! !","任务结束",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	//inner class, enter button handler
	private class EnterHandler implements ActionListener {
		//declare private attributes
		private TodoCenter centerframe;
		private String title;
		private int taskt,taskn;
		private Date time;
		//constructor
		public EnterHandler(TodoCenter centerframe) {
			this.centerframe = centerframe;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//get the information
			account = mainframe.getAccount();
			title = nameText.getText();
			time = timePick.getDate();
			if(setStudy) {
				if(!title.equals("") && !taskTime.getText().equals("") && !taskNumber.getText().equals("")){
					//get the task
					taskt = Integer.parseInt(taskTime.getText());
					taskn = Integer.parseInt(taskNumber.getText());
					LocalTodo todo = new LocalTodo(title,time,true,taskn,taskt);
					rightframe.addTodo(todo);
					connection.uploadData("addLocal", account);
					
					//set the main frame and repaint the frame
					mainframe.remove(centerframe);
					mainframe.revalidate();
					mainframe.repaint();
				}else {
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"请输入完整信息 ! !","创建失败",JOptionPane.WARNING_MESSAGE);
				}
			}else {
				if(!title.equals("")) {
					//add a new TODO to right frame
					LocalTodo todo = new LocalTodo(title,time,false);
					rightframe.addTodo(todo);
					connection.uploadData("addLocal", account);
					
					//set the main frame and repaint the frame
					mainframe.remove(centerframe);
					mainframe.revalidate();
					mainframe.repaint();
				}else {
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"请输入待办名称 ! !","创建失败",JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		
	}
	
	
	
}
