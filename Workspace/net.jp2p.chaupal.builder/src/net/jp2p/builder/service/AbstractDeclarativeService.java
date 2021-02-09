/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.builder.service;

import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.service.log.LogService;

import net.jp2p.builder.Activator;

/**
 * <p>
 * Wrapper class which listens for all framework services of
 * a certain class. Each such service is picked
 * up and installed into the module container
 * </p>
 * <p>
 * <p>
 * The alias used for the servlet/resource is taken from the 
 * PROP_ALIAS service property.
 * </p>
 * <p>
 * The resource dir used for contexts is taken from the 
 * PROP_DIR service property.
 * </p>
 */
public abstract class AbstractDeclarativeService<T extends Object> {

	private static final String S_ERR_NO_DATA_PROVIDED = "No data has been included with the service";

	private BundleContext  bc;
	
	private String filter;
	private Class<T> clss;
	
	protected AbstractDeclarativeService() {
	}

	protected void prepare( BundleContext bc, Class<T> clss, String filter){
		this.bc = bc;
		this.clss = clss;
		this.filter = filter;		
	}
	
	protected abstract void onDataRegistered( T data );
	
	protected abstract void onDataUnRegistered( T data );

	/**
	 * Open the service
	 */
	public void open() {
		ServiceListener sl = new ServiceListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void serviceChanged(ServiceEvent ev) {
				ServiceReference<T> sr = (ServiceReference<T>) ev.getServiceReference();
				switch(ev.getType()) {
				case ServiceEvent.REGISTERED:
					try {
						register(sr);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case ServiceEvent.UNREGISTERING:
					try {
						unregister(sr);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		};

		try {
			bc.addServiceListener(sl, filter);
			Collection<ServiceReference<T>> srl = bc.getServiceReferences( clss, filter);
			for(ServiceReference<T> sr: srl ) {
				register( sr );
			}
		} catch (InvalidSyntaxException e) { 
			e.printStackTrace(); 
		}
	}
	
	/**
	 * Register the service
	 * @param sr
	 */
	private void register(ServiceReference<T> sr) {
		try {
			T service = bc.getService( sr );
			if( service == null )
				throw new NullPointerException( S_ERR_NO_DATA_PROVIDED);
			this.onDataRegistered( service );
		} catch (Exception e) {
			e.printStackTrace();
			Activator.getLog().log( LogService.LOG_WARNING,"Failed to register resource", e);
		}
	}

	/**
	 * Register the service
	 * @param sr
	 */
	private void unregister(ServiceReference<T> sr) {
		try {
			T service = bc.getService( sr );
			if( service == null )
				throw new NullPointerException( S_ERR_NO_DATA_PROVIDED);
			this.onDataUnRegistered( service);			
		} catch (Exception e) {
			e.printStackTrace();
			Activator.getLog().log( LogService.LOG_WARNING,"Failed to unregister resource", e);
		}
	}

	public void close(){
		try {
			Collection<ServiceReference<T>> srl = bc.getServiceReferences( clss, filter);
			for(ServiceReference<T> sr: srl ) {
				unregister( sr );
			}
		} catch (InvalidSyntaxException e) { 
			e.printStackTrace(); 
		}		
	}
}
