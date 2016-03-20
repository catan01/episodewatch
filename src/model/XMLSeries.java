package model;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XMLSeries {

	private Series[] series;
	
	@SuppressWarnings("unused")
	private XMLSeries(){}
	
	public XMLSeries(ArrayList<Series> series) {
		this.series = series.toArray(new Series[0]);
	}

	public Series[] getSeries() {
		return series;
	}

	public void setSeries(Series[] series) {
		this.series = series;
	}
}
