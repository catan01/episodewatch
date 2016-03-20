package controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.UpdateSeriesDialog;
import model.SeriesManager;

public class UpdateListener  implements ActionListener {

	private SeriesManager manager;
	
	public UpdateListener(SeriesManager manager) {
		this.manager = manager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new UpdateSeriesDialog(manager);
	}
	
	

}
