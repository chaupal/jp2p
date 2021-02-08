/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

public abstract class AbstractJp2pWritePropertySource 
extends AbstractJp2pPropertySource implements IJp2pWritePropertySource<IJp2pProperties> {

	protected AbstractJp2pWritePropertySource( String bundleId, String componentName) {
		this( bundleId, componentName, 0);
	}

	protected AbstractJp2pWritePropertySource( String bundleId, String componentName, int depth ) {
		super( bundleId, componentName );
	}

	protected AbstractJp2pWritePropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent ) {
		super( componentName, parent );
	}

	@Override
	public ManagedProperty<IJp2pProperties,Object> getOrCreateManagedProperty(IJp2pProperties id, Object value, boolean derived ) {
		ManagedProperty<IJp2pProperties,Object> select = super.getManagedProperty(id);
		if( select == null ){
			select = new ManagedProperty<IJp2pProperties, Object>( this, id, value, derived );
			super.setManagedProperty( select );
		}else{
			select.setValue(value);
		}
		return select;
	}
	
	@Override
	public boolean setProperty(IJp2pProperties id, Object value) {
		return this.setProperty(id, value, null, false );
	}

	protected boolean setProperty(IJp2pProperties id, Object value, boolean derived ) {
		return this.setProperty(id, value, null, derived );
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean setProperty(IJp2pProperties id, Object value, IJp2pValidator<IJp2pProperties,Object> validator, boolean derived ) {
		IJp2pWritePropertySource source = this;
		ManagedProperty select = source.getOrCreateManagedProperty(id, value, derived);
		if( validator != null )
			select.setValidator(validator);
		return select.setValue(value);
	}
		
	/**
	 * Set the directive
	 * @param id
	 * @param value
	 * @return
	 */
	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		return super.setDirective(id, value);
	}
}