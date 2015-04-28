package net.jp2p.chaupal.sync;

import java.util.EventObject;

import net.jp2p.container.Jp2pContainer.ServiceChange;

public class SyncEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private String bundle_id;
	private String componentName;
	private ServiceChange event;
	
	public SyncEvent(Object source, String bundle_id, String componentName, ServiceChange event ) {
		super(source);
		this.bundle_id = bundle_id;
		this.componentName = componentName;
		this.event = event;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public String getComponentName() {
		return componentName;
	}

	public ServiceChange getEvent() {
		return event;
	}
}
