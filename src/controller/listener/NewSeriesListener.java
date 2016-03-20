package controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.SeriesManager;


public class NewSeriesListener implements ActionListener {

	SeriesManager manager;
	
	public NewSeriesListener(SeriesManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// open newSeriesBox 
		manager.getFrame().getAddDialog().setVisible(true);
	}

}
