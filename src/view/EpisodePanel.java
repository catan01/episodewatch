package view;

import java.awt.Color;
import java.awt.Dimension;
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

public class EpisodePanel extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Episode episode;
	
	private JLabel episodeLbl;
	private JCheckBox watched;
	private JLabel dateLbl;
	
	public EpisodePanel(final Episode episode) {
		this.episode = episode;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);
		setBackground(Color.WHITE);
		
		watched = new JCheckBox("", episode.isWatched());
		watched.setOpaque(false);
		episodeLbl = new JLabel(episodeText(episode));
		dateLbl = new JLabel(formatDate(episode.getDate()));
		
		add(Box.createHorizontalStrut(5));
		add(episodeLbl);
		add(watched);
		add(Box.createHorizontalGlue());
		add(dateLbl);
		add(Box.createHorizontalStrut(5));
		
		//hide at start
		setVisible(false);
		
		watched.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				episode.setWatched(!episode.isWatched());
			}
		});
		
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					episode.setWatched(!episode.isWatched());
				}
			}
		});
		
		episode.addObserver(this);
	}
	
	private String formatDate(String date) {
		if(date.equals("")) {
			return date;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date airDate = format.parse(date);
			String result = "";
			if(!SeasonPanel.episodeAvailable(date)) {
				long diff = airDate.getTime() - new Date().getTime();
				int days = (int) (diff / (1000l * 3600l * 24l) + 1);
				result += days + " days remaining - ";
			}
			result += DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(airDate);
			return result;
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}
	
	private String episodeText(Episode episode) {
		String number = "";
		if(episode.getNumber() < 10) {
			number = "0" + episode.getNumber();
		} else {
			number += episode.getNumber();
		}
		return "E" + number + " - " + episode.getTitle();
	}
	
	public Episode getEpisode() {
		return episode;
	}

	public void setEpisode(Episode episode) {
		this.episode = episode;
	}

	@Override
	public void update(Observable o, Object arg) {
		episodeLbl.setText(episodeText(episode));
		watched.setSelected(episode.isWatched());
		dateLbl.setText(formatDate(episode.getDate()));
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(ContentPanel.BANNER_WIDTH, super.getPreferredSize().height);
	}
}
