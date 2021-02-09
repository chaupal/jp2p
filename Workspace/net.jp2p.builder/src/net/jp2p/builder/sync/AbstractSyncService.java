/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.builder.sync;

import java.util.Collection;
import java.util.TreeSet;

import org.osgi.framework.BundleContext;

import net.jp2p.builder.service.AbstractDeclarativeService;

public abstract class AbstractSyncService<T extends Object> extends AbstractDeclarativeService<ISyncServer> implements ISyncService<T>{

	private static String filter = "(jp2p.syncr=*)"; 

	private Collection<ISyncServer> clients;
	
	private ISyncListener listener = new ISyncListener(){

		@Override
		public void notifySyncEvent(SyncEvent event) {
			onSync(event);
		}
	};

	protected AbstractSyncService() {
		clients = new TreeSet<ISyncServer>();
	}

	public void prepare(BundleContext bc ) {
		super.prepare(bc, ISyncServer.class, filter);
	}

	/**
	 * Determine the sync event that starts the build prcess
	 * @param event
	 * @return
	 */
	protected abstract void onSync( SyncEvent event );
	
	public void clear(){
		for( ISyncServer server: this.clients )
			server.removeSyncListener(listener);
		clients.clear();
	}

	@Override
	protected void onDataRegistered(ISyncServer data) {
		this.clients.add( data );
		data.addSyncListener(listener);
	}

	@Override
	protected void onDataUnRegistered(ISyncServer data) {
		data.removeSyncListener(listener);
		this.clients.remove( data );
	}
}