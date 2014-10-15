/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container;

import java.util.Map;

import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.context.Jp2pContext;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;

public class ContainerFactory extends AbstractComponentFactory<Object>
{
	private String bundleId;
	
	public ContainerFactory( String bundleId) {
		this.bundleId = bundleId;
	}	
	
	@Override
	public void prepare(String componentName,
			IJp2pPropertySource<IJp2pProperties> parentSource,
			IContainerBuilder builder, Map<String, String> attributes) {
		super.prepare(componentName, parentSource, builder, attributes);
		super.setCanCreate(true);
	}


	@Override
	public String getComponentName() {
		return Jp2pContext.Components.JP2P_CONTAINER.toString();
	}
	
	@Override
	protected Jp2pContainerPropertySource onCreatePropertySource() {
		return new Jp2pContainerPropertySource( this.bundleId, null );
	}

	@Override
	public void extendContainer() {
		this.onPropertySourceCreated();
		super.extendContainer();	
	}

	
	@Override
	public Jp2pContainer createComponent() {
		return (Jp2pContainer) super.createComponent();
	}

	@Override
	protected Jp2pContainer onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		Jp2pContainer context = new Jp2pContainer( super.getPropertySource() );
		return context;
	}

	//Make public
	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		super.notifyChange(event);
	}

	/**
	 * Returns true if the context can be autostarted 
	 * @return
	 */
	public boolean isAutoStart(){
		IContainerBuilder container = super.getBuilder();
		boolean autostart = AbstractJp2pPropertySource.isAutoStart(this.getPropertySource());
		if( autostart)
			return true;
		String comp = Jp2pContext.Components.STARTUP_SERVICE.toString();
		IPropertySourceFactory startup = container.getFactory( comp );
		return (( startup != null ) &&  AbstractJp2pPropertySource.isAutoStart(startup.getPropertySource() ));
	}

	private void onPropertySourceCreated(){
		IContainerBuilder builder = super.getBuilder();
		boolean autostart = AbstractJp2pPropertySource.isAutoStart(this.getPropertySource());
		String comp = Jp2pContext.Components.STARTUP_SERVICE.toString();
		IPropertySourceFactory startup = builder.getFactory( comp );
		if( !autostart || ( startup != null ))
			return;
		
		startup = Jp2pContext.getDefaultFactory( comp);
		startup.prepare( comp, super.getPropertySource(), builder, null );
		IJp2pWritePropertySource<IJp2pProperties> props = (IJp2pWritePropertySource<IJp2pProperties>) startup.createPropertySource();
		props.setDirective( Directives.AUTO_START, Boolean.TRUE.toString());
		builder.addFactory(startup);
	}
}
