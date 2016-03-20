package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.UIManager;
import javax.xml.bind.JAXBException;

import model.SeriesManager;
import view.EWFrame;

public class EWCtrl {

	public static void main(String[] args) {
		// look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// load properties
		Properties prop = new Properties();
		InputStream in = EWCtrl.class.getResourceAsStream("/config.properties");
		try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// api key
		Downloader.getDownloader().setApiKey(prop.getProperty("apiKey"));
		
		// series container
		SeriesManager manager = new SeriesManager();
		try {
			manager.setSeries(XMLEncoder.getEncoder().loadSeries());
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}

		new EWFrame(manager);

	}

}
