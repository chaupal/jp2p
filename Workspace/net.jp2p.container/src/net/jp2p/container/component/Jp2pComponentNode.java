/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.component;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.container.AbstractJp2pContainer;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;

public class Jp2pComponentNode<T extends Object> extends Jp2pComponent<T> implements IJp2pComponentNode<T>{

	private ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
	private Collection<IJp2pComponent<?>> children;

	public Jp2pComponentNode( IJp2pPropertySource<IJp2pProperties> source, T component ) {
		super( source, component);
		this.children = new ArrayList<IJp2pComponent<?>>();
	}

	@Override
	public boolean addChild( IJp2pComponent<?> child ){
		this.children.add( child );
		String identifier = AbstractJp2pPropertySource.getBundleId( super.getPropertySource());
		dispatcher.serviceChanged( new ComponentChangedEvent<IJp2pComponent<?>>( this, identifier, AbstractJp2pContainer.ServiceChange.CHILD_ADDED ));
		return true;
	}

	@Override
	public void removeChild( IJp2pComponent<?> child ){
		this.children.remove( child );
		String identifier = AbstractJp2pPropertySource.getBundleId( super.getPropertySource());
		dispatcher.serviceChanged( new ComponentChangedEvent<IJp2pComponent<?>>( this, identifier, AbstractJp2pContainer.ServiceChange.CHILD_REMOVED ));
	}

	@Override
	public IJp2pComponent<?>[] getChildren(){
		return this.children.toArray( new IJp2pComponent<?>[this.children.size()] );
	}

	@Override
	public boolean hasChildren() {
		return !this.children.isEmpty();
	}

	/**
	 * add a module to the container. returns the JxseComponent, or null if something went wrong
	 * @param module
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static IJp2pComponent<?> addModule( IJp2pComponentNode node, Object module ){
		IJp2pComponent<Object> component = null;
		if( module instanceof IJp2pComponent )
			component = (IJp2pComponent<Object>) module;
		else
			component = new Jp2pComponent( module );

		node.addChild( component );
		return component;
	}

	/**
	 * Remove a child from the context
	 * @param node
	 * @param module
	 */
	public static void removeModule( IJp2pComponentNode<?> node, Object module ){
		for( IJp2pComponent<?> component: node.getChildren() ){
			if( component.getModule().equals( module ))
				node.removeChild(component);
		}
	}

	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return false;
	}
}
