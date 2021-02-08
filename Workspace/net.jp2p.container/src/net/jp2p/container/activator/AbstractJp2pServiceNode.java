/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.activator;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.container.Jp2pContainer;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.IJp2pComponentNode;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;

public abstract class AbstractJp2pServiceNode<M extends Object>
		extends AbstractJp2pService<M> implements IJp2pComponentNode<M,Object> {

	private Collection<IJp2pComponent<Object>> children;

	protected AbstractJp2pServiceNode( IJp2pComponentNode<M,?> source, IComponentFactory<M> factory  ) {
		super( factory );
		this.children = new ArrayList<>();
	}
	
	protected AbstractJp2pServiceNode(
			IJp2pWritePropertySource<IJp2pProperties> source, M component) {
		super(source, component);
		this.children = new ArrayList<>();
	}

	protected AbstractJp2pServiceNode(String bundleId, String componentName) {
		super(bundleId, componentName);
		this.children = new ArrayList<>();
	}


	@Override
	public boolean isRoot() {
		return (super.getPropertySource().getParent() == null );
	}

	public boolean addChild( IJp2pComponent<Object> child ){
		this.children.add( child );
		String identifier = AbstractJp2pPropertySource.getBundleId( super.getPropertySource());
		notifyComponentChanged( new ComponentChangedEvent( this, child, identifier, Jp2pContainer.ServiceChange.CHILD_ADDED ));
		return true;
	}

	public void removeChild( IJp2pComponent<Object> child ){
		this.children.remove( child );
		String identifier = AbstractJp2pPropertySource.getBundleId( super.getPropertySource());
		notifyComponentChanged( new ComponentChangedEvent( this, child, identifier, Jp2pContainer.ServiceChange.CHILD_REMOVED ));
	}

	protected void notifyComponentChanged( ComponentChangedEvent<IJp2pComponent<M>> event){
		super.getDispatcher().serviceChanged( event );		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IJp2pComponent<Object>[] getChildren(){
		return this.children.toArray(new IJp2pComponent[ this.children.size() ]);
	}

	@Override
	public boolean hasChildren() {
		return !this.children.isEmpty();
	}
}