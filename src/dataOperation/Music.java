/* Name: Music.java 
 * Description: ≤•∑≈“Ù¿÷¿‡
 * Author: 19301050-wumengning 
 * Date: 08-12-20 17:10
 * Version: 1.0
 */
package dataOperation;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {
	//declare private attributes
	private Clip clip;
	private AudioInputStream audioInput;
	private File path;
	private int number;
	private String musicLocation;
	private Random random;
	
	//constructor, initialize the attributes
	public Music() {
		random = new Random();
		number = random.nextInt(5)+1;
		musicLocation = new String("musics\\bkTask"+number+".wav");
		path = new File(musicLocation);
	}
	
	//play the music
	public void playMusic(){
		try{
			//check if find the path
			if(path.exists()){
				//play the music
				audioInput = AudioSystem.getAudioInputStream(path);
				clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start(); // start music
				clip.loop(Clip.LOOP_CONTINUOUSLY); // set the music loop
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	//stop the music
	public void stopMusic() {
		//stop the clip and input stream
		clip.stop();
		try {
			audioInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
