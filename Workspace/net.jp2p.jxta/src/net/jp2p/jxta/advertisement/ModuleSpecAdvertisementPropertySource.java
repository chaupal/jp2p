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
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.ModuleImplAdvertisementPropertySource.ModuleImplProperties;
import net.jxta.document.AdvertisementFactory;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public class ModuleSpecAdvertisementPropertySource extends AdvertisementPropertySource{
	
	/**
	 * Properties specific for module spec services
	 * @author Kees
	 *
	 */
	public enum ModuleSpecProperties implements IJp2pProperties{
		CREATOR,
		VERSION,
		DESCRIPTION,
		MODULE_SPEC_ID,
		SPEC_URI;
	
		public static boolean isValidProperty( String str ){
			if( Utils.isNull( str ))
				return false;
			for( ModuleSpecProperties dir: values() ){
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

	public ModuleSpecAdvertisementPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( AdvertisementTypes.MODULE_SPEC, parent);
	}

	@Override
	protected void fillDefaultValues( IJp2pPropertySource<IJp2pProperties> parent, AdvertisementTypes type ) {
		super.fillDefaultValues(parent, type );
		String name = super.getParent().getDirective( Directives.NAME );
		if(Utils.isNull( name )){
			name = (String) super.getParent().getProperty( ModuleImplProperties.CODE );
		}
		if(!Utils.isNull( name )){
			this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, ModuleImplProperties.CODE, name ));
		}
		String description = (String) super.getParent().getProperty( ModuleImplProperties.DESCRIPTION );
		if(!Utils.isNull( description )){
			super.setProperty( ModuleImplProperties.DESCRIPTION, description );
		}
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new ModuleSpecAdvertisementPreferences(this, null );
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return ModuleSpecProperties.isValidProperty(id.toString());	
	}	
	
	/**
	 * Create a module spec advertisement
	 * @return
	 * @throws URISyntaxException 
	 */
	public static ModuleSpecAdvertisement createModuleSpecAdvertisement( IJp2pPropertySource<IJp2pProperties> source, ModuleClassAdvertisement mcAdv, PipeAdvertisement pipeAdv ) throws URISyntaxException{
		AdvertisementTypes type = AdvertisementTypes.valueOf( StringStyler.styleToEnum( (String) source.getDirective( AdvertisementDirectives.TYPE )));
		ModuleSpecAdvertisementPreferences preferences = new ModuleSpecAdvertisementPreferences( (IJp2pWritePropertySource<IJp2pProperties>) source, mcAdv );
		ModuleSpecAdvertisement msad = ( ModuleSpecAdvertisement )AdvertisementFactory.newAdvertisement( AdvertisementTypes.convertTo(type));
		String name = (String) source.getProperty( AdvertisementProperties.NAME );
		msad.setName(name);
		msad.setVersion(( String )source.getProperty( ModuleSpecProperties.VERSION ));
		msad.setCreator(( String )source.getProperty( ModuleSpecProperties.CREATOR ));
		msad.setSpecURI(( String )source.getProperty( ModuleSpecProperties.SPEC_URI ));
		msad.setModuleSpecID( preferences.getModuleSpecID());
		msad.setPipeAdvertisement(pipeAdv);
		return msad;
	}

	
}