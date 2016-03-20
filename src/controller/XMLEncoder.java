package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import model.Series;
import model.XMLSeries;

public class XMLEncoder {
	
	public final static String FILEPATH = "series.xml";
	private static XMLEncoder instance = null;
	
	private XMLEncoder() {
		
	}
	
	public static XMLEncoder getEncoder() {
		if(instance == null) {
			instance = new XMLEncoder();
		}
		return instance;
	}
	
	public void saveSeries(ArrayList<Series> series) throws JAXBException{
		File file = new File(FILEPATH);
		if(series.isEmpty()) {
			file.delete();
		} else {
			Marshaller m = JAXBContext.newInstance(XMLSeries.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(new XMLSeries(series), file);
		}
	}
	
	public ArrayList<Series> loadSeries() throws JAXBException, IOException {
		File seriesFile = new File(FILEPATH);
		ArrayList<Series> loadedSeries = new ArrayList<Series>();
		if(!seriesFile.createNewFile()) {
			//file did already exist
			Unmarshaller um = JAXBContext.newInstance(XMLSeries.class).createUnmarshaller();
			XMLSeries series = (XMLSeries) um.unmarshal(seriesFile);
			if(series != null) {
				loadedSeries = new ArrayList<Series>(Arrays.asList(series.getSeries()));
			}
		}
		return loadedSeries;
	}

}
