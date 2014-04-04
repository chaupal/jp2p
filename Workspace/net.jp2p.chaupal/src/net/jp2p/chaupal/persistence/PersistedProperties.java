/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.persistence;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import net.jp2p.container.persistence.AbstractPersistedProperty;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.ManagedProperty;

public class PersistedProperties extends AbstractPersistedProperty<String,Object>{

	private IScopeContext scope;
	private IPropertyConvertor<String,Object> convertor;
	
	public PersistedProperties( IJp2pWritePropertySource<IJp2pProperties> source, IScopeContext scope ) {
		super( source );
		this.scope = scope;
	}

	@Override
	public void setConvertor( IPropertyConvertor<String,Object> convertor) {
		this.convertor = convertor;
	}

	/**
	 * Clear the property
	 * @param source
	 */
	@Override
	public void clear( IJp2pPropertySource<IJp2pProperties> source ){
		Preferences pref1 = scope.getNode( AbstractJp2pPropertySource.getBundleId(source) + "." + AbstractJp2pPropertySource.getIdentifier(source));
		try {
			pref1.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the persisted property with the given id, if it exists
	 * @param id
	 * @return
	 */
	@Override
	public String getProperty( IJp2pPropertySource<IJp2pProperties> source, IJp2pProperties id ){
		IPreferencesService service = Platform.getPreferencesService();
		Preferences pref1 = scope.getNode( AbstractJp2pPropertySource.getBundleId(source) + "." + AbstractJp2pPropertySource.getIdentifier(source));
		Preferences[] nodes = new Preferences[] {pref1};
		String defaultValue = convertor.convertFrom( id );
		String value = service.get( id.toString(), defaultValue, nodes );
		return value;
	}
	
	/**
	 * Set the property of a preference store
	 * @param id
	 * @param value
	 * @return
	 */
	@Override
	public boolean setProperty( IJp2pPropertySource<IJp2pProperties> source, IJp2pProperties id, String value ){
		ManagedProperty<IJp2pProperties, Object> mp = source.getManagedProperty(id);
		if( !ManagedProperty.isPersisted(mp))
			return false;
		Preferences pref1 = scope.getNode( AbstractJp2pPropertySource.getBundleId(source) + "." + AbstractJp2pPropertySource.getIdentifier(source));
		pref1.put( id.toString(), value.toString());
		try {
			pref1.flush();
			return true;
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		return false;
	}	
}
