package it.unibz.testhunter.plugin.hudson.siaps;

import java.net.URL;

import it.unibz.testhunter.shared.IJob;

public interface IJobFactory {
	
	public IJob create(URL url);
}
