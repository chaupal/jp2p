/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.advertisement;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupProperties;
import net.jxta.document.AdvertisementFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.ModuleSpecID;
import net.jxta.protocol.ModuleImplAdvertisement;

public class ModuleImplAdvertisementPropertySource extends AdvertisementPropertySource{
	
	/**
	 * Properties specific for module spec services
	 * @author Kees
	 *
	 */
	public enum ModuleImplProperties implements IJp2pProperties{
		BASE_ADVERTISEMENT_TYPE,
		CODE,
		DESCRIPTION,
		MODULE_IMPL_ID,
		MODULE_SPEC_ID,
		PROVIDER,
		URI;
	
		public static boolean isValidProperty( String str ){
			if( Utils.isNull( str ))
				return false;
			for( ModuleImplProperties dir: values() ){
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

	public ModuleImplAdvertisementPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( AdvertisementTypes.MODULE_IMPL, parent);
	}

	protected void fillDefaultValues( IJp2pPropertySource<IJp2pProperties> parent, AdvertisementTypes type ) {
		super.fillDefaultValues( parent, type );
		String name = super.getParent().getDirective( Directives.NAME );
		if(Utils.isNull( name )){
			name = (String) super.getParent().getProperty( PeerGroupProperties.NAME );
		}
			
		if(!Utils.isNull( name ))		
			this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, ModuleImplProperties.CODE, name ));
		String description = (String) super.getParent().getProperty( ModuleImplProperties.DESCRIPTION );
		if(!Utils.isNull( description )){
			super.setProperty( ModuleImplProperties.DESCRIPTION, description );
		}
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		if( ModuleImplProperties.isValidProperty(key))
			return ModuleImplProperties.valueOf(key);
		return super.getIdFromString(key);
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return ModuleImplProperties.isValidProperty(id.toString());	
	}
	
	/**
	 * Create a module class advertisement
	 * @return
	 * @throws Exception 
	 */
	public static ModuleImplAdvertisement createModuleImplAdvertisement( IJp2pPropertySource<IJp2pProperties> source, PeerGroup peergroup ) throws Exception{
		if( source == null )
			return peergroup.getAllPurposePeerGroupImplAdvertisement();
		ModuleImplAdvertisement mcimpl = ( ModuleImplAdvertisement )AdvertisementFactory.newAdvertisement( AdvertisementTypes.convertTo( AdvertisementTypes.MODULE_IMPL ));
		mcimpl.setModuleSpecID( (ModuleSpecID) source.getProperty( ModuleImplProperties.MODULE_SPEC_ID ));
		mcimpl.setCode(( String )source.getProperty( ModuleImplProperties.CODE ));
		mcimpl.setDescription(( String )source.getProperty( ModuleImplProperties.DESCRIPTION ));
		mcimpl.setProvider(( String )source.getProperty( ModuleImplProperties.PROVIDER ));
		mcimpl.setUri(( String )source.getProperty( ModuleImplProperties.URI ));
		return mcimpl;
	}	
}