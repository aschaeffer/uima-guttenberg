package de.hda.gutti.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.uima.collection.CollectionException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.uimafit.component.JCasCollectionReader_ImplBase;

/**
 * This CollectionReader reads raw text documents from the MiningQueue. Also it
 * creates a document wide meta data annotation to make it accessible within the
 * text mining process.
 * 
 * 
 * @author aschaeffer
 * @since 1.0.beta9
 *
 */
@Service
public class PlainTextCollectionReader extends JCasCollectionReader_ImplBase {

	private String path = "documents";

	/**
	 * TypeSystemDescription.
	 */
	private TypeSystemDescription typeSystemDescription;

	private final List<File> files = new ArrayList<File>();
	
	private ListIterator<File> iterator = files.listIterator(0);

	private final Logger logger = LoggerFactory.getLogger(PlainTextCollectionReader.class);

	public PlainTextCollectionReader() {
		prepareDirectory();
	}
	
	public void prepareDirectory() {
		try {
			Resource fileResource = new ClassPathResource(path);
			File filePath = fileResource.getFile();
			File[] f = filePath.listFiles();
			System.out.println(f.length);
			if (f.length > 0) {
				for (File file : f) {
					files.add(file);
				}
				iterator = files.listIterator(0);
			}
		} catch (IOException e) {
			logger.error("could not read directory!!!!!", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void getNext(final JCas jCas) throws IOException, CollectionException {
		File file = iterator.next();
		StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            // repeat until all lines is read
            while ((text = reader.readLine()) != null) {
                contents.append(text).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		jCas.setDocumentLanguage("de");
		jCas.setDocumentText(contents.toString());
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
	 */
	@Override
	public final void close() throws IOException {
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#getProgress()
	 */
	@Override
	public final Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(0, 100, Progress.ENTITIES) };
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public final boolean hasNext() throws IOException, CollectionException {
		return iterator.hasNext();
	}

	public TypeSystemDescription getTypeSystemDescription() {
		return typeSystemDescription;
	}

	public void setTypeSystemDescription(TypeSystemDescription typeSystemDescription) {
		this.typeSystemDescription = typeSystemDescription;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		prepareDirectory();
	}
	
}
