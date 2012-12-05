package de.hda.gutti;

import java.io.IOException;
import java.util.*;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.impl.CompositeResourceFactory_impl;
import org.apache.uima.impl.UIMAFramework_impl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.CustomResourceSpecifier;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.JCasFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.uimafit.spring.SpringContextResourceManager;
import org.uimafit.spring.factory.AnalysisEngineFactory_impl;
import org.uimafit.spring.factory.CasConsumerFactory_impl;
import org.uimafit.spring.factory.CollectionReaderFactory_impl;
import org.uimafit.spring.factory.CustomResourceFactory_impl;

import de.hda.gutti.domains.AnnotatorConfig;

public class Pipeline {

	/**
	 * The SpringContextResourceManager.
	 */
	private SpringContextResourceManager springContextResourceManager = new SpringContextResourceManager();

	/**
	 * TypeSystemDescription.
	 */
	private TypeSystemDescription typeSystemDescription;

	
	/**
	 * A list of analysis engines.
	 */
	private final LinkedList<AnalysisEngine> analysisEngines = new LinkedList<AnalysisEngine>();

	/**
	 * The current position.
	 */
	private final Integer cursor = 0;

	private final Logger logger = LoggerFactory.getLogger(Pipeline.class);
	
	public void init() {
		
	}

	/**
	 * Initializes uimafit-spring features.
	 * 
	 * @param applicationContext The ApplicationContext.
	 */
	public final void enableSpringFeatures(final ApplicationContext applicationContext) {
		new UIMAFramework_impl() {
			{
				CompositeResourceFactory_impl factory = (CompositeResourceFactory_impl) getResourceFactory();
				factory.registerFactory(CasConsumerDescription.class, applicationContext.getBean(CasConsumerFactory_impl.class));
				// factory.registerFactory(CasInitializerDescription.class, applicationContext.getBean(CasInitializerFactory_impl.class));
				factory.registerFactory(CollectionReaderDescription.class, applicationContext.getBean(CollectionReaderFactory_impl.class));
				factory.registerFactory(ResourceCreationSpecifier.class, applicationContext.getBean(AnalysisEngineFactory_impl.class));
				factory.registerFactory(CustomResourceSpecifier.class, applicationContext.getBean(CustomResourceFactory_impl.class));
			}
		};
		springContextResourceManager.setAutowireEnabled(true);
		springContextResourceManager.setApplicationContext(applicationContext);
	}

	/**
	 * Creates a TypeSystemDescription.
	 * @param path The path.
	 */
	public final void createTypeSystemDescription(final String path) {
		typeSystemDescription = TypeSystemDescriptionFactory.createTypeSystemDescription(path);
	}

	/**
	 * Creates a TypeSystemDescription.
	 * @param path Sets the path of the type system description (e.g. desc/TypeSystem).
	 * @param name Sets the name of the type system description.
	 * @param description Sets the description of the type system description.
	 * @param vendor Sets the vendor of the type system description.
	 * @param version Sets the version of the type system description.
	 */
	public final void createTypeSystemDescription(final String path, final String name, final String description, final String vendor, final String version) {
		typeSystemDescription = TypeSystemDescriptionFactory.createTypeSystemDescription(path);
		typeSystemDescription.setName(name);
		typeSystemDescription.setDescription(description);
		typeSystemDescription.setVendor(vendor);
		typeSystemDescription.setVersion(version);
	}

	/**
	 * Creates a new jCas object.
	 * 
	 * @param document The document text.
	 * @param language The document language.
	 * @return A new jCas object.
	 * @throws UIMAException If no jCas object was created.
	 */
	public final JCas createJCas(final String document, final String language) throws UIMAException {
		JCas jCas = JCasFactory.createJCas(typeSystemDescription);
		jCas.setDocumentText(document);
		jCas.setDocumentLanguage(language);
		return jCas;
	}

	public SpringContextResourceManager getSpringContextResourceManager() {
		return springContextResourceManager;
	}

	public void setSpringContextResourceManager(
			SpringContextResourceManager springContextResourceManager) {
		this.springContextResourceManager = springContextResourceManager;
	}

	public TypeSystemDescription getTypeSystemDescription() {
		return typeSystemDescription;
	}

	public void setTypeSystemDescription(TypeSystemDescription typeSystemDescription) {
		this.typeSystemDescription = typeSystemDescription;
	}

	/**
	 * Adds an analysis engine to the pipeline.
	 * 
	 * @param analysisEngineClass The analysis engine class.
	 * @param annotatorConfig An annotator configuration.
	 * @throws ResourceInitializationException 
	 */
	public final void create(final Class<? extends AnalysisComponent> analysisEngineClass, final AnnotatorConfig configuration) throws ResourceInitializationException {
		// create(analysisEngineClass, annotatorConfig);
		Object[] config = new Object[configuration.size() * 2];
		Set<String> parameters = configuration.keySet();
		Iterator<String> iterator = parameters.iterator();
		Integer i = 0;
		while (iterator.hasNext()) {
			String parameterName = iterator.next();
			config[i] = parameterName;
			config[i + 1] = configuration.get(parameterName);
			i = i + 2;
		}
		AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(analysisEngineClass, getTypeSystemDescription(), config);
		add(analysisEngine);
	}

	/**
	 * Adds an analysis engine to the pipeline.
	 * 
	 * @param analysisEngineClass The analysis engine class.
	 * @throws ResourceInitializationException 
	 */
	public final void create(final Class<? extends AnalysisComponent> analysisEngineClass) throws ResourceInitializationException {
		AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(analysisEngineClass, getTypeSystemDescription());
		add(analysisEngine);
	}

	/**
	 * Adds an analysis engine to the pipeline.
	 * 
	 * @param analysisEngine The analysis engine.
	 */
	public final void add(final AnalysisEngine analysisEngine) {
		analysisEngines.add(analysisEngine);
	}

	/**
	 * Adds an analysis engines to the pipeline.
	 * 
	 * @param analysisEngineDescription The analysis engine description.
	 * @throws UIMAException TODO: doc
	 */
	public final void add(final AnalysisEngineDescription analysisEngineDescription) throws UIMAException {
		if (analysisEngineDescription.isPrimitive()) {
			analysisEngines.add(AnalysisEngineFactory.createPrimitive(analysisEngineDescription));
		} else {
			analysisEngines.add(AnalysisEngineFactory.createAggregate(analysisEngineDescription));
		}
	}

	/**
	 * Adds a list of analysis engine to the pipeline.
	 * 
	 * @param analysisEngines A list of analysis engines.
	 */
	public final void add(final List<AnalysisEngine> analysisEngines) {
		analysisEngines.addAll(analysisEngines);
	}

	/**
	 * Adds a list of analysis engines to the pipeline.
	 * 
	 * @param analysisEngineDescriptions A list of analysis engine descriptions.
	 */
	public final void addByDescriptions(final List<AnalysisEngineDescription> analysisEngineDescriptions) {
		ListIterator<AnalysisEngineDescription> iterator = analysisEngineDescriptions.listIterator(0);
		while (iterator.hasNext()) {
			try {
				add(iterator.next());
			} catch (UIMAException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Inserts an analysis engine at the current position.
	 * 
	 * @param analysisEngine The analysis engine.
	 */
	public final void insert(final AnalysisEngine analysisEngine) {
		analysisEngines.add(cursor, analysisEngine);
	}

	/**
	 * Inserts an analysis engine at the current position.
	 * 
	 * @param analysisEngineDescription The analysis engine description.
	 * @throws UIMAException On error.
	 */
	public final void insert(final AnalysisEngineDescription analysisEngineDescription) throws UIMAException {
		if (analysisEngineDescription.isPrimitive()) {
			analysisEngines.add(cursor, AnalysisEngineFactory.createPrimitive(analysisEngineDescription));
		} else {
			analysisEngines.add(cursor, AnalysisEngineFactory.createAggregate(analysisEngineDescription));
		}
	}

	/**
	 * Removes an analysis engine.
	 * 
	 * @param analysisEngine The analysis engine.
	 */
	public final void remove(final AnalysisEngine analysisEngine) {
		analysisEngines.remove(analysisEngine);
		
	}

	/**
	 * Removes the analysis engine at the current position.
	 */
	public final void remove() {
		analysisEngines.remove(cursor);
		
	}

	/**
	 * Moves the analysis engine at the current position to the left.
	 */
	public final void moveLeft() {
		if (cursor > 1) {
			Collections.swap(analysisEngines, cursor, cursor - 1);
		}
	}

	/**
	 * Moves the analysis engine at the current position to the right.
	 */
	public final void moveRight() {
		if (cursor < analysisEngines.size() - 1) {
			Collections.swap(analysisEngines, cursor, cursor + 1);
		}
	}

	/**
	 * This method allows you to run a list of analysis engines over a jCas.
	 *
	 * @param jCas The jCas to process.
	 * @throws UIMAException TODO: doc
	 */
	public final void run(final JCas jCas) throws UIMAException {
		ListIterator<AnalysisEngine> iterator = analysisEngines.listIterator(0);
		while (iterator.hasNext()) {
			try {
				AnalysisEngine analysisEngine = iterator.next();
				logger.info("running analysis engine: " + analysisEngine.getMetaData().getName());
				analysisEngine.process(jCas);
			} catch (AnalysisEngineProcessException e) {
				e.printStackTrace();
			}
		}
		iterator = analysisEngines.listIterator(0);
		while (iterator.hasNext()) {
			iterator.next().collectionProcessComplete();
		}
	}

	public final JCas run() throws UIMAException {
		JCas jCas = JCasFactory.createJCas(getTypeSystemDescription());
		run(jCas);
		return jCas;
	}

	/**
	 * Provides a simple way to run a pipeline for a given collection reader and sequence of
	 * analysis engines.
	 *
	 * @param reader A collection reader.
	 * @throws UIMAException TODO: doc
	 * @throws IOException TODO: doc
	 */
	public final void run(final CollectionReader reader) throws UIMAException, IOException {
		ListIterator<AnalysisEngine> iterator;
		JCas jCas = JCasFactory.createJCas(getTypeSystemDescription());
		CAS cas = jCas.getCas();
		if (!reader.hasNext()) {
			logger.warn("Could not read documents from collection reader");
			reader.close();
		}
		while (reader.hasNext()) {
			reader.getNext(cas);
			iterator = analysisEngines.listIterator(0);
			while (iterator.hasNext()) {
				try {
					AnalysisEngine analysisEngine = iterator.next();
					logger.info("running analysis engine: " + analysisEngine.getMetaData().getName());
					analysisEngine.process(cas);
				} catch (AnalysisEngineProcessException e) {
					e.printStackTrace();
				}
			}
			cas.reset();
		}
		iterator = analysisEngines.listIterator(0);
		while (iterator.hasNext()) {
			iterator.next().collectionProcessComplete();
		}
		reader.close();
	}

	/**
	 * Returns the size of the pipeline.
	 * @return The size of the pipeline.
	 */
	public final Integer size() {
		return analysisEngines.size();
	}
	
	public final JCas createJCas() throws UIMAException {
		return JCasFactory.createJCas(getTypeSystemDescription());
	}

}
