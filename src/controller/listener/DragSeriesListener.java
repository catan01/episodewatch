package controller.listener;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.SeriesManager;
import view.SeriesPanel;
import view.ToggleBanner;

public class DragSeriesListener  extends MouseAdapter {

	private static Component recentlyEntered = null;
	private SeriesManager manager;
	
	public DragSeriesListener(SeriesManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(recentlyEntered instanceof ToggleBanner) {
				if(e.getSource() instanceof ToggleBanner) {
					manager.exchangeSeries(((SeriesPanel) recentlyEntered.getParent()).getSeries(), ((SeriesPanel) e.getComponent().getParent()).getSeries());
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		recentlyEntered = e.getComponent();
	}

}
