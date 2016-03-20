package view;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;
import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import model.Episode;
import model.Series;
import model.SeriesManager;
import controller.Downloader;
import controller.XMLEncoder;

public class UpdateSeriesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SeriesManager manager;
	
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	
	private JPanel progressPnl;
	private JProgressBar progressBar;
	private JScrollPane progressScrollArea;
	private JTextPane progressArea;
	private String progressText;
	private JPanel buttonPnl;
	private JButton btnOK;
	
	public UpdateSeriesDialog(SeriesManager manager) {
		super(manager.getFrame());
		
		this.manager = manager;
		
		//center
		setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x - (WIDTH / 2), GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y - (HEIGHT / 2));
		
		setModal(true);
		setLayout(new BorderLayout());
		setSize(WIDTH, HEIGHT);
		setTitle("Update episodes");
		setResizable(false);
		
		progressPnl = new JPanel();
		progressPnl.setLayout(new BoxLayout(progressPnl, BoxLayout.Y_AXIS));
		progressPnl.setBorder(BorderFactory.createTitledBorder("Updating..."));
		progressBar = new JProgressBar(0, manager.getSeries().size());
		progressArea = new JTextPane();
		((DefaultCaret) progressArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		progressArea.setEditable(false);
		progressArea.setContentType("text/html");
		progressScrollArea = new JScrollPane(progressArea);
		progressText = "<font face='Arial,Verdana'>";
		buttonPnl = new JPanel();
		buttonPnl.setLayout(new BoxLayout(buttonPnl, BoxLayout.X_AXIS));
		btnOK = new JButton("OK");
		btnOK.setEnabled(false);
		
		progressPnl.add(Box.createVerticalStrut(10));
		progressPnl.add(progressBar);
		progressPnl.add(Box.createVerticalStrut(10));
		progressPnl.add(progressScrollArea);
		progressPnl.add(Box.createVerticalStrut(10));
		
		buttonPnl.add(Box.createHorizontalGlue());
		buttonPnl.add(btnOK);
		buttonPnl.add(Box.createHorizontalStrut(5));
		
		add(progressPnl, BorderLayout.CENTER);
		add(buttonPnl, BorderLayout.SOUTH);
		
		
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		
		Thread updateThread = new Thread(new Runnable() {

			@Override
			public void run() {
				update();
			}
		});
		updateThread.start();
		
		setVisible(true);
	}
	
	public void update() {
		for(Series series : manager.getSeries()) {
			if(series.isContinuing()) {
				progressText += "Updating " + series.getTitle() + "...<br/>";
				progressArea.setText(progressText);
				try {
					//download new series-data
					Series newSeries = Downloader.getDownloader().getSeries(series.getId());
					//copy watched flags
					copyWatchedFlags(series, newSeries);
					//count new episodes
					int addedEpisodes = newSeries.getEpisodes().size() - series.getEpisodes().size();
					//update the series object
					manager.updateSeries(newSeries);
					
					progressText += "<b>" + series.getTitle() + "</b>" + " <font color=#33CC33>successfully</font> updated.<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Added <b>" + addedEpisodes + " new</b> episodes.<br/>";
					progressArea.setText(progressText);
				} catch (IOException | XMLStreamException
						| FactoryConfigurationError e) {
					progressText += "<font color=#990000>Failed updating " + series.getTitle() + ". (" + e.getMessage() + ")</font><br/>";
					progressArea.setText(progressText);
				}
			}
			progressBar.setValue(progressBar.getValue() + 1);
		}

		manager.notifyObservers();		
		
		try {
			XMLEncoder.getEncoder().saveSeries(manager.getSeries());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		btnOK.setEnabled(true);
		
	}
	
	private void copyWatchedFlags(Series oldSeries, Series newSeries) {
		ArrayList<Episode> newEpisodes = newSeries.getEpisodes();
		for (Episode oldEpisode : oldSeries.getEpisodes()) {
			int newIndex = newEpisodes.indexOf(oldEpisode);
			if(!(newIndex < 0)) {
				newEpisodes.get(newEpisodes.indexOf(oldEpisode)).setWatched(oldEpisode.isWatched());
			}
		}
//		Iterator<Episode> newEpisodes = newSeries.getEpisodes().iterator();
//		Iterator<Episode> oldEpisodes = oldSeries.getEpisodes().iterator();
//		
//		while(oldEpisodes.hasNext()) {
//			Episode oldEpisode = oldEpisodes.next();
//			Episode newEpisode = newEpisodes.next();
//			if(oldEpisode.getId() == newEpisode.getId()) {
//				//same episode
//				newEpisode.setWatched(oldEpisode.isWatched());
//			} else {
//				//not the same episode
//				//search complete episodes
//				for(Episode e : newSeries.getEpisodes()) {
//					if(e.getId() == oldEpisode.getId()) {
//						//same episode
//						e.setWatched(oldEpisode.isWatched());
//					}
//				}
//			}
//		}
	}
	
}
