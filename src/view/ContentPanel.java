package view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import model.Series;
import model.SeriesManager;


public class ContentPanel extends JPanel implements Observer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int BORDER = 10;
	public static final int BANNER_WIDTH = 758;
	public static final int BANNER_HEIGHT = 140;
	
	private SeriesManager manager;
	
	public ContentPanel(SeriesManager manager) {
		this.manager = manager;
		
		setBackground(Color.GRAY);
		setAlignmentX(CENTER_ALIGNMENT);
		setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, 0, 0));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		for(Series s : manager.getSeries()) {
			add(new SeriesPanel(s, manager));
		}

		manager.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		removeAll();
		for(Series s : manager.getSeries()) {
			add(new SeriesPanel(s, manager));
		}
		revalidate();
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(BANNER_WIDTH , super.getPreferredSize().height);
	}
}
