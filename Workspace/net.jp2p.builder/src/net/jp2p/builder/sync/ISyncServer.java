package net.jp2p.builder.sync;

public interface ISyncServer {

	/**
	 * Add a sync listener 
	 * @param listener
	 */
	public void addSyncListener( ISyncListener listener );

	/**
	 * Remove a sync listener 
	 * @param listener
	 */
	public void removeSyncListener( ISyncListener listener );
}
