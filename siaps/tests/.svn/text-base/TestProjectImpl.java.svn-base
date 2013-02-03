package it.unibz.testhunter.plugin.hudson.siaps.tests;

import it.unibz.testhunter.plugin.hudson.IUrlResolver;
import it.unibz.testhunter.plugin.hudson.MockUrlResolver;
import it.unibz.testhunter.plugin.hudson.siaps.BuildImpl;
import it.unibz.testhunter.plugin.hudson.siaps.IBuildFactory;
import it.unibz.testhunter.plugin.hudson.siaps.IJobFactory;
import it.unibz.testhunter.plugin.hudson.siaps.IProjectFactory;
import it.unibz.testhunter.plugin.hudson.siaps.JobImpl;
import it.unibz.testhunter.plugin.hudson.siaps.ProjectImpl;
import it.unibz.testhunter.shared.IBuild;
import it.unibz.testhunter.shared.IProject;
import it.unibz.testhunter.shared.TException;

import java.net.URL;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryProvider;
import com.google.inject.name.Names;

public class TestProjectImpl {

	private static IProjectFactory factory;
	private static URL prjUrl;
	private static URL jobUrl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		jobUrl = TestProjectXml.class.getResource("siaps_job.xml");
		prjUrl = TestProjectXml.class.getResource("siaps_project.xml");

		Injector inj = Guice.createInjector(new Module() {

			@Override
			public void configure(Binder binder) {

				binder.bind(IProjectFactory.class).toProvider(
						FactoryProvider.newFactory(IProjectFactory.class,
								ProjectImpl.class));

				binder.bind(IBuildFactory.class).toProvider(
						FactoryProvider.newFactory(IBuildFactory.class,
								BuildImpl.class));

				binder.bind(IJobFactory.class).toProvider(
						FactoryProvider.newFactory(IJobFactory.class,
								JobImpl.class));

				binder.bind(IUrlResolver.class)
						.annotatedWith(Names.named("job"))
						.to(MockUrlResolver.class);

				binder.bind(IUrlResolver.class)
						.annotatedWith(Names.named("build"))
						.toInstance(new IUrlResolver() {

							@Override
							public URL resolve(URL url) throws TException {
								return jobUrl;
							}
						});

				binder.bind(IUrlResolver.class)
						.annotatedWith(Names.named("project"))
						.to(MockUrlResolver.class);
			}
		});
		factory = inj.getInstance(IProjectFactory.class);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		factory = null;
		prjUrl = null;
		jobUrl = null;
	}

	@Test
	public void getName() throws TException {
		IProject prj = factory.create(prjUrl);
		Assert.assertEquals("SIAPS", prj.getName());
	}

	@Test
	public void getUrl() throws TException {
		IProject prj = factory.create(prjUrl);
		Assert.assertEquals("http://10.7.20.119:8080/job/SIAPS/", prj.getUrl()
				.toString());
	}

	@Test
	public void getBuildCount() throws TException {
		IProject prj = factory.create(prjUrl);
		Assert.assertEquals(133, prj.getBuildCount());
	}

	@Test
	public void getAllBuilds() throws TException {
		IProject prj = factory.create(prjUrl);
		int i = 0;
		for (IBuild b : prj.getAllBuilds()) {
			System.out.println("number: " + b.getNumber() + ", result: "
					+ b.getResult() + ", start: " + b.getStart());
			i++;
		}
	}

}
