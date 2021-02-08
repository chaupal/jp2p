package net.jp2p.chaupal.jxse.context;

import net.jp2p.chaupal.jxse.advertisement.ChaupalAdvertisementFactory;
import net.jp2p.chaupal.jxse.discovery.ChaupalDiscoveryServiceFactory;
import net.jp2p.chaupal.jxse.pipe.ChaupalPipeFactory;
import net.jp2p.container.context.AbstractJp2pServiceBuilder;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.jxta.advertisement.service.JxtaAdvertisementFactory;
import net.jp2p.jxta.discovery.DiscoveryServiceFactory;
import net.jp2p.jxta.peergroup.PeerGroupFactory;
import net.jp2p.jxta.pipe.PipeServiceFactory;
import net.jp2p.jxta.registration.RegistrationServiceFactory;
import net.jp2p.jxta.rendezvous.RendezVousFactory;
import net.jp2p.jxta.socket.SocketFactory;

public class Jp2pJxtaBuilder extends AbstractJp2pServiceBuilder {

	
	public Jp2pJxtaBuilder() {
		super( Contexts.JXTA.toString());
	}

	
	@Override
	protected void prepare() {
		super.addFactory(  new PipeServiceFactory());
		super.addFactory(  new RegistrationServiceFactory());
		super.addFactory(  new DiscoveryServiceFactory());
		super.addFactory(  new PeerGroupFactory());
		super.addFactory(  new JxtaAdvertisementFactory());
		super.addFactory(  new SocketFactory());
		super.addFactory(  new RendezVousFactory());
	}

	/**
	 * Returns true if the given factory is a chaupal factory
	 * @param factory
	 * @return
	 */
	public boolean isChaupalFactory( IComponentFactory<?> factory ){
		if( factory instanceof ChaupalAdvertisementFactory )
			return true;
		if( factory instanceof ChaupalDiscoveryServiceFactory )
			return true;
		return ( factory instanceof ChaupalPipeFactory );
	}

	@Override
	public Object createValue( String componentName, IJp2pProperties id ){
		return null;
	}
}
