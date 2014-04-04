package net.jp2p.chaupal.jxta.advertisement;

import net.jp2p.chaupal.jxta.IChaupalComponents.ChaupalComponents;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jxta.peergroup.PeerGroup;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;

public class AdvertisementServicePropertySource extends AdvertisementPropertySource {

	/**
	 * The scope of an advertisement determines whether it will be published or not
	 * @author keesp
	 *
	 */
	public enum Scope{
		INTERNAL,
		LOCAL,
		REMOTE;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	/**
	 * The mode gives clues on what to do with the service. either advertisements are only discovered,
	 * or they are (also) published
	 * @author Kees
	 *
	 */
	public enum AdvertisementMode{
		DISCOVERY,
		PUBLISH,
		DISCOVERY_AND_PUBLISH;

	@Override
	public String toString() {
		return StringStyler.prettyString( super.toString() );
	}}


	public enum AdvertisementServiceProperties implements IJp2pProperties{
		NAME,
		MODE,
		SCOPE,
		LIFE_TIME,
		EXPIRATION,
		DESCRIPTION;
	
		public static boolean isValidProperty( String str ){
			if( Utils.isNull( str ))
				return false;
			for( AdvertisementServiceProperties dir: values() ){
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

	/**
	 * The categories that are included as children
	 * @author Kees
	 *
	 */
	public enum AdvertisementCategories{
		BODY,
		ADVERTISEMENT,
		ADVERTISEMENT_SERVICE,
		DISCOVERY_SERVICE;

		public static boolean isValidCategory( String category ){
			if( Utils.isNull( category ))
				return false;
			for( AdvertisementCategories cat: values() ){
				if( cat.name().equals( category ))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public AdvertisementServicePropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( ChaupalComponents.ADVERTISEMENT_SERVICE.toString(), parent);
		this.fillDefaultValues( parent );
	}

	public AdvertisementServicePropertySource(String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		super(componentName, parent);
		this.fillDefaultValues( parent);
	}

	@Override
	protected void fillDefaultValues( IJp2pPropertySource<IJp2pProperties> parent ){
		super.setDirective(Directives.CREATE, Boolean.TRUE.toString());
		super.setDirective(AdvertisementDirectives.TYPE, parent.getDirective( AdvertisementDirectives.TYPE ));
		super.setDirective(AdvertisementDirectives.PEERGROUP, parent.getDirective( AdvertisementDirectives.PEERGROUP ));
		super.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, AdvertisementServiceProperties.LIFE_TIME, PeerGroup.DEFAULT_LIFETIME ));
		super.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, AdvertisementServiceProperties.EXPIRATION, PeerGroup.DEFAULT_EXPIRATION ));	
		super.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, AdvertisementServiceProperties.MODE, AdvertisementMode.DISCOVERY_AND_PUBLISH, true ));
		super.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, AdvertisementServiceProperties.SCOPE, Scope.REMOTE ));
	}
	
	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		if( AdvertisementDirectives.isValidDirective( id.name()))
			return super.setDirective(AdvertisementDirectives.valueOf( id.name()), value );
		return super.setDirective(id, value);
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		if( AdvertisementServiceProperties.isValidProperty(key))
			return AdvertisementServiceProperties.valueOf(key);
		return null;
	}

	@Override
	public boolean validate( IJp2pProperties id, Object value) {
		return AdvertisementServiceProperties.isValidProperty( id.toString());
	}

	/**
	 * Get the scope of the given source and present a default value when it is null;
	 * @param source
	 * @return
	 */
	public static Scope getScope( IJp2pPropertySource<IJp2pProperties> source ){
		Scope scope = (Scope) source.getProperty( AdvertisementServiceProperties.SCOPE );
		if( scope == null )
			return Scope.REMOTE;
		else
			return scope;
		
	}
}
