package de.hda.gutti.analysis;

import static org.uimafit.util.JCasUtil.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.component.xwriter.XWriterFileNamer;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.ConfigurationParameterFactory;
import org.uimafit.factory.initializable.InitializableFactory;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class DocumentWordExporter extends JCasAnnotator_ImplBase {

	/**
	 * The parameter name for the configuration parameter that specifies the output directory
	 */
	public static final String PARAM_OUTPUT_DIRECTORY_NAME = ConfigurationParameterFactory.createConfigurationParameterName(DocumentWordExporter.class, "outputDirectoryName");
	@ConfigurationParameter(mandatory = true, description = "takes a path to directory into which output files will be written.")
	private String outputDirectoryName;

	/**
	 * The parameter name for the configuration parameter that specifies the name of the class that
	 * implements the file namer
	 */
	public static final String PARAM_FILE_NAMER_CLASS_NAME = ConfigurationParameterFactory.createConfigurationParameterName(DocumentWordExporter.class, "fileNamerClassName");
	@ConfigurationParameter(mandatory = true, description = "the class name of the XWriterFileNamer implementation to use", defaultValue = "org.uimafit.component.xwriter.IntegerFileNamer")
	protected String fileNamerClassName;

	private File outputDirectory;

	private XWriterFileNamer fileNamer;

	/**
	 * The Logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(DocumentWordExporter.class);

	/**
	 * Initialization of this analysis engine.
	 * 
	 * @param aContext The UimaContext.
	 * @throws ResourceInitializationException If analysis engine cannot be initialized.
	 */
	@Override
	public final void initialize(final UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		
		outputDirectory = new File(outputDirectoryName);
		if (!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}

		fileNamer = InitializableFactory.create(aContext, fileNamerClassName, XWriterFileNamer.class);

	}

	/**
	 * Processing method of this analysis engine.
	 * 
	 * @param jCas A jCas structure.
	 * @throws AnalysisEngineProcessException On processing error.
	 */
	@Override
	public final void process(final JCas jCas) throws AnalysisEngineProcessException {
		Set<String> listOfWords = new HashSet<String>();
		for (Token token : select(jCas, Token.class)) {
			if (!listOfWords.contains(token.getCoveredText())) {
				listOfWords.add(token.getCoveredText());
			}
		}
		logger.info("detected " + listOfWords.size() + " words");
		String filename = fileNamer.nameFile(jCas);
		File outFile = new File(outputDirectory, filename + ".words");
		FileOutputStream out = null;
		PrintStream printStream = null;
		try {
			out = new FileOutputStream(outFile);
			printStream = new PrintStream(out);
			Iterator<String> iterator = listOfWords.iterator();
			while (iterator.hasNext()) {
				printStream.println(iterator.next());
			}
			printStream.close();
			logger.info("wrote list of words");
		} catch (FileNotFoundException e) {
			logger.error("could not find file", e);
		} finally {
			try {
				printStream.close();
			} catch (Exception e1) {
				logger.error("could not close print stream", e1);
			}
		}

	}

}
