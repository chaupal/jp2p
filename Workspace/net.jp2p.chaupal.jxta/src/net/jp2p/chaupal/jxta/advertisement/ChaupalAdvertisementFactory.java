/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxta.advertisement;

import net.jp2p.chaupal.jxta.IChaupalComponents.ChaupalComponents;
import net.jp2p.chaupal.jxta.advertisement.AdvertisementServicePropertySource.AdvertisementServiceProperties;
import net.jp2p.chaupal.jxta.discovery.ChaupalDiscoveryService;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.factory.filter.AbstractComponentFactoryFilter;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;
import net.jp2p.container.filter.FilterChain;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.Utils;
import net.jxta.document.Advertisement;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.advertisement.service.AbstractJxtaAdvertisementFactory;
import net.jp2p.jxta.discovery.DiscoveryPropertySource;
import net.jp2p.jxta.discovery.DiscoveryPropertySource.DiscoveryProperties;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.factory.JxtaFactoryUtils;

public abstract class ChaupalAdvertisementFactory<T extends Object, U extends Advertisement> extends AbstractJxtaAdvertisementFactory<T,U>{
	
	private ChaupalDiscoveryService service;
	private Jp2pAdvertisementService<U> jas;
	private U advertisement;
	
	@Override
	protected AdvertisementPropertySource onCreatePropertySource() {
		AdvertisementPropertySource source = super.onCreatePropertySource();
		source.setDirective( Directives.CREATE, Boolean.TRUE.toString());
		return source;
	}

	@Override
	public void extendContainer() {
		IContainerBuilder builder = super.getBuilder();
		String componentName = ChaupalComponents.DISCOVERY_SERVICE.toString();
		IPropertySourceFactory df = builder.getFactory( componentName);
		if( df == null ){
			df = JxtaFactoryUtils.getDefaultFactory(  componentName );
			df.prepare(componentName, super.getPropertySource(), builder, new String[0]);
			df.createPropertySource();
			builder.addFactory( df ); 
		}
		DiscoveryPropertySource ds = (DiscoveryPropertySource) df.getPropertySource();

		AdvertisementPropertySource source = (AdvertisementPropertySource) super.getPropertySource().getChild( JxtaComponents.ADVERTISEMENT.toString() );
		if( source == null )
			return;
		Object value = ds.getProperty( DiscoveryProperties.ATTRIBUTE );
		if( value == null ){
			ds.setProperty(DiscoveryProperties.ATTRIBUTE, DiscoveryPropertySource.S_NAME );
		}
		String name = (String) source.getProperty( AdvertisementServiceProperties.NAME );
		if( Utils.isNull( name ))
			name = DiscoveryPropertySource.S_WILDCARD;
		value = ds.getProperty( DiscoveryProperties.WILDCARD );
		if( value == null ){
			ds.setProperty(DiscoveryProperties.WILDCARD, name );
		}
		super.extendContainer();
	}

	

	@Override
	@SuppressWarnings("unchecked")
	protected IComponentFactoryFilter createFilter(){
		FilterChain<U> bf = (FilterChain<U>) super.createFilter();
		IComponentFactoryFilter filter = new AbstractComponentFactoryFilter<U>((IComponentFactory<U>) this){

			@Override
			public boolean onAccept(ComponentBuilderEvent<?> event) {
				switch( event.getBuilderEvent() ){
				case COMPONENT_CREATED:
					if( ((AbstractComponentFactory<T>) event.getFactory()).getComponent() instanceof ChaupalDiscoveryService){
						service = (ChaupalDiscoveryService) ((AbstractComponentFactory<T>) event.getFactory()).getComponent();
						return true;
					}
					return false;
				default:
					return false;
				}
			}
			
		};
		bf.addFilter(filter);
		return bf;
	}
	/**
	 * Get the used discovery service
	 * @return
	 */
	public ChaupalDiscoveryService getDiscoveryService(){
		return service;
	}

	/**
	 * Get the advertisement
	 * @return
	 */
	protected U getAdvertisement() {
		return advertisement;
	}

	/**
	 * Get the advertisement service
	 * @return
	 */
	protected Jp2pAdvertisementService<U> getAdvertisementService() {
		return jas;
	}

	/**
	 * Returns true if the property source is a child of the parent. 
	 * @param source
	 * @return
	 */
	protected boolean isChild( IJp2pPropertySource<?> source ){
		if(  AbstractJp2pPropertySource.isChild( this.getPropertySource(), source ))
			return true;
		return AbstractJp2pPropertySource.isChild( this.getPropertySource().getParent(), source );
	}

	@Override
	protected IJp2pComponent<T> onCreateComponent( IJp2pPropertySource<IJp2pProperties> source) {
		jas = new Jp2pAdvertisementService<U>((IJp2pWritePropertySource<IJp2pProperties>) super.getPropertySource(), advertisement, service );
		return super.onCreateComponent(source);
	}
}
