/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

public abstract class AbstractPropertyFacade<T extends Object> extends AbstractJp2pPropertySource{

	private T module;
	
	public AbstractPropertyFacade( String bundleId, T module) {
		super(bundleId, module.getClass().getSimpleName());
		this.module = module;
	}

	protected T getModule() {
		return module;
	}
	
	@Override
	public String getComponentName() {
		return this.getModule().getClass().getSimpleName();
	}

	@Override
	public String getId() {
		return this.getModule().getClass().getCanonicalName();
	}

	@Override
	public int getDepth() {
		return 0;
	}
	
	@Override
	public Object getDefault( IJp2pProperties id) {
		return this.getProperty(id);
	}

	@Override
	public String getCategory( IJp2pProperties id) {
		return S_JP2P;
	}

	@Override
	public ManagedProperty<IJp2pProperties, Object> getManagedProperty( IJp2pProperties id) {
		return new ManagedProperty<IJp2pProperties, Object>( this, id );
	}
}

