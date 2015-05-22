package net.jp2p.chaupal.jxta.platform.configurator;

import java.io.File;
import java.net.URI;
import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource;
import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jxta.peer.PeerID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager.ConfigMode;

public class NetworkConfigurationPropertySource extends AbstractJp2pWritePropertySource
	implements IJp2pWritePropertySource<IJp2pProperties>

{
	public enum NetworkConfiguratorProperties implements IJp2pProperties{
		DESCRIPTION,
		HOME,
		CONFIG_MODE,
		NAME,
		PEER_ID,
		STORE_HOME;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		/**
		 * Returns true if the given property is valid for this enumeration
		 * @param property
		 * @return
		 */
		public static boolean isValidProperty( IJp2pProperties property ){
			if( property == null )
				return false;
			for( NetworkConfiguratorProperties prop: values() ){
				if( prop.equals( property ))
					return true;
			}
			return false;
		}

		public static NetworkConfiguratorProperties convertTo( String str ){
			String enumStr = StringStyler.styleToEnum( str );
			return valueOf( enumStr );
		}
	}

	/**
	 * supported directives
	 * @author Kees
	 *
	 */
	public enum NetworkConfigurationDirectives implements IJp2pDirectives{
		CLEAR_CONFIG;

		public static boolean isValidDirective( String str ){
			if( Utils.isNull( str ))
				return false;
			for( NetworkConfigurationDirectives dir: values() ){
				if( dir.name().equals( str ))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public NetworkConfigurationPropertySource( NetworkManagerPropertySource nmps ) {
		super( JxtaPlatformComponents.NETWORK_CONFIGURATOR.toString(), nmps );
		this.fill();
	}

	private void fill(){
		NetworkManagerPropertySource source = (NetworkManagerPropertySource) super.getParent();
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		NetworkConfiguratorProperties nmp = null;
		while( iterator.hasNext() ){
			IJp2pProperties cp = iterator.next();
			nmp = convertTo( cp );
			if( nmp != null ){
				Object value = source.getProperty( cp );
				super.setProperty(nmp, value, true);
			}
		}
		//if( Utils.isNull( (String) super.getProperty( NetworkConfiguratorProperties.SECURITY_8PRINCIPAL )))
		//	super.setProperty( NetworkConfiguratorProperties.SECURITY_8PRINCIPAL, getBundleId(source) );
		//super.setProperty( NetworkConfiguratorProperties.TCP_8ENABLED, source.isEnabled() );
		//super.setProperty( NetworkConfiguratorProperties.HTTP_8ENABLED, true );
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	public static final void fillNetworkConfigurator( NetworkConfigurationPropertySource source, NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext() ){
			NetworkConfiguratorProperties property = ( NetworkConfiguratorProperties ) iterator.next();
			switch( property ){
			case STORE_HOME:
				configurator.setStoreHome((URI) source.getProperty( property ));
				break;
			case PEER_ID:
				configurator.setPeerID(( PeerID ) source.getProperty( property ));
				break;
			case DESCRIPTION:
				configurator.setDescription(( String )source.getProperty( property ));
				break;
			case NAME:
				configurator.setName(( String ) source.getProperty( property ));
				break;
			case HOME:
				configurator.setHome( (File ) source.getProperty( property ));
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return false;
	}

	public static boolean isClearConfig( NetworkConfigurationPropertySource source ){
		String clearConfig = source.getDirective( NetworkConfigurationDirectives.CLEAR_CONFIG );
		return ( Utils.isNull( clearConfig ) ? Boolean.TRUE: Boolean.parseBoolean( clearConfig ));
	}
	
	/**
	 * convert the given context property to a networkManagerProperty, or null if there is
	 * no relation between them
	 * @param context
	 * @return
	 */
	public static NetworkManagerProperties convertFrom( NetworkConfiguratorProperties context ){
		switch( context ){
		case NAME:
			return NetworkManagerProperties.INSTANCE_NAME;
		case HOME:
			return NetworkManagerProperties.INSTANCE_HOME;
		case PEER_ID:
			return NetworkManagerProperties.PEER_ID;
		default:
			break;
		}
		return null;
	}

	/**
	 * convert the given context property to a networkManagerProperty, or null if there is
	 * no relation between them
	 * @param context
	 * @return
	 */
	public static NetworkConfiguratorProperties convertTo( IJp2pProperties props ){
		if(!( props instanceof NetworkManagerProperties ))
			return null;
		NetworkManagerProperties id = (NetworkManagerProperties) props;
		switch( id ){
		case INSTANCE_NAME:
			return NetworkConfiguratorProperties.NAME;			
		case INSTANCE_HOME:
			return NetworkConfiguratorProperties.STORE_HOME;
		case PEER_ID:
			return NetworkConfiguratorProperties.PEER_ID;
		default:
			break;
		}
		return null;
	}

	/**
	 * Get the config modes as string
	 * @return
	 */
	public static final String[] getConfigModes(){
		ConfigMode[] modes = ConfigMode.values();
		String[] results = new String[ modes.length];
		for( int i=0; i<modes.length; i++ ){
			ConfigMode mode = modes[i];
			results[i] = mode.toString();
		}
		return results;
	}

	private class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public NetworkConfiguratorProperties getIdFromString(String key) {
			return NetworkConfiguratorProperties.valueOf( key );
		}

		@Override
		public Object convertTo(IJp2pProperties id, String value) {
			NetworkConfiguratorProperties property = ( NetworkConfiguratorProperties )id;
			switch( property ){
			case CONFIG_MODE:
				String str = StringStyler.styleToEnum( value );
				return ConfigMode.valueOf( str );
			case PEER_ID:
				return PeerID.create( URI.create( value ));
			case HOME:
				return new File( value );
			default:
				break;
			}
			return super.convertTo(id, value);
		}		
	}
}
