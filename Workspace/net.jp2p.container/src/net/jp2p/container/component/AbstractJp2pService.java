/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.component;

import java.util.Calendar;
import java.util.Date;

import net.jp2p.container.AbstractJp2pContainer;
import net.jp2p.container.activator.AbstractActivator;
import net.jp2p.container.activator.IJp2pService;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.DefaultPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.utils.StringProperty;

public abstract class AbstractJp2pService<T extends Object> extends AbstractActivator
implements IJp2pService<T>{

	public static final String S_SERVICE = "Service";
	
	public static final String S_ERR_ILLEGAL_INIT = 
			"The module is initialised but already exists. Please finalise first.";
	public static final String S_ERR_NOT_COMPLETED = 
			"The factory did not create the module. The flag setCompleted must be true, which is usually checked  with setAvailable.";
	
	private T module;
	private IJp2pWritePropertySource<IJp2pProperties> source;
	
	private ComponentEventDispatcher dispatcher;

	protected AbstractJp2pService( String bundleId, String componentName) {
		this( new DefaultPropertySource( bundleId, componentName),null);
	}

	protected AbstractJp2pService( IJp2pWritePropertySource<IJp2pProperties> source, T module ) {
		dispatcher = ComponentEventDispatcher.getInstance();
		this.source = source;
		this.module = module;
		super.setStatus( Status.AVAILABLE );
		super.initialise();
	}

	protected AbstractJp2pService( IComponentFactory<T> factory ) {
		this( (IJp2pWritePropertySource<IJp2pProperties>) factory.getPropertySource(), factory.getComponent() );
	}

	/**
	 * Get the id
	 */
	@Override
	public String getId(){
		return this.source.getId();
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
	 * Get the create date
	 */
	public Date getCreateDate(){
		Object value = this.source.getProperty( ModuleProperties.CREATE_DATE);
		if( value == null )
			return Calendar.getInstance().getTime();
		return ( Date )value;
	}

	@Override
	public IJp2pPropertySource<IJp2pProperties> getPropertySource() {
		return source;
	}

	/**
	 * Make public
	 */
	@Override
	public void initialise() {
		super.initialise();
	}

	
	@Override
	protected boolean onInitialising() {
		return true;	
	}

	/**
	 * Make public
	 */
	@Override
	public void finalise() {
		super.finalise();
	}

	@Override
	protected void onFinalising(){
		this.module = null;
	}

	@Override
	public T getModule(){
		return module;
	}
	
	protected void setModule( T module ){
		this.module = module;
	}
	
	protected void putProperty( IJp2pProperties key, Object value ){
		String[] split = key.toString().split("[.]");
		StringProperty id = new StringProperty( split[ split.length - 1]);
		ManagedProperty<IJp2pProperties, Object> mp = source.getOrCreateManagedProperty(id, value, false);
		mp.setValue(value);
	}

	/**
	 * Get the category for the given key
	 * @param key
	 * @return
	 */
	public String getCategory( Object key ){
		return this.source.getCategory(( IJp2pProperties )key );
	}
	
	@Override
	protected void notifyListeners(Status previous, Status status) {
		super.notifyListeners(previous, status);
		ComponentChangedEvent event = new ComponentChangedEvent( this, AbstractJp2pContainer.ServiceChange.STATUS_CHANGE );
		dispatcher.serviceChanged(event);
	}
}