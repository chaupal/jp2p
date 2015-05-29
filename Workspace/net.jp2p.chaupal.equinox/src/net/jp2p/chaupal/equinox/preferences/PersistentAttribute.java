/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.equinox.preferences;

import java.util.HashMap;
import java.util.Map;

import net.jp2p.chaupal.equinox.preferences.PreferenceStore.Persistence;
import net.jp2p.chaupal.equinox.preferences.PreferenceStore.SupportedAttributes;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

public class PersistentAttribute{
	
	private Map<SupportedAttributes, String> attributes;
	private String key, value;
	
	
	public PersistentAttribute() {
		this( new HashMap<SupportedAttributes, String>());
		this.attributes.put(SupportedAttributes.PERSIST, Persistence.NULL.toString());
	}

	public PersistentAttribute( Map<SupportedAttributes, String> attributes ) {
		super();
		this.attributes = attributes;
	}

	public PersistentAttribute( String key, String defaultValue ) {
		this();
		this.key = key;
		this.value = defaultValue;
	}

	public PersistentAttribute( Map<SupportedAttributes, String> attributes, String key, String defaultValue ) {
		this( attributes );
		this.key = key;
		this.value = defaultValue;
	}

	public String getKey() {
		return key;
	}
	void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}
	
	void setValue(String value) {
		this.value = value;
	}
	
	public Persistence getPersistence() {
		String str = this.attributes.get( SupportedAttributes.PERSIST );
		if( Utils.isNull(str))
			return Persistence.NULL;
		return Persistence.valueOf( StringStyler.styleToEnum( str));
	}
	
	boolean isPersistent() {
		Persistence persistence = getPersistence();
		return persistence.ordinal() > 1;
	}
}
