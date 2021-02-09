package net.jp2p.jxse.core.peer;

import net.jp2p.chaupal.peer.IJp2pPeerID;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroupID;
import net.jp2p.jxse.core.id.Jp2pID;
import net.jp2p.jxse.core.id.Jp2pPeerGroupID;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

public class Jp2pPeerID extends Jp2pID<PeerID > implements IJp2pPeerID {
	private static final long serialVersionUID = 4852037930913800749L;

	public Jp2pPeerID(PeerID id) {
		super(id);
	}

	@Override
	public PeerID getID() {
		return super.getID();
	}

	@Override
	public IJp2pPeerID intern() {
		return (IJp2pPeerID) new Jp2pPeerID( getID().intern() );
	}

    /**
     *  Returns PeerGroupID of the Peer Group to which this Peer ID belongs.
     *
     *  @return PeerGroupID of the Peer Group to which this Peer ID belongs.
     */
	public IJp2pPeerGroupID getPeerGroupID() {
		return new Jp2pPeerGroupID( (PeerGroupID) getID().getPeerGroupID());
	}
}
