/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.pipe;

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
import net.jp2p.jxta.pipe.PipePropertySource.PipeServiceProperties;
import net.jp2p.jxta.pipe.PipePropertySource.PipeServiceTypes;
import net.jxta.document.AdvertisementFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.protocol.PipeAdvertisement;

public class PipeAdvertisementPropertySource extends AdvertisementPropertySource{
	
	/**
	 * Properties specific for module spec services
	 * @author Kees
	 *
	 */
	public enum PipeAdvertisementProperties implements IJp2pProperties{
		DESCRIPTION,
		TYPE,
		PIPE_ID;

		public static boolean isValidProperty( String str ){
			if( Utils.isNull( str ))
				return false;
			for( PipeAdvertisementProperties dir: values() ){
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

	public PipeAdvertisementPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( AdvertisementTypes.PIPE, parent);
	}

	@Override
	protected void fillDefaultValues( IJp2pPropertySource<IJp2pProperties> parent, AdvertisementTypes type ) {
		super.fillDefaultValues(parent, type );
		String name = super.getParent().getDirective( Directives.NAME );
		super.setDirective( Directives.NAME, name);
		super.setProperty( AdvertisementProperties.NAME, name);
		if(Utils.isNull( name )){
			name = (String) super.getParent().getProperty( ModuleImplProperties.CODE );
		}
		if(!Utils.isNull( name )){
			this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, ModuleImplProperties.CODE, name ));
		}
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return PipeAdvertisementProperties.isValidProperty(id.toString());	
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
			if( PipeAdvertisementProperties.isValidProperty(key))
				return PipeAdvertisementProperties.valueOf(key);
			return super.getIdFromString(key);
		}
	}

	/**
	 * Create a pipe advertisement
	 * @return
	 * @throws URISyntaxException 
	 */
	public static PipeAdvertisement createPipeAdvertisement( IJp2pWritePropertySource<IJp2pProperties> source, PeerGroup peergroup ) throws URISyntaxException{
		PipeAdvertisementPreferences preferences = new PipeAdvertisementPreferences( source, peergroup );
		PipeAdvertisement pipead = ( PipeAdvertisement )AdvertisementFactory.newAdvertisement( AdvertisementTypes.convertTo( AdvertisementTypes.PIPE ));
		String name = (String) source.getProperty( AdvertisementProperties.NAME );
		pipead.setName(name);
		PipeServiceTypes type = ( PipeServiceTypes )source.getProperty( PipeAdvertisementProperties.TYPE );
		if( type == null )
			type = PipeServiceTypes.UNICAST;
		pipead.setType( PipeServiceTypes.convert( type ));
		pipead.setDescription(( String )source.getProperty( PipeAdvertisementProperties.DESCRIPTION ));
		
		PipeID pipeID = null;
		Object value = source.getProperty(PipeServiceProperties.PIPE_ID );
		if( value instanceof String ){
			pipeID = (PipeID)preferences.convertTo( PipeServiceProperties.PIPE_ID, (String) value );
			source.setProperty(PipeServiceProperties.PIPE_ID, pipeID );
		}else
			pipeID = (PipeID) value;
		pipead.setPipeID( pipeID );
		return pipead;
	}	
}