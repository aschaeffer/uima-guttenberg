package de.hda.gutti.domains;

import java.util.HashMap;

public class AnnotatorConfig extends HashMap<String, Object> {

	private static final long serialVersionUID = -4756049862568698095L;

	/**
	 * Constructor for configuration.
	 * 
	 * @param configuration The configuration as map.
	 */
	public AnnotatorConfig(final HashMap<String, Object> configuration) {
		this.putAll(configuration);
	}

	/**
	 * Default Constructor.
	 */
	public AnnotatorConfig() {
	}

}
