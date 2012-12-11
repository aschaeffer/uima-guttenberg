package de.hda.gutti;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.uimafit.component.xwriter.XWriter;
import org.xml.sax.SAXException;

import de.hda.gutti.analysis.CommaParagraphCounter;
import de.hda.gutti.analysis.DocumentWordExporter;
import de.hda.gutti.domains.AnnotatorConfig;
import de.hda.gutti.services.PDFCollectionReader;
import de.hda.gutti.services.PlainTextCollectionReader;
import de.hda.gutti.util.DateFileNamer;
import de.tudarmstadt.ukp.dkpro.core.ngrams.NGramAnnotator;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.tokit.ParagraphSplitter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context/uima-context.xml" })
public class PipelineTest implements ApplicationContextAware {

	/**
	 * Test objective.
	 */
	private final Pipeline pipe = new Pipeline();

	/**
	 * The ApplicationContext.
	 */
	private ApplicationContext applicationContext;

	@Autowired
	private PlainTextCollectionReader plainTextCollectionReader;
	
	@Autowired
	private PDFCollectionReader pdfCollectionReader;

	private final Logger logger = LoggerFactory.getLogger(PipelineTest.class);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Builds the pipeline.
	 * 
	 * @throws UIMAException
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public final void defaultPipeline() throws UIMAException, IOException, SAXException {
		try {

			// enable uimafit spring features, so we can inject
			// spring beans into analysis engines!
			pipe.enableSpringFeatures(applicationContext);
			
			// create type system
			pipe.createTypeSystemDescription("desc.TypeSystem", "PipelineTest", "Generated by PipelineTest", "h-da", "1.0");

			// append the Stanford Segmenter
			pipe.create(StanfordSegmenter.class);

			// append the Paragraph Splitter
			pipe.create(ParagraphSplitter.class);

			// NGramAnnotator (dkpro) for ngrams with up to
			// 5 words within a sentence
			AnnotatorConfig nGramAnnotatorConfig = new AnnotatorConfig();
			nGramAnnotatorConfig.put(NGramAnnotator.PARAM_N, 5);
			pipe.create(NGramAnnotator.class, nGramAnnotatorConfig);

			// Stanford Parser (German)
			AnnotatorConfig stanfordParserAnnotatorConfig = new AnnotatorConfig();
			stanfordParserAnnotatorConfig.put(StanfordParser.PARAM_VARIANT, "pcfg"); // pcfg or factored
			stanfordParserAnnotatorConfig.put(StanfordParser.PARAM_CREATE_PENN_TREE_STRING, true);
			pipe.create(StanfordParser.class, stanfordParserAnnotatorConfig);

			// Stanford POS Tagger
			AnnotatorConfig stanfordPosTaggerAnnotatorConfig = new AnnotatorConfig();
			stanfordPosTaggerAnnotatorConfig.put(StanfordPosTagger.PARAM_VARIANT, "dewac"); // fast or dewac or hgc
			// stanfordPosTaggerAnnotatorConfig.put(StanfordPosTagger.PARAM_VARIANT, "fast");
			// stanfordPosTaggerAnnotatorConfig.put(StanfordPosTagger.PARAM_VARIANT, "hgc");
			pipe.create(StanfordPosTagger.class, stanfordPosTaggerAnnotatorConfig);

			// Stanford Named Entity Recognizer
			AnnotatorConfig stanfordNamedEntityRecognizerConfig = new AnnotatorConfig();
			stanfordNamedEntityRecognizerConfig.put(StanfordNamedEntityRecognizer.PARAM_VARIANT, "dewac_175m_600.crf"); // dewac_175m_600.cfg or hgc_175m_600.cfg
			pipe.create(StanfordNamedEntityRecognizer.class, stanfordNamedEntityRecognizerConfig);

			// Comma Paragraph Counter
			pipe.create(CommaParagraphCounter.class);

			// XMI Writer (writes document to a xmi file)
			AnnotatorConfig xWriterAnnotatorConfig = new AnnotatorConfig();
			xWriterAnnotatorConfig.put(XWriter.PARAM_OUTPUT_DIRECTORY_NAME, "target/");
			xWriterAnnotatorConfig.put(XWriter.PARAM_XML_SCHEME_NAME, XWriter.XMI);
			xWriterAnnotatorConfig.put(XWriter.PARAM_FILE_NAMER_CLASS_NAME, DateFileNamer.class.getName());
			pipe.create(XWriter.class, xWriterAnnotatorConfig);

			// Document Word Exporter (creates a list of all words)
			AnnotatorConfig documentWordExporterAnnotatorConfig = new AnnotatorConfig();
			documentWordExporterAnnotatorConfig.put(DocumentWordExporter.PARAM_OUTPUT_DIRECTORY_NAME, "target/");
			documentWordExporterAnnotatorConfig.put(DocumentWordExporter.PARAM_FILE_NAMER_CLASS_NAME, DateFileNamer.class.getName());
			pipe.create(DocumentWordExporter.class, documentWordExporterAnnotatorConfig);

			// Output TypeSystemDescription to XML File.
			// pipe.getTypeSystemDescription().toXML(new FileOutputStream("TypeSystem.xml"));

			// Run pipeline
			pipe.run(plainTextCollectionReader);
			// pipe.run(pdfCollectionReader);

		} catch (Exception e) {
			logger.error("error: ", e);
		}
	}

}
