package net.jp2p.chaupal.peer;

import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroupID;

public interface IJp2pPeerID extends IJp2pID{

	/**
	 *  {@inheritDoc}
	 */
	IJp2pPeerID intern();

	/**
	 *  Returns PeerGroupID of the Peer Group to which this Peer ID belongs.
	 *
	 *  @return PeerGroupID of the Peer Group to which this Peer ID belongs.
	 */
	IJp2pPeerGroupID getPeerGroupID();

}