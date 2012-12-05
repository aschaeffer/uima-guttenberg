package de.hda.gutti.util;

import java.util.UUID;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.initialize.ConfigurationParameterInitializer;
import org.uimafit.component.xwriter.XWriterFileNamer;
import org.uimafit.factory.initializable.Initializable;

/**
 * Creates filenames for XWriter based on uuids.
 * Falls back to an integer if there is no uuid.
 * 
 * @author aschaeffer
 * @since 1.0.beta10
 *
 */
public class UuidFileNamer implements XWriterFileNamer, Initializable {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String nameFile(final JCas jCas) {
		return UUID.randomUUID().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void initialize(final UimaContext context) throws ResourceInitializationException {
		ConfigurationParameterInitializer.initialize(this, context);
	}

}
