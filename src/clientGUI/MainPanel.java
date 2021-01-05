/* Name: MainPanel.java 
 * Description: Ö÷Ãæ°å
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package clientGUI;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
	//declare private attribute
    private ImageIcon imgicon;
    
    //constructor, initialize the attribute
    public MainPanel(String path) {
    	imgicon = new ImageIcon(path);
    }
    
	//overloading painComponent to set background image
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = imgicon.getImage();
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
