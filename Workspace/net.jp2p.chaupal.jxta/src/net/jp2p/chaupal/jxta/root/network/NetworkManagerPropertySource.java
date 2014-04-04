package net.jp2p.chaupal.jxta.root.network;

import java.util.Iterator;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.IJp2pContainer.ContainerProperties;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.ProjectFolderUtils;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaNetworkComponents;

public class NetworkManagerPropertySource extends AbstractJp2pWritePropertySource
	implements IJp2pWritePropertySource<IJp2pProperties>
{
	/**
	 * Supported properties
	 * @author Kees
	 *
	 */
	public enum NetworkManagerProperties implements IJp2pProperties{
		CONFIG_PERSISTENT,
		INFRASTRUCTURE_ID,
		INSTANCE_HOME,
		INSTANCE_NAME,
		CONFIG_MODE,
		PEER_ID;
	
		/**
		 * Returns true if the given property is valid for this enumeration
		 * @param property
		 * @return
		 */
		public static boolean isValidProperty( IJp2pProperties property ){
			if( property == null )
				return false;
			for( NetworkManagerProperties prop: values() ){
				if( prop.equals( property ))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	/**
	 * supported directives
	 * @author Kees
	 *
	 */
	public enum NetworkManagerDirectives implements IJp2pDirectives{
		CLEAR_CONFIG;

		public static boolean isValidDirective( String str ){
			if( Utils.isNull( str ))
				return false;
			for( NetworkManagerDirectives dir: values() ){
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

	public NetworkManagerPropertySource( Jp2pContainerPropertySource parent) {
		super( parent.getComponentName(), parent );
		this.fill( parent );
	}

	@Override
	public String getComponentName() {
		return JxtaNetworkComponents.NETWORK_MANAGER.toString();
	}

	private void fill( Jp2pContainerPropertySource parent ){
		//Get the name from the various options
		String name = (String) super.getProperty( NetworkManagerProperties.INSTANCE_NAME );
		if( Utils.isNull( name )){
			name=  parent.getDirective( Directives.NAME );
			if( Utils.isNull( name )){
				name = AbstractJp2pPropertySource.getIdentifier(parent);
			}else{
				this.setDirective( Directives.NAME, name );
			}
		}	
		super.setProperty(NetworkManagerProperties.INSTANCE_NAME, name);
		
		//Determine the properties and directives to take over from the parent 
		this.setDirective( Directives.AUTO_START, parent.getDirective( Directives.AUTO_START ));
		this.setDirective( Directives.CLEAR, parent.getDirective( Directives.CLEAR ));
		Iterator<IJp2pProperties> iterator = parent.propertyIterator();

		while( iterator.hasNext() ){
			IJp2pProperties cp =  iterator.next();
			IJp2pProperties nmp = convertFrom( cp );
			if( nmp == null )
				continue;
			Object retval = parent.getProperty( cp );
			if( NetworkManagerProperties.INSTANCE_HOME.equals(nmp ) && ( retval instanceof String ))
				retval = ProjectFolderUtils.getParsedUserDir((String) retval, getBundleId( this ));
			super.setProperty(nmp, retval, true);
		}
	}
	
	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		if( NetworkManagerDirectives.isValidDirective( id.name() ))
			return super.setDirective( NetworkManagerDirectives.valueOf( id.name() ), value );
		return super.setDirective(id, value);
	}

	@Override
	public NetworkManagerProperties getIdFromString(String key) {
		return NetworkManagerProperties.valueOf( key );
	}

	@Override
	public Object getDefault( IJp2pProperties id) {
		Jp2pContainerPropertySource source = (Jp2pContainerPropertySource) super.getParent();
		return source.getDefault( convertTo( id ));
	}
	
	@Override
	public boolean validate( IJp2pProperties id, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * convert the given context property to a networkManagerProperty, or null if there is
	 * no relation between them
	 * @param context
	 * @return
	 */
	public IJp2pProperties convertFrom( IJp2pProperties context ){
		if(!( context instanceof ContainerProperties ))
			return context;
		ContainerProperties key = (ContainerProperties) context;
		switch( key ){
		case HOME_FOLDER:
			return NetworkManagerProperties.INSTANCE_HOME;
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
	public ContainerProperties convertTo( IJp2pProperties id ){
		if(!( id instanceof IJp2pProperties ))
			return null;
		NetworkManagerProperties props = (NetworkManagerProperties) id;
		switch( props ){
		case INSTANCE_HOME:
			return ContainerProperties.HOME_FOLDER;
		default:
			break;
		}
		return null;
	}
}
