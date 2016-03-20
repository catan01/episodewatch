package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.Timer;

import model.SeriesManager;
import controller.listener.DeleteSeriesListener;
import controller.listener.NewSeriesListener;
import controller.listener.UpdateListener;


public class EWToolBar extends JToolBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton btnNew;
	private JButton btnDelete;
//	private JButton btnConfig;
	private JButton btnUpdate;
	private JLabel clock;
	
	public EWToolBar(SeriesManager manager) {
		
		setFloatable(false);
		
		btnNew = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/add.png")));
		btnNew.setFocusable(false);
		btnNew.setToolTipText("Add a new series");
		btnDelete = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/cross.png")));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Delete a series");
//		btnConfig = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/wrench.png")));
//		btnConfig.setFocusable(false);
//		btnConfig.setToolTipText("Configuration");
		btnUpdate = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/download.png")));
		btnUpdate.setFocusable(false);
		btnUpdate.setToolTipText("Update the episode-list");
		clock = new JLabel(new Date().toString());
		
		btnNew.addActionListener(new NewSeriesListener(manager));
		btnDelete.addActionListener(new DeleteSeriesListener(manager));
		btnUpdate.addActionListener(new UpdateListener(manager));
		
		add(btnNew);
		add(btnDelete);
		addSeparator();
		add(btnUpdate);
//		add(btnConfig);
		add(Box.createHorizontalGlue());
		add(Box.createHorizontalStrut(5));
		addSeparator();
		add(Box.createHorizontalStrut(5));
		add(clock);
		
		Timer timer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clock.setText(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG, Locale.getDefault()).format(new Date()));
			}
			
		});
		timer.setInitialDelay(0);
		timer.start();
	}
}
