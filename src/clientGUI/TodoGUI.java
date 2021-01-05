/* Name: TodoGUI.java 
 * Description: ��������İ�ť
 * Author: 19301050-wumengning 
 * Date: 12-11-20 17:10
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
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import elements.Account;
import elements.LocalTodo;

public class TodoGUI extends JButton {
	//declare private attributes
	private JButton finish,del;
	private JLabel titleL;
	private String title;
	private TodoRight rightframe;
	private ImageIcon icon1,icon2,icon3,icon4;
	private Long day;
	private Date date;
	private Account account;
	private MainGUI mainframe;
	//declare public attribute
	public LocalTodo todo;
	
	//constructor, initialize the attributes and set the button
	public TodoGUI(MainGUI mainframe,TodoRight rightframe, LocalTodo todo) {
		//set the button
		setLayout(new BorderLayout());
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setPreferredSize(new Dimension(280, 75));
		setMinimumSize(new Dimension(280, 75));
		setMaximumSize(new Dimension(280, 75));
		
		//initialize the attributes
		icon1 = new ImageIcon("images\\oribut.png");
		icon2 = new ImageIcon("images\\todofinish.png");
		icon3 = new ImageIcon("images\\oricancel.png");
		icon4 = new ImageIcon("images\\cancel.png");
		this.rightframe = rightframe;
		this.mainframe = mainframe;
		this.todo = todo;
		title = todo.getTitle();
		finish = new JButton();
		del = new JButton();
		titleL = new JLabel(title);
		date = new Date();
		
		//set the image of the finish icon
		icon1.setImage(icon1.getImage().getScaledInstance(45, 45, Image.SCALE_AREA_AVERAGING));
		icon2.setImage(icon2.getImage().getScaledInstance(45, 45, Image.SCALE_AREA_AVERAGING));
		
		//check whether the time is over
		day = (todo.getDate().getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
		if(day == 0) {
			Calendar caltodo = Calendar.getInstance();
			Calendar calnow = Calendar.getInstance();
			caltodo.setTime(todo.getDate());
			calnow.setTime(date);
			if(todo.getDate().getTime() < date.getTime() && caltodo.get(Calendar.DATE) != calnow.get(Calendar.DATE)) {
				day--;
			}
		}
		
		//show the hint for overtime todo
		if(day < 0) {
			account = mainframe.getAccount();
			titleL.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 0, 0), new Color(160, 160, 160)), "�ѹ���", TitledBorder.CENTER, TitledBorder.TOP, new Font("����", Font.BOLD, 10), new Color(255, 0, 0)));
		}
		
		//set the delete button
		del.setIcon(icon3);
		del.setRolloverIcon(icon4);
		del.setBorderPainted(false);
		del.setContentAreaFilled(false);
		del.setFocusPainted(false);
		
		//set the finish
		finish.setIcon(icon1);
		finish.setRolloverIcon(icon2);
		finish.setBorderPainted(false);
        finish.setContentAreaFilled(false);
        finish.setFocusPainted(false);

		//set the title
		titleL.setFont(new Font("����", Font.BOLD,15));
		titleL.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add components to button
		add(finish,BorderLayout.WEST);
		add(del,BorderLayout.EAST);
		add(titleL,BorderLayout.CENTER);
        setFocusPainted(false);
        //setOpaque(false);
        
        //add action listeners
        finish.addActionListener(new FinishHandler(this));
        del.addActionListener(new DeleteHandler(this));
	}
	
	//inner class, finish button handler
	private class FinishHandler implements ActionListener{
		//declare private attribute
		private TodoGUI todoGUI;
		//constructor
		public FinishHandler(TodoGUI todoGUI) {
			this.todoGUI = todoGUI;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			account = mainframe.getAccount();
			//delete the ToDoGUI
			rightframe.deleteTodo(todoGUI);
			account.addFinish();
			if(day < 0) {
				account.addOver();
			}
		}
	}
	
	//inner class, finish button handler
	private class DeleteHandler implements ActionListener{
		//declare private attribute
		TodoGUI todoGUI;
		//constructor
		public DeleteHandler(TodoGUI todoGUI) {
			this.todoGUI = todoGUI;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			account = mainframe.getAccount();
			//delete the ToDoGUI
			rightframe.deleteTodo(todoGUI);
			account.addGiveUp();
			if(day < 0) {
				account.addOver();
			}
		}
	}
	
}
