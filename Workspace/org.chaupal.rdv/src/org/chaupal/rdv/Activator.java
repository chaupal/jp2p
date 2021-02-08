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
package org.chaupal.rdv;

import net.jp2p.chaupal.activator.Jp2pBundleActivator;
import net.jp2p.container.properties.IJp2pDirectives.DeveloperModes;

import org.osgi.framework.*;
import org.chaupal.rdv.service.ContainerObserver;

public class Activator extends Jp2pBundleActivator {

	public static final String S_BUNDLE_ID = "org.chaupal.rdv";
	
	private static Jp2pBundleActivator activator;
	
	public Activator() {
		super( S_BUNDLE_ID, DeveloperModes.DEBUG );
		activator = this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.addObserver( new ContainerObserver() );
		super.start(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		activator = null;
	}
	
	public static Jp2pBundleActivator getDefault(){
		return activator;
	}
}