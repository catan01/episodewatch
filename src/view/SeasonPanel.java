package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Episode;

public class SeasonPanel extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Episode[] episodes;
	private Episode lastEpisode;
	
	private JPanel header;
	private JLabel seasonLbl;
	private JCheckBox watched;
	private JLabel latestEpisode;
	
	
	public SeasonPanel(final Episode[] episodes) {
		this.episodes = episodes;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);
		setOpaque(false);

		header = new SeasonHeader();

		if(episodes.length > 0) {
			seasonLbl = new JLabel("Season " + episodes[0].getSeason());
			watched = new JCheckBox("" , allWatched(episodes));
			watched.setOpaque(false);
			latestEpisode = new JLabel("Latest episode: -");
			header.add(Box.createHorizontalStrut(5));
			header.add(seasonLbl);
			header.add(watched);
			header.add(Box.createHorizontalGlue());
			header.add(latestEpisode);
			header.add(Box.createHorizontalGlue());
			
			add(header);
		}
		
		boolean colored = false;
		
		for(Episode episode : episodes) {
			EpisodePanel newPanel = new EpisodePanel(episode);
			
			//set background of non-released episodes to grey
			if(!episodeAvailable(episode.getDate())) {
				if(colored) {
					newPanel.setBackground(new Color(120, 120, 120));
				} else {
					newPanel.setBackground(new Color(180, 180, 180));
				}
			} else {
				//set latest available episode
				lastEpisode = episode;
				//alternate between blue and white background-color
				if(colored) {
					newPanel.setBackground(new Color(211, 223, 238));
				}
			}
			
			add(newPanel);
			colored = !colored;
			
			episode.addObserver(this);
		}
		
		if(lastEpisode != null) {
			//change text of latestEpisode
			int epNr = lastEpisode.getNumber();
			String epDate = lastEpisode.getDate();
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date airDate = format.parse(epDate);
				epDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(airDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			latestEpisode.setText("Latest episode: " + epDate + " - E" + (epNr > 10 ? epNr : ("0" + epNr)) + " - " + lastEpisode.getTitle());
		}
		
		//hide at start
		setVisible(false);
		
		watched.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for(Episode episode : episodes) {
					episode.setWatched(watched.isSelected());
				}
			}
			
		});
		
		header.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				for(int i = 1; i < getComponentCount(); i++) {
					getComponent(i).setVisible(!getComponent(i).isVisible());
				}
			}
			
		});
	}
	
	public Episode[] getEpisodes() {
		return episodes;
	}

	public void setEpisodes(Episode[] episodes) {
		this.episodes = episodes;
	}

	public static boolean episodeAvailable(String date) {
		if(date.equals("")) {
			return false;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(date).before(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean allWatched(Episode[] episodes) {
		for(Episode episode : episodes) {
			if(!episode.isWatched()) {
				return false;
			}
		}
		return true;
	}

	public boolean isViewable() {
		if(lastEpisode != null) {
			return !lastEpisode.isWatched();
		}
		return false;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Episode) {
			if(!((Episode)o).isWatched()) {
				//episode un-watched -> season watched to false
				watched.setSelected(false);
			} else {
				//episode watched -> check for allWatched
				if(((Episode) o).equals(lastEpisode)) {
					((SeriesPanel) getParent()).setViewable(false);
				}
			}
		}
	}

}
