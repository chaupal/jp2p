package net.jp2p.jxta.rendezvous;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.jp2p.container.properties.AbstractPropertyFacade;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.jxta.rendezvous.RendezVousPropertySource.RendezVousProperties;
import net.jxta.rendezvous.RendezVousService;

public class RendezVousPropertyFacade extends AbstractPropertyFacade<RendezVousService> {

	public RendezVousPropertyFacade( String bundleId, RendezVousService rdv) {
		super( bundleId, rdv );
	}

	@Override
	public Object getProperty( IJp2pProperties id) {
		RendezVousService rdv = super.getModule();
		if(!( id instanceof RendezVousProperties ))
			return null;
		RendezVousProperties property  = ( RendezVousProperties )id;
		switch( property ){
		case IMPL_ADVERTISEMENT:
			return rdv.getImplAdvertisement();
		case IS_CONNECTED_TO_RDV:
			return rdv.isConnectedToRendezVous();
		case IS_RDV:
			return rdv.isRendezVous();
		case LOCAL_EDGE_VIEW:
			return rdv.getLocalEdgeView();
		case LOCAL_RDV_VIEW:
			return rdv.getLocalRendezVousView();
		case RDV_STATUS:
			return rdv.getRendezVousStatus();
		}
		return null;
	}

	@Override
	public Iterator<IJp2pProperties> propertyIterator() {
		Collection<IJp2pProperties> results = new ArrayList<IJp2pProperties>();
		for( RendezVousProperties nmp: RendezVousProperties.values())
			results.add( nmp );
		return results.iterator();
	}
}
