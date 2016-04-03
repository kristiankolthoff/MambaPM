package de.unima.ki.mamba.evaluation.renderer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.html.B;
import org.apache.ecs.html.Caption;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import de.unima.ki.mamba.evaluation.Characteristic;
import de.unima.ki.mamba.evaluation.TypeCharacteristic;
import de.unima.ki.mamba.exceptions.CorrespondenceException;
import de.unima.ki.mamba.om.alignment.CorrespondenceType;


public class HTMLTableRenderer extends Renderer{

	public static final boolean PRETTY_PRINT = true;
	
	private Table table;
	private DecimalFormat df;
	private boolean initialized;
	
	public HTMLTableRenderer(String file) throws IOException {
		super(file);
		this.table = new Table();
		this.df = new DecimalFormat("#.###");
		this.initialized = false;
	}
	
	private void init(List<TypeCharacteristic> characteristics) throws CorrespondenceException {
		this.startTable();
		this.createTableHeadRow1();
		this.createTableHeadRow3(characteristics);
		this.createTableHeadRow2();
		this.initialized = true;
	}
	
	@Override
	public void render(List<? extends Characteristic> characteristics, String mappingInfo)
			throws IOException, CorrespondenceException {
		List<TypeCharacteristic> typeCharacteristics = new ArrayList<>();
		for(Characteristic c : characteristics) {
			if(c instanceof TypeCharacteristic) {
				typeCharacteristics.add((TypeCharacteristic) c);
			}
		}
		if(!this.initialized) {
			this.init(typeCharacteristics);			
		}
		this.appendMetricData(typeCharacteristics, mappingInfo);
	}
	
	@Override
	public void flush() throws IOException {
		this.bw.append(this.table.toString());
		super.flush();
	}

	private void startTable() {
		this.table.setBorder(2);
		this.table.addAttribute("cellpadding", "3");
		this.table.addAttribute("cellspacing", "5");
		Caption caption = new Caption().addElement("Matcher Evaluation Summary");
		caption.setStyle("font-size:50;font-weight:bold");
		this.table.addElement(caption);
		this.table.setPrettyPrint(HTMLTableRenderer.PRETTY_PRINT);
		TR trStart = new TR();
		trStart.addAttribute("colspan", "13");
		trStart.setPrettyPrint(HTMLTableRenderer.PRETTY_PRINT);
		this.table.addElement(trStart);
	}
	
	private void createTableHeadRow1() {
		TR trHead1 = new TR();
		trHead1.setPrettyPrint(HTMLTableRenderer.PRETTY_PRINT);
		trHead1.addElement(new TH().setStyle("border:none;"));
		TH th1 = new TH();
		th1.setStyle("border:none;");
		trHead1.addElement(th1.addAttribute("width", "15"));
		trHead1.addElement(new TH("Precision").addAttribute("colspan", "3"));
		TH th2 = new TH();
		th2.setStyle("border:none;");
		trHead1.addElement(th2.addAttribute("width", "20"));
		trHead1.addElement(new TH("Recall").addAttribute("colspan", "3"));
		TH th3 = new TH();
		th3.setStyle("border:none;");
		trHead1.addElement(th3.addAttribute("width", "20"));
		trHead1.addElement(new TH("F-Measure").addAttribute("colspan", "3"));
		for(String type : CorrespondenceType.getNames()) {
			TH th4 = new TH();
			th4.setStyle("border:none;");
			trHead1.addElement(th4.addAttribute("width", "20"));
			trHead1.addElement(new TH("Recall " + type).addAttribute("colspan", "3"));
		}
		this.table.addElement(trHead1);
	}
	
	private void createTableHeadRow3(List<TypeCharacteristic> characteristics) throws CorrespondenceException {
		TR trHead1 = new TR();
		trHead1.setPrettyPrint(HTMLTableRenderer.PRETTY_PRINT);
		trHead1.addElement(new TH().setStyle("border:none;"));
		TH th1 = new TH();
		th1.setStyle("border:none;");
		trHead1.addElement(th1.addAttribute("width", "15"));
		trHead1.addElement(new TH("").addAttribute("colspan", "3"));
		TH th2 = new TH();
		th2.setStyle("border:none;");
		trHead1.addElement(th2.addAttribute("width", "20"));
		trHead1.addElement(new TH("").addAttribute("colspan", "3"));
		TH th3 = new TH();
		th3.setStyle("border:none;");
		trHead1.addElement(th3.addAttribute("width", "20"));
		trHead1.addElement(new TH("").addAttribute("colspan", "3"));
		for(CorrespondenceType type : CorrespondenceType.values()) {
			TH th4 = new TH();
			th4.setStyle("border:none;");
			trHead1.addElement(th4.addAttribute("width", "20"));
			trHead1.addElement(new TH(String.valueOf(this.df.format
					(TypeCharacteristic.getTypePercentage(characteristics, type) * 100)
					+ "%"))
					.addAttribute("colspan", "3"));
		}
		this.table.addElement(trHead1);
	}
	
	private void createTableHeadRow2() {
		final String AVG_ICON = "&#x2205";
		TR trHead2 = new TR();
		trHead2.setPrettyPrint(HTMLTableRenderer.PRETTY_PRINT);
		trHead2.addElement(new TD(new B("Approach")));
		trHead2.addElement(new TD().setStyle("border:none;"));
		for (int i = 0; i < 3; i++) {
			trHead2.addElement(new TD(AVG_ICON + ";-mic"));
			trHead2.addElement(new TD(AVG_ICON + "-mac"));
			trHead2.addElement(new TD("SD"));
			trHead2.addElement(new TD().setStyle("border:none;"));
		}
		for(int i = 0; i < CorrespondenceType.values().length; i++) {
			trHead2.addElement(new TD(AVG_ICON + ";-mic"));
			trHead2.addElement(new TD(AVG_ICON + "-mac"));
			trHead2.addElement(new TD("SD"));
			if(i!=CorrespondenceType.values().length-1) {
				trHead2.addElement(new TD().setStyle("border:none;"));
			}
		}
		this.table.addElement(trHead2);
	}
	

	private void appendMetricData(List<TypeCharacteristic> characteristics, String mappingInfo) throws CorrespondenceException {
		List<Characteristic> tCharacteristics = new ArrayList<>();
		for(TypeCharacteristic c: characteristics) {
			tCharacteristics.add(c);
		}
		this.table.addElement(new TR().addAttribute("colspan", "13"));
		/**
		 * Standard metrics
		 */
		String microPrecision = this.df.format(Characteristic.getPrecisionMicro(tCharacteristics));
		String macroPrecision = this.df.format(Characteristic.getPrecisionMacro(tCharacteristics));
		String stdDevPrecision = this.df.format(Characteristic.getPrecisionStdDev(tCharacteristics));
		String microRecall = this.df.format(Characteristic.getRecallMicro(tCharacteristics));
		String macroRecall = this.df.format(Characteristic.getRecallMacro(tCharacteristics));
		String stdDevRecall = this.df.format(Characteristic.getRecallStdDev(tCharacteristics));
		String microFMeasure = this.df.format(Characteristic.getFMeasureMicro(tCharacteristics));
		String macroFMeasure = this.df.format(Characteristic.getFMeasureMacro(tCharacteristics));
		String stdDevFMeasure = this.df.format(Characteristic.getFMeasureStdDev(tCharacteristics));
		
		this.table.addElement(new TD(mappingInfo));
		this.table.addElement(new TD().setStyle("border:none;"));
		this.table.addElement(new TD(microPrecision));
		this.table.addElement(new TD(macroPrecision));
		this.table.addElement(new TD(stdDevPrecision));
		this.table.addElement(new TD().setStyle("border:none;"));
		this.table.addElement(new TD(microRecall));
		this.table.addElement(new TD(macroRecall));
		this.table.addElement(new TD(stdDevRecall));
		this.table.addElement(new TD().setStyle("border:none;"));
		this.table.addElement(new TD(microFMeasure));
		this.table.addElement(new TD(macroFMeasure));
		this.table.addElement(new TD(stdDevFMeasure));
		/**
		 * Recall values for different correpsondence types
		 */
		for (CorrespondenceType type : CorrespondenceType.values()) {
			String typeRecallMicro = null;
			String typeRecallMacro = null;
			String typeRecallStdDev = null;
			/**
			 * Check for NaNs
			 */
			if(Double.isNaN(TypeCharacteristic.getRecallMicro(characteristics, type)))  {
				typeRecallMicro = "NA";
			} else {
				typeRecallMicro = this.df.format(TypeCharacteristic.getRecallMicro(characteristics, type));			
			}
			if(Double.isNaN(TypeCharacteristic.getRecallMacro(characteristics, type))) {
				typeRecallMacro = "NA";
			} else {
				typeRecallMacro = this.df.format(TypeCharacteristic.getRecallMacro(characteristics, type));				
			}
			if(Double.isNaN(TypeCharacteristic.getRecallStdDev(characteristics, type))) {
				typeRecallStdDev = "NA";
			} else {
				typeRecallStdDev = this.df.format(TypeCharacteristic.getRecallStdDev(characteristics, type));				
			}
			this.table.addElement(new TD().setStyle("border:none;"));
			this.table.addElement(new TD(typeRecallMicro));
			this.table.addElement(new TD(typeRecallMacro));
			this.table.addElement(new TD(typeRecallStdDev));
		}
	}

}
