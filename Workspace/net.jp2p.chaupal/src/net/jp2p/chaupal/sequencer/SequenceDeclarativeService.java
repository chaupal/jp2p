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

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;

import net.jp2p.chaupal.utils.AbstractDeclarativeService;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.component.IJp2pComponent;

public class SequenceDeclarativeService<T extends Object> extends AbstractDeclarativeService<IJp2pBundleSequencer<T>> implements IComponentChangedListener<IJp2pComponent<T>>{

	//in the component.xml file you will use target="(chaupal.jp2p.bundle.sequence=sequenceName)"
	private static String identifier = "chaupal.jp2p.bundle.sequence"; 
	private static String filter = "(chaupal.jp2p.bundle.sequence=*)"; 

	private IJp2pBundleSequencer<T> sequencer;

	public SequenceDeclarativeService( String bundle_id ) {
		this( new BundleSequencer<T>( bundle_id ) );
	}
	
	public SequenceDeclarativeService( IJp2pBundleSequencer<T> sequencer ) {
		super(filter, true);
		this.sequencer = sequencer;
	}
	
	
	@Override
	public void start(BundleContext bc, Class<?> clss) {
		Dictionary<String, String> dict = new Hashtable<String, String>();
		dict.put( identifier, this.sequencer.getBundleId());
		bc.registerService(IJp2pBundleSequencer.class, this.sequencer, dict );
		super.start(bc, clss);
	}
	
	protected void onDataRegistered(IJp2pBundleSequencer<T> data) {
		sequencer.addListener( data );
	}

	@Override
	protected void onDataUnRegistered( IJp2pBundleSequencer<T> data) {
		sequencer.removeListener( data );
	}

	@Override
	public void notifyServiceChanged(ComponentChangedEvent<IJp2pComponent<T>> event) {
		String id = event.getTarget().getId();
		if( id.startsWith( sequencer.getBundleId()))
			return;
		SequencerEvent<T> sevent = new SequencerEvent<T>( sequencer, event );
		System.out.println( "SEQUENCE:" + sevent);
		sequencer.registerEvent( sevent);
	}

	private static class BundleSequencer<T extends Object> extends AbstractJp2pBundleSequencer<T>{

		protected BundleSequencer( String bundle_id ) {
			super( bundle_id );
		}

		@Override
		protected boolean accept(SequencerEvent<T> event) {
			return false;
		}

		@Override
		protected void onNotifyBundleAccepted(SequencerEvent<T> event) {
		}
		
	}
}
