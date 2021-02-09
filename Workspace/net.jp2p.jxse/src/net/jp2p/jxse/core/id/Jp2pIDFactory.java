package net.jp2p.jxse.core.id;

import java.net.URI;

import net.jp2p.chaupal.id.IJp2pID;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;

public class Jp2pIDFactory<I extends ID> {

	public static IJp2pID create(URI uri) {
		return new Jp2pID<ID>( IDFactory.create(uri));
	}
	
}
