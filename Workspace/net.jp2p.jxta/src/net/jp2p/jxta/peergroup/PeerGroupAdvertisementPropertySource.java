/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.peergroup;

import java.net.URISyntaxException;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.advertisement.ModuleImplAdvertisementPropertySource.ModuleImplProperties;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupProperties;
import net.jxta.document.AdvertisementFactory;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;

public class PeerGroupAdvertisementPropertySource extends AdvertisementPropertySource{
	
	/**
	 * Properties specific for module spec services
	 * @author Kees
	 *
	 */
	public enum PeerGroupAdvertisementProperties implements IJp2pProperties{
		DESCRIPTION,
		TYPE,
		PIPE_ID;
	
		public static boolean isValidProperty( String str ){
			if( Utils.isNull( str ))
				return false;
			for( PeerGroupAdvertisementProperties dir: values() ){
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

	public PeerGroupAdvertisementPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( AdvertisementTypes.PEERGROUP, parent);
	}

	protected void fillDefaultValues( IJp2pPropertySource<IJp2pProperties> parent, AdvertisementTypes type ) {
		super.fillDefaultValues(parent, type );
		String name = super.getParent().getDirective( Directives.NAME );
		if(Utils.isNull( name )){
			name = (String) super.getParent().getProperty( ModuleImplProperties.CODE );
		}
		if(!Utils.isNull( name )){
			this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, ModuleImplProperties.CODE, name ));
		}
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return PeerGroupAdvertisementProperties.isValidProperty(id.toString());	
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
		public IJp2pProperties getIdFromString(String key) {
			if( PeerGroupAdvertisementProperties.isValidProperty(key))
				return PeerGroupAdvertisementProperties.valueOf(key);
			return super.getIdFromString(key);
		}
	}

	/**
	 * Create a pipe advertisement
	 * @return
	 * @throws URISyntaxException 
	 */
	public static PeerGroupAdvertisement createPeerGroupAdvertisement( IJp2pPropertySource<IJp2pProperties> source, ModuleSpecAdvertisement msadv ) throws URISyntaxException{
		AdvertisementTypes type = AdvertisementTypes.convertFrom((String) source.getDirective( AdvertisementDirectives.TYPE ));
		PeerGroupAdvertisement pgad = ( PeerGroupAdvertisement )AdvertisementFactory.newAdvertisement( AdvertisementTypes.convertTo(type));
		String name = (String) source.getProperty( AdvertisementProperties.NAME );
		pgad.setName(name);
		pgad.setModuleSpecID( msadv.getModuleSpecID());
		pgad.setDescription(( String )source.getProperty( PeerGroupProperties.DESCRIPTION ));
		PeerGroupPreferences preferences = new PeerGroupPreferences((IJp2pWritePropertySource<IJp2pProperties>) source); 
		pgad.setPeerGroupID(preferences.getPeerGroupID());
		return pgad;
	}

	
}