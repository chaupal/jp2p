/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.activator.IActivator;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.IJp2pComponentNode;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

public class AbstractJp2pContainer<T extends Object> extends Jp2pComponent<T> 
implements	IJp2pContainer<T>{

	public static enum ServiceChange{
		CHILD_ADDED,
		CHILD_REMOVED,
		PEERGROUP_ADDED,
		PEERGROUP_REMOVED,
		STATUS_CHANGE,
		COMPONENT_EVENT;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public static final String S_SERVICE_CONTAINER = "JXSE Container";
	
	private Collection<IJp2pComponent<?>> children;

	//Takes care of all the messaging through the container
	private ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
	
	protected AbstractJp2pContainer( String bundleId, String identifier) {
		this( new Jp2pContainerPropertySource( bundleId, identifier ));
	}

	protected AbstractJp2pContainer( IJp2pWritePropertySource<IJp2pProperties> source ) {
		super( source, null );
		this.children = new ArrayList<IJp2pComponent<?>>();
	}

	/**
	 * Get the dispatcher for this container
	 * @return
	 */
	@Override
	public ComponentEventDispatcher getDispatcher(){
		return dispatcher;
	}
	
	public void clearModules(){
		children.clear();
	}
	@Override
	public String getIdentifier() {
		IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
		return source.getDirective( Directives.NAME );
	}
	
	/**
	 * Get the category for the given key
	 * @param key
	 * @return
	 */
	public String getCategory( Object key ){
		IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
		return source.getCategory( (IJp2pProperties) key );
	}

	@Override
	public boolean isRoot() {
		return true;
	}

	public void deactivate() {
		for( IJp2pComponent<?> component: this.children ){
			if( component instanceof IActivator ){
				IActivator service = (IActivator )component;
				service.stop();
			}
		}
	}	

	@Override
	public IJp2pComponent<?>[] getChildren(){
		return this.children.toArray( new IJp2pComponent<?>[ this.children.size()]);
	}

	@Override
	public boolean addChild( IJp2pComponent<?> child ){
		this.children.add( child );
		IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
		String identifier = Jp2pContainerPropertySource.getBundleId(source);
		dispatcher.serviceChanged( new ComponentChangedEvent<IJp2pComponent<?>>( this, child, identifier, AbstractJp2pContainer.ServiceChange.CHILD_ADDED ));
		return true;
	}

	@Override
	public void removeChild( IJp2pComponent<?> child ){
		this.children.remove( child );
		IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
		String identifier = Jp2pContainerPropertySource.getBundleId(source);
		dispatcher.serviceChanged( new ComponentChangedEvent<IJp2pComponent<?>> ( this, child, identifier, AbstractJp2pContainer.ServiceChange.CHILD_REMOVED ));
	}

	@Override
	public boolean hasChildren() {
		return !this.children.isEmpty();
	}

	protected static void removeModule( AbstractJp2pContainer<?> context, Object module ){
		for( IJp2pComponent<?> component: context.getChildren() ){
			if( component.getModule().equals( module ))
				context.removeChild(component);
		}
	}
	
	/**
	 * Find the component with the given name 
	 * @param componentName
	 * @param root
	 * @return
	 */
	public static IJp2pComponent<?> findComponent( String componentName, IJp2pComponent<?> root ){
		if( Utils.isNull( componentName ))
			return null;
		if( componentName.equals( root.getPropertySource().getComponentName()))
			return root;
		if(!( root instanceof IJp2pComponentNode ))
			return null;
		IJp2pComponentNode<?> node = ( IJp2pComponentNode<?>) root;
		IJp2pComponentNode<?> result;
		for( IJp2pComponent<?> child: node.getChildren() ){
			result = (IJp2pComponentNode<?>) findComponent(componentName, child);
			if( result != null )
				return result;
		}
		return null;
	}
}