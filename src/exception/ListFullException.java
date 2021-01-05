/* Name: ListFullException.java 
 * Description: 列表状态异常，用于显示列表已满
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package exception;

public class ListFullException extends Exception {
	//constructor
	public ListFullException(int capacity, String listType) {
		super("最多只能添加"+capacity+"个"+listType);
	}
	
}
