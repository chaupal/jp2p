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

import net.jp2p.container.Jp2pContainer.ServiceChange;
import net.jp2p.container.activator.IActivator.Status;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.dispatcher.ServiceChangedEvent;

public class SequencerEvent<T extends Object> extends ServiceChangedEvent {
	private static final long serialVersionUID = 1L;
	
	private String target;
	private Status status;
	private String componentName;

	public SequencerEvent( IJp2pBundleSequencer<T> sequencer, ComponentChangedEvent<IJp2pComponent<T>> event){
		this( sequencer, event.getIdentifier(), Status.UNKNOWN, event.getChange() );
	}

	@SuppressWarnings("unchecked")
	public SequencerEvent( IJp2pBundleSequencer<T> sequencer, Status status, ComponentChangedEvent<IJp2pComponent<T>> event){
		this( sequencer, event.getIdentifier(), status, event.getChange() );
		IJp2pComponent<T> component = (IJp2pComponent<T>) event.getSource();
		this.target = component.getId();
		this.componentName = component.getComponentLabel();
	}
	
	protected SequencerEvent( IJp2pBundleSequencer<T> sequencer, String target, Status status, ServiceChange change) {
		super(sequencer, change);
		this.target = target;
		this.status = status;
	}

	@SuppressWarnings("unchecked")
	public String getBundleId(){
		IJp2pBundleSequencer<T> sequencer = (IJp2pBundleSequencer<T>) super.getSource();
		return sequencer.getBundleId();
	}

	public final String getComponentName() {
		return componentName;
	}

	public final String getTarget() {
		return target;
	}

	public final Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "<" + this.getBundleId() + ">" + this.target + ": " + super.getChange().toString() + "=>" + getSource().toString();
	}	

}