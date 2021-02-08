/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.sequencer;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.IJp2pContainer.ContainerProperties;
import net.jp2p.container.activator.IActivator.Status;
import net.jp2p.container.context.IJp2pServiceBuilder.Components;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;

public class SequencerPropertySource extends AbstractJp2pWritePropertySource{
	
	private static final int TIME_OUT = 500;
	
	public enum SequenceProperties implements IJp2pProperties{
		TIME_OUT,
		WATCH_ID,
		STATUS;

		@Override
		public String toString() {
			return StringStyler.prettyString(super.toString());
		}
	}
	
	public SequencerPropertySource( Jp2pContainerPropertySource parent ) {
		super( Components.SEQUENCER_SERVICE.toString(), parent );
		super.setDirective( Directives.AUTO_START, parent.getDirective( Directives.AUTO_START ));
		super.setProperty( SequenceProperties.TIME_OUT, TIME_OUT );
		super.setProperty( SequenceProperties.STATUS, Status.ACTIVE );
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	/**
	 * Get the id of the component that is monitored
	 * @return
	 */
	public String getWatchID(){
		return (String) super.getProperty( SequenceProperties.WATCH_ID );
	}
	
	public Status getWatchStatus(){
		return (Status ) super.getProperty( SequenceProperties.STATUS );
	}
	
	public int getTimeOut(){
		return (Integer) super.getProperty( SequenceProperties.TIME_OUT );		
	}
	
	@Override
	public Object getDefault( IJp2pProperties id) {
		if(!( id instanceof ContainerProperties ))
			return null;
		ContainerProperties cp = (ContainerProperties )id;
		switch( cp ){
		default:
			break;
		}
		return null;
	}

	@Override
	public boolean validate( IJp2pProperties id, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	private class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public SequenceProperties getIdFromString(String key) {
			return SequenceProperties.valueOf( key );
		}

		@Override
		public Object convertTo(IJp2pProperties id, String value) {
			SequenceProperties property = ( SequenceProperties )id;
			switch( property ){
			case STATUS:
				return Status.valueOf( StringStyler.styleToEnum( value ));
			case TIME_OUT:
				return Integer.parseInt( value );
			default:
				break;
			}
			return super.convertTo(id, value);
		}		
	}

}
