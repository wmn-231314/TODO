/* Name: Todo.java 
 * Description: 待办事项模型类
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package elements;

import java.io.Serializable;
import java.util.Date;

public class Todo implements Serializable {
	//declare private attributes
	private String title;
	private Date date;
	
	//constructor with tasks
	public Todo(String title,Date date) {
		this.title = title;
		this.date = date;
	}
	
	//get the date
	public Date getDate() {
		return date;
	}
	
	//get the title
	public String getTitle() {
		return title;
	}
	
}
