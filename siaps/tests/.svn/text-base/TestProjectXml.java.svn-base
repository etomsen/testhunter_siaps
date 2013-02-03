package it.unibz.testhunter.plugin.hudson.siaps.tests;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import it.unibz.testhunter.plugin.hudson.HudsonDocument;
import it.unibz.testhunter.plugin.hudson.MockUrlResolver;
import it.unibz.testhunter.plugin.hudson.StringHolder;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestProjectXml {

	private HudsonDocument doc;
	private URL url;

	public static File fileFromString(String s) throws IOException {
		File file = File.createTempFile("stdout", "");
		PrintStream out = new PrintStream(file);
		out.println(s);
		return file;		
	}
	
	@Before
	public void setUp() throws Exception {
		doc = new HudsonDocument(new MockUrlResolver());
		url = TestProjectXml.class.getResource("siaps_project.xml");
	}

	@After
	public void tearDown() throws Exception {
		doc = null;
	}

	@Test
	public void simpleFetch() throws Exception {
		try {
			
			doc.fetch(url);
			
		} catch (Exception e) {
			Assert.assertFalse(true);
		}
	}
	
	@Test
	public void fetchWithHandlers() throws Exception {
		final StringHolder nameElement = new StringHolder("");
		final StringHolder urlElement = new StringHolder("");
		final List<Integer> builds = new ArrayList<Integer>();
		
		try {
			doc.addEventHandler("/mavenModuleSet/name", new ElementHandler() {
				
				@Override
				public void onStart(ElementPath arg0) {}
				
				@Override
				public void onEnd(ElementPath arg0) {
					nameElement.setValue(arg0.getCurrent().getText());
				}
			});
			
			doc.addEventHandler("/mavenModuleSet/url", new ElementHandler() {
				
				@Override
				public void onStart(ElementPath arg0) {}
				
				@Override
				public void onEnd(ElementPath arg0) {
					urlElement.setValue(arg0.getCurrent().getText());
				}
			});
			
			doc.addEventHandler("/mavenModuleSet/build", new ElementHandler() {
				
				@Override
				public void onStart(ElementPath arg0) { }
				
				@Override
				public void onEnd(ElementPath arg0) { 
					Element el = arg0.getCurrent();
					String s = el.element("number").getText();
					builds.add(new Integer(s));
					el.detach();
				}
			});
			
			doc.fetch(url);
			Assert.assertEquals(nameElement.getValue(), "SIAPS");
			Assert.assertEquals(urlElement.getValue(), "http://10.7.20.119:8080/job/SIAPS/");
			Assert.assertEquals(builds.size(), 133);
			
		} catch (Exception e) {
			Assert.assertFalse(true);
		}
		
	}
}
