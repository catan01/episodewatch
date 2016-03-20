package model;

import java.util.ArrayList;

public class Season {
	
	private int number;
	private ArrayList<Episode> episodes;
	
	public Season(int number) {
		this.number = number;
		this.episodes = new ArrayList<Episode>();
	}
	
	public String getFormattedNumber() {
		if(number > 9) {
			return "" + this.number;
		} else {
			return "0" + this.number;
		}
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ArrayList<Episode> getEpisodes() {
		return episodes;
	}
	
	public void setEpisodes(ArrayList<Episode> episodes) {
		this.episodes = episodes;
	}
}
