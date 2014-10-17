package net.jp2p.jxta.socket;

import java.io.IOException;
import java.io.InputStream;

import net.jp2p.jxta.socket.SocketPropertySource.SocketProperties;
import net.jp2p.jxta.socket.SocketPropertySource.SocketTypes;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaMulticastSocket;
import net.jxta.socket.JxtaServerSocket;
import net.jxta.socket.JxtaSocket;

class SocketService<T extends PipeMsgListener> implements
		ISocketService<T> {

	private SocketPropertySource source;
	private PeerGroup peerGroup;
	private PipeAdvertisement pipead;
	
	public SocketService( SocketPropertySource source, PeerGroup peerGroup, PipeAdvertisement pipead ) {
		this.source = source;
		this.pipead = pipead;
		this.peerGroup = peerGroup;
	}

	private T socket;
	
	/**
	 * Get the correct server socket by selecting the correct constructor
	 * @return
	 * @throws IOException
	 */
	protected JxtaSocket getSocket( PipeAdvertisement pipeAdv) throws IOException{
		int time_out = (int) source.getProperty( SocketProperties.TIME_OUT );
		//boolean reliable = (boolean)source.getProperty( SocketProperties.RELIABLE );
		JxtaSocket socket;
		if( time_out <= 0 )
			socket = new JxtaSocket( peerGroup, pipeAdv );
		else
			socket = new JxtaSocket( peerGroup, pipeAdv, time_out );
		return socket;
	}

	/**
	 * Get the correct server socket by selecting the correct constructor
	 * @return
	 * @throws IOException
	 */
	protected JxtaServerSocket getServerSocket( PipeAdvertisement pipeAdv ) throws IOException{
		int back_log = (int) source.getProperty( SocketProperties.BACKLOG );
		int time_out = (int) source.getProperty( SocketProperties.TIME_OUT );
		boolean encrypt = (boolean)source.getProperty( SocketProperties.ENCRYPT );
		if(( back_log <= 0 ) && ( time_out <= 0 ))
			return new JxtaServerSocket( peerGroup, pipeAdv, encrypt );
		if(( time_out <= 0 ))
			return new JxtaServerSocket( peerGroup, pipeAdv, back_log, encrypt );
		else
			return new JxtaServerSocket( peerGroup, pipeAdv, back_log, time_out, encrypt );			
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createSocket() {
		PipeMsgListener socket = null;
		SocketTypes type = SocketPropertySource.getSocketType(source);
		try {
			switch( type ){
			case CLIENT:
				socket = this.getSocket( pipead);
				break;
			case SERVER:
				socket = this.getServerSocket( pipead);
				break;
			case MULTICAST:
				socket = new JxtaMulticastSocket( peerGroup, pipead );
				break;
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		return (T) socket;
	}

	@Override
	public void close() {
		try {
			if( socket instanceof JxtaSocket ){
				JxtaSocket js = (JxtaSocket) socket;
				InputStream in = js.getInputStream();
				in.close();
				js.close();
			}
			if( socket instanceof JxtaServerSocket ){
				JxtaServerSocket js = (JxtaServerSocket) socket;
				js.close();
			}
			if( socket instanceof JxtaMulticastSocket ){
				JxtaMulticastSocket js = (JxtaMulticastSocket) socket;
				js.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
