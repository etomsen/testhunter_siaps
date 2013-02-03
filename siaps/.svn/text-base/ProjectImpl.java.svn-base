package it.unibz.testhunter.plugin.hudson.siaps;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import it.unibz.testhunter.plugin.hudson.BooleanHolder;
import it.unibz.testhunter.plugin.hudson.HudsonDocument;
import it.unibz.testhunter.plugin.hudson.IUrlResolver;
import it.unibz.testhunter.plugin.hudson.StringHolder;
import it.unibz.testhunter.shared.IBuild;
import it.unibz.testhunter.shared.IProject;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.shared.TExceptionHolder;

public class ProjectImpl implements IProject {

	private static final String sFetchingErrorMsg = "siaps plugn encountered an error fetching the project from url";
	private static final String sInternalPluginErrorMsg = "siaps plugin internal error";

	private URL url;
	private IBuildFactory buildFactory;
	private HudsonDocument doc;

	private final BooleanHolder isFetched = new BooleanHolder(false);
	private final TExceptionHolder onFetchError = new TExceptionHolder(null);
	private final StringHolder fetchedName = new StringHolder("");
	private final StringHolder fetchedUrl = new StringHolder("");
	private final List<IBuild> builds = new ArrayList<IBuild>();

	@Inject
	public ProjectImpl(IBuildFactory buildFactory, @Named("project") IUrlResolver urlResolver, @Assisted URL url) {
		this.url = url;
		this.buildFactory = buildFactory;
		this.doc = new HudsonDocument(urlResolver);
		this.setupHandlers();
	}

	private void lazyFetch() throws TException {
		if (!isFetched.getValue()) {
			this.doc.fetch(url);
			if (onFetchError.getValue() != null) {
				throw onFetchError.getValue();
			} else {
				isFetched.setValue(true);
			}
		}
	}

	private void setupHandlers() {
		doc.addEventHandler("/mavenModuleSet/name", new ElementHandler() {

			@Override
			public void onStart(ElementPath arg0) {
			}

			@Override
			public void onEnd(ElementPath arg0) {
				try {
					fetchedName.setValue(arg0.getCurrent().getText());
				} catch (Exception e) {
					onFetchError.setValue(new TException(e.getMessage())
							.setTerminateApp().setUserMsg(sFetchingErrorMsg));
				}

			}
		});

		doc.addEventHandler("/mavenModuleSet/url", new ElementHandler() {

			@Override
			public void onStart(ElementPath arg0) {
			}

			@Override
			public void onEnd(ElementPath arg0) {
				try {
					fetchedUrl.setValue(arg0.getCurrent().getText());
				} catch (Exception e) {
					onFetchError.setValue(new TException(e.getMessage())
							.setTerminateApp().setUserMsg(sFetchingErrorMsg));
				}
			}
		});

		doc.addEventHandler("/mavenModuleSet/build", new ElementHandler() {

			@Override
			public void onStart(ElementPath arg0) {
			}

			@Override
			public void onEnd(ElementPath arg0) {
				try {
					Element el = arg0.getCurrent();
					String sBuildNumber = el.element("number").getText();
					String sBuildResult = el.element("result").getText();
					String sBuildUrl = el.element("url").getText();
					String sBuildStart = el.element("timestamp").getText();
					Timestamp buildStart = new Timestamp(new Long(sBuildStart));
					builds.add(buildFactory.create(new URL(sBuildUrl),
							new Long(sBuildNumber), buildStart, sBuildResult));
					el.detach();
				} catch (Exception e) {
					onFetchError.setValue(new TException(e.getMessage())
							.setTerminateApp().setUserMsg(sFetchingErrorMsg));
				}

			}
		});
	}

	@Override
	public String getName() throws TException {
		lazyFetch();
		return fetchedName.getValue();
	}

	@Override
	public List<IBuild> getAllBuilds() throws TException {
		lazyFetch();
		return builds;
	}

	@Override
	public IBuild getBuild(int index) throws TException {
		lazyFetch();
		try {
			return builds.get(index);
		} catch (IndexOutOfBoundsException e) {
			throw new TException(e.getMessage()).setTerminateApp().setUserMsg(
					"build list index is out of bounds");
		}
	}

	@Override
	public int getBuildCount() throws TException {
		lazyFetch();
		return builds.size();
	}

	@Override
	public URL getUrl() throws TException {
		lazyFetch();
		try {
			return new URL(fetchedUrl.getValue());
		} catch (MalformedURLException e) {
			throw new TException(e.getMessage()).setTerminateApp().setUserMsg(
					sInternalPluginErrorMsg
							+ ": malformed project url fetched!");
		}

	}

}
