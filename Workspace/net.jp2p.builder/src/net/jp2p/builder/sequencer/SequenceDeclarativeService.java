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
package net.jp2p.builder.sequencer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;

import net.jp2p.builder.utils.AbstractDeclarativeService;
import net.jp2p.container.Jp2pContainer.ServiceChange;
import net.jp2p.container.activator.IJp2pActivatorService;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.sequencer.IJp2pBundleSequencer;
import net.jp2p.container.sequencer.Jp2pBundleSequencer;
import net.jp2p.container.sequencer.SequencerEvent;

public class SequenceDeclarativeService<T extends Object> extends AbstractDeclarativeService<IJp2pBundleSequencer<T>> implements IComponentChangedListener<IJp2pComponent<T>>{

	//in the component.xml file you will use target="(chaupal.jp2p.bundle.sequence=sequenceName)"
	private static String identifier = "chaupal.jp2p.bundle.sequence"; 
	private static String filter = "(" + identifier + "=*)"; 

	private Jp2pBundleSequencer<T> sequencer;
	private Collection<IJp2pBundleSequencer<T>> sequencers;

	public SequenceDeclarativeService( String bundle_id ) {
		super( filter, IJp2pBundleSequencer.class );
		this.sequencer = new Jp2pBundleSequencer<T>( bundle_id ) ;
		this.sequencers = new ArrayList<IJp2pBundleSequencer<T>>();
	}
	
	public final Jp2pBundleSequencer<T> getSequencer() {
		return sequencer;
	}

	@Override
	public void start(BundleContext bc) {
		super.start(bc);
		Dictionary<String, String> dict = new Hashtable<String, String>();
		dict.put( identifier, this.sequencer.getBundleId());
		bc.registerService(IJp2pBundleSequencer.class, this.sequencer, dict );
	}
	
	protected void onDataRegistered(IJp2pBundleSequencer<T> data) {
		if( !sequencer.equals(data ))
			sequencers.add( data );
	}

	@Override
	protected void onDataUnRegistered( IJp2pBundleSequencer<T> data) {
		if( !sequencer.equals(data ))
			sequencers.remove( data );
	}

	@SuppressWarnings("unchecked")
	@Override
	public void notifyServiceChanged(ComponentChangedEvent<IJp2pComponent<T>> event) {
		if( !ServiceChange.STATUS_CHANGE.equals( event.getChange() ))
			return;
		if( !( event.getSource() instanceof IJp2pActivatorService ))
			return;
		IJp2pActivatorService<T> component = (IJp2pActivatorService<T>) event.getSource();
		SequencerEvent<T> sevent = new SequencerEvent<T>( sequencer, null /*component.getStatus()*/, event );
		for( IJp2pBundleSequencer<T> seq: this.sequencers )
			seq.notifyComponentChanged(sevent);
	}
}
