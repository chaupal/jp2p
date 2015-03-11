package net.jp2p.chaupal.jxta.context;

import net.jp2p.chaupal.jxta.IChaupalComponents.ChaupalComponents;
import net.jp2p.chaupal.jxta.advertisement.ChaupalAdvertisementFactory;
import net.jp2p.chaupal.jxta.discovery.ChaupalDiscoveryServiceFactory;
import net.jp2p.chaupal.jxta.network.http.HttpServiceFactory;
import net.jp2p.chaupal.jxta.peergroup.ChaupalPeerGroupFactory;
import net.jp2p.chaupal.jxta.persistence.OsgiPersistenceFactory;
import net.jp2p.chaupal.jxta.pipe.ChaupalPipeFactory;
import net.jp2p.container.context.AbstractJp2pServiceBuilder;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.discovery.DiscoveryPreferences;
import net.jp2p.jxta.network.NetworkManagerPreferences;
import net.jp2p.jxta.peergroup.PeerGroupPreferences;

public class ChaupalBuilder extends AbstractJp2pServiceBuilder {

	public ChaupalBuilder() {
		super(Contexts.CHAUPAL.toString());
	}

	
	@Override
	protected void prepare() {
		super.addFactory( new ChaupalDiscoveryServiceFactory());
		super.addFactory( new ChaupalPipeFactory());
		super.addFactory( new ChaupalPeerGroupFactory());
		super.addFactory( new OsgiPersistenceFactory());
		super.addFactory( new HttpServiceFactory());
	}


	/**
	 * Get the supported services
	 */
	@Override
	public String[] getSupportedServices() {
		ChaupalComponents[] components = ChaupalComponents.values();
		String[] names = new String[ components.length ];
		for( int i=0; i<components.length; i++ )
			names[i] = components[i].toString();
		return names;
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
		if( !ChaupalComponents.isComponent( comp ))
			return null;
		ChaupalComponents component = ChaupalComponents.valueOf(comp);
		IPropertyConvertor<String, Object> convertor = null;
		switch( component ){
			case NET_PEERGROUP_SERVICE:
			convertor = new NetworkManagerPreferences( (IJp2pWritePropertySource<IJp2pProperties>) source );
			break;			
		case DISCOVERY_SERVICE:
			convertor = new DiscoveryPreferences( (IJp2pWritePropertySource<IJp2pProperties>) source );
			break;			
		case PEERGROUP_SERVICE:
			convertor = new PeerGroupPreferences( (IJp2pWritePropertySource<IJp2pProperties>) source );
			break;			
		case ADVERTISEMENT_SERVICE:
			//factory = new Jp2pAdvertisementFactory<Advertisement>( builder, parentSource );
			break;
		default:
			break;
		}
		if( convertor != null )
			return convertor;
		return null;
	}
}
