package de.unima.ki.mamba.semafor;

public class FrameNetOptions {

	private boolean serverModeOn;
	private boolean goldTargetsOn;
	private boolean autoTargetIDModeOn;
	private boolean graphFilesOn;
	private String decodingType;
	private String goldTargetsPath;
	private String absRootPath;
	private String javaHomePath;
	
	public static final String DECODING_TYPE_AD3 = "ad3";
	public static final String DECODING_TYPE_BEAM = "beam";
	
	public FrameNetOptions() {
		
	}
	
	
	
}
