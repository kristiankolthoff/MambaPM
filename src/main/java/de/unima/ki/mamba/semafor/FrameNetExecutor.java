package de.unima.ki.mamba.semafor;

import java.io.IOException;

public class FrameNetExecutor {

	public FrameNetExecutor() {}
	
	public static void execute(String command) {
		try{
		 Process proc = Runtime.getRuntime().exec(command);
//		 Process proc = Runtime.getRuntime().exec("/home/studio/git/MambaPM/src/resources/framenet-semantic-"
//			 		+ "parsing/semafor-semantic-parser-master/release/fnParserDriver.sh /home/studio/git/MambaPM/"
//			 		+ "src/resources/framenet-semantic-parsing/frame-data/test.txt");
		 proc.waitFor();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

