/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.startup.Jp2pStartupService;

public class Jp2pContainer extends AbstractJp2pContainer<Jp2pStartupService>{

	public Jp2pContainer( IJp2pPropertySource<IJp2pProperties> iJxsePropertySource ) {
		super( iJxsePropertySource );
	}

	@Override
	public void clearModules(){
		super.clearModules();
	}

	@Override
	public Jp2pStartupService getModule() {
		for( IJp2pComponent<?> component: super.getChildren() ){
			if( component.getModule() instanceof Jp2pStartupService ){
				return ( Jp2pStartupService )component.getModule();
			}
		}
		return super.getModule();
	}

	protected void removeModule( Object module ){
		removeModule( this, module );
	}

	/**
	 * Make public
	 */
	@Override
	public void initialise() {
		super.initialise();
	}

	@Override
	protected void activate() {
		super.activate();
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
	public String toString() {
		return this.getIdentifier() + ":" + super.toString();
	}
}