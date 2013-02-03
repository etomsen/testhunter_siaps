package it.unibz.testhunter.plugin.hudson.siaps.tests;


import it.unibz.testhunter.plugin.hudson.IUrlResolver;
import it.unibz.testhunter.plugin.hudson.MockUrlResolver;
import it.unibz.testhunter.plugin.hudson.siaps.IJobFactory;
import it.unibz.testhunter.plugin.hudson.siaps.JobImpl;
import it.unibz.testhunter.shared.IJob;
import it.unibz.testhunter.shared.TException;
import it.unibz.testhunter.shared.TestInstanceModel;

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

public class TestJobImpl {

	private static IJobFactory jobFactory;
	private static URL url;
	
	@BeforeClass
	public static void setUp() throws Exception {
		url = TestProjectXml.class.getResource("siaps_job.xml");
		Injector inj = Guice.createInjector(new Module() {
			
			@Override
			public void configure(Binder binder) {
				
				binder.bind(IJobFactory.class).toProvider(
						FactoryProvider.newFactory(IJobFactory.class, JobImpl.class));
				
				binder.bind(IUrlResolver.class).annotatedWith(Names.named("job"))
				.to(MockUrlResolver.class);
				
			}
		});
		jobFactory = inj.getInstance(IJobFactory.class);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		url = null;
		jobFactory = null;
	}
	
	@Test
	public void getName() throws Exception {
		IJob job = jobFactory.create(url);
		Assert.assertEquals(job.getName(), "it.unibz$unibz");
	}
	
	@Test
	public void getTestCount() throws TException {
		IJob job = jobFactory.create(url);
		Assert.assertEquals(job.getTestCount(), 138);
	}
	
	@Test
	public void getTest() throws Exception {
		IJob job = jobFactory.create(url);
		TestInstanceModel t = job.getTest(137);
		
		Assert.assertEquals("testCreateWithoutPermission", t.getName());
		Assert.assertEquals("UserManagerImplTest", t.getClassName());
		Assert.assertEquals("it.unibz.siaps", t.getPackageName());
		Assert.assertEquals("PASSED", t.getStatus());
		Assert.assertEquals(1, t.getDuration());
	}
	
	@Test
	public void getAllTest() throws Exception {
		IJob job = jobFactory.create(url);
		int i = 0;
		for (TestInstanceModel t : job.getAllTest()) {
			System.out.println(t.toString());
			i++;
		}
		Assert.assertEquals(i, 138);
		System.out.print("tests fetched: " + i);
	}

}
