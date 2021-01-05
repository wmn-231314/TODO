/* Name: GroupGUI.java 
 * Description: 单个小组待办的按钮
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
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import elements.Account;
import elements.GroupTodo;

public class GroupGUI extends JButton {
	//declare private attributes
	private JButton finish,del;
	private JLabel titleL;
	private String title;
	private GroupRight rightframe;
	private ImageIcon icon1,icon2,icon3,icon4;
	private Long day;
	private Date date;
	private Account account;
	private MainGUI mainframe;
	
	//declare public attribute
	public GroupTodo todo;
	
	//constructor, initialize the attributes and set the button
	public GroupGUI(MainGUI mainframe,GroupRight rightframe, GroupTodo todo) {
		//set the button
		setLayout(new BorderLayout());
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setPreferredSize(new Dimension(280, 75));
		setMinimumSize(new Dimension(280, 75));
		setMaximumSize(new Dimension(280, 75));
		
		//initialize the attributes
		this.rightframe = rightframe;
		this.mainframe = mainframe;
		this.todo = todo;
		title = todo.getTitle();
		finish = new JButton();
		del = new JButton();
		titleL = new JLabel(title);
		date = new Date();
		icon1 = new ImageIcon("images\\oribut.png");
		icon2 = new ImageIcon("images\\todofinish.png");
		icon3 = new ImageIcon("images\\oricancel.png");
		icon4 = new ImageIcon("images\\cancel.png");
		
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
			titleL.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 0, 0), new Color(160, 160, 160)), "已过期", TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 10), new Color(255, 0, 0)));
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
		titleL.setFont(new Font("宋体", Font.BOLD,15));
		titleL.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add components to button
		add(finish,BorderLayout.WEST);
		add(del,BorderLayout.EAST);
		add(titleL,BorderLayout.CENTER);
        setFocusPainted(false);
        
        //add action listeners
        finish.addActionListener(new FinishHandler(this));
        del.addActionListener(new DeleteHandler(this));
	}
	
	//inner class, finish button handler
	private class FinishHandler implements ActionListener{
		//declare private attribute
		private GroupGUI gtodoGUI;
		//constructor
		public FinishHandler(GroupGUI gtodoGUI) {
			this.gtodoGUI = gtodoGUI;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//finish the Group todoGUI
			account = mainframe.getAccount();
			rightframe.deleteGroupTodo(gtodoGUI);
			account.addFinish();
			if(day < 0) {
				account.addOver();
			}
		}
	}
	
	//inner class, finish button handler
	private class DeleteHandler implements ActionListener{
		//declare private attribute
		GroupGUI gtodoGUI;
		//constructor
		public DeleteHandler(GroupGUI gtodoGUI) {
			this.gtodoGUI = gtodoGUI;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//delete the Group toddoGUI
			account = mainframe.getAccount();
			rightframe.deleteGroupTodo(gtodoGUI);
			account.addGiveUp();
			if(day < 0) {
				account.addOver();
			}
		}
	}		
}
