package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SeasonHeader extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeasonHeader() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(ContentPanel.BANNER_WIDTH, super.getPreferredSize().height);
	}
}
