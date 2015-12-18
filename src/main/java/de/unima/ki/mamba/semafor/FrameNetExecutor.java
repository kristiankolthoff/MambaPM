package de.unima.ki.mamba.semafor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FrameNetExecutor {

	public FrameNetExecutor() {
		
	}
	
	public static void main(String[] args) {
		
		 try {
	            Process proc = Runtime.getRuntime().exec("/home/studio/git/MambaPM/src/resources/framenet-semantic-parsing/semafor-semantic-parser-master/release/fnParserDriver.sh /home/studio/git/MambaPM/src/resources/framenet-semantic-parsing/frame-data/test.txt"); //Whatever you want to execute
	            BufferedReader read = new BufferedReader(new InputStreamReader(
	                    proc.getInputStream()));
	            try {
	                proc.waitFor();
	            } catch (InterruptedException e) {
	                System.out.println(e.getMessage());
	            }
	            while (read.ready()) {
	                System.out.println(read.readLine());
	            }
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
		
//		ProcessBuilder pb = new ProcessBuilder("bash" , "/home/studio/git/MambaPM/src/resources/framenet-semantic-parsing/semafor-semantic-parser-master/release/fnParserDriver.sh", "/home/studio/git/MambaPM/src/resources/framenet-semantic-parsing/frame-data/test.txt");
//		try {
//			Process p = pb.start();
//			InputStream is = p.getInputStream();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
