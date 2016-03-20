package view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.xml.bind.JAXBException;
import model.SeriesManager;
import controller.XMLEncoder;

public class EWFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WINDOW_WIDTH = 820;
	public static final int WINDOW_HEIGHT = 600;
	
	private static final int SCROLLSPEED = 10;
	
	private EWToolBar toolbar;
	private ContentPanel content;
	private NewSeriesDialog addDialog;
	
	public EWFrame(final SeriesManager manager) {
		manager.setFrame(this);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					XMLEncoder.getEncoder().saveSeries(manager.getSeries());
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		setTitle("Episode Watch");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		
		setLayout(new BorderLayout());
		
		toolbar = new EWToolBar(manager);
		content = new ContentPanel(manager);
		addDialog = new NewSeriesDialog(manager);

		add(toolbar, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(content);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLLSPEED);
		add(scrollPane, BorderLayout.CENTER);
		
		
		setVisible(true);
	}

	public NewSeriesDialog getAddDialog() {
		return addDialog;
	}

	public void setAddDialog(NewSeriesDialog addDialog) {
		this.addDialog = addDialog;
	}
}
