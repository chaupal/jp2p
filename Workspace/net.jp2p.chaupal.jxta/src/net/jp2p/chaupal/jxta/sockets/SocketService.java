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
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.jp2p.container.component.AbstractJp2pService;
import net.jp2p.container.utils.IOUtils;
import net.jxta.refplatform.platform.NetworkManager;
import net.jxta.socket.JxtaSocket;

public class SocketService extends AbstractJp2pService<JxtaSocket> implements Runnable{

	/**
	* number of runs to make
	*/
	//private final static long RUNS = 8;
	/**
	* number of iterations to send the payload
	*/
	private final static long ITERATIONS = 1000;
	/**
	* payload size
	*/
	private final static int PAYLOADSIZE = 64 * 1024;
	
	private NetworkManager nms;
	private JxtaSocket socketService;
	//private IJxtaServiceComponent<PipeAdvertisement> pipeAdService;

	private ExecutorService service;
	
	public SocketService( JxtaSocket component ) {
		super( null );//component );
		service = Executors.newCachedThreadPool();
	}

	@Override
	protected void activate() {
		service.execute(this);
	}

	@Override
	protected void deactivate() {
		service.shutdown();
		JxtaSocket serverSocket = super.getModule();
		IOUtils.closeSocket(serverSocket);
	}	
	
	@Override
	public void run() {
		System.out.println("Starting Client Socket");
		boolean waitForRendezvous = true;//(boolean) socketService.getProperty( JxtaSocketFactory.Properties.WAIT_FOR_RENDEZ_VOUS );
		NetworkManager manager = nms;
		try {
			if (waitForRendezvous) {
				manager.waitForRendezvousConnection(0);
			}
			long start = System.currentTimeMillis();
			System.out.println("Connecting to the server");
			JxtaSocket socket = socketService;
			if( socket == null )
				return;
			
			// get the socket output stream
			OutputStream out = socket.getOutputStream();
			DataOutput dos = new DataOutputStream(out);
			// get the socket input stream
			InputStream in = socket.getInputStream();
			DataInput dis = new DataInputStream(in);
			long total = ITERATIONS * PAYLOADSIZE * 2;
			System.out.println("Sending/Receiving " + total + " bytes.");
			dos.writeLong(ITERATIONS);
			dos.writeInt(PAYLOADSIZE);
			long current = 0;
			while (current < ITERATIONS) {
				byte[] out_buf = new byte[PAYLOADSIZE];
				byte[] in_buf = new byte[PAYLOADSIZE];
				Arrays.fill(out_buf, (byte) current);
				out.write(out_buf);
				out.flush();
				dis.readFully(in_buf);
				assert Arrays.equals(in_buf, out_buf);
				current++;
			}
			out.close();
			in.close();
			long finish = System.currentTimeMillis();
			long elapsed = finish - start;
			System.out.println(MessageFormat.format("EOT. Processed {0} bytes in {1} ms. Throughput = {2} KB/sec.",
							total, elapsed, (total / elapsed) * 1000 /
							1024));
			socket.close();
			System.out.println("Socket connection closed");
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
}
