package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Sound 
{
	//Final statics
	public final static int CombatTrigger = 0; public final static int OverWorld = 1;
	public final static int Battle1 = 2;
	
	public final static int CombatLoading = 100; public final static int ItemPickup = 101;
	public final static int DoorOpen = 102; public final static int OpenBook = 103;
	public final static int CloseBook = 104; public final static int MoneyPickup = 105;
	
	
	Clip clip;
	URL soundURL[] = new URL[200];
	FloatControl fc; 
	public boolean STOPPED = false;
	public int volumeScale = 8; //Divide scale by 10 points
	public float volume;
	
	public Sound()
	{
		String subFolder = "/Music/";
		//Indices 0 to 99 are reserved for music files
		soundURL[CombatTrigger] = getClass().getResource(subFolder + "CombatTrigger.wav");
		soundURL[OverWorld] = getClass().getResource(subFolder + "OverWorld.wav");
		soundURL[Battle1] = getClass().getResource(subFolder + "Battle1.wav");
		
		subFolder = "/Sounds/";
		//The rest are Sound effects
		soundURL[CombatLoading] = getClass().getResource(subFolder + "CombatLoading.wav");
		soundURL[ItemPickup] = getClass().getResource(subFolder + "ItemPickup.wav");
		soundURL[DoorOpen] = getClass().getResource(subFolder + "DoorOpen.wav");
		soundURL[OpenBook] = getClass().getResource(subFolder + "OpenBook.wav");
		soundURL[CloseBook] = getClass().getResource(subFolder + "CloseBook.wav");
		soundURL[MoneyPickup] = getClass().getResource(subFolder + "MoneyPickup.wav");
	}
	
	public void setFile(int i)
	{
		try
		{
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
			
			clip.addLineListener(new LineListener() 
			{
				@Override
				public void update(LineEvent event) 
				{
					if(event.getType() == LineEvent.Type.STOP)
					{
						STOPPED = true;
					}
				}
				
			});
			
			fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			
			checkVolume();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void play()
	{
		clip.start();
	}
	
	public void loop()
	{
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop()
	{
		clip.stop();
	}
	
	public void checkVolume()
	{
		fc.setValue(volume);
	}
	
}
