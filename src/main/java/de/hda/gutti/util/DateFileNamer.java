package de.hda.gutti.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.initialize.ConfigurationParameterInitializer;
import org.uimafit.component.xwriter.XWriterFileNamer;
import org.uimafit.factory.initializable.Initializable;

public class DateFileNamer implements XWriterFileNamer, Initializable {

	private final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss-S");

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String nameFile(final JCas jCas) {
		return formater.format(new Date());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void initialize(final UimaContext context) throws ResourceInitializationException {
		ConfigurationParameterInitializer.initialize(this, context);
	}

}
