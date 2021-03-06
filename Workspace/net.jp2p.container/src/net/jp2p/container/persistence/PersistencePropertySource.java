/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.persistence;

import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;

public class PersistencePropertySource extends AbstractJp2pWritePropertySource {

	/**
	 * Directives give additional clues on how to create the component
	 * @author Kees
	 *
	 */
	public enum PersistenceDirectives implements IJp2pDirectives{
		CONTEXT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isDirective( String str ){
			str = StringStyler.styleToEnum(str);
			if(( str == null ) || ( str.length() == 0 ))
				return false;
			for( PersistenceDirectives comp: values()){
				if( comp.name().equals( str.toUpperCase() ))
					return true;
			}
			return false;
		}
	}
	
	public PersistencePropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( IJp2pServiceBuilder.Components.PERSISTENCE_SERVICE.toString(), parent);
		super.setDirective( Directives.CREATE, Boolean.TRUE.toString());
		super.setDirective( Directives.AUTO_START, Boolean.TRUE.toString());
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new SimplePropertyConvertor( this );
	}
}
