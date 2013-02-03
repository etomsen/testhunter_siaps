package it.unibz.testhunter.plugin.hudson.siaps;

import java.net.MalformedURLException;
import java.net.URL;

import it.unibz.testhunter.plugin.hudson.IUrlResolver;
import it.unibz.testhunter.shared.TException;

public class BuildVirtualUrlResolver implements IUrlResolver {

	@Override
	public URL resolve(URL url) throws TException {
		String virtualJobUrl = url.toString();
		if (virtualJobUrl.endsWith("/")) {
			virtualJobUrl += "testReport";
		} else {
			virtualJobUrl += "/testReport";
		}
		try {
			return new URL(virtualJobUrl);
		} catch (MalformedURLException e) {
			throw new TException(e.getMessage()).setTerminateApp().setUserMsg(
					virtualJobUrl + " is not well-formed!");
		}
	}
}
