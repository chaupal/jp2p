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
package net.jp2p.container.sequencer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.jp2p.container.context.IJp2pServiceBuilder.Components;

public class Jp2pBundleSequencer<T extends Object> implements IJp2pBundleSequencer<T> {

	private String bundle_id;
	private Collection<ISequenceEventListener<T>> listeners;
	private Collection<SequencerEvent<T>> events;
	
	private Lock lock;
	
	public Jp2pBundleSequencer( String bundle_id) {
		this.bundle_id = bundle_id;
		this.listeners = new ArrayList<ISequenceEventListener<T>>();
		events = new ArrayList<SequencerEvent<T>>();
		lock = new ReentrantLock();
	}
	
	public void clearEvents(){
		this.events.clear();
	}

	/* (non-Javadoc)
	 * @see net.jp2p.chaupal.sequencer.IJp2pBundleSequencer#getBundle_id()
	 */
	@Override
	public final String getBundleId() {
		return bundle_id;
	}

	/**
	 * Add a listener and notify it of events that had already occurred
	 * @param listener
	 */
	public void addListener( ISequenceEventListener<T>listener ){
		lock.lock();
		try{
			for( SequencerEvent<T> event: events )
				listener.notifySequenceEvent(event);
			this.listeners.add(listener);
		}
		finally{
			lock.unlock();
		}
	}
	
	public void removeListener( ISequenceEventListener<T> listener ){
		lock.lock();
		try{
			this.listeners.remove( listener);
		}
		finally{
			lock.unlock();
		}
	}
	
	/* (non-Javadoc)
	 * @see net.jp2p.chaupal.sequencer.IJp2pBundleSequencer#notifyBundleActive(net.jp2p.chaupal.sequencer.SequencerEvent)
	 */
	@Override
	public synchronized void notifyComponentChanged( SequencerEvent<T> event ){
		//ignore changes of the sequencer service
		if( Components.SEQUENCER_SERVICE.toString().equals( event.getComponentName() ))
			return;
		lock.lock();
		try{
			events.add( event );
			for( ISequenceEventListener<T> listener: this.listeners )
				listener.notifySequenceEvent(event);
		}
		finally{
			lock.unlock();
		}
	}
}
