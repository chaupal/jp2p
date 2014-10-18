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
package net.jp2p.chaupal.jxta.network;

import java.util.logging.Logger;

import net.jp2p.chaupal.dispatcher.ServiceEventDispatcher;
import net.jp2p.container.component.AbstractJp2pService;
import net.jp2p.container.log.Jp2pLevel;

import net.jxta.rendezvous.RendezVousService;
import net.jxta.rendezvous.RendezvousEvent;
import net.jxta.rendezvous.RendezvousListener;

public class RendezVousComponent extends AbstractJp2pService<RendezVousService> implements RendezvousListener, IRendezVousComponent{

	public static final String S_RENDEZ_VOUS_SERVICE = "RendezVous Service";

	public static final String S_ERR_SERVICE_NOT_STARTED = "The RendezVous Service is not started. Please do this first";

	private ServiceEventDispatcher dispatcher;

	public RendezVousComponent( RendezVousService module ) {
		super( null);//module );
	}

	
	/* (non-Javadoc)
	 * @see net.osgi.jxse.service.network.IRendezVousComponent#getProperty(net.osgi.jxse.service.network.RendezVousServiceComponent.RendezVousServiceProperties)
	 */
	@Override
	public Object getProperty( RendezVousServiceProperties key) {
		if( super.getModule() == null )
			return super.getPropertySource().getProperty(key);
		RendezVousService service = super.getModule();
		switch( key ){
		case STATUS:
			return super.getStatus();
		case IS_RENDEZVOUS:
			return service.isRendezVous();
		case IS_CONNECTED_TO_RENDEZVOUS:
			return service.isConnectedToRendezVous();
		case RENDEZVOUS_STATUS:
			return service.getRendezVousStatus();
		default:
			return super.getPropertySource().getProperty(key);
		}
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.service.network.IRendezVousComponent#putProperty(net.osgi.jxse.service.network.RendezVousServiceComponent.RendezVousServiceProperties, java.lang.Object)
	 */
	@Override
	public void putProperty( RendezVousServiceProperties key, Object value) {
		if(( key == null ) || ( value == null ))
			return;
		if( super.getModule() == null ){
			super.putProperty(key, value );
			return;
		}
		RendezVousService service = super.getModule();
		switch( key ){
		case AUTO_START:
			service.setAutoStart( (boolean) value );
		default:
			super.putProperty(key, value );
		}
	}

	@Override
	protected void activate() {
		dispatcher = ServiceEventDispatcher.getInstance();
		super.activate();
	}

	@Override
	protected void deactivate() {
		dispatcher = null;
		super.getModule().stopApp();
	}

	@Override
	public void rendezvousEvent(RendezvousEvent event) {
		if( dispatcher == null ){
			Logger log = Logger.getLogger( this.getClass().getName() );
			log.log( Jp2pLevel.JP2PLEVEL, S_ERR_SERVICE_NOT_STARTED );
		}
	}
}
