/* Name: GroupTodo.java 
 * Description: 小组待办类
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package elements;

import java.io.Serializable;
import java.util.Date;

public class GroupTodo extends Todo implements Serializable {
	//declare private attributes
	private String memberTip;
	
	//constructor
	public GroupTodo(String title, Date date, String content) {
		super(title,date);
		this.memberTip = content;
	}
	
	//get the content
	public String getContent() {
		return memberTip;
	}
}
