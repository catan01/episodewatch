package controller.listener;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Series;
import model.SeriesManager;
import view.SeriesPanel;
import view.ToggleBanner;

public class DragSeriesListener extends MouseAdapter {

	private static Component recentlyEntered = null;
	private SeriesManager manager;

	public DragSeriesListener(SeriesManager manager) {
		this.manager = manager;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(recentlyEntered instanceof ToggleBanner) {
			if(e.getSource() instanceof ToggleBanner) {
				Series to = ((SeriesPanel) recentlyEntered.getParent()).getSeries();
				Series from = ((SeriesPanel) e.getComponent().getParent()).getSeries();
				if(e.getButton() == MouseEvent.BUTTON1) {
					manager.shiftSeries(from, to);
				} else if(e.getButton() == MouseEvent.BUTTON3) {
					manager.exchangeSeries(from, to);
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		recentlyEntered = e.getComponent();
	}

}
