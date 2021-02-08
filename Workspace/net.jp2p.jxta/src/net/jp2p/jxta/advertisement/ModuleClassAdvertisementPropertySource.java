/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.advertisement;

import java.net.URISyntaxException;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.ModuleImplAdvertisementPropertySource.ModuleImplProperties;
import net.jxta.document.AdvertisementFactory;
import net.jxta.protocol.ModuleClassAdvertisement;

public class ModuleClassAdvertisementPropertySource extends AdvertisementPropertySource{
	
	/**
	 * Properties specific for module class services
	 * @author Kees
	 *
	 */
	public enum ModuleClassProperties implements IJp2pProperties{
		DESCRIPTION,
		MODULE_CLASS_ID;

		public static boolean isValidProperty( String str ){
			if( Utils.isNull( str ))
				return false;
			for( ModuleClassProperties dir: values() ){
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

	public ModuleClassAdvertisementPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( AdvertisementTypes.MODULE_CLASS, parent);
	}

	@Override
	protected void fillDefaultValues( IJp2pPropertySource<IJp2pProperties> parent, AdvertisementTypes type ) {
		super.fillDefaultValues(parent, type );
		String name = super.getParent().getDirective( Directives.NAME );
		if(!Utils.isNull( name )){
			this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, ModuleImplProperties.CODE, name ));
		}
		String description = (String) super.getParent().getProperty( ModuleImplProperties.DESCRIPTION );
		if(!Utils.isNull( description )){
			super.setProperty( ModuleImplProperties.DESCRIPTION, description );
		}
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return ModuleClassProperties.isValidProperty(id.toString());	
	}
	
	/**
	 * Create a module class advertisement
	 * @return
	 * @throws URISyntaxException 
	 */
	public static ModuleClassAdvertisement createModuleClassAdvertisement( IJp2pPropertySource<IJp2pProperties> source ) throws URISyntaxException{
		AdvertisementTypes type = AdvertisementTypes.convertFrom((String) source.getDirective( AdvertisementDirectives.TYPE ));
		ModuleClassAdvertisementPreferences preferences = new ModuleClassAdvertisementPreferences( (IJp2pWritePropertySource<IJp2pProperties>) source );
		ModuleClassAdvertisement mcad = ( ModuleClassAdvertisement )AdvertisementFactory.newAdvertisement( AdvertisementTypes.convertTo(type));
		String name = (String) source.getProperty( AdvertisementProperties.NAME );
		mcad.setName(name);
		mcad.setDescription(( String )source.getProperty( ModuleClassProperties.DESCRIPTION ));
		mcad.setModuleClassID( preferences.getModuleClassID());
		return mcad;
	}		
}