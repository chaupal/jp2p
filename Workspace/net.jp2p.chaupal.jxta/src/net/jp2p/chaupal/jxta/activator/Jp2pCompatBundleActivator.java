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
package net.jp2p.chaupal.jxta.activator;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import net.jp2p.chaupal.activator.AbstractJp2pBundleActivator;
import net.jp2p.container.AbstractJp2pContainer;
import net.jp2p.container.AbstractJp2pContainer.ServiceChange;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jxse.osgi.compat.CompatibilityEvent;
import net.jxse.osgi.compat.ICompatibilityListener;
import net.jxse.osgi.compat.IJP2PCompatibility;
import net.jxta.impl.loader.JxtaLoaderModuleManager;
import net.jxta.module.IModuleBuilder;
import net.jxta.module.IModuleManager;
import net.jxta.peergroup.core.Module;

public class Jp2pCompatBundleActivator extends AbstractJp2pBundleActivator<Object> {

	private IJP2PCompatibility<Object> compat;
	private String identfier;
	private IModuleManager<Module> manager;
	
	private BundleContext context;
	
	private String filter = "(objectclass=" + IModuleBuilder.class.getName() + ")";

	private ServiceListener sl = new ServiceListener() {

		@SuppressWarnings("unchecked")
		public void serviceChanged(ServiceEvent ev) {
			ServiceReference<?> sr = ev.getServiceReference();
			Object obj = context.getService(sr);
			System.out.println( obj.getClass().getName());
					
			IModuleBuilder<Module> builder = (IModuleBuilder<Module>)obj;
			switch(ev.getType()) {
			case ServiceEvent.REGISTERED:
				manager.registerBuilder(builder);
				break;
			case ServiceEvent.UNREGISTERING:
				manager.unregisterBuilder( builder);
				break;
			default:
				break;
			}
		}
	};
		
	private ICompatibilityListener cl = new ICompatibilityListener(){

		@Override
		public void notifyNodeChanged(CompatibilityEvent event) {
			ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
			ServiceChange change = ServiceChange.CHILD_ADDED;
			//if( event.)
			dispatcher.serviceChanged( new ComponentChangedEvent<Object>(this, compat.getIdentifier(), change ));
			
		}
		
	};
	
	//private 
	protected Jp2pCompatBundleActivator(String bundle_id, String identifier, IJP2PCompatibility<Object> compat ) {
		super( bundle_id );
		this.compat = compat;
		this.identfier = identifier;
		manager = JxtaLoaderModuleManager.getRoot( this.getClass(), true );
	}

	
	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		context = bundleContext;
		bundleContext.addServiceListener(sl, filter);
		super.start(bundleContext);
	}


	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		bundleContext.removeServiceListener( sl );
		this.compat.removeListener( cl );
		super.stop(bundleContext);
	}

	@Override
	protected IJp2pContainer<Object> onCreateContainer() {
		JxtaContainer container = new JxtaContainer( super.getBundleId(), this.identfier, this.compat );
		super.setContainer(  container );

		String[] args = Platform.getCommandLineArgs();
		this.compat.addListener( cl );
		try{
			this.compat.main(args);
		  container.setModule();
		  super.notifyListeners( new ContainerBuilderEvent<Object>( this, container ));
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		return super.getContainer();
	}

	private static class JxtaContainer extends AbstractJp2pContainer<Object> {

		private IJP2PCompatibility<Object> compat;
		
		JxtaContainer( String bundleName, String identifier, IJP2PCompatibility<Object> compat ) {
			super( new PropertySource( bundleName, identifier ) );
			this.compat = compat;
		}
		
		/**
		 * Set the module
		 */
		void setModule(){
			IJp2pComponent<Object> module = new Jp2pComponent<Object>( super.getPropertySource(), this.compat.getRoot().getModule() ); 
			super.addChild( module );
		}
		
		/**
		 * Create a default property source
		 * @author Kees
		 *
		 */
		private static class PropertySource extends AbstractJp2pWritePropertySource{

			PropertySource( String bundleName, String identifier) {
				super( bundleName, identifier );
				setDirective( IJp2pDirectives.Directives.NAME, identifier );
			}
			
		}
	}

}