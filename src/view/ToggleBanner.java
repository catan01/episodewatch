package view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public class ToggleBanner extends JToggleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon viewableIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/views.png"));
	private ImageIcon icon;
	private boolean viewable;
	
	public ToggleBanner(ImageIcon icon, boolean selected) {
		super(icon, selected);
		this.icon = icon;
		this.viewable = false;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(icon.getImage(), 0, 0, null);
		g2.setStroke(new BasicStroke(3));
		if(isSelected()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
		}
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight() / 2);
		if(viewable) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
			g2.drawImage(viewableIcon.getImage(), (icon.getIconWidth() - 64),(icon.getIconHeight() - 64), null);
		}
	}
	
	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	
	public void setViewable(boolean viewable) {
		this.viewable = viewable;
		revalidate();
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(icon.getIconWidth(), icon.getIconHeight());
	}
}
