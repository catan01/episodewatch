package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import model.Series;
import model.SeriesManager;
import controller.Downloader;

public class NewSeriesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int WINDOW_WIDTH = 500;
	private static int WINDOW_HEIGHT = 500;
	private static int SCROLLSPEED = 10;
	public static int BANNER_HEIGHT = 70;
	public static int BANNER_WIDTH = 379;

	private SeriesManager manager;

	private JPanel searchPnl;
	private JLabel searchLbl;
	private JTextField searchTxt;
	private JButton searchBtn;
	private JTable resultTbl;
	private JPanel buttonPnl;
	private JProgressBar loadBar;
	private JButton addBtn;
	private JButton cancelBtn;

	private ImageLoader imgLoader;


	public NewSeriesDialog(SeriesManager manager) {
		super(manager.getFrame());
		this.manager = manager;

		//center
		setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x - (WINDOW_WIDTH / 2), GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y - (WINDOW_HEIGHT / 2));

		setModal(true);
		setResizable(false);
		setTitle("Add a series");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

		searchPnl = new JPanel();
		searchPnl.setLayout(new BoxLayout(searchPnl, BoxLayout.X_AXIS));
		searchLbl = new JLabel("Name: ");
		searchTxt = new JTextField();
		searchBtn = new JButton("Search");
		resultTbl = new JTable(new SeriesModel());

		buttonPnl = new JPanel();
		buttonPnl.setLayout(new BoxLayout(buttonPnl, BoxLayout.X_AXIS));
		loadBar = new JProgressBar();
		addBtn = new JButton("Add");
		cancelBtn = new JButton("Cancel");

		resultTbl.setTableHeader(null);
		resultTbl.setBorder(BorderFactory.createEmptyBorder());
		resultTbl.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		//render the cell depending on the object it receives
		DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void setValue(Object value) {
				if (value instanceof Icon) {
					//banner
					setHorizontalAlignment(SwingConstants.CENTER);
					setIcon((Icon) value);
					setText("");
				} else {
					//seriesTitle
					setHorizontalAlignment(SwingConstants.LEFT);
					setIcon(null);
					setToolTipText((String)value);
					super.setValue(value);
				}
			}

			//remove focus border
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				return super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
			}
		};
		resultTbl.setDefaultRenderer(Object.class, r);
		resultTbl.setRowHeight(BANNER_HEIGHT);
		resultTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		searchPnl.add(searchLbl);
		searchPnl.add(searchTxt);
		searchPnl.add(searchBtn);
		add(searchPnl, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(resultTbl);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLLSPEED);
		add(scrollPane, BorderLayout.CENTER);
		buttonPnl.add(loadBar);
		buttonPnl.add(Box.createHorizontalGlue());
		buttonPnl.add(addBtn);
		buttonPnl.add(cancelBtn);
		add(buttonPnl, BorderLayout.SOUTH);

		searchTxt.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					search();
				}
			}

		});

		searchBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				search();
			}

		});

		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
				add();
			}
		});

		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}

		});
	}

	private void close() {
		setVisible(false);
		if(imgLoader != null) {
			imgLoader.cancel(true);
		}
	}

	private void search() {
		if(imgLoader != null) {
			imgLoader.cancel(true);
		}
		searchSeries(searchTxt.getText());
		searchTxt.setText("");
	}

	private void error(Throwable e) {
		JOptionPane.showMessageDialog(this, "Something's wrong:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void add() {
		int id = ((SeriesModel)resultTbl.getModel()).getIdAt(resultTbl.getSelectedRow());
		try {
			manager.addSeries(Downloader.getDownloader().getSeries(id));
		} catch (IOException | XMLStreamException | FactoryConfigurationError e) {
			error(e);
		}
	}

	private void searchSeries(String name) {
		Series[] newSeries;
		loadBar.setValue(0);
		try {
			newSeries = Downloader.getDownloader().searchSeries(name);
			loadBar.setMaximum(newSeries.length + 1);
			resultTbl.setModel(new SeriesModel(newSeries));
			resultTbl.getColumnModel().getColumn(0).setPreferredWidth(WINDOW_WIDTH - BANNER_WIDTH);
			resultTbl.getColumnModel().getColumn(1).setPreferredWidth(BANNER_WIDTH);
			imgLoader = new ImageLoader(newSeries, resultTbl.getModel(), loadBar);
			imgLoader.execute();
		} catch (IOException | XMLStreamException | FactoryConfigurationError e) {
			error(e);
		}
	}
}

class ImageLoader extends SwingWorker<Void, Object> {

	private Series[] series;
	private TableModel tableModel;
	private JProgressBar loadBar;
	private int index;

	ImageLoader(Series[] series, TableModel tableModel, JProgressBar loadBar) {
		this.series = series;
		this.tableModel = tableModel;
		this.loadBar = loadBar;
		this.index = 0;
	}

	@Override
	protected Void doInBackground() throws Exception {
		for(Series s : series) {
			if(this.isCancelled()) {
				return null;
			}
			Object icon;
			if(s.getBannerURL() != null) {
				icon = new ImageIcon(new URL(s.getBannerURL()));
				((ImageIcon)icon).setImage(((ImageIcon)icon).getImage().getScaledInstance(NewSeriesDialog.BANNER_WIDTH, NewSeriesDialog.BANNER_HEIGHT, 0));
			} else {
				icon = "No image found";
			}
			publish(icon);
			loadBar.setValue(loadBar.getValue() + 1);
		}
		loadBar.setValue(loadBar.getMaximum());
		return null;
	}

	@Override
	protected void process(final List<Object> chunks) {
		for (Object icon : chunks) {
			tableModel.setValueAt(icon, index++, 1);
		}
	}
}

class SeriesModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object[][] data;
	int[] ids;

	SeriesModel() {
		data = null;
	}

	SeriesModel(Series[] series) {
		data = new Object[series.length][2];
		ids = new int[series.length];
		for(int i = 0; i < series.length; i++) {
			data[i][0] = series[i].getTitle();
			data[i][1] = "Loading banner...";
			ids[i] = series[i].getId();
		}
	}

	@Override
	public int getRowCount() {
		if(data != null) {
			return data.length;
		}
		return 0;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	public int getIdAt(int rowIndex) {
		return ids[rowIndex];
	}

	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}