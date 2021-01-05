/* Name: FutureGUI.java 
 * Description: 单个未来目标的按钮
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.BorderLayout;
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

import elements.FutureInfo;

public class FutureGUI extends JButton {
	//declare private attributes
	private JButton finish,del;
	private JLabel titleL,timetip;
	private String title;
	private FutureRight rightframe;
	private ImageIcon icon1,icon2,icon3,icon4;
	private long day;
	private Date date;
	
	//declare public attribute
	public FutureInfo goal;
	
	//constructor, initialize the attributes and set the button
	public FutureGUI(FutureRight rightframe, FutureInfo goal) {
		//set the button
		setLayout(new BorderLayout());
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setPreferredSize(new Dimension(280, 105));
		setMinimumSize(new Dimension(280, 105));
		setMaximumSize(new Dimension(280, 105));
		
		//initialize the attributes
		this.rightframe = rightframe;
		this.goal = goal;
		title = goal.getTitle();
		finish = new JButton();
		del = new JButton();
		timetip = new JLabel();
		titleL = new JLabel(title);
		date = new Date();
		
		icon1 = new ImageIcon("images\\oribut.png");
		icon2 = new ImageIcon("images\\todofinish.png");
		icon3 = new ImageIcon("images\\oricancel.png");
		icon4 = new ImageIcon("images\\cancel.png");
		
		//check whether the time is over
		day = (goal.getDate().getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
		if(day == 0) {
			Calendar caltodo = Calendar.getInstance();
			Calendar calnow = Calendar.getInstance();
			caltodo.setTime(goal.getDate());
			calnow.setTime(date);
			if(goal.getDate().getTime() > date.getTime() && caltodo.get(Calendar.DATE) != calnow.get(Calendar.DATE)) {
				day++;
			}else if(goal.getDate().getTime() < date.getTime() && caltodo.get(Calendar.DATE) != calnow.get(Calendar.DATE)) {
				day--;
			}
		}
		
		//show hint for the time of the goal
		if(day >= 0) {
			timetip.setText("离计划还有: "+Math.abs(day)+" 天");
		}else {
			timetip.setText("已超过计划: "+Math.abs(day)+" 天");
		}
		timetip.setFont(new Font("宋体", Font.BOLD,20));
		
		//set the delete button
		del.setIcon(icon3);
		del.setRolloverIcon(icon4);
		del.setBorderPainted(false);
		del.setContentAreaFilled(false);
		del.setFocusPainted(false);
		
		//set the image of the finish icon
		icon1.setImage(icon1.getImage().getScaledInstance(50, 50, Image.SCALE_AREA_AVERAGING));
		icon2.setImage(icon2.getImage().getScaledInstance(50, 50, Image.SCALE_AREA_AVERAGING));
		
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
		add(timetip,BorderLayout.SOUTH);
        setFocusPainted(false);
        
        //add action listeners
        finish.addActionListener(new FinishHandler(this));
        del.addActionListener(new DeleteHandler(this));
	}
	
	//inner class, finish button handler
	private class FinishHandler implements ActionListener{
		//declare private attribute
		private FutureGUI futureGUI;
		//constructor
		public FinishHandler(FutureGUI futureGUI) {
			this.futureGUI = futureGUI;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//delete the FutureGUI
			rightframe.deleteFuture(futureGUI);
		}
	}
	
	//inner class, finish button handler
	private class DeleteHandler implements ActionListener{
		//declare private attribute
		FutureGUI futureGUI;
		//constructor
		public DeleteHandler(FutureGUI futureGUI) {
			this.futureGUI = futureGUI;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//delete the FutureGUI
			rightframe.deleteFuture(futureGUI);
		}
	}
}
