package main;

public class StaticSound 
{
//	//Final statics
//	public final static int CombatTrigger = 0; public final static int OverWorld = 1;
//	public final static int Battle1 = 2;
//	
//	public final static int CombatLoading = 100; public final static int ItemPickup = 101;
//	public final static int DoorOpen = 102; public final static int OpenBook = 103;
//	public final static int CloseBook = 104; public final static int MoneyPickup = 105;
//	
//	private static Clip clip;
//	private static URL[] soundURL = new URL[200];
//	
//	public static boolean STOPPED = false;
//	public static float volume = 6f;
//	public static FloatControl fc;
//	
//	public StaticSound()
//	{
//		String subFolder = "/Music/";
//		//Indices 0 to 99 are reserved for music files
//		soundURL[CombatTrigger] = getClass().getResource(subFolder + "CombatTrigger.wav");
//		soundURL[OverWorld] = getClass().getResource(subFolder + "OverWorld.wav");
//		soundURL[Battle1] = getClass().getResource(subFolder + "Battle1.wav");
//		
//		subFolder = "/Sounds/";
//		//The rest are Sound effects
//		soundURL[CombatLoading] = getClass().getResource(subFolder + "CombatLoading.wav");
//		soundURL[ItemPickup] = getClass().getResource(subFolder + "ItemPickup.wav");
//		soundURL[DoorOpen] = getClass().getResource(subFolder + "DoorOpen.wav");
//		soundURL[OpenBook] = getClass().getResource(subFolder + "OpenBook.wav");
//		soundURL[CloseBook] = getClass().getResource(subFolder + "CloseBook.wav");
//		soundURL[MoneyPickup] = getClass().getResource(subFolder + "MoneyPickup.wav");
//	}
//	
//	public static void setFile(int i)
//	{
//		try
//		{
//			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
//			clip = AudioSystem.getClip();
//			clip.open(ais);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		clip.addLineListener(new LineListener() 
//		{
//			@Override
//			public void update(LineEvent event) 
//			{
//				if(event.getType() == LineEvent.Type.STOP)
//				{
//					STOPPED = true;
//					playMusic(musicI);
//				}
//			}
//			
//		});
//		
//		fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
//		
//		checkVolume();
//	}
//	
//	private static int musicI;
//	public static void playMusic(int i)
//	{
//		if(musicI != i || !STOPPED)
//		{
//			stop(musicI);
//		}
//		
//		musicI = i;
//		setFile(musicI);
//		
//		clip.start();
//		clip.loop(Clip.LOOP_CONTINUOUSLY);
//	}
//	
//	public static void playSE(int i)
//	{
//		setFile(i);
//		
//		clip.start();
//	}
//	
//	public static void stop(int i)
//	{
//		setFile(i);
//		
//		clip.stop();
//	}
//	
//	public static void checkVolume()
//	{
//		fc.setValue(volume);
//	}
}
