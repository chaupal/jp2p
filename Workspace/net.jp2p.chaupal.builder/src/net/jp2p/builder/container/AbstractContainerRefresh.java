/*******************************************************************************
 * Copyright 2014 Chaupal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.builder.container;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.IJp2pComponent;

public abstract class AbstractContainerRefresh<T extends Object> {

	public static final int DEFAULT_TIME = 5000;
	
	private int refreshTime;
	private ComponentChangedEvent<IJp2pComponent<T>> event;
	private ExecutorService service;
	private boolean refresh;
	private Lock lock;

	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			if( event == null )
				return;
			while( refresh ){
				lock.lock();
				try{
					onRefresh( event );
					refresh = false;
				}
				finally{
					lock.unlock();
				}
				try{
					Thread.sleep( refreshTime );
				}
				catch( InterruptedException ex ){
					ex.printStackTrace();
				}
				event = null;		
			}
		}	
	};
	
	protected AbstractContainerRefresh( int refreshTime ) {
		this.refreshTime = refreshTime;
		service = Executors.newCachedThreadPool();
		lock = new ReentrantLock();
	}
	
	protected abstract void onRefresh( ComponentChangedEvent<IJp2pComponent<T>> event );	

	public void refresh( ComponentChangedEvent<IJp2pComponent<T>> event ){
		lock.lock();
		try{
			this.event = event;
			if( refresh )
				return;
			this.refresh = true;
		}
		finally{
			lock.unlock();
		}
		service.execute(runnable);

	}

	/**
	 * Shut down the service
	 */
	public void shutdown(){
		Thread.currentThread().interrupt();
		service.shutdown();
	}
}
