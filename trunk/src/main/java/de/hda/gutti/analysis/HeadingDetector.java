package de.hda.gutti.analysis;

import static org.uimafit.util.JCasUtil.*;

import java.util.ArrayList;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uimafit.component.JCasAnnotator_ImplBase;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Heading;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

public class HeadingDetector extends JCasAnnotator_ImplBase {

	/**
	 * The Logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(HeadingDetector.class);

	/**
	 * Initialization of this analysis engine.
	 * 
	 * @param aContext The UimaContext.
	 * @throws ResourceInitializationException If analysis engine cannot be initialized.
	 */
	@Override
	public final void initialize(final UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		/**
		 * TODO:
		 */
	}

	/**
	 * Processing method of this analysis engine.
	 * 
	 * @param jCas A jCas structure.
	 * @throws AnalysisEngineProcessException On processing error.
	 */
	@Override
	public final void process(final JCas jCas) throws AnalysisEngineProcessException {
		for (Paragraph paragraph : select(jCas, Paragraph.class)) {
			ArrayList<Sentence> sentences = new ArrayList<Sentence>(selectCovered(Sentence.class, paragraph));
			if (sentences.size() == 0) {
				Heading heading = new Heading(jCas);
				heading.setBegin(paragraph.getBegin());
				heading.setEnd(paragraph.getEnd());
				jCas.addFsToIndexes(heading);
				logger.debug("found heading \"" + paragraph.getCoveredText() + "\"");
			}
		}
	}

}
