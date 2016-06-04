package myGame;

import java.util.ArrayList;

import myFiles.MyFileReader;

public class OrdinanceBook {
	
	private ArrayList<Ordinance> ords;
	private ArrayList<Boolean> actives;

	public OrdinanceBook() {
		ords = new ArrayList<Ordinance>();
		actives = new ArrayList<Boolean>();
	}
	
	public OrdinanceBook(String filename) {
		ords = new ArrayList<Ordinance>();
		actives = new ArrayList<Boolean>();
		MyFileReader f = new MyFileReader();
		boolean firstLine = true;
		for (String s : f.readTextFile(filename)) {
			if (!firstLine) {
				String[] t = s.split(",");
				int[] p = {Integer.parseInt(t[2]),
						Integer.parseInt(t[3]),
						Integer.parseInt(t[4]),
						Integer.parseInt(t[5]),
						Integer.parseInt(t[6]),
						Integer.parseInt(t[7]),
						Integer.parseInt(t[8]),
						Integer.parseInt(t[9])
				};
				ords.add(new Ordinance(t[0], t[1], p, Integer.parseInt(t[10])));
				actives.add(false);
			} else {
				firstLine = false;
			}
		}
	}
	
	public Ordinance get(String name) {
		return this.ords.get(find(name));
	}
	
	public void add(Ordinance e) {
		ords.add(e);
	}
	
	public void remove(Ordinance e) {
		ords.remove(e);
	}
	
	private int find(String name) {
		for (int i = 0; i < ords.size(); ++i) {
			if (ords.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean isActive(String name) {
		return actives.get(find(name));
	}
	
	public void enact(String name) {
		actives.set(find(name), true);
	}
	
	public void repeal(String name) {
		actives.set(find(name), false);
	}
	
	public ArrayList<Ordinance> getAllActive() {
		ArrayList<Ordinance> ret = new ArrayList<Ordinance>();
		for (int i = 0; i < ords.size(); ++i) {
			if (actives.get(i)) {
				ret.add(ords.get(i));
			}
		}
		return ret;
	}
	
	public ArrayList<Ordinance> getAllInactive() {
		ArrayList<Ordinance> ret = new ArrayList<Ordinance>();
		for (int i = 0; i < ords.size(); ++i) {
			if (!actives.get(i)) {
				ret.add(ords.get(i));
			}
		}
		return ret;
	}
	
	public PointSet sumActive() {
		PointSet points = new PointSet();
		points.set("Military", 0); points.set("Technology", 0);
		points.set("Nature", 0); points.set("Diplomacy", 0);
		points.set("Commerce", 0); points.set("Industry", 0);
		points.set("Population", 0); points.set("Happiness", 0);
		for (Ordinance o : getAllActive()) {
			points.add(o.getPointSet());
		}
		return points;
	}

}
