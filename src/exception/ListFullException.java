/* Name: ListFullException.java 
 * Description: �б�״̬�쳣��������ʾ�б�����
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package exception;

public class ListFullException extends Exception {
	//constructor
	public ListFullException(int capacity, String listType) {
		super("���ֻ�����"+capacity+"��"+listType);
	}
	
}
