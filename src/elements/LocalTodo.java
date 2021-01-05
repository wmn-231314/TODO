/* Name: LocalTodo.java 
 * Description: 待办事项类
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package elements;

import java.io.Serializable;
import java.util.Date;

public class LocalTodo extends Todo implements Serializable {
	//declare private attributes
	private boolean study;
	private int times,per;
	
	//constructor with tasks
	public LocalTodo(String title,Date date,boolean study, int times, int per) {
		super(title,date);
		this.study = study;
		this.times = times;
		this.per = per;
	}
	
	//constructor without tasks
	public LocalTodo(String title,Date date,boolean study) {
		super(title,date);
		this.study = study;
		times = 0;
		per = 0;
	}
	
	//whether it use task to study
	public void setStudy(boolean study) {
		this.study = study;
	}
	
	//set time of a task
	public void setPerTime(int per) {
		this.per = per;
	}
	
	//set number of task
	public void setTimes(int times) {
		this.times = times;
	}
	
	//get number of task
	public int getTimes() {
		return times;
	}
	
	//get time of a task
	public int getPerTime() {
		return per;
	}
	
	//check whether use task
	public boolean isStudy() {
		return study;
	}
	
	//finish one task
	public void finishOnce() {
		if(times > 0) {
			times--;
		}
	}
}