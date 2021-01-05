/* Name: Statistic.java 
 * Description: 统计图界面
 * Author: 19301050-wumengning 
 * Date: 12-11-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import dataOperation.Export;
import elements.Account;

public class Statistic extends JPanel {
	//declare private attributes
	private ChartPanel frame1;
	private JFreeChart chart;
	private DefaultPieDataset data;
	private MainGUI mainframe;
	private Account account;
	private JPanel buttonPanel;
	private JButton export,reset;
	private int finish,over,giveUp;
	private Export exportFile;
	
	//constructor, initialize the attributes and set the panel
	public Statistic(MainGUI mainframe) {
		//initialize
		this.mainframe = mainframe;
		export = new JButton("导出所有待办和计划");
		reset = new JButton("重新统计");
		buttonPanel = new JPanel(new GridLayout(1,2,15,0));

		//set the components
		export.setAlignmentX(CENTER_ALIGNMENT);
		export.setFont(new Font("宋体", Font.BOLD, 15));
		reset.setAlignmentX(CENTER_ALIGNMENT);
		reset.setFont(new Font("宋体", Font.BOLD, 15));
		
		buttonPanel.setBorder(new EmptyBorder(0,10,0,10));
		buttonPanel.add(export);
		buttonPanel.add(reset);
		buttonPanel.setOpaque(false);
		
		//add listeners
		reset.addActionListener(new ResetHandler());
		export.addActionListener(new ExportHandler());
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(10,10,10,10));
		setOpaque(false);
	}
	
	//launch the image frame
	public void launchImage() {
		//get the date set and chart
		data = getDataSet();
		chart = ChartFactory.createPieChart("待办统计", data, true, false, false);
		frame1 = new ChartPanel(chart, true);
		frame1.setOpaque(false);
		
		//set the percentage
		PiePlot pieplot = (PiePlot) chart.getPlot();
		DecimalFormat df = new DecimalFormat("0.00%");//get an instance of DecimalFormat to set float numbers
		NumberFormat nf = NumberFormat.getNumberInstance();//get an instance of NumberFormat
		StandardPieSectionLabelGenerator sp1 = new StandardPieSectionLabelGenerator("{0}  {2}", nf, df);//get an instance of StandardPieSectionLabelGenerator
		pieplot.setLabelGenerator(sp1);//设置饼图显示百分比
		
		//show information when there is no data
		pieplot.setNoDataMessage("无数据显示");
		pieplot.setNoDataMessageFont(new Font("宋体", Font.BOLD, 30));
		
		//set the pieplot
		pieplot.setCircular(false);
		pieplot.setLabelGap(0.01D);
		pieplot.setIgnoreNullValues(true);
		pieplot.setIgnoreZeroValues(true);
		pieplot.setBackgroundAlpha(0.0f);
		pieplot.setLabelFont(new Font("宋体", Font.BOLD, 20));//solve the messy code
		
		//set the chart
		chart.getTitle().setFont(new Font("宋体", Font.BOLD, 30));
		chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 20));
		chart.setBackgroundPaint(null);
		
		//repaint the panel
		removeAll();
		add(frame1);
		add(buttonPanel);
		this.revalidate();
		this.repaint();
    }
	
	//get the data set
    public DefaultPieDataset getDataSet() {
    	account = mainframe.getAccount();
    	finish = account.getFinish();
    	over = account.getOver();
    	giveUp = account.getGiveUp();
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("已完成", finish);
        dataset.setValue("已过期", over);
        dataset.setValue("已放弃", giveUp);
        return dataset;
    }
    
    //show the file chooser to choose the file to export
    public void ChooseFile() {
    	JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("请选择保存路径");
		chooser.setApproveButtonText("保存");
		chooser.setMultiSelectionEnabled(false);
		chooser.setSelectedFile(new File("Summary.xls"));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("excel(*xls,*.xlsx)","xls","xlsx"));
		int result = chooser.showOpenDialog(this);
		
		//export the file
		if(result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			exportFile = new Export(file,mainframe);
			exportFile.exportExcel();
		}
	}
    
    //inner class, export button handler
    private class ExportHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			ChooseFile();
		}
    	
    }
    
    //inner class, reset button handler
    private class ResetHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			account.resetCount();
			launchImage();
		}
    	
    }
}
