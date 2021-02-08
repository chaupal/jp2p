/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.advertisement.service;

import java.net.URISyntaxException;
import java.util.Map;

import org.xml.sax.Attributes;

import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.AbstractFilterFactory;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;
import net.jp2p.container.filter.FilterChain;
import net.jp2p.container.filter.FilterChainEvent;
import net.jp2p.container.filter.IFilterChainListener;
import net.jp2p.container.filter.FilterChain.Operators;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.advertisement.ModuleClassAdvertisementPropertySource;
import net.jp2p.jxta.advertisement.ModuleImplAdvertisementPropertySource;
import net.jp2p.jxta.advertisement.ModuleSpecAdvertisementPropertySource;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementDirectives;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementTypes;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.filter.PeerGroupFilter;
import net.jp2p.jxta.peergroup.PeerGroupAdvertisementPropertySource;
import net.jp2p.jxta.pipe.PipeAdvertisementPropertySource;
import net.jxta.document.Advertisement;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public abstract class AbstractJxtaAdvertisementFactory<M extends Object, A extends Advertisement> extends AbstractFilterFactory<M> {
	
	private AdvertisementTypes type;
	private PeerGroup peergroup;
	
	
	protected AbstractJxtaAdvertisementFactory(String componentName) {
		super(componentName);
	}

	@Override
	public void prepare( IJp2pPropertySource<IJp2pProperties> parentSource,
			IContainerBuilder<Object> builder, Map<String, String> attributes) {
		String id = AdvertisementDirectives.TYPE.toString().toLowerCase();
		this.type =	AdvertisementTypes.convertFrom( attributes.get( id ));
		super.prepare( parentSource, builder, attributes);
	}

	protected PeerGroup getPeerGroup() {
		return peergroup;
	}


	@Override
	public String getComponentName() {
		return JxtaComponents.ADVERTISEMENT.toString();
	}

	
	@Override
	protected AdvertisementPropertySource onCreatePropertySource() {
		return ceatePropertySource(type, super.getParentSource());
	}
	
	@Override
	public void extendContainer() {
		AdvertisementTypes type = AdvertisementTypes.convertFrom((String) super.getPropertySource().getDirective( AdvertisementDirectives.TYPE ));
		if( !AdvertisementTypes.MODULE_SPEC.equals( type ))
			return;
		ModuleClassAdvertisementPropertySource msps = (ModuleClassAdvertisementPropertySource) AdvertisementPropertySource.findAdvertisementDescendant(super.getPropertySource(), AdvertisementTypes.MODULE_CLASS );
		if( msps == null )
			super.getPropertySource().addChild( new ModuleClassAdvertisementPropertySource( super.getPropertySource() ));
		super.extendContainer();
	}

	
	@Override
	protected IComponentFactoryFilter createFilter() {
		FilterChain<M> bf = new FilterChain<M>( Operators.SEQUENTIAL_AND, this );
		PeerGroupFilter<M> pgf = new PeerGroupFilter<M>( this );
		bf.addFilter(pgf);
		bf.addListener( new IFilterChainListener(){

			@SuppressWarnings("unchecked")
			@Override
			public boolean notifyComponentCompleted(FilterChainEvent event) {
				if( event.getFilter() instanceof PeerGroupFilter ){
					IJp2pComponent<PeerGroup> component = (IJp2pComponent<PeerGroup>) event.getFactory().createComponent();
					peergroup = component.getModule();
				}
				return true;
			}
			
		});
		return bf;
	}

	/**
	 * Create the advertisement
	 * @param source
	 * @return
	 */
	protected abstract A createAdvertisement( IJp2pPropertySource<IJp2pProperties> source );

	/**
	 * Create the advertisement
	 * @param source
	 * @return
	 */
	protected abstract IJp2pComponent<M> createComponent( A advertisement );

	@Override
	protected IJp2pComponent<M> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
		String tp = StringStyler.styleToEnum((String) source.getDirective( AdvertisementDirectives.TYPE ));
		if( Utils.isNull(tp))
			return null;
		A adv = this.createAdvertisement( super.getPropertySource());
		return this.createComponent( adv );
	}
	
	/**
	 * Get the correct advertisement type
	 * @param attrs
	 * @param qName
	 * @param parent
	 * @return
	 */
	public static AdvertisementTypes getAdvertisementType( Attributes attrs, String qName ,IJp2pPropertySource<IJp2pProperties> parent ){
		if(( attrs == null ) || ( attrs.getLength() == 0))
				return AdvertisementTypes.ADV;
		String type = attrs.getValue(AdvertisementDirectives.TYPE.toString().toLowerCase() );
		if( Utils.isNull(type))
			return AdvertisementTypes.ADV;
		return AdvertisementTypes.valueOf( StringStyler.styleToEnum( type ));
	}	
	
	/**
	 * Get the correct property source from the given parent source and type
	 * @param type
	 * @param parentSource
	 * @return
	 */
	public static AdvertisementPropertySource ceatePropertySource( AdvertisementTypes type, IJp2pPropertySource<IJp2pProperties> parentSource ) {
		AdvertisementPropertySource source = null;
		switch( type ){
		case MODULE_CLASS:
			source = new ModuleClassAdvertisementPropertySource(parentSource );
			break;
		case MODULE_SPEC:
			source = new ModuleSpecAdvertisementPropertySource(parentSource );
			break;
		case MODULE_IMPL:
			source = new ModuleImplAdvertisementPropertySource(parentSource );
			break;
		case PEERGROUP:
			source = new PeerGroupAdvertisementPropertySource(parentSource );
			break;
		case PEER:
			source = null;//new PeerAdvertisementPropertySource(super.getParentSource() );
			break;
		case PIPE:
			source = new PipeAdvertisementPropertySource(parentSource );
			break;
		default:
			source = new AdvertisementPropertySource( parentSource );
			break;
		}
		return source;
	}

	/**
	 * Get the correct advertisement from the given parent source and type
	 * @param type
	 * @param parentSource
	 * @return
	 * @throws URISyntaxException 
	 */
	public static Advertisement createAdvertisement( Object[] args, IJp2pPropertySource<IJp2pProperties> source ) throws Exception {
		AdvertisementTypes type = AdvertisementTypes.convertFrom( source.getDirective( AdvertisementDirectives.TYPE ));
		Advertisement adv = null;
		switch( type ){
		case MODULE_CLASS:
			adv = ModuleClassAdvertisementPropertySource.createModuleClassAdvertisement(source);
			break;
		case MODULE_SPEC:
			adv = ModuleSpecAdvertisementPropertySource.createModuleSpecAdvertisement(source, (ModuleClassAdvertisement) args[0], (PipeAdvertisement) args[1] );
			break;
		case MODULE_IMPL:
			adv = ModuleImplAdvertisementPropertySource.createModuleImplAdvertisement(source, (PeerGroup) args[0]);
			break;
		case PEERGROUP:
			adv = PeerGroupAdvertisementPropertySource.createPeerGroupAdvertisement(source, (ModuleSpecAdvertisement) args[0] );
			break;
		case PEER:
			source = null;//new PeerAdvertisementPropertySource(super.getParentSource() );
			break;
		case PIPE:
			adv = PipeAdvertisementPropertySource.createPipeAdvertisement((IJp2pWritePropertySource<IJp2pProperties>) source, (PeerGroup) args[0] );
			break;
		default:
			break;
		}
		return adv;
	}
}