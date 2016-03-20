package model;

import java.util.Observable;

public class Episode extends Observable {
	
	private int id;
	private int season;
	private int number;
	private String title;
	private String date;
	private boolean watched;
	
	@SuppressWarnings("unused")
	private Episode() {
	}
	
	public Episode(int id, int season, int number, String title, String date) {
		this.id = id;
		this.season = season;
		this.number = number;
		this.title = title;
		this.date = date;
		this.setWatched(false);
	}
	
	public Episode(int id, int season, int number, String title, String date, boolean watched) {
		this.id = id;
		this.season = season;
		this.number = number;
		this.title = title;
		this.date = date;
		this.setWatched(watched);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isWatched() {
		return watched;
	}

	public void setWatched(boolean watched) {
		this.watched = watched;
		
		notifyObservers();
	}

	@Override
	public void notifyObservers() {
		this.setChanged();
		super.notifyObservers();
	}
	
	@Override
	public boolean equals(Object episode) {
		if(episode == this) {
			return true;
		} else {
			if(episode instanceof Episode) {
				if(((Episode) episode).getId() == this.getId()) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
	}
}
