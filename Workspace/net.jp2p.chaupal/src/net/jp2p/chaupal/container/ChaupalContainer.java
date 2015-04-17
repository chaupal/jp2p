/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.container;

import net.jp2p.container.Jp2pContainer;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.startup.Jp2pStartupService;

public class ChaupalContainer extends Jp2pContainer<Object>{

	public ChaupalContainer( IJp2pWritePropertySource<IJp2pProperties> source ) {
		super( source );
	}

	@Override
	public void clear(){
		super.clear();
	}

	@Override
	public Jp2pStartupService getModule() {
		for( IJp2pComponent<?> component: super.getChildren() ){
			if( component.getModule() instanceof Jp2pStartupService ){
				return ( Jp2pStartupService )component.getModule();
			}
		}
		return (Jp2pStartupService) super.getModule();
	}

	protected void removeModule( Object module ){
		removeModule( this, module );
	}

	@Override
	public String toString() {
		return this.getIdentifier() + ":" + super.toString();
	}
}