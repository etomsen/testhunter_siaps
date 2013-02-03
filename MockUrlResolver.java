package it.unibz.testhunter.plugin.hudson;

import it.unibz.testhunter.shared.TException;

import java.net.MalformedURLException;
import java.net.URL;

public class MockUrlResolver implements IUrlResolver {

	@Override
	public URL resolve(URL projectUrl) throws TException {
		try {
			return new URL(projectUrl.toString());
		} catch (MalformedURLException e) {
			throw new TException(e.getMessage());
		}
	}

}
