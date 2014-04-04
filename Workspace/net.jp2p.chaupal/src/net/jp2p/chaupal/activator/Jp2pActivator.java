/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.activator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Jp2pActivator<T extends Object> implements Runnable {

	private AbstractJp2pBundleActivator<T> activator;
	
	private ExecutorService executor;
		
	public Jp2pActivator( AbstractJp2pBundleActivator<T> activator ) {
		this.activator = activator;
		executor = Executors.newSingleThreadExecutor();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(){
		executor.execute(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(){
		executor.shutdown();
	}

	@Override
	public void run() {
		try{
			activator.createContainer();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
}