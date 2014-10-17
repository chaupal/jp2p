package net.jp2p.jxta.socket;

import net.jxta.pipe.PipeMsgListener;

public interface ISocketService<T extends PipeMsgListener> {

	/**
	 * Create the socket
	 * @return
	 */
	public T createSocket();
	
	/**
	 * Close the socket
	 */
	public void close();
}
