/* Name: Clock.java 
 * Description: 倒计时显示类
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dataOperation.Music;
import elements.LocalTodo;

public class Clock extends JFrame {
	//declare private attributes
	private JPanel contentPane;
	private JLabel time,finish;
	private JButton ahead;
	private long timeInSe;
	private long minute,seconds;
	private Music music;
	private boolean state;
	
	//constructor, initialize the attribute
	public Clock(LocalTodo todo) {
		this.timeInSe = (long)todo.getPerTime()*60;
		state = true;
		music = new Music();
		ahead = new JButton("提前完成！");
		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.Y_AXIS));
		time = new JLabel();
		finish = new JLabel("任务计时已完成！");
		
		//set the components and add to the contentPane
		time.setFont(new Font("Broadway", Font.BOLD, 40));
		time.setBorder(new EmptyBorder(0,0,20,0));
		time.setAlignmentX(CENTER_ALIGNMENT);
		
		finish.setFont(new Font("宋体", Font.BOLD, 25));
		finish.setAlignmentX(CENTER_ALIGNMENT);
		
		ahead.setAlignmentX(CENTER_ALIGNMENT);
		
		contentPane.add(time);
		contentPane.add(ahead);
		
		//set the contentPane
		contentPane.setBorder(new EmptyBorder(50,0,50,0));
		contentPane.setAlignmentX(CENTER_ALIGNMENT);
		
		//add listener
		ahead.addActionListener(new AheadHandler());
		
		//set the frame
		setTitle("任务: "+todo.getTitle());
		setContentPane(contentPane);
		setBounds(600, 300, 300, 200);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		//start the thread for timer
		ClockThread clock = new ClockThread();
		clock.start();
		
		//record finish
		todo.finishOnce();
	}
	
	//inner class, use for timer
	private class ClockThread extends Thread{
		public void run() {
			state = true;
			//start the timer and the music
			music.playMusic();
			while (timeInSe >= 0 && state) {
	            minute = timeInSe / 60;
	            seconds = timeInSe - minute * 60;
	            time.setText(String.format("%02d",minute) + " : "+String.format("%02d",seconds));
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            timeInSe--;
	        }
			
			//repaint the panel
			contentPane.removeAll();
			contentPane.add(finish);
			contentPane.revalidate();
			contentPane.repaint();
			
			//change the mode
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			//stop the music
			music.stopMusic();
		}
	}
	
	//inner class, ahead button handler
	private class AheadHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//change the state
			state = false;
		}
	}
}
