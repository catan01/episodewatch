package controller;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import model.Episode;
import model.Series;

public class XMLParser {

	private static final String SERIESTAG = "Series";
	private static final String EPISODETAG = "Episode";
	private static final String IDTAG = "id";
	private static final String SERIESNAMETAG = "SeriesName";
	private static final String BANNERTAG = "banner";
	private static final String STATUSTAG = "status";
	private static final String EPISODENAMETAG = "EpisodeName";
	private static final String EPISODENUMBERTAG = "EpisodeNumber";
	private static final String EPISODEDATETAG = "FirstAired";
	private static final String EPISODESEASONTAG = "SeasonNumber";
	
	private static XMLParser instance = null;

	private XMLParser() {
	}

	public static XMLParser getParser() {
		if(instance == null) {
			instance = new XMLParser();
		}
		return instance;
	}

	public Series parseSeries(InputStream stream) throws XMLStreamException, FactoryConfigurationError, IOException {
		XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(stream);

		
		String tag = "";
		int id = 0;
		int episodeNr = 0;
		int seasonNr = 0;
		String name = "";
		String date = "";
		String bannerURL = "";
		boolean continuing = true;
		
		Series newSeries = null;
		
		while(parser.hasNext()) {
			//seriesInfo
			switch (parser.getEventType()) {
			case XMLStreamConstants.START_ELEMENT: {
				tag = parser.getLocalName();
			}
			break;
			case XMLStreamConstants.CHARACTERS: {
				//filter out empty values
				if(parser.getText().startsWith("\n")) {
					break;
				}
				switch(tag) {
				
				case IDTAG: {
					id = Integer.valueOf(parser.getText());
					tag = "";
				}
				break;
				case SERIESNAMETAG: {
					name = parser.getText();
					tag = "";
				}
				break;
				case BANNERTAG: {
					bannerURL = parser.getText();
					tag = "";
				}
				break;
				case STATUSTAG: {
					//series is "Continuing" or "Ended"
					if(parser.getText().equals("Ended")) {
						continuing = false;
					}
					tag = "";
				}
				case EPISODENAMETAG: {
					name = parser.getText();
					tag = "";
				}
				break;
				case EPISODENUMBERTAG: {
					episodeNr = Integer.valueOf(parser.getText());
					tag = "";
				}
				break;
				case EPISODESEASONTAG: {
					seasonNr = Integer.valueOf(parser.getText());
					tag = "";
				}
				break;
				case EPISODEDATETAG: {
					date = parser.getText();
					tag = "";
				}
				break;
				}
			}
			break;
			case XMLStreamConstants.END_ELEMENT: {
				String endtag = parser.getLocalName();
				switch(endtag) {
				case SERIESTAG: {
					newSeries = new Series(id, name, bannerURL, continuing);
					tag = "";
					id = 0;
					name = "";
					bannerURL = "";
					continuing = true;
				}
				break;
				case EPISODETAG: {
					if(seasonNr > 0) {
						newSeries.getEpisodes().add(new Episode(id, seasonNr, episodeNr, name, date));
						tag = "";
						id = 0;
						name = "";
						episodeNr = 0;
						seasonNr = 0;
					}
				}
				break;
				}
			}
			break;
			}
			
			parser.next();
		}
		
		parser.close();
		stream.close();
		
		return newSeries;
	}
	
}
