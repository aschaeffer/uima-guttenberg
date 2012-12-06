package de.hda.gutti.analysis;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uimafit.component.JCasAnnotator_ImplBase;

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
public class CommaSectionCounter extends JCasAnnotator_ImplBase {

	/**
	 * The Logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(CommaSectionCounter.class);

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
		String text = jCas.getDocumentText();
		logger.debug(text);

//		ListIterator<PatternConfig> iterator = patternManager.getPatternsForGroup(patternGroup).listIterator(0);
//		Integer patternCount = 0;
//		while (iterator.hasNext() && patternCount < maxPatternCount) {
//			patternCount++;
//			PatternConfig patternConfig = iterator.next();
//			Pattern pattern = patternManager.getCompiledPattern(patternConfig);
//			Type type = jCas.getTypeSystem().getType(patternConfig.getType());
//			if (type == null) {
//				System.out.println("Could not find Annotation Type for: " + patternConfig.getType());
//				continue;
//			}
//			Matcher matcher = pattern.matcher(text);
//			Integer matchesCount = 0;
//			while (matcher.find() && matchesCount < maxMatchesPerPattern) {
//				matchesCount++;
//				logger.debug("Matches count: " + matchesCount + "  type: " + type.getName() + "  start: " + matcher.start() + "  end: " + matcher.end());
//		        AnnotationFS newAnnotation = jCas.getCas().createAnnotation(type, matcher.start(), matcher.end());
//		        jCas.getCas().addFsToIndexes(newAnnotation);
//			}
//		}
	}

}
