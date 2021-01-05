/* Name: FutureInfo.java 
 * Description: 未来目标类
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package elements;

import java.io.Serializable;
import java.util.Date;

public class FutureInfo extends Todo implements Serializable {
	//declare private attributes
	private String content;
	
	//constructor
	public FutureInfo(String title, Date date, String content) {
		super(title,date);
		this.content = content;
	}
	
	//get the content
	public String getContent() {
		return content;
	}
}