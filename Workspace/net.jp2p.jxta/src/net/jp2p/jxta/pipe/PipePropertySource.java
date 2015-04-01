/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.pipe;

import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;
import net.jxta.pipe.PipeService;

public class PipePropertySource extends AdvertisementPropertySource{

	public static final long DEFAULT_OUTPUT_PIPE_TIME_OUT = 5000;
	
	/**
	 * Properties specific for pipe services
	 * @author Kees
	 *
	 */
	public enum PipeServiceProperties implements IJp2pProperties{
		PIPE_ID,
		TIME_OUT,
		TYPE;

		public static boolean isValidProperty( String str ){
			if( Utils.isNull( str ))
				return false;
			for( PipeServiceProperties dir: values() ){
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

	public enum PipeServiceTypes{
		UNICAST,
		SECURE_UNICAST,
		PROPAGATE;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		/**
		 * Convert the enum to a form that the jxta lib can understand
		 * @param pipeType
		 * @return
		 */
		public static String convert( PipeServiceTypes pipeType ){
			switch( pipeType ){
			case UNICAST:
				return PipeService.UnicastType;
			case SECURE_UNICAST:
				return PipeService.UnicastSecureType;
			default:
				return PipeService.PropagateType;
			}
		}
	}
	public PipePropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( JxtaComponents.PIPE_SERVICE.toString(), parent);
		super.setProperty( PipeServiceProperties.TIME_OUT, DEFAULT_OUTPUT_PIPE_TIME_OUT);
	}

	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		if( PeerGroupPropertySource.PeerGroupDirectives.isValidDirective( id.name()))
			return super.setDirective(PeerGroupPropertySource.PeerGroupDirectives.valueOf( id.name()), value );
		return super.setDirective(id, value);
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return PipeServiceProperties.isValidProperty(id.toString());	
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
			if( PipeServiceProperties.isValidProperty(key))
				return PipeServiceProperties.valueOf(key);
			return super.getIdFromString(key);
		}

	}

}