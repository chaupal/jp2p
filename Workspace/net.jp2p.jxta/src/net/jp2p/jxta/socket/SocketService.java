package net.jp2p.jxta.socket;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.jp2p.container.component.AbstractJp2pService;
import net.jp2p.jxta.socket.SocketPropertySource.SocketProperties;
import net.jp2p.jxta.socket.SocketPropertySource.SocketTypes;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaMulticastSocket;
import net.jxta.socket.JxtaServerSocket;
import net.jxta.socket.JxtaSocket;

public class SocketService<T extends PipeMsgListener> extends AbstractJp2pService<T>{

	private PeerGroup peerGroup;
	private PipeAdvertisement pipead;
	private ExecutorService executor;
	private Runnable runnable;
	
	public SocketService( SocketPropertySource source, PeerGroup peerGroup, PipeAdvertisement pipead ) {
		super( source, null );
		this.pipead = pipead;
		this.peerGroup = peerGroup;
		executor = Executors.newCachedThreadPool();
	}

	/**
	 * Get the correct server socket by selecting the correct constructor
	 * @return
	 * @throws IOException
	 */
	protected JxtaSocket getSocket( PipeAdvertisement pipeAdv) throws IOException{
		int time_out = (Integer) super.getPropertySource().getProperty( SocketProperties.TIME_OUT );
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
		SocketPropertySource source = (SocketPropertySource) super.getPropertySource();
		int back_log = (Integer) source.getProperty( SocketProperties.BACKLOG );
		int time_out = (Integer) source.getProperty( SocketProperties.TIME_OUT );
		boolean encrypt = (Boolean)source.getProperty( SocketProperties.ENCRYPT );
		if(( back_log <= 0 ) && ( time_out <= 0 ))
			return new JxtaServerSocket( peerGroup, pipeAdv, encrypt );
		if(( time_out <= 0 ))
			return new JxtaServerSocket( peerGroup, pipeAdv, back_log, encrypt );
		else
			return new JxtaServerSocket( peerGroup, pipeAdv, back_log, time_out, encrypt );			
	}

	/**
	 * Activate the super class
	 */
	protected final void superActivate(){
		super.activate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected final void activate() {
		runnable = new Runnable(){

			@Override
			public void run() {
				T socket=  null;
				try{
					SocketTypes type = SocketPropertySource.getSocketType((SocketPropertySource) getPropertySource());
					
					try {
						switch( type ){
						case CLIENT:
							socket = (T) getSocket( pipead);
							break;
						case SERVER:
							socket = (T) getServerSocket( pipead);
							break;
						case MULTICAST:
							socket = (T) new JxtaMulticastSocket( peerGroup, pipead );
							break;
						}
					}
					catch( Exception ex ){
						ex.printStackTrace();
					}
					setModule( socket );
					superActivate();
				}
				catch( Exception ex ){
					ex.printStackTrace();
				}
			}
		};
		executor.execute( runnable);
	}

	@Override
	protected void deactivate() {
		T socket = super.getModule();
		if( socket == null )
			return;
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
