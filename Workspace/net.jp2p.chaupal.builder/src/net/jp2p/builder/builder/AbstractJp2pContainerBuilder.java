/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.builder.builder;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.builder.IJp2pContainerBuilder;
import net.jp2p.container.builder.Jp2pBuildException;
import net.jp2p.container.properties.IJp2pDirectives.DeveloperModes;

public abstract class AbstractJp2pContainerBuilder<M extends Object> implements IJp2pContainerBuilder<M> {

	private String id;
	private Class<?> clss; 
	
	private IJp2pContainer<M> container;
	private Collection<IContainerBuilderListener<M>> containerListeners;
	
	private DeveloperModes mode;

	protected AbstractJp2pContainerBuilder( String id, Class<?> clss ) {
		this( id, clss, DeveloperModes.ANY );
	}
	
	protected AbstractJp2pContainerBuilder( String id, Class<?> clss, DeveloperModes mode ) {
		this.id = id;
		this.clss = clss;
		this.mode = mode;
		containerListeners = new ArrayList<IContainerBuilderListener<M>>();
	}

	public String getId() {
		return id;
	}
	
	protected Class<?> getClss() {
		return clss;
	}

	protected DeveloperModes getMode() {
		return mode;
	}

	public IJp2pContainer<M> getContainer() {
		return container;
	}

	@Override
	public void addContainerBuilderListener(
			IContainerBuilderListener<M> listener) {
		this.containerListeners.add( listener );
	}

	@Override
	public void removeContainerBuilderListener(
			IContainerBuilderListener<M> listener) {
		this.containerListeners.remove( listener );
	}

	protected final void notifyContainerBuilderEvent( ContainerBuilderEvent<M> event ){
		for(IContainerBuilderListener<M> listener: this.containerListeners )
			listener.notifyContainerBuilt(event);
	}

	@Override
	public IJp2pContainer<M> build(Class<?> resourceClass) throws Jp2pBuildException {
		return build( resourceClass, S_DEFAULT_PATH);
	}

	@Override
	public IJp2pContainer<M> build(Class<?> resourceClass, String path) throws Jp2pBuildException {
		return build( resourceClass.getResourceAsStream(path));
	}	
}