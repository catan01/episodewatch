package model;

import java.util.ArrayList;
import java.util.Observable;

import javax.xml.bind.JAXBException;

import controller.Downloader;
import controller.XMLEncoder;
import view.EWFrame;


public class SeriesManager extends Observable {
	
	private ArrayList<Series> series;
	private EWFrame frame;
	
	public SeriesManager() {
		this.series = new ArrayList<Series>();
	}

	public ArrayList<Series> getSeries() {
		return series;
	}

	public void setSeries(ArrayList<Series> series) {
		this.series = series;
	}
	
	public void addSeries(Series series) {
		if(!this.series.contains(series)) {
			this.series.add(series);
			try {
				XMLEncoder.getEncoder().saveSeries(this.series);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			notifyObservers();
		}
	}
	
	public void deleteSeries(Series series) {
		if(this.series.remove(series)) {
			Downloader.getDownloader().deleteBanner(series.getId());
			try {
				XMLEncoder.getEncoder().saveSeries(this.series);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			notifyObservers();
		}
	}
	
	public void exchangeSeries(Series s1, Series s2) {
		if(s1 == s2) return;
		int oldIndexS2 = series.indexOf(s2);
		series.set(series.indexOf(s1), s2);
		series.set(oldIndexS2, s1);
		notifyObservers();
	}
	
	public void shiftSeries(Series from, Series to) {
		if(from == to) return;
		int indexTo = series.indexOf(to);
		series.remove(from);
		series.add(indexTo, from);
		notifyObservers();
	}
	
	public void updateSeries(Series newSeries) {
		series.set(series.indexOf(newSeries), newSeries);
	}

	public EWFrame getFrame() {
		return frame;
	}

	public void setFrame(EWFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void notifyObservers() {
		this.setChanged();
		super.notifyObservers();
	}
}
