/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.component;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringProperty;

public class Jp2pComponent<T extends Object> implements IJp2pComponent<T>, Comparable< IJp2pComponent<T>>{

	public static final String S_DEFAULT_PROPERTY = "Default";
	private T module;
	private IJp2pPropertySource<IJp2pProperties> source;

	protected Jp2pComponent( T component ) {
		this( null, component );
	}

	public Jp2pComponent( IJp2pPropertySource<IJp2pProperties> source, T component ) {
		this.module = component;
		this.source = source;
	}

	@Override
	public String getId() {
		return this.source.getDirective( Directives.ID );
	}

	/**
	 * Get a String label for this component. This can be used for display options and 
	 * is not meant to identify the component;
	 * @return
	 */
	@Override
	public String getComponentLabel(){
		return this.source.getComponentName();
	}

	/**
	 * Get the property source of this component
	 * @return
	 */
	@Override
	public IJp2pPropertySource<IJp2pProperties> getPropertySource(){
		return source;
	}

	@Override
	public T getModule() {
		return this.module;
	}

	/**
	 * Set the module
	 * @param module
	 */
	protected void setModule(T module) {
		this.module = module;
	}

	/**
	 * Get the category for the given key
	 * @param key
	 * @return
	 */
	public String getCategory( Object key ){
		return ManagedProperty.S_DEFAULT_CATEGORY;
	}

	protected void putProperty(IJp2pProperties key, Object value ) {
		if( !( source instanceof IJp2pWritePropertySource ))
			return;
		String[] split = key.toString().split("[.]");
		StringProperty id = new StringProperty( split[ split.length - 1]);
		IJp2pWritePropertySource<IJp2pProperties> jwps = (IJp2pWritePropertySource<IJp2pProperties>) source;
		ManagedProperty<IJp2pProperties, Object> mp = jwps.getOrCreateManagedProperty(id, value, false);
		mp.setValue(value);
	}

	@Override
	public int compareTo(IJp2pComponent<T> o) {
		return Integer.MAX_VALUE;
	}
}
