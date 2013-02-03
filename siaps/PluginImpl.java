package it.unibz.testhunter.plugin.hudson.siaps;

import java.net.URL;
import java.util.UUID;

import it.unibz.testhunter.shared.IPlugin;
import it.unibz.testhunter.shared.IProject;
import it.unibz.testhunter.shared.TException;

import com.google.inject.Inject;

public class PluginImpl implements IPlugin {

	private final IProjectFactory _projectFactory;

	@Inject
	public PluginImpl(IProjectFactory projectFactory) {
		_projectFactory = projectFactory;
	}

	@Override
	public String getName() {
		return "siaps hudson plugin";
	}

	@Override
	public IProject getProject(URL url) {
		return _projectFactory.create(url);
	}

	@Override
	public UUID getUUID() throws TException {
		try {
			return UUID.fromString("FB649430-B9F9-4D52-8499-F6FB797B41BE");
		} catch (IllegalArgumentException e) {
			throw new TException(e.getMessage()).setTerminateApp().setUserMsg(
					"siaps plugin internal error: malformed plugin UUID");
		}

	}

}
