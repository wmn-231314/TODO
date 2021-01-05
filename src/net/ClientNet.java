/* Name: ClientNet.java 
 * Description: 客户端网络连接
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import elements.Account;
import elements.GroupTodo;

public class ClientNet {
	//declare private attributes
	private Socket connection;
	public ObjectOutputStream serverObo;
	public ObjectInputStream serverObi;
	private static ClientNet client = new ClientNet();
	
	//constructor, initialize the attributes
	private ClientNet() {
		//make a socket and get stream
		try { // use try-catch
			connection = new Socket("127.0.0.1", 2020);
			serverObo = new ObjectOutputStream(connection.getOutputStream());
			serverObo.flush();
			serverObi = new ObjectInputStream(connection.getInputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//send group TODO and upload account
	public void sendGroupTodo(GroupTodo todo, Account account) {
		try {
			uploadData("addGroupTodo",account);
			serverObo.reset();
			serverObo.writeObject(todo);
			serverObo.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//upload the data to server
	public void uploadData(String order,Account account) {
		try {
			serverObo.writeUTF(order);
			serverObo.reset();
			serverObo.writeObject(account);
			serverObo.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//get the client net
	public static ClientNet getNet() {
		return client;
	}
}
