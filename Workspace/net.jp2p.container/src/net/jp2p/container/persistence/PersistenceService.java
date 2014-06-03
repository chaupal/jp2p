/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.persistence;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.container.component.AbstractJp2pService;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IManagedPropertyListener;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IPropertyEventDispatcher;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.ManagedPropertyEvent;

public class PersistenceService<T,U extends Object> extends AbstractJp2pService<IManagedPropertyListener<IJp2pProperties, Object>> {

	private IPersistedProperties<T,U> properties;
	private IManagedPropertyListener<IJp2pProperties, Object> listener;
	
	private Collection<IPropertyEventDispatcher> dispatchers;
	
	public PersistenceService(final IJp2pWritePropertySource<IJp2pProperties> source, IPersistedProperties<T,U> props, final IPropertyConvertor<T, U> convertor ){
		super(source, null );
		this.properties = props;
		dispatchers = new ArrayList<IPropertyEventDispatcher>();
		listener = new IManagedPropertyListener<IJp2pProperties, Object>(){

			@Override
			public void notifyValueChanged(
					ManagedPropertyEvent<IJp2pProperties, Object> event) {
				ManagedProperty<IJp2pProperties, Object> mp = event.getProperty();
				if( !ManagedProperty.isPersisted( mp ))
					return;
				properties.setConvertor(convertor);
				switch( event.getEvent() ){
				case DEFAULT_VALUE_SET:
					Object value = convertor.convertTo( mp.getKey(), properties.getProperty( mp.getSource(), mp.getKey() )); 
					mp.setValue( value );
					mp.reset();
					break;
				default:
					properties.setProperty( source, mp.getKey(), convertor.convertFrom( mp.getKey() ));
					break;
				}
			}
		};
		super.setModule(listener);
	}
	
	public void addDispatcher( IPropertyEventDispatcher dispatcher ){
		this.dispatchers.add( dispatcher );
		dispatcher.addPropertyListener(listener);
	}
	
	public void removeDispatcher( IPropertyEventDispatcher dispatcher ){
		dispatcher.removePropertyListener(listener);
		this.dispatchers.remove( dispatcher );
	}

	
	@Override
	protected void deactivate() {
		for( IPropertyEventDispatcher dispatcher: this.dispatchers ){
			dispatcher.removePropertyListener(listener);
		}
	}
}
