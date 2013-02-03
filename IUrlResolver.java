package it.unibz.testhunter.plugin.hudson;

import it.unibz.testhunter.shared.TException;

import java.net.URL;

public interface IUrlResolver {
	
	public URL resolve(URL url) throws TException;
	
}
