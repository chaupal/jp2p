package net.jp2p.chaupal.jxta.root.network.http;

import net.jp2p.chaupal.jxta.IChaupalComponents.ChaupalComponents;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;

public class HttpServicePropertySource extends AbstractJp2pWritePropertySource
	implements IJp2pWritePropertySource<IJp2pProperties>

{
	public enum TransportProperties implements IJp2pProperties{
		ENABLED,
		INCOMING_STATUS,
		INTERFACE_ADDRESS,
		OUTGOING_STATUS,
		PORT,
		PUBLIC_ADDRESS,
		PUBLIC_ADDRESS_EXCLUSIVE,
		START_PORT,
		END_PORT;
	
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
			for( TransportProperties prop: values() ){
				if( prop.equals( property ))
					return true;
			}
			return false;
		}

		public static TransportProperties convertTo( String str ){
			String enumStr = StringStyler.styleToEnum( str );
			return valueOf( enumStr );
		}
	}

	public HttpServicePropertySource( IJp2pPropertySource<IJp2pProperties> parent ) {
		super( ChaupalComponents.HTTP_SERVICE.toString(), parent );
		this.fill();
	}

	private void fill(){
		super.setProperty( TransportProperties.ENABLED, true );
		super.setDirective( Directives.CREATE, Boolean.TRUE.toString());
		super.setDirective( Directives.CONTEXT, Contexts.CHAUPAL.toString() );
	}

	@Override
	public TransportProperties getIdFromString(String key) {
		return TransportProperties.valueOf( key );
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return false;
	}
}
