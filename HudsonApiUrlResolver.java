package it.unibz.testhunter.plugin.hudson;

import it.unibz.testhunter.shared.TException;

import java.net.MalformedURLException;
import java.net.URL;

public class HudsonApiUrlResolver implements IUrlResolver {

	private final int depth;
	private final String xpath;

	public HudsonApiUrlResolver() {
		depth = 1;
		xpath = "";
	}
	
	public HudsonApiUrlResolver(int depth, String xpath) {
		this.depth = depth;
		this.xpath = xpath;
	}

	@Override
	public URL resolve(URL url) throws TException {
		try {
			// replace space character codes with spaces
			String query = url.toString().replace("%20", " ");

			// Hudson XML API is normally exposed via the $url/api/xml?
			query += "/api/xml?";

			// specify the depth of the retrieval
			query += String.format("depth=%d", depth);

			// specify xpath to be matched on Hudson server
			if (xpath != null && !xpath.isEmpty()) {
				query += String.format("&xpath=%s&wrapper=xpath", xpath);
			}
			return new URL(query);

		} catch (MalformedURLException e) {
			throw new TException(e.getMessage()).setUserMsg(
					"internal plugin error").setTerminateApp();
		}
	}

}
