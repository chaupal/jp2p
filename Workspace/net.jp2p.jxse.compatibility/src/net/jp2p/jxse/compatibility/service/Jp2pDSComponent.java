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
package net.jp2p.jxse.compatibility.service;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.IJp2pDSComponent;
import net.jp2p.container.activator.IJp2pBundleActivator;

import org.eclipselabs.osgi.ds.broker.service.AbstractAttendeeProviderComponent;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractProvider;


public class Jp2pDSComponent extends AbstractAttendeeProviderComponent implements IJp2pDSComponent {

	private Jp2pContainerProvider provider;

	protected Jp2pDSComponent( IJp2pBundleActivator<Object> activator ) {
		this( S_IJP2P_CONTAINER_PACKAGE_ID, S_IP2P_TOKEN, activator);
	}

	protected Jp2pDSComponent( String introduction, String token, IJp2pBundleActivator<Object> activator ) {
		provider = new Jp2pContainerProvider( activator, introduction, token );
	}

	@Override
	protected void initialise() {
		super.addAttendee( this.provider );
	}
}

/**
 * The content provider that offers the context as a declarative service
 * @author Kees
 *
 */
class Jp2pContainerProvider extends AbstractProvider<String, Object, IJp2pContainer<Object>> {

	private IJp2pBundleActivator<Object> activator;
	private IJp2pContainer<Object>  container;
	
	Jp2pContainerProvider( IJp2pBundleActivator<Object> activator, String introduction, String token ) {
		super( new Palaver( introduction, token ));
		this.activator = activator;
		this.setIdentifier( activator.getBundleId()  + Jp2pDSComponent.S_CONTAINER);
	}

	/**
	 * Get the container
	 * @return
	 */
	IJp2pContainer<Object> getContainer() {
		return container;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDataReceived( Object msg ){
		if(!( msg instanceof String ))
			return;
		if( activator.getContainer() != null )
			super.provide( (IJp2pContainer<Object>) activator.getContainer());
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