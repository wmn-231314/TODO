/* Name: GroupThread.java 
 * Description: 小组待办接收线程
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package thread;

import java.io.IOException;

import clientGUI.GroupRight;
import elements.GroupTodo;
import net.ClientNet;

public class GroupThread extends Thread{
	//declare private attributes
	private ClientNet connection;
	private GroupRight groupRight;
	private boolean state,still;
	private GroupTodo groupTodo;
	
	//constructor, initialize the attributes
	public GroupThread(GroupRight groupRight) {
		connection = ClientNet.getNet();
		this.groupRight = groupRight;
		state = true;
	}
	
	//change the state
	public void changeState() {
		state = false;
	}
	
	@Override
	public void run() {
		while(state) {
			try {
				//get the order and object
				still = connection.serverObi.readBoolean();
				if(still) {
					groupTodo = (GroupTodo) connection.serverObi.readObject();
					groupRight.addGroupTodo(groupTodo);
				}else {
					break;
				}
			} catch (ClassNotFoundException | IOException e) {
				break;
			}
		}
	}

}
