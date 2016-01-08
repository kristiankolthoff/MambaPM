package de.unima.ki.mamba.evaluation.renderer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.unima.ki.mamba.evaluation.TypeCharacteristic;
import de.unima.ki.mamba.exceptions.CorrespondenceException;


public abstract class Renderer {
	
	protected BufferedWriter bw;
	protected File file;
	
	public Renderer(String file) throws IOException {
		this.file = new File(file);
		this.bw = new BufferedWriter(new FileWriter(this.file));
	}

	public abstract void render(List<TypeCharacteristic> characteristics, String mappingInfo) throws IOException, CorrespondenceException;
	
	public void flush() throws IOException {
		this.bw.flush();
		this.bw.close();
	}
	
}
