package de.hda.gutti;

import org.apache.uima.UIMAException;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.impl.CompositeResourceFactory_impl;
import org.apache.uima.impl.UIMAFramework_impl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.CustomResourceSpecifier;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.springframework.context.ApplicationContext;
import org.uimafit.factory.JCasFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.uimafit.spring.SpringContextResourceManager;
import org.uimafit.spring.factory.AnalysisEngineFactory_impl;
import org.uimafit.spring.factory.CasConsumerFactory_impl;
import org.uimafit.spring.factory.CollectionReaderFactory_impl;
import org.uimafit.spring.factory.CustomResourceFactory_impl;

public class Pipeline {

	/**
	 * The SpringContextResourceManager.
	 */
	private SpringContextResourceManager springContextResourceManager = new SpringContextResourceManager();

	/**
	 * TypeSystemDescription.
	 */
	private TypeSystemDescription typeSystemDescription;

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

}
