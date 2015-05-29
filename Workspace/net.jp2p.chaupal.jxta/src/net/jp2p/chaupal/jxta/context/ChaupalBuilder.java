package net.jp2p.chaupal.jxta.context;

import net.jp2p.chaupal.jxta.advertisement.ChaupalAdvertisementFactory;
import net.jp2p.chaupal.jxta.discovery.ChaupalDiscoveryServiceFactory;
import net.jp2p.chaupal.jxta.network.http.HttpServiceFactory;
import net.jp2p.chaupal.jxta.peergroup.ChaupalPeerGroupFactory;
import net.jp2p.chaupal.jxta.pipe.ChaupalPipeFactory;
import net.jp2p.container.context.AbstractJp2pServiceBuilder;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;

public class ChaupalBuilder extends AbstractJp2pServiceBuilder {

	public ChaupalBuilder() {
		super(Contexts.CHAUPAL.toString());
	}

	
	@Override
	protected void prepare() {
		super.addFactory( new ChaupalDiscoveryServiceFactory());
		super.addFactory( new ChaupalPipeFactory());
		super.addFactory( new ChaupalPeerGroupFactory());
		super.addFactory( new HttpServiceFactory());
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
