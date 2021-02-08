/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.filter.AbstractComponentFactoryFilter;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;

public class FilterChain<T extends Object> extends AbstractComponentFactoryFilter<T> {

	//Supported operators
	public enum Operators{
		AND,
		SEQUENTIAL_AND,
		OR;
	}
	
	private Operators operator;
	
	private Map<IComponentFactoryFilter, Boolean> filters;
	
	private Collection<IFilterChainListener> listeners;
	
	private boolean completed;
	
	public FilterChain( Operators operator, IComponentFactory<T> factory) {
		super(factory);
		this.operator = operator;
		filters = new HashMap<IComponentFactoryFilter, Boolean>();
		listeners = new ArrayList<IFilterChainListener>();
		this.completed = false;
	}

	public FilterChain( IComponentFactory<T> factory) {
		this( Operators.AND, factory);
	}

	public boolean addFilter( IComponentFactoryFilter filter ){
		this.filters.put( filter, Boolean.FALSE );
		return true;
	}

	public boolean removeFilter( IComponentFactoryFilter filter ){
		return this.filters.remove( filter );
	}

	public boolean addListener( IFilterChainListener listener ){
		return this.listeners.add( listener );
	}

	public boolean removeListener( IFilterChainListener listener ){
		return this.listeners.remove( listener );
	}

	public void reset(){
		this.completed = false;
	}
	
	protected boolean notifyFilterAccept( IComponentFactoryFilter filter, IComponentFactory<?> factory){
		if( !filter.hasAccepted() )
			return false;
		boolean retval = true;
		for( IFilterChainListener listener: this.listeners )
			retval |= listener.notifyComponentCompleted( new FilterChainEvent( filter, factory ));
		return retval;
	}
	
	@Override
	public boolean onAccept(ComponentBuilderEvent event) {
		if( completed )
			return false;
		boolean accept = false;	
		switch( operator ){
			case OR:
				for( IComponentFactoryFilter filter: this.filters.keySet() ){
					if( filters.get(filter))
						continue;
					accept = filter.accept(event);
					filters.put(filter, accept);
					if( accept ){
						completed = notifyFilterAccept(filter, (IComponentFactory<?>) event.getFactory());
					}
				}
				break;
			case SEQUENTIAL_AND:
				boolean retval = true;
				for( IComponentFactoryFilter filter: this.filters.keySet() ){
					if( filters.get(filter))
						continue;
					accept = filter.hasAccepted() || filter.accept(event);
					retval &= accept;
					filters.put(filter, accept);
					if( accept )
						this.notifyFilterAccept(filter, (IComponentFactory<?>) event.getFactory());
						//retval &= accept;
				}
				completed = retval;
				break;
			default:
				for( IComponentFactoryFilter filter: this.filters.keySet() ){
					if( filters.get(filter))
						continue;
					accept = filter.accept(event);
					filters.put(filter, accept);
					if( !accept )
						return false;
					else
						accept = this.notifyFilterAccept(filter, (IComponentFactory<?>) event.getFactory());
				}
				completed = true;
				break;
			}
		return completed;
	}
}
