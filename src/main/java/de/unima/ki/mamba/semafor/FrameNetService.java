package de.unima.ki.mamba.semafor;

import java.io.IOException;

public class FrameNetService {

	private FrameNetOptions fnOpt;
	
	public FrameNetService() {
		this.fnOpt = FrameNetOptions.getStandardOpt();
		if(this.fnOpt.isServerModeOn()) {
//			startServer();
		}
	}
	
	public FrameNetService(FrameNetOptions fnOpt) {
		this.fnOpt = fnOpt;
		if(this.fnOpt.isServerModeOn()) {
			startServer();
		}
	}
	
	public void runFNSemanticParsing() {
		try{
		 Process proc = Runtime.getRuntime().exec(FrameNetOptions.ABS_PATH_DRIVER_SCRIPT + " " 
				 			+ FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_NAME);
		 proc.waitFor();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void startServer() {
		try{
			 Process proc = Runtime.getRuntime().exec(FrameNetOptions.ABS_PATH_DRIVER_SCRIPT + " " 
					 			+ FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_NAME);
			 //Notice that this will run as long as it is not shut down. Hence this should be removed here
			 proc.waitFor();
	         } catch (InterruptedException e) {
	             e.printStackTrace();
	         } catch (IOException e) {
				e.printStackTrace();
			}
	}

	public FrameNetOptions getFnOpt() {
		return fnOpt;
	}

	public void setFnOpt(FrameNetOptions fnOpt) {
		this.fnOpt = fnOpt;
	}
	
}

