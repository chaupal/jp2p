/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxta.sockets;

import net.jp2p.container.utils.IOUtils;
import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.refplatform.platform.NetworkManager;
import net.jxta.socket.JxtaServerSocket;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

/**
 * This tutorial illustrates the use JxtaServerSocket It creates a
 * JxtaServerSocket with a back log of 10. it also blocks indefinitely, until a
 * connection is established.
 * <p/>
 * Once a connection is established data is exchanged with the initiator.
 * The initiator will provide an iteration count and buffer size. The peers will
 * then read and write buffers. (or write and read for the initiator).
 */
public class SocketServer {
	private transient PeerGroup netPeerGroup = null;
	public final static String SOCKETIDSTR = "urn:jxta:uuid-59616261646162614E5047205032503393B5C2F6CA7A41FBB0F890173088E79404";
	
	public SocketServer() throws IOException, PeerGroupException {
		NetworkManager manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC,
				"SocketServer",
				new File(new File(".cache"), "SocketServer").toURI());
		manager.startNetwork();
		netPeerGroup = manager.getNetPeerGroup();
	}
	
	public static PipeAdvertisement createSocketAdvertisement() {
		PipeID socketID = null;
		try {
			socketID = (PipeID) IDFactory.fromURI(new URI(SOCKETIDSTR));
		} catch (URISyntaxException use) {
			use.printStackTrace();
		}
		PipeAdvertisement advertisement = (PipeAdvertisement)

				AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(socketID);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("Socket tutorial");
		return advertisement;
	}
	
	/**
	 * Wait for connections
	 */
	public void run() {
		System.out.println("Starting ServerSocket");
		JxtaServerSocket serverSocket = null;
		try {
			serverSocket = new JxtaServerSocket(netPeerGroup, createSocketAdvertisement(), 10);
			serverSocket.setSoTimeout(0);
		} catch (IOException e) {
			System.out.println("failed to create a server socket");
			IOUtils.closeSocket( serverSocket );
			e.printStackTrace();
			System.exit(-1);
		}
		while (true) {
			try {
				System.out.println("Waiting for connections");
				Socket socket = serverSocket.accept();
				if (socket != null) {
					System.out.println("New socket connection accepted");
					Thread thread = new Thread(new ConnectionHandler(socket),
							"Connection Handler Thread");
					thread.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ConnectionHandler implements Runnable {
		Socket socket = null;
		ConnectionHandler(Socket socket) {
			this.socket = socket;
		}
		
		/**
		 * Sends data over socket
		 *
		 * @param socket the socket
		 */
		private void sendAndReceiveData(Socket socket) {
			try {
				long start = System.currentTimeMillis();
				// get the socket output stream

				OutputStream out = socket.getOutputStream();
				// get the socket input stream
				InputStream in = socket.getInputStream();
				DataInput dis = new DataInputStream(in);
				long iterations = dis.readLong();
				int size = dis.readInt();
				long total = iterations * size * 2L;
				long current = 0;
				System.out.println(MessageFormat.format("Sending/Receiving {0} bytes.", total));
				while (current < iterations) {
					byte[] buf = new byte[size];
					dis.readFully(buf);
					out.write(buf);
					out.flush();
					current++;
				}
				out.close();
				in.close();
				long finish = System.currentTimeMillis();
				long elapsed = finish - start;
				System.out.println(MessageFormat.format("EOT. Received {0} bytes in {1} ms. Throughput = {2} KB/sec.",
						total, elapsed, (total / elapsed) * 1000 / 1024));
				socket.close();
				System.out.println("Connection closed");
			} catch (Exception ie) {
				ie.printStackTrace();
			}
		}
		@Override
		public void run() {
			sendAndReceiveData(socket);
		}
	}
	
	/**
	 * main
	 *
	 * @param args command line args
	 */
	public static void main(String args[]) {
		/*
	System.setProperty("net.jxta.logging.Logging", "FINEST");
	System.setProperty("net.jxta.level", "FINEST");
	System.setProperty("java.util.logging.config.file", "logging.properties");
		 */
		try {
			Thread.currentThread().setName(SocketServer.class.getName() + ".main()");
			SocketServer socEx = new SocketServer();
			socEx.run();
		} catch (Throwable e) {
			System.err.println("Failed : " + e);
			e.printStackTrace(System.err);
			System.exit(-1);
		}
	}
}