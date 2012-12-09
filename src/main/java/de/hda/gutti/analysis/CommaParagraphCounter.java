package de.hda.gutti.analysis;

import static org.uimafit.util.JCasUtil.*;

import java.util.ArrayList;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uimafit.component.JCasAnnotator_ImplBase;

import de.hda.gutti.model.CommasPerParagraphCounter;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PUNC;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;

/**
 * This class provides an universal regular pattern annotator.
 * Patterns are stored in configuration files. You can select
 * pattern configurations by a pattern group. Also you can
 * replace matches with an string. Finally there you can
 * specify the annotation type.
 * 
 * @author aschaeffer
 * @since 1.0.beta10
 *
 */
public class CommaParagraphCounter extends JCasAnnotator_ImplBase {

	private final static String COMMA_STRING = ",";
	
	/**
	 * The Logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(CommaParagraphCounter.class);

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
		// String text = jCas.getDocumentText();
		// logger.debug(text);
		// Type type = jCas.getTypeSystem().getType(CommasPerParagraphCounter.class.getName());
		Integer p = 0;
		for (Paragraph paragraph : select(jCas, Paragraph.class)) {
			p++;
			ArrayList<PUNC> punctuations = new ArrayList<PUNC>(selectCovered(PUNC.class, paragraph));
			Integer commaCounter = 0;
			for (int i = 0; i < punctuations.size(); i++) {
				PUNC punctuation = punctuations.get(i);
				if (punctuation.getCoveredText().equals(COMMA_STRING)) {
					commaCounter++;
				}
			}
			CommasPerParagraphCounter commasPerParagraphCounter = new CommasPerParagraphCounter(jCas);
			commasPerParagraphCounter.setCount(commaCounter);
			commasPerParagraphCounter.setBegin(paragraph.getBegin());
			commasPerParagraphCounter.setEnd(paragraph.getEnd());
			jCas.addFsToIndexes(commasPerParagraphCounter);
			logger.info("Paragraph " + p + ": " + commaCounter + " commas found");
			// AnnotationFS commaCounterAnnotation = jCas.getCas().createAnnotation(type, paragraph.getBegin(), paragraph.getEnd());
			// jCas.getCas().addFsToIndexes(commaCounterAnnotation);
		}
	}

}
