/* Name: Account.java 
 * Description: 用户信息类
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exception.ListFullException;

public class Account implements Serializable {
	//declare private attributes
	private static final int CAPACITY = 20;
	private String userID;
	private String password;
	private List<LocalTodo> toDoList;
	private List<FutureInfo> futureList;
	private List<GroupTodo> groupList;
	private String groupNumber;
	private int finish,over,giveUp;
	
	//constructor, initialize the attributes
	public Account(String userID,String password) {
		this.userID = userID;
		this.password = password;
		groupNumber = null;
		toDoList = new ArrayList<LocalTodo>();
		futureList = new ArrayList<FutureInfo>();
		groupList = new ArrayList<GroupTodo>();
		finish = 0;
		over = 0;
		giveUp = 0;
	}
	
	//constructor for toDoList and futureList
	public Account() {
		groupNumber = null;
		toDoList = new ArrayList<LocalTodo>();
		futureList = new ArrayList<FutureInfo>();
		groupList = new ArrayList<GroupTodo>();
		finish = 0;
		over = 0;
		giveUp = 0;
	}
	
	//update the Account
	public void updateAccount(Account account) {
		this.userID = account.userID;
		this.password = account.password;
		this.toDoList = account.toDoList;
		this.futureList = account.futureList;
		this.groupNumber = account.groupNumber;
		this.groupList = account.groupList;
		this.finish = account.finish;
		this.over = account.over;
		this.giveUp = account.giveUp;
	}
	
	//add a finish
	public void addFinish() {
		finish++;
	}
	
	//add an over
	public void addOver() {
		over++;
	}
	
	//add a give-up
	public void addGiveUp() {
		giveUp++;
	}
	
	//reset the count of statistic
	public void resetCount() {
		finish = 0;
		over = 0;
		giveUp = 0;
	}
		
	//check the list
	private boolean todoIsFull() {
		return(toDoList.size() >= CAPACITY);
	}
	
	//check the list
	private boolean futureIsFull() {
		return(futureList.size() >= CAPACITY);
	}
	
	//set the group number
	public void setGroup(String groupNumber) {
		this.groupNumber = groupNumber;
	}
	
	//get the group number
	public String getGroup() {
		return groupNumber;
	}
	
	//get the user id
	public String getID() {
		return userID;
	}
	
	//get the finish count
	public int getFinish() {
		return finish;
	}
	
	//get the over count
	public int getOver() {
		return over;
	}
	
	//get the give-up count
	public int getGiveUp() {
		return giveUp;
	}
	
	//get the password
	public String getPassword() {
		return password;
	}
	
	//set the user id
	public void setID(String userID) {
		this.userID = userID;
	}
	
	//get the password
	public void getPassword(String password) {
		this.password = password;
	}
	
	//add a TODO info
	public void addToDo(LocalTodo todo) throws ListFullException {
		if(todoIsFull()) {
			throw new ListFullException(CAPACITY,"待办事项");
		}else {
			toDoList.add(todo);
		}
	}
	
	//add group TODO
	public void addGroupTodo(GroupTodo todo) {
		groupList.add(todo);
	}
	
	//add a future info
	public void addGoals(FutureInfo f) throws ListFullException {
		if(futureIsFull()) {
			throw new ListFullException(CAPACITY,"未来目标");
		}else {
			futureList.add(f);
		}
	}
	
	//delete a TODO info
	public void deleteTodo(LocalTodo todo) {
		toDoList.remove(todo);
	}
	
	//delete a future info
	public void deleteFuture(FutureInfo f) {
		futureList.remove(f);
	}
	
	public void deleteGroupTodo(GroupTodo todo) {
		groupList.remove(todo);
	}
	
	//get the iterator of TODO info
	public Iterator<LocalTodo> getTodos(){
		Iterator<LocalTodo> it = toDoList.iterator();
		return it;
	}
	
	//get the iterator of TODO info
	public Iterator<GroupTodo> getGroupTodos(){
		Iterator<GroupTodo> it = groupList.iterator();
		return it;
	}
	
	//get the iterator of future info
	public Iterator<FutureInfo> getFutures(){
		Iterator<FutureInfo> it = futureList.iterator();
		return it;
	}
	
	//quit a group
	public void quitGroup() {
		groupNumber = null;
		groupList.clear();
	}
	
	//assign the password
	public void setPass(String password) {
		this.password = password;
	}
	
}
