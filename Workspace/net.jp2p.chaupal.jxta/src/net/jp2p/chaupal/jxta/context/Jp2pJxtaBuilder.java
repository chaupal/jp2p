package net.jp2p.chaupal.jxta.context;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.chaupal.jxta.advertisement.ChaupalAdvertisementFactory;
import net.jp2p.chaupal.jxta.discovery.ChaupalDiscoveryServiceFactory;
import net.jp2p.chaupal.jxta.pipe.ChaupalPipeFactory;
import net.jp2p.chaupal.jxta.service.Component;
import net.jp2p.container.context.AbstractJp2pServiceBuilder;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.advertisement.AdvertisementPreferences;
import net.jp2p.jxta.advertisement.service.JxtaAdvertisementFactory;
import net.jp2p.jxta.discovery.DiscoveryPreferences;
import net.jp2p.jxta.discovery.DiscoveryServiceFactory;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.netpeergroup.NetPeerGroupFactory;
import net.jp2p.jxta.network.NetworkManagerPreferences;
import net.jp2p.jxta.peergroup.PeerGroupFactory;
import net.jp2p.jxta.peergroup.PeerGroupPreferences;
import net.jp2p.jxta.pipe.PipeServiceFactory;
import net.jp2p.jxta.registration.RegistrationServiceFactory;
import net.jp2p.jxta.rendezvous.RendezVousFactory;
import net.jp2p.jxta.socket.SocketFactory;
import net.jp2p.jxta.socket.SocketPreferences;

public class Jp2pJxtaBuilder extends AbstractJp2pServiceBuilder {

	
	public Jp2pJxtaBuilder() {
		super( Contexts.JXTA.toString());
	}

	
	@Override
	protected void prepare() {
		super.addFactory( new NetPeerGroupFactory());
		super.addFactory(  new PipeServiceFactory());
		super.addFactory(  new RegistrationServiceFactory());
		super.addFactory(  new DiscoveryServiceFactory());
		super.addFactory(  new PeerGroupFactory());
		super.addFactory(  new JxtaAdvertisementFactory());
		super.addFactory(  new SocketFactory());
		super.addFactory(  new RendezVousFactory());
	}


	/**
	 * Get the supported services
	 */
	@Override
	public String[] getSupportedServices() {
		JxtaComponents[] components = JxtaComponents.values();
		Collection<String> names = new ArrayList<String>();
		for( int i=0; i<components.length; i++ )
			if( Component.canBuild( components[i])){
				names.add( components[i].toString() );
		}
		return names.toArray( new String[ names.size() ]);
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
	
	/**
	 * Get the default factory for this container
	 * @param parent
	 * @param componentName
	 * @return
	 */
	@Override
	public IPropertyConvertor<String, Object> getConvertor( IJp2pPropertySource<IJp2pProperties> source ){
		String comp = StringStyler.styleToEnum( source.getComponentName());
		if( !JxtaComponents.isComponent( comp ))
			return null;
		JxtaComponents component = JxtaComponents.valueOf(comp);
		IPropertyConvertor<String, Object> convertor = null;
		switch( component ){
		case ADVERTISEMENT:
			convertor = new AdvertisementPreferences(( IJp2pWritePropertySource<IJp2pProperties>) source );
			break;
		case PIPE_SERVICE:
			break;
		case NET_PEERGROUP_SERVICE:
			convertor = new NetworkManagerPreferences(( IJp2pWritePropertySource<IJp2pProperties>) source );
			break;			
		case DISCOVERY_SERVICE:
			convertor = new DiscoveryPreferences((IJp2pWritePropertySource<IJp2pProperties>) source );
			break;			
		case PEERGROUP_SERVICE:
			convertor = new PeerGroupPreferences(( IJp2pWritePropertySource<IJp2pProperties>) source );
			break;			
		case JXSE_SOCKET_SERVICE:
			convertor = new SocketPreferences(( IJp2pWritePropertySource<IJp2pProperties>) source );
			break;
		case REGISTRATION_SERVICE:
			break;
		default:
			break;
		}
		return convertor;
	}
}
