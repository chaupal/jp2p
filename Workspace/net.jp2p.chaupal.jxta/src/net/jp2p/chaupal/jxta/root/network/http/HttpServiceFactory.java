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
package net.jp2p.chaupal.jxta.root.network.http;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.AbstractFilterFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.filter.AbstractComponentFilter;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jxta.platform.Module;

public class HttpServiceFactory extends AbstractFilterFactory<Module> {

	@Override
	protected IJp2pPropertySource<IJp2pProperties> onCreatePropertySource() {
		IJp2pPropertySource<IJp2pProperties> source = new HttpServicePropertySource( super.getParentSource());
		return source;
	}

	@Override
	protected IComponentFactoryFilter createFilter() {
		return new Filter( this );
	}

	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		System.out.println( event.toString());
		super.notifyChange(event);
	}


	@Override
	protected synchronized IJp2pComponent<Module> createComponent() {
		// TODO Auto-generated method stub
		return super.createComponent();
	}


	@Override
	protected IJp2pComponent<Module> onCreateComponent(
			IJp2pPropertySource<IJp2pProperties> properties) {
		return new HttpService( this );
	}

	private static class Filter extends AbstractComponentFilter<IJp2pComponent<Module>, IJp2pContainer>{

		public Filter(IComponentFactory<IJp2pComponent<Module>> factory) {
			super( BuilderEvents.COMPONENT_CREATED, factory);
		}

		@Override
		protected boolean checkComponent(
				IComponentFactory<IJp2pContainer> factory) {
			return factory.getComponent() instanceof IJp2pContainer;
		}
		
	}
}