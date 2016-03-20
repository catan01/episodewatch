package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.Episode;
import model.Series;
import model.SeriesManager;
import controller.Downloader;
import controller.listener.DragSeriesListener;

public class SeriesPanel extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Series series;
	private ToggleBanner seriesBtn;
	
	public SeriesPanel(Series series, SeriesManager manager) {
		this.series = series;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);
		setOpaque(false);

		ImageIcon banner = null;
		if(series.getBannerURL() != null) {
			try {
				banner = new ImageIcon(Downloader.getDownloader().getBanner(series.getId(), series.getBannerURL()).getAbsolutePath());
			} catch (IOException e1) {
				//download failed - create new banner
				banner = createSeriesBanner(series.getTitle());
			}
		} else {
			//create new banner
			banner = createSeriesBanner(series.getTitle());
		}
		seriesBtn = new ToggleBanner(banner, false);
		add(seriesBtn);

		int seasonNr = 0;
		ArrayList<Episode> season = null;
		for(Episode episode : series.getEpisodes()) {
			if(seasonNr < episode.getSeason()) {
				if(seasonNr++ != 0) {
					add(new SeasonPanel(season.toArray(new Episode[0])));
				}
				season = new ArrayList<Episode>();
			}
			season.add(episode);
		}
		SeasonPanel lastSeason = new SeasonPanel(season.toArray(new Episode[0]));
		add(lastSeason);
		setViewable(lastSeason.isViewable());
		
		seriesBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleSeasonPanels(seriesBtn.isSelected());
			}
			
		});
		DragSeriesListener listener = new DragSeriesListener(manager);
		seriesBtn.addMouseListener(listener);
		
		series.addObserver(this);
	}
	
	private ImageIcon createSeriesBanner(String title) {
		ImageIcon banner = new ImageIcon(new BufferedImage(NewSeriesDialog.BANNER_WIDTH, NewSeriesDialog.BANNER_HEIGHT, BufferedImage.TYPE_INT_RGB));
		banner.getImage().getGraphics().setColor(Color.WHITE);
		banner.getImage().getGraphics().drawString(title, 50, 50);
		return banner;
	}
	
	private void toggleSeasonPanels(boolean visible) {
		for(int i = 1; i < getComponentCount(); i++) {
			getComponent(i).setVisible(visible);
		}
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}
	
	public void setViewable(boolean viewable) {
		seriesBtn.setViewable(viewable);
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
}
