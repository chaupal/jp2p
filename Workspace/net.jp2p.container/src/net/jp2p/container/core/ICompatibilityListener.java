package net.jp2p.container.core;

public interface ICompatibilityListener {

	public enum ChangeEvents{
		NODE_ADDED,
		NODE_REMOVED
	}
	
	public void notifyNodeChanged( CompatibilityEvent event );
}
