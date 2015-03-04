/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.service;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.IJp2pDSComponent;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.builder.IJp2pContainerBuilder;

import org.eclipselabs.osgi.ds.broker.service.AbstractAttendeeProviderComponent;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractProvider;

/**
 * This component is used to register a provider in declarative services, and monitors the build process
 * that is offered through the activator. A listener regiters itself with the builder and sets the container
 * so that it can be provided
 * @author Kees
 *
 */
public class Jp2pDSComponent extends AbstractAttendeeProviderComponent implements IJp2pDSComponent {

	private Jp2pContainerProvider<Object> provider;
	private String bundle_id;
	private String introduction;
	private String token;
	private IJp2pContainerBuilder<Object> builder;
	private IContainerBuilderListener<Object> listener;

	protected Jp2pDSComponent( String bundle_id, IJp2pContainerBuilder<Object> activator ) {
		this( bundle_id, S_IJP2P_CONTAINER_PACKAGE_ID, S_IP2P_TOKEN, activator);
	}

	protected Jp2pDSComponent( String bundle_id, String introduction, String token, IJp2pContainerBuilder<Object> builder ) {
		this.bundle_id = bundle_id;
		this.token = token;
		this.introduction = introduction;
		this.builder = builder;
		this.setActivator();
	}

	/**
	 * set the activator
	 */
	private final void setActivator() {
		provider = new Jp2pContainerProvider<Object>( this.bundle_id + S_CONTAINER, introduction, token );
		if( builder.getContainer() != null ){
			provider.setContainer( builder.getContainer() );
			return;
		}
		listener = new IContainerBuilderListener<Object>() {

			@Override
			public void notifyContainerBuilt(ContainerBuilderEvent<Object> event) {
				IJp2pContainer<Object> container = event.getContainer();
				provider.setContainer( container );
			}
		};
		builder.addContainerBuilderListener(listener);
	}

	@Override
	protected void initialise() {
		super.addAttendee( this.provider );
	}

	@Override
	protected void finalise() {
		builder.removeContainerBuilderListener(listener);
		listener = null;
		builder = null;
		super.finalise();
	}
}

/**
 * The content provider that offers the context as a declarative service
 * @author Kees
 *
 */
class Jp2pContainerProvider<T extends Object> extends AbstractProvider<String, Object, IJp2pContainer<T>> {

	private IJp2pContainer<T>  container;
	
	Jp2pContainerProvider( String bundleId, String introduction, String token ) {
		super( new Palaver( introduction, token ));
		super.setIdentifier(bundleId);
	}

	/**
	 * Get the container
	 * @return
	 */
	IJp2pContainer<T> getContainer() {
		return container;
	}

	/**
	 * Add a container and 
	 * @param container
	 */
	void setContainer( IJp2pContainer<T>  container) {
		if( container == null )
			throw new NullPointerException();
		this.container = container;
		super.provide(container);
	}

	@Override
	protected void onDataReceived( Object msg ){
		if(!( msg instanceof String ))
			return;
		if( this.container != null )
			super.provide(container);
	}	

	/**
	 * The palaver contains the conditions for attendees to create an assembly. In this case, the attendees must
	 * pass a string identifier (the package id) and provide a token that is equal
	 * @author Kees
	 *
	 */
	private static class Palaver extends AbstractPalaver<String>{

		private String providedToken;

		protected Palaver() {
			super( IJp2pDSComponent.S_IJP2P_CONTAINER_PACKAGE_ID);
		}

		protected Palaver( String introduction, String token ) {
			super(  introduction );
			this.providedToken = token;
		}

		@Override
		public String giveToken() {
			if( providedToken == null )
				return IJp2pDSComponent.S_IP2P_TOKEN;
			return providedToken;
		}

		@Override
		public boolean confirm(Object token) {
			boolean retval = false;
			if( providedToken == null )
				retval = IJp2pDSComponent.S_IP2P_TOKEN .equals( token );
			else
				retval = providedToken.equals(token);
			return retval;
		}	
	}
}