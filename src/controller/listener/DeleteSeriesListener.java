package controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.Series;
import model.SeriesManager;

public class DeleteSeriesListener implements ActionListener {

	SeriesManager manager;
	
	public DeleteSeriesListener(SeriesManager manager) {
		this.manager = manager;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Series[] series = manager.getSeries().toArray(new Series[0]);
		
		Series seriesToDelete = (Series) JOptionPane.showInputDialog(manager.getFrame(), "Which series do you want to delete?", "Delete a series", JOptionPane.QUESTION_MESSAGE, new ImageIcon("icons/cross.png"), series, null);
		
		if(seriesToDelete != null) {
			if(JOptionPane.showConfirmDialog(manager.getFrame(), "Are you sure you want to delete the series \"" + seriesToDelete + "\"?", "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
				manager.deleteSeries(seriesToDelete);
			}
		}
	}

}
