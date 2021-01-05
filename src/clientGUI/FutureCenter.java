/* Name: FutureCenter.java 
 * Description: 未来目标中间界面
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXDatePicker;

import elements.Account;
import elements.FutureInfo;
import net.ClientNet;

public class FutureCenter extends JPanel {
	//declare private attributes
	private JPanel namePanel, timePanel, buttonPanel;
	private JScrollPane contentPanel;
	private JLabel name, time;
	private JTextField nameText;
	private JXDatePicker timePick;
	private JTextArea content;
	private JButton enter;
	private FutureRight rightframe = null;
	private MainGUI mainframe;
	private Account account;
	private ClientNet connection = ClientNet.getNet();
	
	//constructor,initialize the attributes and set the panel
	public FutureCenter(MainGUI mainframe) {
		this.mainframe = mainframe;
		namePanel = new JPanel();
		timePanel = new JPanel();
		buttonPanel = new JPanel();
		contentPanel = new JScrollPane();
		name = new JLabel("未来计划：");
		time = new JLabel("\u622A\u6B62\u65E5\u671F\uFF1A");
		enter = new JButton("\u786E\u5B9A");
		nameText = new JTextField();
		timePick = new JXDatePicker();
		content = new JTextArea(15,15);
		
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
		
		//set timePanel and add to Panel
		timePanel.setPreferredSize(new Dimension(10, 100));
		timePanel.setMaximumSize(new Dimension(32767, 100));
		timePanel.setBorder(new EmptyBorder(30, 25, 30, 25));
		timePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(timePanel);
		
		//set time, timePick and add to timePanel
		time.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		timePanel.add(time);
		
		timePick.setPreferredSize(new Dimension(120, 30));
		timePanel.add(timePick);
		timePanel.add(timePick);
		
		//set content and contentPanel
		contentPanel.setPreferredSize(new Dimension(2, 200));
		contentPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "\u5907\u6CE8\u4FE1\u606F", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		content.setMaximumSize(new Dimension(2147483647, 300));
		contentPanel.setViewportView(content);
		add(contentPanel);
		
		//set buttonPanel and add to Panel
		buttonPanel.setPreferredSize(new Dimension(10, 100));
		buttonPanel.setMaximumSize(new Dimension(32767, 100));
		buttonPanel.setBorder(new EmptyBorder(30, 25, 30, 25));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(buttonPanel);
		
		//add actionListener
		enter.addActionListener(new EnterHandler(this));
		
	}
	
	//set the right frame
	public void setRight(FutureRight rightframe) {
		this.rightframe = rightframe;
	}
	
	//create a future goal
	public void createFuture() {
		//set the component of the Panel
		nameText.setEditable(true);
		timePick.setEditable(true);
		content.setEditable(true);
		nameText.setText("");
		timePick.setDate(new Date());
		content.setText("");
		buttonPanel.removeAll();
		buttonPanel.add(enter);
		
		//set the main frame and repaint the frame
		mainframe.remove(rightframe);
		mainframe.add(this,BorderLayout.CENTER);
		mainframe.add(rightframe,BorderLayout.EAST);
		mainframe.revalidate();
		mainframe.repaint();
	}
	
	//show a future goal
	public void showFuture(FutureInfo todo) {
		//set the component of the Panel
		nameText.setEditable(false);
		timePick.setEditable(false);
		content.setEditable(false);
		nameText.setText(todo.getTitle());
		timePick.setDate(todo.getDate());
		content.setText(todo.getContent());
		buttonPanel.removeAll();
		
		//set the main frame and repaint the frame
		mainframe.remove(this);
		mainframe.add(this,BorderLayout.CENTER);
		mainframe.revalidate();
		mainframe.repaint();
	}
	
	//inner class, enter button handler
	private class EnterHandler implements ActionListener {
		//declare private attributes
		private FutureCenter centerframe;
		private String title;
		private Date time;
		private String cont;
		//constructor
		public EnterHandler(FutureCenter centerframe) {
			this.centerframe = centerframe;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//get the information
			account = mainframe.getAccount();
			title = nameText.getText();
			time = timePick.getDate();
			cont = content.getText();
			
			if(!title.equals("")) {
				//add a new future goal to right frame
				FutureInfo todo = new FutureInfo(title,time, cont);
				rightframe.addFuture(todo);
				connection.uploadData("addGoal", account);
				
				//set the main frame and repaint the frame
				mainframe.remove(centerframe);
				mainframe.revalidate();
				mainframe.repaint();
			}else {
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"请输入计划名称 ! !","创建失败",JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
}
