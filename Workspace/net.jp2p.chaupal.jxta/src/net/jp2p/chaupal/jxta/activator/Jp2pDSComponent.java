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
package net.jp2p.chaupal.jxta.activator;

import net.jp2p.chaupal.activator.ContainerBuilderEvent;
import net.jp2p.chaupal.activator.IContainerBuilderListener;
import net.jp2p.chaupal.activator.IJp2pDSComponent;
import net.jp2p.container.IJp2pContainer;

import org.eclipselabs.osgi.ds.broker.service.AbstractAttendeeProviderComponent;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractProvider;


public class Jp2pDSComponent extends AbstractAttendeeProviderComponent implements IJp2pDSComponent {

	private Jp2pContainerProvider provider;
	private String introduction;
	private String token;
	private Jp2pBundleActivator activator;
	private IContainerBuilderListener listener;

	protected Jp2pDSComponent( Jp2pBundleActivator activator ) {
		this( S_IJP2P_CONTAINER_PACKAGE_ID, S_IP2P_TOKEN, activator);
	}

	protected Jp2pDSComponent( String introduction, String token, Jp2pBundleActivator activator ) {
		this.token = token;
		this.introduction = introduction;
		this.activator = activator;
		this.setActivator();
	}

	private void provideContainer( IJp2pContainer container ){
		try{
			provider.setContainer( container );		
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	private final void setActivator() {
		provider = new Jp2pContainerProvider( introduction, token );
		if( activator.getContainer() != null ){
			provideContainer( activator.getContainer() );
			return;
		}
		listener = new IContainerBuilderListener() {

			@Override
			public void notifyContainerBuilt(ContainerBuilderEvent event) {
				IJp2pContainer container = event.getContainer();
				provideContainer( container );
			}
		};
		activator.addContainerBuilderListener(listener);
	}

	@Override
	protected void initialise() {
		super.addAttendee( this.provider );
	}

	@Override
	protected void finalise() {
		activator.removeContainerBuilderListener(listener);
		listener = null;
		activator = null;
		super.finalise();
	}


}

/**
 * The content provider that offers the context as a declarative service
 * @author Kees
 *
 */
class Jp2pContainerProvider extends AbstractProvider<String, Object, IJp2pContainer> {

	private IJp2pContainer  container;
	
	Jp2pContainerProvider() {
		super( new Palaver());
	}

	Jp2pContainerProvider( String introduction, String token ) {
		super( new Palaver( introduction, token ));
	}

	/**
	 * Get the container
	 * @return
	 */
	IJp2pContainer getContainer() {
		return container;
	}

	/**
	 * Add a container and 
	 * @param container
	 */
	void setContainer( IJp2pContainer  container) {
		if( container == null )
			throw new NullPointerException();
		this.container = container;
		super.setIdentifier( container.getId() );
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