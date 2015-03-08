package net.jp2p.chaupal.jxta.context;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.chaupal.jxta.advertisement.ChaupalAdvertisementFactory;
import net.jp2p.chaupal.jxta.discovery.ChaupalDiscoveryServiceFactory;
import net.jp2p.chaupal.jxta.pipe.ChaupalPipeFactory;
import net.jp2p.chaupal.jxta.service.Component;
import net.jp2p.container.context.IJp2pContext;
import net.jp2p.container.context.Jp2pContext;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.container.xml.IJp2pHandler;
import net.jp2p.jxta.advertisement.AdvertisementPreferences;
import net.jp2p.jxta.discovery.DiscoveryPreferences;
import net.jp2p.jxta.factory.JxtaFactoryUtils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.network.NetworkManagerPreferences;
import net.jp2p.jxta.peergroup.PeerGroupPreferences;
import net.jp2p.jxta.socket.SocketPreferences;

public class JxtaContext implements IJp2pContext {

	@Override
	public String getName() {
		return Contexts.JXTA.toString();
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
	 * Returns true if the given component name is valid for this context
	 * @param componentName
	 * @return
	 */
	@Override
	public boolean isValidComponentName( String contextName, String componentName ){
		if( !Utils.isNull( contextName ) && !Jp2pContext.isContextNameEqual(Contexts.JXTA.toString(), contextName ))
			return false;
		return JxtaComponents.isComponent( componentName );
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
		IPropertySourceFactory factory = JxtaFactoryUtils.getDefaultFactory( componentName);
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

	@Override
	public IJp2pHandler getHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(IJp2pContext o) {
		return this.getName().compareTo( o.getName());
	}

}
