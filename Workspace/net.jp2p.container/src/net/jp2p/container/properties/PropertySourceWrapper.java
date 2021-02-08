/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import java.util.Iterator;

public class PropertySourceWrapper< T extends Object> implements IJp2pPropertySource<T> {

	boolean parentIsSource;
	private IJp2pPropertySource<T> source;
	
	public PropertySourceWrapper( IJp2pPropertySource<T> source, boolean parentIsSource ) {
		this.source = source;
		this.parentIsSource = parentIsSource;
	}

	public PropertySourceWrapper( IJp2pPropertySource<T> source ) {
		this( source, false );
	}

	/**
	 * Get the property source
	 * @return
	 */
	protected  IJp2pPropertySource<T> getSource(){
		return source;
	}
	
	@Override
	public String getId() {
		return source.getId();
	}

	@Override
	public boolean isEnabled() {
		return source.isEnabled();
	}

	@Override
	public String getComponentName() {
		return source.getComponentName();
	}

	@Override
	public int getDepth() {
		return source.getDepth();
	}

	@Override
	public Object getDefault(T id) {
		return source.getDefault(id);
	}

	@Override
	public ManagedProperty<T, Object> getManagedProperty(T id) {
		return source.getManagedProperty(id);
	}

	@Override
	public Iterator<T> propertyIterator() {
		return this.source.propertyIterator();
	}

	@Override
	public boolean validate(T id, Object value) {
		return this.source.validate(id, value);
	}

	@Override
	public String getDirective(IJp2pDirectives id) {
		return this.source.getDirective(id);
	}

	
	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		return this.source.setDirective(id, value);
	}

	@Override
	public Iterator<IJp2pDirectives> directiveIterator() {
		return this.source.directiveIterator();
	}
	
	@Override
	public IJp2pPropertySource<?>[] getChildren() {
		return this.source.getChildren();
	}


	@Override
	public IPropertyConvertor<T, String, Object> getConvertor() {
		return source.getConvertor();
	}

	@Override
	public IJp2pPropertySource<?> getParent() {
		if( this.parentIsSource )
			return source;
		return source.getParent();
	}

	@Override
	public boolean isEmpty() {
		return source.isEmpty();
	}

	@Override
	public Object getProperty(T id) {
		return this.source.getProperty(id);
	}

	@Override
	public IJp2pPropertySource<?> getChild(String componentName) {
		return this.source.getChild(componentName);
	}

	@Override
	public String getCategory(T id) {
		return this.source.getCategory(id);
	}

	@Override
	public boolean addChild(IJp2pPropertySource<?> child) {
		return this.source.addChild(child);
	}

	@Override
	public void removeChild(IJp2pPropertySource<?> child) {
		this.removeChild(child);
	}

	@Override
	public boolean isRoot() {
		return this.source.isRoot();
	}

	@Override
	public boolean hasChildren() {
		return this.source.hasChildren();
	}

	@Override
	public boolean setManagedProperty(
			ManagedProperty<IJp2pProperties, Object> property) {
		return this.source.setManagedProperty(property);
	}

	@Override
	public void copy(IJp2pPropertySource<IJp2pProperties> source)
			throws CloneNotSupportedException {
		this.source.copy(source);
	}	
}