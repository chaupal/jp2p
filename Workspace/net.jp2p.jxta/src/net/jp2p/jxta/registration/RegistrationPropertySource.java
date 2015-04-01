/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.registration;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;

public class RegistrationPropertySource extends AbstractJp2pWritePropertySource
{
	public enum RegistrationProperties implements IJp2pProperties{
		DISCOVERY_MODE,
		WAIT_TIME,
		PEER_ID,
		ATTRIBUTE,
		WILDCARD,
		THRESHOLD;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public enum RegistrationDirectives implements IJp2pDirectives{
		PEERGROUP;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		public static final boolean isValidDirective( String key){
			for( RegistrationDirectives directive: RegistrationDirectives.values() ){
				if( directive.name().equals( key ))
					return true;
			}
			return false;
		}
	}

	public RegistrationPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		this( JxtaComponents.REGISTRATION_SERVICE.toString(), parent );
	}


	public RegistrationPropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		super( componentName,parent );
		this.fillDefaultValues();
	}

	protected void fillDefaultValues() {
		this.setManagedProperty( new ManagedProperty< IJp2pProperties, Object>( this, RegistrationProperties.WAIT_TIME, 60000, true ));
		this.setManagedProperty( new ManagedProperty< IJp2pProperties, Object>( this, RegistrationProperties.PEER_ID, null, true ));
		this.setManagedProperty( new ManagedProperty< IJp2pProperties, Object>( this, RegistrationProperties.ATTRIBUTE, null, true ));
		this.setManagedProperty( new ManagedProperty< IJp2pProperties, Object>( this, RegistrationProperties.WILDCARD, null, true ));
		this.setManagedProperty( new ManagedProperty< IJp2pProperties, Object>( this, RegistrationProperties.THRESHOLD, 1, true ));
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
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
		public RegistrationProperties getIdFromString(String key) {
			return null;
		}


	}

}
