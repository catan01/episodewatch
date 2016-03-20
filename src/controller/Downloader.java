package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import model.Series;

public class Downloader {

	private static final String TEMPDIR = "temp";
	private static final String BANNERDIR = "banners";

	private static final String APIURL = "http://www.thetvdb.com/api/";
	private static final String BANNERURL = "http://www.thetvdb.com/banners/";

	private static final String GETSERIES = APIURL + "GetSeries.php?seriesname="; 
	private static final String SERIESURL = "/series/";
	private static final String SERIESALL = "/all/en.zip";

	private static Downloader instance;

	private HttpURLConnection con;
	private String apikey = "";

	private Downloader() {
	}

	public void setApiKey(String key) {
		if(key != null)
			this.apikey = key;
	}

	public static Downloader getDownloader() {
		if(instance == null) {
			instance = new Downloader();
		}
		return instance;
	}

	public Series[] searchSeries(String name) throws IOException, XMLStreamException, FactoryConfigurationError {
		ArrayList<Series> newSeries = new ArrayList<Series>();
		InputStream stream = connect(GETSERIES + name);
		XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(stream);

		String tag = "";
		int id = 0;
		String title = "";
		String banner = "";

		while(parser.hasNext()) {
			switch (parser.getEventType()) {

			case XMLStreamConstants.START_ELEMENT: {
				tag = parser.getLocalName();
			}
			break;
			case XMLStreamConstants.CHARACTERS: {
				switch (tag) {

				case "seriesid": {
					id = Integer.valueOf(parser.getText());
					tag = "";
				}
				break;
				case "SeriesName": {
					title = parser.getText();
					tag = "";
				}
				break;
				case "banner": {
					banner = parser.getText();
					tag = "";
				}
				break;
				}
			}
			break;
			case XMLStreamConstants.END_ELEMENT: {
				if(parser.getLocalName().equals("Series")) {
					if(!banner.equals("")) {
						//banner found
						newSeries.add(new Series(id, title, BANNERURL + banner));
					} else {
						//no banner found
						newSeries.add(new Series(id, title, null));
					}
					tag = "";
					id = 0;
					title = "";
					banner = "";
				}
			}
			break;
			}
			parser.next();
		}
		stream.close();
		con.disconnect();

		return newSeries.toArray(new Series[0]);
	}

	public Series getSeries(int id) throws IOException, XMLStreamException, FactoryConfigurationError {
		ZipFile zipFile = new ZipFile(download(APIURL + apikey + SERIESURL + id + SERIESALL));
		Series series = XMLParser.getParser().parseSeries(zipFile.getInputStream(zipFile.getEntry("en.xml")));
		zipFile.close();
		return series;
	}

	private InputStream connect(String address) throws IOException {
		InputStream stream = null;

		//replace whitespaces with %20
		address = address.replace(" ", "%20");

		URL url = new URL(address);
		con = (HttpURLConnection) url.openConnection();
		stream = con.getInputStream();
		return stream;
	}

	private File download(String address) throws IOException {
		InputStream stream = this.connect(address);

		File tempFile = new File(TEMPDIR);

		if(!tempFile.exists()) {
			tempFile.mkdir();
		}

		while((tempFile = new File(TEMPDIR + File.separatorChar + generateFilename())).exists()) {
			//create new tempfile
		}
		tempFile.deleteOnExit();

		FileOutputStream out = new FileOutputStream(tempFile);
		byte[] b = new byte[1024];
		int count;

		while ((count = stream.read(b)) > 0) {
			out.write(b, 0, count);
		}
		out.flush();
		out.close();

		stream.close();
		con.disconnect();

		return tempFile;
	}

	private String generateFilename() {
		return "" + (int) (Math.random() * 100000);
	}

	public File getBanner(int id, String bannerURL) throws IOException {
		File banner = new File(BANNERDIR);

		if(!banner.exists()) {
			banner.mkdir();
		}

		banner = new File(BANNERDIR + File.separatorChar + id); 

		if(!banner.exists()) {
			//download banner
			download(BANNERURL + bannerURL).renameTo(banner);
		}
		return banner;
	}

	public void deleteBanner(int id) {
		File banner = new File(BANNERDIR + File.separatorChar + id);
		banner.delete();
	}
}
