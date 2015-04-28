package net.jp2p.chaupal.sync;

public interface ISyncListener {

	/**
	 * Notify listeners of a sync event
	 * @param event
	 */
	public void notifySyncEvent( SyncEvent event );
}
