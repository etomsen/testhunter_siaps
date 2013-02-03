package it.unibz.testhunter.plugin.hudson;

import it.unibz.testhunter.shared.TException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.inject.Inject;

public class HudsonDocument {

	private final SAXReader reader;
	private IUrlResolver urlResolver;

	@Inject
	public HudsonDocument(IUrlResolver urlResolver) {
		this.urlResolver = urlResolver;
		this.reader = new SAXReader();
		
		reader.setDefaultHandler(new ElementHandler() {

			@Override
			public void onStart(ElementPath arg0) {
				// do nothing
			}

			@Override
			public void onEnd(ElementPath arg0) {
				// detach all the elements that were not
				// processed by other handlers
				arg0.getCurrent().detach();

			}
		});
	}

	private static void setXsdValidation(SAXReader r, URL xsdSchema)
			throws TException {
		try {
			r.setValidation(true);
			r.setFeature("http://apache.org/xml/features/validation/schema",
					true);
			r.setProperty(
					"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			r.setProperty(
					"http://java.sun.com/xml/jaxp/properties/schemaSource",
					new File(xsdSchema.getFile()));
		} catch (SAXException e) {
			throw new TException(e.getMessage()).setUserMsg(
					"internal plugin error").setTerminateApp();
		}
	}

	public void setOnFetchValidate(URL xsdSchema) throws TException {
		setXsdValidation(reader, xsdSchema);
	}

	public static boolean ValidateWithXsd(Document doc, URL xsdSchema) throws TException {
		final BooleanHolder result = new BooleanHolder(true);
		SAXReader docReader = new SAXReader(true);
		setXsdValidation(docReader, xsdSchema);
		docReader.setErrorHandler(new ErrorHandler() {

			@Override
			public void warning(SAXParseException exception)
					throws SAXException {
				result.setValue(false);
			}

			@Override
			public void fatalError(SAXParseException exception)
					throws SAXException {
				result.setValue(false);
			}

			@Override
			public void error(SAXParseException exception) throws SAXException {
				result.setValue(false);
			}
		});
		InputStream is = new ByteArrayInputStream(doc.asXML().getBytes());
		try {
			docReader.read(is);
		} catch (DocumentException e) {
			throw new TException(e.getMessage()).setUserMsg(
					"internal plugin error").setTerminateApp();
		} finally {
			try {
				is.close();
				is = null;

			} catch (IOException e) {
				throw new TException(e.getMessage()).setUserMsg(
						"internal plugin error").setTerminateApp();
			}
		}
		return result.getValue();
	}
	
	public void addEventHandler(String path, ElementHandler handler) {
		reader.addHandler(path, handler);
	}

	public void fetch(URL url) throws TException {
		URL apiUrl = urlResolver.resolve(url);
		try {
			reader.read(apiUrl);
		} catch (DocumentException e) {
			throw new TException(e.getMessage()).setUserMsg(
					"internal plugin error").setTerminateApp();
		}
	}

	public static void print(Document doc, OutputStream out) throws TException {
		if (doc != null) {
			try {
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setIndent("\t");
				format.setLineSeparator("\r\n");
				format.setSuppressDeclaration(true);
				format.setExpandEmptyElements(false);
				XMLWriter writer = new XMLWriter(out, format);
				writer.write(doc);
			} catch (UnsupportedEncodingException e) {
				throw new TException(e.getMessage()).setUserMsg(
						"internal plugin error").setTerminateApp();
			} catch (IOException e) {
				throw new TException(e.getMessage()).setUserMsg(
						"internal plugin error").setTerminateApp();
			}
		} else {
			try {
				out.write("document is empty".getBytes());
			} catch (IOException e) {
				throw new TException(e.getMessage()).setUserMsg(
						"internal plugin error").setTerminateApp();
			}
		}
	}
}
