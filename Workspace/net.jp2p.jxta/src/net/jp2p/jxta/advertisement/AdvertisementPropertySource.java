/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.advertisement;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public class AdvertisementPropertySource extends AbstractJp2pWritePropertySource {

	public enum AdvertisementTypes{
		ADV,
		PEERGROUP,
		PEER,
		MODULE_CLASS,
		MODULE_SPEC,
		MODULE_IMPL,
		PIPE;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		/**
		 * Convert the enum to a form that the jxta lib can understand
		 * @param adType
		 * @return
		 */
		public static String convert( AdvertisementTypes adType ){
			switch( adType ){
			case PEERGROUP:
				return PeerGroupAdvertisement.getAdvertisementType();
			case PIPE:
				return PipeAdvertisement.getAdvertisementType();
			case MODULE_CLASS:
				return ModuleClassAdvertisement.getAdvertisementType();
			case MODULE_SPEC:
				return ModuleSpecAdvertisement.getAdvertisementType();
			case MODULE_IMPL:
				return ModuleImplAdvertisement.getAdvertisementType();
			default:
				return Advertisement.getAdvertisementType();
			}
		}

		/**
		 * Convert the enum to a form that the jxta lib can understand
		 * @param adType
		 * @return
		 */
		public static int convertForDiscovery( AdvertisementTypes adType ){
			switch( adType ){
			case PEERGROUP:
				return DiscoveryService.GROUP;
			case PEER:
				return DiscoveryService.PEER;
			default:
				return DiscoveryService.ADV;
			}
		}

		/**
		 * Returns true if the given String type is contained in the enum
		*/
		public static boolean isValidType( String tp ){
			if( Utils.isNull(tp))
				return false;
			String type = StringStyler.styleToEnum(tp);
			for( AdvertisementTypes adtype: AdvertisementTypes.values() ){
				if( adtype.name().equals(type ))
					return true;
			}

			return false;
		}

		/**
		 * Concert from advertisement type 
		*/
		public static AdvertisementTypes convertFrom( String tp ){
			if( !isValidType( tp))
				return AdvertisementTypes.ADV;
			return AdvertisementTypes.valueOf( StringStyler.styleToEnum( tp ));
		}

		/**
		 * Concert from advertisement type 
		 */
		public static String convertTo( AdvertisementTypes advType ){
			switch( advType ){
			case PEERGROUP:
				return PeerGroupAdvertisement.getAdvertisementType();
			case PEER:
				return PeerAdvertisement.getAdvertisementType();
			case PIPE:
				return PipeAdvertisement.getAdvertisementType();
			case MODULE_CLASS: 
				return ModuleClassAdvertisement.getAdvertisementType();
			case MODULE_SPEC:
				return ModuleSpecAdvertisement.getAdvertisementType();
			case MODULE_IMPL:
				return ModuleImplAdvertisement.getAdvertisementType();
			default:
				return null;
			}
		}
	}

	public enum AdvertisementProperties implements IJp2pProperties{
		NAME;

		public static boolean isValidProperty( String str ){
			if( Utils.isNull( str ))
				return false;
			for( AdvertisementProperties dir: values() ){
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

	public enum AdvertisementDirectives implements IJp2pDirectives{
		PEERGROUP,
		TYPE;
	
		public static boolean isValidDirective( String directive ){
			if( Utils.isNull( directive ))
				return false;
			for( AdvertisementDirectives dir: values() ){
				if( dir.name().equals( directive ))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		/**
		 * Get the given directive as lowercase
		 * @param directive
		 * @return
		 */
		public String toLowerCase(){
			return toString().toLowerCase();
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

	public AdvertisementPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		this( JxtaComponents.ADVERTISEMENT.toString(), parent);
	}

	protected AdvertisementPropertySource(String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		this(componentName, AdvertisementTypes.ADV, parent);
	}

	protected AdvertisementPropertySource(AdvertisementTypes type, IJp2pPropertySource<IJp2pProperties> parent) {
		this( JxtaComponents.ADVERTISEMENT.toString(), type, parent );
	}

	protected AdvertisementPropertySource(String componentName, AdvertisementTypes type, IJp2pPropertySource<IJp2pProperties> parent) {
		super(componentName, parent);
		this.fillDefaultValues( parent, type );
	}

	protected void fillDefaultValues( IJp2pPropertySource<IJp2pProperties> parent, AdvertisementTypes type ){
		super.setDirective(Directives.CREATE, Boolean.FALSE.toString());
		super.setDirective( AdvertisementDirectives.TYPE, type.toString() );
}	

	@Override
	public boolean validate( IJp2pProperties id, Object value) {
		return true;
	}

	@Override
	public boolean addChild(IJp2pPropertySource<?> child) {
		if( !AdvertisementCategories.isValidCategory( StringStyler.styleToEnum( child.getComponentName() )))
			return false;
		return super.addChild(child);
	}
	
	/**
	 * Find the descendant advertisement with the given ad type.
	 * @param source
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static IJp2pPropertySource<IJp2pProperties> findAdvertisementDescendant( IJp2pPropertySource<IJp2pProperties> source, AdvertisementTypes type ){
		AdvertisementTypes adtype =null;
		if(( source == null ) || ( type == null ))
			return null;
		if( JxtaComponents.ADVERTISEMENT.toString().equals( source.getComponentName() )){
			adtype = AdvertisementTypes.convertFrom( source.getDirective( AdvertisementDirectives.TYPE ));
			if( type.equals(adtype ))
				return (IJp2pPropertySource<IJp2pProperties>) source;
		}
		IJp2pPropertySource<IJp2pProperties> retval;
		for( IJp2pPropertySource<?> child: source.getChildren() ){
			retval = findAdvertisementDescendant((IJp2pPropertySource<IJp2pProperties>) child, type);
			if( retval != null )
				return retval;
		}
		return null;
	}
	
	/**
	 * Get the correct property source from a given type
	 * @param parent
	 * @param adType
	 * @return
	 */
	public static IJp2pPropertySource<IJp2pProperties> getAdvertisementFromType( IJp2pPropertySource<IJp2pProperties> parent, AdvertisementTypes adType ){
		IJp2pPropertySource<IJp2pProperties> source;
		switch( adType ){
		case MODULE_SPEC:
			source = new ModuleSpecAdvertisementPropertySource(parent);
			break;
		case MODULE_CLASS:
			source = new ModuleClassAdvertisementPropertySource(parent);
			break;
		case MODULE_IMPL:
			source = new ModuleImplAdvertisementPropertySource(parent);
			break;
		default:
			source = new AdvertisementPropertySource(parent);
		}
		return source;
	}
}