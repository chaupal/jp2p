package org.chaupal.rdv.service;

import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.component.IJp2pComponent;


public class ContainerObserver implements IComponentChangedListener<IJp2pComponent<Object>> {

	public ContainerObserver() {
		System.out.println( this.getClass().getName() + ": " + "Starting to Observe.");
	}
	
	@Override
	public void notifyServiceChanged(ComponentChangedEvent<IJp2pComponent<Object>> event) {
		System.out.println( "Observing: " + this.getClass().getName() + ": " + event.toString());
	}
}
