package myGame;

import java.util.ArrayList;
import java.util.HashMap;

import myFiles.MyFileHandler;

public class Options {
	
	HashMap<String,String> opts;
	ArrayList<String> cKeys;
	ArrayList<String> oKeys;

	public Options(String configFile, String optionsFile) {
		// Creating hash map, array lists and reader.
		opts = new HashMap<String,String>();
		cKeys = new ArrayList<String>();
		oKeys = new ArrayList<String>();
		MyFileHandler rdr = new MyFileHandler();
		// Load in config file.
		ArrayList<String> c = rdr.readTextFile(configFile);
		for (String s : c) {
			if (!s.equals("")) {
				opts.put(s.split("\\:")[0], s.split("\\:")[1]);
				cKeys.add(s.split("\\:")[0]);
			}
		}
		// Load in options file.
		ArrayList<String> o = rdr.readTextFile(optionsFile);
		for (String s : o) {
			if (!s.equals("")) {
				opts.put(s.split("\\:")[0], s.split("\\:")[1]);
				oKeys.add(s.split("\\:")[0]);
			}
		}
	}
	
	public boolean getStatusFullscreen() {return opts.get("fullscreen").equals("true");}
	public boolean getStatusFPS() {return opts.get("fpscounter").equals("true");}
	public boolean getStatusTTAD() {return opts.get("timetoactdraw").equals("true");}
	public boolean getStatusDebug() {return opts.get("debugall").equals("true");}
	
	public void setStatusFullscreen(boolean in) {opts.put("fullscreen", Boolean.toString(in));}
	public void setStatusFPS(boolean in) {opts.put("fpscounter", Boolean.toString(in));}
	public void setStatusTTAD(boolean in) {opts.put("timetoactdraw", Boolean.toString(in));}
	public void setStatusDebug(boolean in) {opts.put("debugall", Boolean.toString(in));}
	
	public int getValueScreenWidth() {return Integer.parseInt(opts.get("resolutionx"));}
	public int getValueScreenHeight() {return Integer.parseInt(opts.get("resolutiony"));}
	public int getValueVolumeMaster() {return Integer.parseInt(opts.get("volumemaster"));}
	public int getValueVolumeMusic() {return Integer.parseInt(opts.get("volumemusic"));}
	public int getValueVolumeSound() {return Integer.parseInt(opts.get("volumesound"));}
	
	public void setValueScreenWidth(int in) {opts.put("resolutionx", Integer.toString(in));}
	public void setValueScreenHeight(int in) {opts.put("resolutiony", Integer.toString(in));}
	public void setValueVolumeMaster(int in) {opts.put("volumemaster", Integer.toString(in));}
	public void setValueVolumeMusic(int in) {opts.put("volumemusic", Integer.toString(in));}
	public void setValueVolumeSound(int in) {opts.put("volumesound", Integer.toString(in));}
	
	public boolean getStatus(String key) {return opts.get(key).equals("true");}
	public int getValue(String key) {return Integer.parseInt(opts.get(key));}
	public String getText(String key) {return opts.get(key);}
	
	public void setStatus(String key, boolean in) {opts.put(key, Boolean.toString(in));}
	public void setValue(String key, int in) {opts.put(key, Integer.toString(in));}
	public void setText(String key, String in) {opts.put(key, in);}
	
	public void writeToFile(String configFile, String optionsFile) {
		MyFileHandler rdr = new MyFileHandler();
		ArrayList<String> cLines = new ArrayList<String>();
		ArrayList<String> oLines = new ArrayList<String>();
		for (String key : cKeys) {
			cLines.add(key + ":" + opts.get(key));
		}
		for (String key : oKeys) {
			oLines.add(key + ":" + opts.get(key));
		}
		rdr.writeTextFile(cLines, configFile);
		rdr.writeTextFile(oLines, optionsFile);
	}

}
