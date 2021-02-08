/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.startup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import net.jp2p.chaupal.document.IJp2pAdvertisement;
import net.jp2p.chaupal.exception.Jp2pPeerGroupException;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroup;
import net.jp2p.container.activator.AbstractActivator;
import net.jp2p.container.activator.IActivator;
import net.jp2p.container.activator.IJp2pService;
import net.jp2p.container.builder.ICompositeBuilderListener;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.Utils;

public class Jp2pStartupService extends AbstractActivator implements IJp2pComponent, IJp2pService<IContainerBuilder<?>>{

	public static final String S_ERR_NO_SERVICE_LOADED = "\n\t!!! No service is loaded. Not starting context:  ";
	public static final String S_ERR_CONTEXT_NOT_BUILT = "\n\t!!! The context was not built! Not starting context:  ";
	public static final String S_INFO_AUTOSTART = "\n\t!!! Autostarting container:  ";

	private Jp2pStartupPropertySource source;
	
	private IContainerBuilder<?> container;
	
	private Collection<ICompositeBuilderListener<Object>> listeners;
	
	public Jp2pStartupService( IContainerBuilder<?> container, Jp2pStartupPropertySource source ) {
		this.source = source;
		this.container = container;
		listeners = new ArrayList<ICompositeBuilderListener<Object>>();
		super.setStatus(Status.AVAILABLE);
	}

	@Override
	public IJp2pPropertySource<IJp2pProperties> getPropertySource() {
		return this.source;
	}


	/**
	 * If true, the service is auto started
	 * @return
	 */
	protected boolean isAutoStart(){
		return Boolean.parseBoolean( this.source.getDirective( IJp2pDirectives.Directives.AUTO_START ));		
	}

	/**
	 * Start a module
	 * @param module
	 */
	protected void stopModule( IPropertySourceFactory factory ){
		if(!( factory instanceof IComponentFactory<?> ))
			return;
		if( ((IComponentFactory<?>) factory).createComponent() instanceof IActivator ){
			IActivator service = (IActivator) ((IComponentFactory<?>) factory).createComponent();
			if( service.isActive())
				service.stop();
		}		
	}
	
	/**
	 * Perform the activation
	 */
	@Override
	public synchronized void activate() {
		if(!this.isAutoStart() )
			return;
		
		//listeners.add(this.container);
		
		//First start the modules that are already present
		//for( IJxseModule<?> module: container.getChildren()){
		//	if( Components.STARTUP_SERVICE.toString().equals( module.getComponentName() ))
		//		continue;
		//	try{
		//	  this.startModule( (IJxseModule<Object>) module);
		//	}
		//	catch( Exception ex ){
		//		ex.printStackTrace();
		//	}
		//}
		
		//Then listen to new additions
		Logger logger = Logger.getLogger( this.getClass().getName());
		String list = container.listModulesNotCompleted();
		if( !Utils.isNull( list )){
			logger.warning( list );
		}
	}
	
	//Make public
	@Override
	public void deactivate() {
		this.listeners.remove(this.container);
		for( IPropertySourceFactory factory: container.getFactories()){
			this.stopModule( factory );
		}
	}

	@Override
	protected void onFinalising() {
		// DO NOTHING AS DEFAULT ACTION		
	}

	@Override
	protected boolean onInitialising() {
		return true;
	}


	@Override
	public String getId() {
		return this.source.getId();
	}

	/**
	 * Get a String label for this component. This can be used for display options and 
	 * is not meant to identify the component;
	 * @return
	 */
	@Override
	public String getComponentLabel(){
		return this.source.getComponentName();
	}

	@Override
	public IContainerBuilder<?> getModule() {
		return this.container;
	}

	@Override
	public void init(IJp2pPeerGroup group, IJp2pID assignedID, IJp2pAdvertisement implAdv)
			throws Jp2pPeerGroupException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int startApp(String[] args) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void stopApp() {
		// TODO Auto-generated method stub
		
	}
}