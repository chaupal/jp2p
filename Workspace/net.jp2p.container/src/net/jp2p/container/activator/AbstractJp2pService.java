/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.activator;

import java.util.Calendar;
import java.util.Date;
import net.jp2p.container.Jp2pContainer;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.DefaultPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.utils.StringProperty;

public abstract class AbstractJp2pService<M extends Object> extends AbstractActivator implements IJp2pComponent<M>, IJp2pService<M>{

	public static final String S_SERVICE = "Service";
	
	public static final String S_ERR_ILLEGAL_INIT = 
			"The module is initialised but already exists. Please finalise first.";
	public static final String S_ERR_NOT_COMPLETED = 
			"The factory did not create the module. The flag setCompleted must be true, which is usually checked  with setAvailable.";
	
	private M module;
	private IJp2pWritePropertySource<IJp2pProperties> source;
	
	private ComponentEventDispatcher dispatcher;

	protected AbstractJp2pService( String bundleId, String componentName) {
		this( new DefaultPropertySource( bundleId, componentName),null);
	}

	protected AbstractJp2pService( IJp2pWritePropertySource<IJp2pProperties> source, M module ) {
		dispatcher = ComponentEventDispatcher.getInstance();
		this.source = source;
		this.module = module;
		super.setStatus( Status.AVAILABLE );
		super.initialise();
	}

	protected AbstractJp2pService( IComponentFactory<M> factory ) {
		this( (IJp2pWritePropertySource<IJp2pProperties>) factory.getPropertySource(), factory.createComponent().getModule() );
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

	/**
	 * Get the dispatcher
	 * @return
	 */
	protected final ComponentEventDispatcher getDispatcher() {
		return dispatcher;
	}

	@Override
	public IJp2pPropertySource<IJp2pProperties> getPropertySource() {
		return source;
	}

	
	@Override
	protected void setStatus(Status status) {
		super.setStatus(status);
		this.source.setProperty( IJp2pProperties.Jp2pProperties.STATUS, super.getStatus());
	}

	@Override
	protected boolean onInitialising() {
		this.source.setProperty( IJp2pProperties.Jp2pProperties.STATUS, super.getStatus());
		return true;	
	}

	@Override
	protected void activate() {
		// DO NOTHING: default behaviour 		
	}

	@Override
	protected void onFinalising(){
		this.source.setProperty( IJp2pProperties.Jp2pProperties.STATUS, super.getStatus());
		this.module = null;
	}

	@Override
	public M getModule(){
		return module;
	}
	
	protected void setModule( M module ){
		this.module = null;
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
	protected void notifyListeners( Status previous, Status status) {
		this.source.setProperty( IJp2pProperties.Jp2pProperties.STATUS, status);
		super.notifyListeners(previous, status);
		String identifier = AbstractJp2pPropertySource.getBundleId(source);
		ComponentChangedEvent<IJp2pService<M>> event = 
				new ComponentChangedEvent<IJp2pService<M>>( this, Jp2pContainer.ServiceChange.STATUS_CHANGE );
		dispatcher.serviceChanged(event);
	}
}