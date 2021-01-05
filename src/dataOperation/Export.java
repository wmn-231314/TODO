/* Name: Export.java 
 * Description: 导出文件类
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package dataOperation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import clientGUI.MainGUI;
import elements.Account;
import elements.FutureInfo;
import elements.GroupTodo;
import elements.LocalTodo;

public class Export{
	//declare private attributes
	private FileOutputStream out;
	private HSSFWorkbook hssfWorkbook;
	private HSSFSheet hssfSheet1,hssfSheet2,hssfSheet3;
	private HSSFRow rowSheet1,rowSheet2,rowSheet3;
	private HSSFCell cell1,cell2,cell3;
	private Account account;
	private String time;
	private SimpleDateFormat simpleDateFormat;
	
	//constructor, initialize the attributes
	public Export(File file,MainGUI mainframe) {
		account = mainframe.getAccount();
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		hssfWorkbook = new HSSFWorkbook();
		try {
			out = new FileOutputStream(file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//create the sheet
		hssfSheet1 = hssfWorkbook.createSheet("待办事项");
		hssfSheet2 = hssfWorkbook.createSheet("小组待办");
		hssfSheet3 = hssfWorkbook.createSheet("未来目标");
	}
	
	//export excel file
	public void exportExcel() {
		//write all the list
		writeLocal();
		writeGroup();
		writeFuture();
		try {
			//write to the file
			hssfWorkbook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//write local todo list
	private void writeLocal() {
		int count = 0;
		//create rows and cells
		rowSheet1 = hssfSheet1.createRow(count);
		cell1 = rowSheet1.createCell(0);
		cell1.setCellValue("待办名称");
		cell1 = rowSheet1.createCell(1);
		cell1.setCellValue("截止日期");
		cell1 = rowSheet1.createCell(2);
		cell1.setCellValue("是否使用番茄钟");
		cell1 = rowSheet1.createCell(3);
		cell1.setCellValue("单次任务时长");
		cell1 = rowSheet1.createCell(4);
		cell1.setCellValue("任务次数");
		count++;
		
		//write to the sheet
		Iterator<LocalTodo> it = account.getTodos();
		while(it.hasNext()) {
			LocalTodo todo = it.next();
			rowSheet1 = hssfSheet1.createRow(count);
			cell1 = rowSheet1.createCell(0);
			cell1.setCellValue(todo.getTitle());
			cell1 = rowSheet1.createCell(1);
			time = simpleDateFormat.format(todo.getDate());
			cell1.setCellValue(time);
			cell1 = rowSheet1.createCell(2);
			cell1.setCellValue(todo.isStudy());
			if(todo.isStudy()) {
				cell1 = rowSheet1.createCell(3);
				cell1.setCellValue(todo.getPerTime());
				cell1 = rowSheet1.createCell(4);
				cell1.setCellValue(todo.getTimes());
			}
			count++;
		}
	}
	
	//write group todo list
	private void writeGroup() {
		int count = 0;
		//create rows and cells
		rowSheet2 = hssfSheet2.createRow(count);
		cell2 = rowSheet2.createCell(0);
		cell2.setCellValue("待办名称");
		cell2 = rowSheet2.createCell(1);
		cell2.setCellValue("截止日期");
		cell2 = rowSheet2.createCell(2);
		cell2.setCellValue("备注信息");
		count++;
		
		//write to the sheet
		Iterator<GroupTodo> it = account.getGroupTodos();
		while(it.hasNext()) {
			GroupTodo todo = it.next();
			rowSheet2 = hssfSheet2.createRow(count);
			cell2 = rowSheet2.createCell(0);
			cell2.setCellValue(todo.getTitle());
			cell2 = rowSheet2.createCell(1);
			time = simpleDateFormat.format(todo.getDate());
			cell2.setCellValue(time);
			cell2 = rowSheet2.createCell(2);
			cell2.setCellValue(todo.getContent());
			count++;
		}
	}
	
	//write future list
	private void writeFuture() {
		int count = 0;
		//create rows and cells
		rowSheet3 = hssfSheet3.createRow(count);
		cell3 = rowSheet3.createCell(0);
		cell3.setCellValue("计划名称");
		cell3 = rowSheet3.createCell(1);
		cell3.setCellValue("截止日期");
		cell3 = rowSheet3.createCell(2);
		cell3.setCellValue("备注信息");
		count++;
		
		//write to the sheet
		Iterator<FutureInfo> it = account.getFutures();
		while(it.hasNext()) {
			FutureInfo info = it.next();
			rowSheet3 = hssfSheet3.createRow(count);
			cell3 = rowSheet3.createCell(0);
			cell3.setCellValue(info.getTitle());
			cell3 = rowSheet3.createCell(1);
			time = simpleDateFormat.format(info.getDate());
			cell3.setCellValue(time);
			cell3 = rowSheet3.createCell(2);
			cell3.setCellValue(info.getContent());
			count++;
		}
	}
	
}