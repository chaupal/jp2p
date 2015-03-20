package net.jp2p.jxta.rendezvous;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;

public class RendezVousPropertySource extends AbstractJp2pWritePropertySource
	implements IJp2pWritePropertySource<IJp2pProperties>
{
	/**
	 * Supported properties
	 * @author Kees
	 *
	 */
	public enum RendezVousProperties implements IJp2pProperties{
		IMPL_ADVERTISEMENT,
		LOCAL_EDGE_VIEW,
		LOCAL_RDV_VIEW,
		RDV_STATUS,
		IS_RDV,
		IS_CONNECTED_TO_RDV;
	
		/**
		 * Returns true if the given property is valid for this enumeration
		 * @param property
		 * @return
		 */
		public static boolean isValidProperty( IJp2pProperties property ){
			if( property == null )
				return false;
			for( RendezVousProperties prop: values() ){
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

	public RendezVousPropertySource( PeerGroupPropertySource parent) {
		super( parent.getComponentName(), parent );
		this.fill( parent );
	}

	@Override
	public String getComponentName() {
		return JxtaComponents.RENDEZVOUS_SERVICE.toString();
	}

	private void fill( PeerGroupPropertySource parent ){
		//Determine the properties and directives to take over from the parent 
		this.setDirective( Directives.AUTO_START, parent.getDirective( Directives.AUTO_START ));
	}
	
	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		if(( id == null ) || ( !IJp2pDirectives.Directives.AUTO_START.equals( id )))
				return false;
		return super.setDirective(id, value);
	}

	@Override
	public boolean validate( IJp2pProperties id, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	private static class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public RendezVousProperties getIdFromString(String key) {
			return RendezVousProperties.valueOf( key );
		}
		
	}
	
}
