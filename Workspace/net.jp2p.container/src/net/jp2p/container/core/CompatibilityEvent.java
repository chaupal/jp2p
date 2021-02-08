package net.jp2p.container.core;

import java.util.EventObject;

public class CompatibilityEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private ICompatibilityListener.ChangeEvents event;
	
	public CompatibilityEvent( Object source, ICompatibilityListener.ChangeEvents event ) {
		super(source);
		this.event = event;
	}

	protected ICompatibilityListener.ChangeEvents getEvent() {
		return event;
	}
}
