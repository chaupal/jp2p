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
package net.jp2p.chaupal.jxta.pipe;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import net.jp2p.chaupal.jxta.IChaupalComponents.ChaupalComponents;
import net.jp2p.chaupal.jxta.advertisement.ChaupalAdvertisementFactory;
import net.jp2p.container.activator.IJp2pService;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementDirectives;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementTypes;
import net.jp2p.jxta.pipe.PipeAdvertisementPropertySource;
import net.jp2p.jxta.pipe.PipePropertySource;

public class ChaupalPipeFactory extends ChaupalAdvertisementFactory<PipeService, PipeAdvertisement>{

	public ChaupalPipeFactory() {
		super( ChaupalComponents.PIPE_SERVICE.toString());
	}

	@Override
	public void prepare( IJp2pPropertySource<IJp2pProperties> parentSource,
			IContainerBuilder<Object> builder, Map<String, String> attributes) {
		Map<String, String> attrs = new HashMap<String, String>();
		attrs.put( AdvertisementDirectives.TYPE.toLowerCase() , AdvertisementTypes.PIPE.toString());
		super.prepare( parentSource, builder, attrs);
	}

	@Override
	protected AdvertisementPropertySource onCreatePropertySource() {
		AdvertisementPropertySource source = new PipePropertySource( super.getParentSource() );
		source.setDirective( AdvertisementDirectives.TYPE, AdvertisementTypes.PIPE.toString());
		return source;
	}
	

	@Override
	protected PipeAdvertisement createAdvertisement( IJp2pPropertySource<IJp2pProperties> source) {
		PipeAdvertisement pipeAd = null;
		try {
			pipeAd = PipeAdvertisementPropertySource.createPipeAdvertisement((IJp2pWritePropertySource<IJp2pProperties>) source, super.getPeerGroup());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return pipeAd;
	}

	@Override
	protected IJp2pComponent<PipeService> createComponent(
			PipeAdvertisement advertisement) {
		PipeService pipes = super.getPeerGroup().getPipeService();
		IJp2pService<PipeAdvertisement> adService = super.getAdvertisementService();
		ChaupalPipeService service = new ChaupalPipeService( (IJp2pWritePropertySource<IJp2pProperties>) super.getPropertySource(), pipes, adService );
		return service;
	}
}