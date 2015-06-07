/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory;

import java.util.Map;

import net.jp2p.container.IContainerFactory;
import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.startup.StartupServiceFactory;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

public class ContainerFactory extends AbstractComponentFactory<Object> implements IContainerFactory<Object>
{
	private String bundleId;
	
	public ContainerFactory( String bundleId) {
		super( IJp2pServiceBuilder.Components.JP2P_CONTAINER.toString() );
		this.bundleId = bundleId;
	}	
	
	@Override
	public void prepare( IJp2pPropertySource<IJp2pProperties> parentSource,
			IContainerBuilder<Object> builder, Map<String, String> attributes) {
		super.prepare( parentSource, builder, attributes);
		super.setCanCreate(true);
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
	protected ChaupalContainer onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		ChaupalContainer context = new ChaupalContainer( (IJp2pWritePropertySource<IJp2pProperties>) super.getPropertySource() );
		return context;
	}

	@Override
	public synchronized IJp2pComponent<Object> createComponent() {
		return super.createComponent();
	}

	@Override
	public void onNotifyChange(ComponentBuilderEvent<Object> event) {
		/* DO NOTHING */
	}

	/* (non-Javadoc)
	 * @see net.jp2p.container.IContainerFactory#isAutoStart()
	 */
	@Override
	public boolean isAutoStart(){
		IContainerBuilder<Object> container = super.getBuilder();
		boolean autostart = AbstractJp2pPropertySource.isAutoStart(this.getPropertySource());
		if( autostart)
			return true;
		String comp = IJp2pServiceBuilder.Components.STARTUP_SERVICE.toString();
		IPropertySourceFactory startup = container.getFactory( comp );
		return (( startup != null ) &&  AbstractJp2pPropertySource.isAutoStart(startup.getPropertySource() ));
	}

	private void onPropertySourceCreated(){
		IContainerBuilder<Object> builder = super.getBuilder();
		boolean autostart = AbstractJp2pPropertySource.isAutoStart(this.getPropertySource());
		String comp = IJp2pServiceBuilder.Components.STARTUP_SERVICE.toString();
		IPropertySourceFactory startup = builder.getFactory( comp );
		if( !autostart || ( startup != null ))
			return;
		
		startup = getDefaultFactory( comp);
		startup.prepare( super.getPropertySource(), builder, null );
		IJp2pWritePropertySource<IJp2pProperties> props = (IJp2pWritePropertySource<IJp2pProperties>) startup.createPropertySource();
		props.setDirective( Directives.AUTO_START, Boolean.TRUE.toString());
		builder.addFactory(startup);
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#getDefaultFactory(net.osgi.jp2p.properties.IJp2pPropertySource, java.lang.String)
	*/
	public static IPropertySourceFactory getDefaultFactory( String componentName ){
		if( Utils.isNull(componentName))
			return null;
		String comp = StringStyler.styleToEnum(componentName);
		IJp2pServiceBuilder.Components component = IJp2pServiceBuilder.Components.valueOf(comp);
		IPropertySourceFactory factory = null;
		switch( component ){
		case STARTUP_SERVICE:
			factory = new StartupServiceFactory();
			break;
		case PERSISTENCE_SERVICE:
			factory = null;//ew SimplePersistenceFactory();
			break;
		case LOGGER_SERVICE:
//			factory = new LoggerFactory();
			break;
		default:
			break;
		}
		return factory;
	}

}
