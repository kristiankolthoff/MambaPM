package de.unima.ki.mamba.semafor;

import java.io.File;

public class FrameNetOptions {

	private boolean serverModeOn;
	private boolean autoTargetIDStrictModeOn;
	private boolean graphFilesOn;
	private String decodingType;
	private String goldTargetsPath;
	private String javaHomePath;
	
	public static final String DECODING_TYPE_AD3 = "ad3";
	public static final String DECODING_TYPE_BEAM = "beam";
	public static final String FN_FILE_NAME = "mambaPM";
	public static final String FN_FILE_OUT_NAME = FN_FILE_NAME + ".out";
	public static final String ABS_PATH_SEMAFOR = new File(".").getAbsolutePath() + "/src/resources/framenet-semantic-parsing/semafor/";
	public static final String ABS_PATH_FNDATA = new File(".").getAbsolutePath() + "/src/resources/framenet-semantic-parsing/frame-data/";
	public static final String ABS_PATH_DRIVER_SCRIPT = ABS_PATH_SEMAFOR + "release/fnParserDriver.sh";
	public static final String ABS_PATH_FILE_CONFIG = ABS_PATH_SEMAFOR + "release/config";
	public static final String ABS_PATH_DIR_TEMP = ABS_PATH_SEMAFOR + "temp/";
	
	public FrameNetOptions(String goldTargetsPath) {
		this.goldTargetsPath = goldTargetsPath;
	}
	
	public static FrameNetOptions getStandardOpt() {
		//TODO: find out correct path of java installation
		final String JAVA_HOME_PATH = "/usr/lib/jvm/java-8-oracle/bin";
		FrameNetOptions fnOpt = new FrameNetOptions("null")
				.setServerModeOn(true)
				.setAutoTargetIDStrictModeOn(true)
				.setGraphFilesOn(true)
				.setDecodingType(DECODING_TYPE_AD3)
				.setJavaHomePath(JAVA_HOME_PATH);
		return fnOpt;
	}

	public boolean isServerModeOn() {
		return serverModeOn;
	}

	public FrameNetOptions setServerModeOn(boolean serverModeOn) {
		this.serverModeOn = serverModeOn;
		return this;
	}

	public boolean isAutoTargetIDStrictModeOn() {
		return autoTargetIDStrictModeOn;
	}

	public FrameNetOptions setAutoTargetIDStrictModeOn(boolean autoTargetIDStrictModeOn) {
		this.autoTargetIDStrictModeOn = autoTargetIDStrictModeOn;
		return this;
	}

	public boolean isGraphFilesOn() {
		return graphFilesOn;
	}

	public FrameNetOptions setGraphFilesOn(boolean graphFilesOn) {
		this.graphFilesOn = graphFilesOn;
		return this;
	}

	public String getDecodingType() {
		return decodingType;
	}

	public FrameNetOptions setDecodingType(String decodingType) {
		this.decodingType = decodingType;
		return this;
	}

	public String getGoldTargetsPath() {
		return goldTargetsPath;
	}

	public FrameNetOptions setGoldTargetsPath(String goldTargetsPath) {
		this.goldTargetsPath = goldTargetsPath;
		return this;
	}

	public String getJavaHomePath() {
		return javaHomePath;
	}

	public FrameNetOptions setJavaHomePath(String javaHomePath) {
		this.javaHomePath = javaHomePath;
		return this;
	}

}