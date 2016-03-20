package controller.listener;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowseURLListener implements ActionListener {

	private String url;
	
	public BrowseURLListener(String url) {
		this.url = url;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}

}
