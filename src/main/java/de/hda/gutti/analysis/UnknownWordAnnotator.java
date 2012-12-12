package de.hda.gutti.analysis;

import static org.uimafit.util.JCasUtil.*;

import java.util.ArrayList;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uimafit.component.JCasAnnotator_ImplBase;

import de.hda.gutti.model.GermanWord;
import de.hda.gutti.model.UnknownWord;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PUNC;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * Basicly it checks if a token is either a german word or punctuation. If not,
 * it's a unknown word.
 *  
 * @author aschaeffer
 *
 */
public class UnknownWordAnnotator extends JCasAnnotator_ImplBase {

	/**
	 * The Logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(UnknownWordAnnotator.class);

	/**
	 * Initialization of this analysis engine.
	 * 
	 * @param aContext The UimaContext.
	 * @throws ResourceInitializationException If analysis engine cannot be initialized.
	 */
	@Override
	public final void initialize(final UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
	}

	/**
	 * Processing method of this analysis engine.
	 * 
	 * @param jCas A jCas structure.
	 * @throws AnalysisEngineProcessException On processing error.
	 */
	@Override
	public final void process(final JCas jCas) throws AnalysisEngineProcessException {
		for (Token token : select(jCas, Token.class)) {
			ArrayList<Annotation> coveredAnnotations = new ArrayList<Annotation>(selectCovered(GermanWord.class, token));
			coveredAnnotations.addAll(selectCovered(PUNC.class, token));
			if (coveredAnnotations.size() == 0) {
				UnknownWord unknownWord = new UnknownWord(jCas);
				unknownWord.setBegin(token.getBegin());
				unknownWord.setEnd(token.getEnd());
				jCas.addFsToIndexes(unknownWord);
				logger.debug("found an unknown word \"" + token.getCoveredText() + "\"");
			}
		}
	}

}
