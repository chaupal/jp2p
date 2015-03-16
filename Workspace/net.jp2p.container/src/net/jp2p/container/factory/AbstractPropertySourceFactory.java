/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory;

import java.util.Iterator;
import java.util.Map;

import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.ManagedProperty;

public abstract class AbstractPropertySourceFactory implements IPropertySourceFactory{

	public static final String S_FACTORY = "Factory:";
	
	private IJp2pPropertySource<IJp2pProperties> parentSource;//Needed for it triggers the child source
	private IJp2pPropertySource<IJp2pProperties> source;
	
	private boolean canCreate;
	private IContainerBuilder builder;
	private int weight;
	private String componentName;
	
	
	protected AbstractPropertySourceFactory( String componentName ) {
		this.componentName = componentName;
	}

	/**
	 * Prepare the factory, by providing the necessary objects to embed the factory in the application
	 * @param componentName
	 * @param parentSource
	 * @param builder
	 * @param attributes
	 */
	@Override
	public void prepare( IJp2pPropertySource<IJp2pProperties> parentSource, IContainerBuilder builder, Map<String, String> attributes ){
		this.canCreate = false;
		this.parentSource = parentSource;
		this.builder = builder;
		this.weight = Integer.MAX_VALUE;
	}

	protected IJp2pPropertySource<IJp2pProperties> getParentSource() {
		return parentSource;
	}

	@Override
	public IJp2pPropertySource<IJp2pProperties> getPropertySource(){
		return this.source;
	}

	/**
	 * Set the source manually
	 * @param source
	 */
	protected void setSource(IJp2pPropertySource<IJp2pProperties> source) {
		this.source = source;
	}

	@Override
	public String getComponentName() {
		return this.componentName;
	}

	/**
	 * This method is called after the property sources have been created,
	 * to allow other factories to be added as well.
	 */
	@Override
	public void extendContainer(){ /* DO NOTHING */}

	/**
	 * Parse the properties
	 */
	@Override
	public void parseProperties(){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext()){
			this.onParseProperty( source.getManagedProperty( iterator.next()));
		}
	}
	protected void onParseProperty( ManagedProperty<IJp2pProperties, Object> property ){/* DO NOTHING */}
	
	/**
	 * Get the builder
	 * @return
	 */
	protected IContainerBuilder getBuilder() {
		return builder;
	}

	/**
	 * Get the weight of the factory. By default, the context factory is zero, startup service is one
	 * @return
	 */
	@Override
	public int getWeight(){
		return weight;
	}

	protected void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * Is called upon creating the property source.
	 * @return
	 */
	protected abstract IJp2pPropertySource<IJp2pProperties> onCreatePropertySource();
	
	@Override
	public IJp2pPropertySource<IJp2pProperties> createPropertySource() {
		if( this.source == null ){
			this.source = this.onCreatePropertySource();
			this.updateState( BuilderEvents.PROPERTY_SOURCE_PREPARED );
		}
		return source;
	}

	@Override
	public final boolean canCreate() {
		return this.canCreate;
	}

	protected void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}
	
	/**
	 * Allow an update of the 
	 * @param event
	 */
	public synchronized void updateState( BuilderEvents event ){
		try{
			this.builder.updateRequest( new ComponentBuilderEvent<IJp2pComponent<Object>>( this, event ));
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		switch( event.getBuilderEvent()){
		default:
			break;
		}
	}

	/**
	 * Returns true if the given factory is an immediate child of this one
	 * @param factory
	 * @return
	 */
	protected boolean isChildFactory( IPropertySourceFactory factory ){
		if( factory == null )
			return false;
		IJp2pPropertySource<IJp2pProperties> source = factory.getPropertySource();
		if(( source == null ) || ( source.getParent() == null ))
			return false;
		return ( source.getParent().equals( this.getPropertySource()));
	}

	/**
	 * Returns true if the given factory is the parent of this one
	 * @param factory
	 * @return
	 */
	protected boolean isParentFactory( IPropertySourceFactory factory ){
		if( factory == null )
			return false;
		IJp2pPropertySource<IJp2pProperties> source = factory.getPropertySource();
		if(( source == null ) || ( source.getParent() == null ))
			return false;
		return ( source.equals( this.getPropertySource().getParent()));
	}

	@Override
	public String toString() {
		return S_FACTORY + this.getPropertySource().getComponentName() + " {" + super.toString() + "}";
	}	
	
	/**
	 * Returns true if the factory has the same component name as the given one.
	 * @param component
	 * @param factory
	 * @return
	 */
	public static boolean isPropertySourceFactory( IJp2pComponents component, IPropertySourceFactory factory ){
		if( component == null )
			return false;
		return component.toString().equals(factory.getComponentName() );
	}
}