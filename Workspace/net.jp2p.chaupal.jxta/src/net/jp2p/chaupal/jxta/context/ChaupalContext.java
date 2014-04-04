package net.jp2p.chaupal.jxta.context;

import net.jp2p.chaupal.jxta.IChaupalComponents.ChaupalComponents;
import net.jp2p.chaupal.jxta.advertisement.ChaupalAdvertisementFactory;
import net.jp2p.chaupal.jxta.discovery.ChaupalDiscoveryServiceFactory;
import net.jp2p.chaupal.jxta.peergroup.ChaupalPeerGroupFactory;
import net.jp2p.chaupal.jxta.persistence.OsgiPersistenceFactory;
import net.jp2p.chaupal.jxta.pipe.ChaupalPipeFactory;
import net.jp2p.chaupal.jxta.root.network.NetworkManagerPreferences;
import net.jp2p.chaupal.jxta.root.network.http.HttpServiceFactory;
import net.jp2p.container.context.IJp2pContext;
import net.jp2p.container.context.Jp2pContext;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.container.xml.IJp2pHandler;
import net.jp2p.jxta.discovery.DiscoveryPreferences;
import net.jp2p.jxta.peergroup.PeerGroupPreferences;

public class ChaupalContext implements IJp2pContext {

	@Override
	public String getName() {
		return Contexts.CHAUPAL.toString();
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
	 * Returns true if the given component name is valid for this context
	 * @param componentName
	 * @return
	 */
	@Override
	public boolean isValidComponentName( String contextName, String componentName ){
		if( !Utils.isNull( contextName ) && !Jp2pContext.isContextNameEqual(Contexts.CHAUPAL.toString(), contextName ))
			return false;
		if( ChaupalComponents.isComponent( componentName ))
			return true;
		JxtaContext jc = new JxtaContext();
		return jc.isValidComponentName( Contexts.JXTA.toString(), componentName);
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

	/**
	 * Change the factory to a Chaupal factory if required and available
	 * @param factory
	 * @return
	 */
	@Override
	public IPropertySourceFactory getFactory( String componentName ){
		IPropertySourceFactory factory = null;
		String str = StringStyler.styleToEnum( componentName );
		if(ChaupalComponents.isComponent(str)){
			ChaupalComponents comp = ChaupalComponents.valueOf(str );
			switch( comp ){
			case ADVERTISEMENT_SERVICE:
				//AdvertisementTypes adType = 
				//AdvertisementTypes.convertFrom( attributes.getValue( AdvertisementDirectives.TYPE.toString().toLowerCase() ));
				//TODO removefactory = new ChaupalAdvertisementFactory<Advertisement>( builder, adType, parentSource );
				break;
			case DISCOVERY_SERVICE:
				factory = new ChaupalDiscoveryServiceFactory();
				break;
			case PIPE_SERVICE:
				factory = new ChaupalPipeFactory();
				break;
			case PEERGROUP_SERVICE:
				factory = new ChaupalPeerGroupFactory( );
				break;
			case PERSISTENCE_SERVICE:
				factory = new OsgiPersistenceFactory();
				break;
			case HTTP_SERVICE:
				factory = new HttpServiceFactory();
				break;
			default:
				break;
			}
		}
		if( factory != null )
			return factory;
		JxtaContext jc = new JxtaContext();
		if( jc.isValidComponentName( Contexts.JXTA.toString(), componentName))
			factory = jc.getFactory( componentName);
		return factory;

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
			return new JxtaContext().getConvertor(source);
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
		return new JxtaContext().getConvertor(source);
	}

	@Override
	public IJp2pHandler getHandler() {
		// TODO Auto-generated method stub
		return null;
	}
}
