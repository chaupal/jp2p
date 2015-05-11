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
package net.jp2p.chaupal.sequencer;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractJp2pBundleSequencer<T extends Object> implements IJp2pBundleSequencer<T> {

	private String bundle_id;
	private Collection<IJp2pBundleSequencer<T>> listeners;
	
	protected AbstractJp2pBundleSequencer( String bundle_id) {
		this.bundle_id = bundle_id;
		this.listeners = new ArrayList<IJp2pBundleSequencer<T>>();
	}

	/* (non-Javadoc)
	 * @see net.jp2p.chaupal.sequencer.IJp2pBundleSequencer#getBundle_id()
	 */
	@Override
	public final String getBundleId() {
		return bundle_id;
	}

	@Override
	public void addListener( IJp2pBundleSequencer<T> listener ){
		this.listeners.add(listener);
	}
	
	@Override
	public void removeListener( IJp2pBundleSequencer<T> listener ){
		this.listeners.remove( listener);
	}
	
	/* (non-Javadoc)
	 * @see net.jp2p.chaupal.sequencer.IJp2pBundleSequencer#registerEvent(net.jp2p.chaupal.sequencer.SequencerEvent)
	 */
	@Override
	public void registerEvent( SequencerEvent<T> event ){
		for( IJp2pBundleSequencer<T> listener: listeners ){
			listener.notifyBundleActive( event );
		}
	}
	
	/**
	 * Determines whether the event is accepted by this sequences
	 * @param event
	 * @return
	 */
	protected abstract boolean accept( SequencerEvent<T> event  );

	/**
	 * The response of the sequencer to an incoming event.
	 * @param event
	 * @return
	 */
	protected abstract void onNotifyBundleAccepted( SequencerEvent<T> event  );

	/* (non-Javadoc)
	 * @see net.jp2p.chaupal.sequencer.IJp2pBundleSequencer#notifyBundleActive(net.jp2p.chaupal.sequencer.SequencerEvent)
	 */
	@Override
	public void notifyBundleActive( SequencerEvent<T> event ){
		if( accept( event )){
			this.onNotifyBundleAccepted(event);
		}
	}
}
