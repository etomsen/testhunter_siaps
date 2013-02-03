package it.unibz.testhunter.plugin.hudson.siaps.tests;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import it.unibz.testhunter.plugin.hudson.IUrlResolver;
import it.unibz.testhunter.plugin.hudson.MockUrlResolver;
import it.unibz.testhunter.plugin.hudson.siaps.BuildImpl;
import it.unibz.testhunter.plugin.hudson.siaps.IBuildFactory;
import it.unibz.testhunter.plugin.hudson.siaps.IJobFactory;
import it.unibz.testhunter.plugin.hudson.siaps.JobImpl;
import it.unibz.testhunter.shared.IBuild;
import it.unibz.testhunter.shared.IJob;
import it.unibz.testhunter.shared.TException;

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

public class TestBuildImpl {

	private static IBuildFactory buildFactory;
	private static URL buildUrl;
	private static URL jobUrl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		jobUrl = TestProjectXml.class.getResource("siaps_job.xml");
		buildUrl = TestProjectXml.class.getResource("siaps_build.xml");
		Injector inj = Guice.createInjector(new Module() {

			@Override
			public void configure(Binder binder) {

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

			}
		});
		buildFactory = inj.getInstance(IBuildFactory.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		jobUrl = null;
		buildUrl = null;
		buildFactory = null;
	}

	@Test
	public void getNumber() throws Exception {
		IBuild b = buildFactory.create(buildUrl, new Long(380), new Timestamp(new Date().getTime()), "SUCCESS");
		Assert.assertEquals(b.getNumber(), new Long(380));
	}
	
	@Test
	public void getJobCount() throws Exception {
		IBuild b = buildFactory.create(buildUrl, new Long(380), new Timestamp(new Date().getTime()), "SUCCESS");
		Assert.assertEquals(b.getJobCount(), 1);
	}
	
	@Test
	public void getResult() throws Exception {
		IBuild b = buildFactory.create(buildUrl, new Long(380), new Timestamp(new Date().getTime()), "SUCCESS");
		Assert.assertEquals(b.getResult(), "SUCCESS");
	}
	
	@Test
	public void getAllJobs() throws Exception {
		IBuild b = buildFactory.create(buildUrl, new Long(380), new Timestamp(new Date().getTime()), "SUCCESS");
		int i = 0;
		for (IJob j: b.getAllJobs()) {
			System.out.println("name: "+ j.getName()+", tests: "+j.getTestCount());
			i++;
		}
		Assert.assertEquals(i, 1);
	}
	
	@Test
	public void getJob() throws Exception {
		IBuild b = buildFactory.create(buildUrl, new Long(380), new Timestamp(new Date().getTime()), "SUCCESS");
		IJob j = b.getJob(0);
		System.out.println("name: "+ j.getName()+", tests: "+j.getTestCount());
	}
	
	
}
