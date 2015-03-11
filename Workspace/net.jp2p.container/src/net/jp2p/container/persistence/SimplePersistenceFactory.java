/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.persistence;

import java.util.Iterator;
import java.util.Stack;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.context.Jp2pServiceLoader;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.filter.BuilderEventFilter;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IManagedPropertyListener;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IPropertyEventDispatcher;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.properties.ManagedProperty;

public class SimplePersistenceFactory extends AbstractComponentFactory<IManagedPropertyListener<IJp2pProperties, Object>> implements IContextFactory {

	private Stack<IPropertyEventDispatcher> stack;
	private Jp2pServiceLoader loader;
	
	private IComponentFactoryFilter filter = new BuilderEventFilter<IJp2pComponent<IManagedPropertyListener<IJp2pProperties, Object>>>( BuilderEvents.PROPERTY_SOURCE_PREPARED, this );

	public SimplePersistenceFactory() {
		stack = new Stack<IPropertyEventDispatcher>();
	}

	protected Jp2pServiceLoader getLoader() {
		return loader;
	}

	/* (non-Javadoc)
	 * @see net.jp2p.container.persistence.IContextFactory#setLoader(net.jp2p.container.context.ContextLoader)
	 */
	@Override
	public void setLoader(Jp2pServiceLoader loader) {
		this.loader = loader;
	}


	@Override
	protected IJp2pPropertySource<IJp2pProperties> onCreatePropertySource() {
		IJp2pPropertySource<IJp2pProperties> source = new PersistencePropertySource( super.getParentSource() );
		return source;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		if( !filter.accept(event))
			return;
		boolean autostart = AbstractJp2pPropertySource.getBoolean(super.getPropertySource(), Directives.AUTO_START );
		if(!autostart )
			return;
		this.setPersistedProperty(event);
		super.setCanCreate(true);
		super.createComponent();
		if(!( event.getFactory().getPropertySource() instanceof IPropertyEventDispatcher))
			return;
		IPropertyEventDispatcher dispatcher = (IPropertyEventDispatcher) event.getFactory().getPropertySource();
		PersistenceService<String,Object> service = (PersistenceService<String, Object>) super.getComponent();
		if( service == null )
			stack.push( dispatcher );
		else
			service.addDispatcher(dispatcher);
	}

	/**
	 * Set the persisted property
	 * @param event
	 */
	protected void setPersistedProperty( ComponentBuilderEvent<Object> event ){
		IJp2pPropertySource<IJp2pProperties> source = event.getFactory().getPropertySource();
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext()){
			IJp2pProperties id = iterator.next();
			ManagedProperty<IJp2pProperties,Object> mp = source.getManagedProperty( id );
			if(!ManagedProperty.isPersisted( mp ))
				continue;
			IPersistedProperties<String,Object> properties = new PersistedProperties( (IJp2pWritePropertySource<IJp2pProperties>) source );
			IPropertyConvertor<String, Object> convertor = loader.getConvertor( source); 
			properties.setProperty( source, id, convertor.convertFrom( id ));	
		}
	}
	
	@Override
	public synchronized IJp2pComponent<IManagedPropertyListener<IJp2pProperties, Object>> createComponent() {
		return super.createComponent();
	}
	
	@Override
	protected IJp2pComponent<IManagedPropertyListener<IJp2pProperties, Object>> onCreateComponent(
			IJp2pPropertySource<IJp2pProperties> source) {
		IPersistedProperties<String,Object> properties = new PersistedProperties( (IJp2pWritePropertySource<IJp2pProperties>) source );
		IPropertyConvertor<String, Object> convertor = loader.getConvertor( source); 
		PersistenceService<String,Object> service = new PersistenceService<String,Object>( (IJp2pWritePropertySource<IJp2pProperties>) source, properties, convertor );
		while( stack.size() > 0)
			service.addDispatcher( stack.pop() );
		service.start();
		stack = null;
		return service;
	}
}