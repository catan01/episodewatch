package model;

import java.util.ArrayList;
import java.util.Observable;

public class Series extends Observable {

	private int id;
	private String title;
	private String bannerURL;
	private boolean continuing;
	private ArrayList<Episode> episodes;
	
	@SuppressWarnings("unused")
	private Series(){
	}

	public Series(int id, String title, String bannerURL) {
		this.id = id;
		this.title = title;
		this.bannerURL = bannerURL;
		this.continuing = true;
		this.episodes = new ArrayList<Episode>();
	}

	public Series(int id, String title, String bannerURL, boolean continuing) {
		this.id = id;
		this.title = title;
		this.bannerURL = bannerURL;
		this.continuing = continuing;
		this.episodes = new ArrayList<Episode>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<Episode> getEpisodes() {
		return episodes;
	}
	
	public void setEpisodes(ArrayList<Episode> episodes) {
		this.episodes = episodes;
	}

	public String getBannerURL() {
		return bannerURL;
	}

	public void setBannerURL(String bannerURL) {
		this.bannerURL = bannerURL;
	}
	
	public boolean isContinuing() {
		return continuing;
	}

	public void setContinuing(boolean continuing) {
		this.continuing = continuing;
	}

	@Override
	public void notifyObservers() {
		this.setChanged();
		super.notifyObservers();
	}
	
	@Override
	public String toString() {
		return title;
	}
	
	public boolean equals(Object series) {
		if(series == this) {
			return true;
		} else {
			if(series instanceof Series) {
				if(((Series) series).getId() == this.getId()) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
	}
}
