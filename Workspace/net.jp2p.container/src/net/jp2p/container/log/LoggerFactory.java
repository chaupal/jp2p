/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.log;

import java.util.logging.Logger;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;

public class LoggerFactory extends
		AbstractComponentFactory<LoggerPropertySource>{

	private Logger logger = Logger.getLogger(LoggerFactory.class.getName());

	
	public LoggerFactory() {
		super( IJp2pServiceBuilder.Components.LOGGER_SERVICE.toString() );
		super.setCanCreate(true);
	}

	@Override
	public LoggerPropertySource onCreatePropertySource() {
		LoggerPropertySource source = new LoggerPropertySource( super.getParentSource());
		return source;
	}

	@Override
	protected LoggerComponent onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		LoggerComponent service = new LoggerComponent( this );
		super.setCompleted( true );
		return service;
	}
	
	@Override
	public void onNotifyChange(ComponentBuilderEvent event) {
		if( !BuilderEvents.COMPONENT_CREATED.equals( event.getBuilderEvent()))
			return;
				
		String msg = getJp2pLogMessage( event.getFactory().getPropertySource(), event.getFactory().getComponentName() ) + 
				"=" + event.getBuilderEvent();
		logger.log( Jp2pLevel.JP2PLEVEL, msg );
		System.out.println(msg);
	}
	
	/**
	 * Create a JP2P log message from the given string
	 * @param source
	 * @param msg
	 * @return
	 */
	public static final String getJp2pLogMessage( IJp2pPropertySource<IJp2pProperties> source, String msg ){
		String bundleName = AbstractJp2pPropertySource.getBundleId( source );
		return " <" + bundleName + "-LOG" + ">: " + msg ;	
	}

	/**
	 * Create a JP2P log message from the given string
	 * @param source
	 * @param msg
	 * @return
	 */
	public static final void jp2pSystemOutMessage( IJp2pPropertySource<IJp2pProperties> source, String msg ){
		System.out.println(getJp2pLogMessage( source, msg ));	
	}

	/**
	 * Create a JP2P log message from the given string
	 * @param source
	 * @param msg
	 * @return
	 */
	public static final void jp2pSystemErrMessage( IJp2pPropertySource<IJp2pProperties> source, String msg ){
		System.err.println(getJp2pLogMessage( source, msg ));
	}

	/**
	 * Create a JP2P log message from the given IJp2pComponent
	 * @param source
	 * @param msg
	 * @return
	 */
	public static final void jp2pSystemOutMessage( IJp2pComponent<?> component, String msg ){
		System.out.println(getJp2pLogMessage( component.getPropertySource(), msg ));	
	}

}