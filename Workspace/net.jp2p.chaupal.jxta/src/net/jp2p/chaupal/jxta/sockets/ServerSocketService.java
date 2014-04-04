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

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.jp2p.container.component.AbstractJp2pService;
import net.jp2p.container.utils.IOUtils;
import net.jxta.socket.JxtaServerSocket;

public class ServerSocketService extends AbstractJp2pService<JxtaServerSocket> implements Runnable{

	private ExecutorService service;
	
	public ServerSocketService(JxtaServerSocket component ) {
		super( null);//component );
		service = Executors.newCachedThreadPool();
	}

	@Override
	protected void activate() {
		service.execute(this);
	}

	@Override
	protected void deactivate() {
		service.shutdown();
		JxtaServerSocket serverSocket = super.getModule();
		IOUtils.closeSocket(serverSocket);
	}

	@Override
	protected boolean onInitialising() {
		//JxtaServerSocketFactory factory =  ( JxtaServerSocketFactory )super.getFactory();
		//IJxtaServiceComponent<JxtaServerSocket> socketComp = 
		//		(IJxtaServiceComponent<JxtaServerSocket>) factory.getComponent( Services.SERVERSOCKET.toString());
		return super.onInitialising();
	}

	@Override
	public void run() {
		System.out.println("Starting ServerSocket");
		JxtaServerSocket serverSocket = super.getModule();
		while ( super.isActive()) {
			try {
				System.out.println("Waiting for connections");
				Socket socket = serverSocket.accept();
				if (socket != null) {
					System.out.println("New socket connection accepted");
					this.service.execute( new ConnectionHandler(socket));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class ConnectionHandler implements Runnable {
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
