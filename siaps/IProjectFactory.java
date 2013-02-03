package it.unibz.testhunter.plugin.hudson.siaps;

import java.net.URL;

import it.unibz.testhunter.shared.IProject;

public interface IProjectFactory {
	
	public IProject create(URL url);
}
