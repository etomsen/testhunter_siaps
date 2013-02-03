package it.unibz.testhunter.plugin.hudson.siaps;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import it.unibz.testhunter.plugin.hudson.BooleanHolder;
import it.unibz.testhunter.plugin.hudson.HudsonDocument;
import it.unibz.testhunter.plugin.hudson.IUrlResolver;
import it.unibz.testhunter.plugin.hudson.StringHolder;
import it.unibz.testhunter.shared.IJob;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.shared.TExceptionHolder;
import it.unibz.testhunter.shared.TestInstanceModel;

public class JobImpl implements IJob {

	private static final String sFetchingErrorMsg = "siaps plugn encountered an error while fetching from url";

	private final List<TestInstanceModel> tests;
	private final StringHolder name;
	private final URL url;

	private final BooleanHolder isFetched;
	private final TExceptionHolder onFetchError;
	private final HudsonDocument doc;

	@Inject
	public JobImpl(@Named("job") IUrlResolver urlResolver, @Assisted URL url) {
		this.url = url;
		this.name = new StringHolder("");
		this.tests = new ArrayList<TestInstanceModel>();
		this.doc = new HudsonDocument(urlResolver);
		this.isFetched = new BooleanHolder(false);
		this.onFetchError = new TExceptionHolder(null);
		setupHandlers();
	}

	private void setupHandlers() {
		doc.addEventHandler("/surefireAggregatedReport/childReport/child",
				new ElementHandler() {

					@Override
					public void onStart(ElementPath arg0) {
					}

					@Override
					public void onEnd(ElementPath arg0) {
						try {
							String sBuildNumber = arg0.getCurrent()
									.element("number").getText();
							String sJobUrl = arg0.getCurrent().element("url")
									.getText();
							name.setValue(getJobNameFromUrl(sJobUrl,
									sBuildNumber));
							arg0.getCurrent().detach();

						} catch (Exception e) {
							onFetchError.setValue(new TException(e.getMessage())
									.setTerminateApp().setUserMsg(
											sFetchingErrorMsg));
						}
					}
				});

		doc.addEventHandler(
				"/surefireAggregatedReport/childReport/result/suite/case",
				new ElementHandler() {

					@Override
					public void onStart(ElementPath arg0) {
					}

					@Override
					public void onEnd(ElementPath arg0) {
						try {
							String sName = arg0.getCurrent().element("name")
									.getText();
							String sClass = arg0.getCurrent()
									.element("className").getText();
							String sDuration = arg0.getCurrent()
									.element("duration").getText();
							String sStatus = arg0.getCurrent()
									.element("status").getText();

							tests.add(new TestInstanceModel(sName,
									getClassFromFullClass(sClass),
									getPackageFromFullClass(sClass), sStatus,
									(int) (Float.parseFloat(sDuration) * 1000)));
							arg0.getCurrent().detach();

						} catch (Exception e) {
							onFetchError.setValue(new TException(e.getMessage())
									.setTerminateApp().setUserMsg(
											sFetchingErrorMsg));
						}
					}
				});
	}

	private static String getJobNameFromUrl(String jobUrl, String buildNumber) {
		int i = buildNumber.trim().length();
		// remove build number part

		String res = jobUrl.substring(0, jobUrl.length() - 2 - i);
		res = res.substring(res.lastIndexOf("/") + 1, res.length());
		return res;
	}

	private static String getPackageFromFullClass(String fullClassName) {
		return fullClassName.substring(0, fullClassName.lastIndexOf("."));
	}

	private static String getClassFromFullClass(String fullClassName) {
		return fullClassName.substring(fullClassName.lastIndexOf(".") + 1,
				fullClassName.length());
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

	@Override
	public Collection<TestInstanceModel> getAllTest() throws TException {
		lazyFetch();
		return tests;
	}

	@Override
	public String getName() throws TException {
		lazyFetch();
		return name.getValue();
	}

	@Override
	public TestInstanceModel getTest(int index) throws TException {
		lazyFetch();
		try {
			return tests.get(index);
		} catch (Exception e) {
			throw new TException(e.getMessage()).setTerminateApp().setUserMsg(
					"test list index is out of bounds");
		}
	}

	@Override
	public int getTestCount() throws TException {
		lazyFetch();
		return tests.size();
	}

}
