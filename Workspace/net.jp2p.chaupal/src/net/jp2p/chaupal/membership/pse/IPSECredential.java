package net.jp2p.chaupal.membership.pse;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;

import javax.ws.rs.core.MediaType;

import org.w3c.dom.Document;

import net.jp2p.chaupal.exception.Jp2pPeerGroupException;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.module.IJp2pService;

public interface IPSECredential {

	/**
	 * Add a listener
	 *
	 * @param listener the listener
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Add a listener
	 *
	 * @param propertyName the property to watch
	 * @param listener     the listener
	 */
	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

	/**
	 * Remove a listener
	 *
	 * @param listener the listener
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Remove a listener
	 *
	 * @param propertyName the property which was watched
	 * @param listener     the listener
	 */
	void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

	/**
	 * {@inheritDoc}
	 */
	IJp2pID getPeerGroupID();

	/**
	 * {@inheritDoc}
	 */
	IJp2pID getPeerID();

	/**
	 * {@inheritDoc}
	 * <p/>
	 * A PSE Credential is valid as long as the associated certificate is
	 * valid.
	 */
	boolean isExpired();

	/**
	 * {@inheritDoc}
	 * <p/>
	 * A PSE Credential is valid as long as the associated certificate is
	 * valid and as long as the membership service still has the credential.
	 */
	boolean isValid();

	/**
	 * {@inheritDoc}
	 */
	Object getSubject();

	/**
	 * {@inheritDoc}
	 */
	IJp2pService getSourceService();

	/**
	 * {@inheritDoc}
	 */
	Document getDocument(MediaType encodeAs) throws Exception;

	/**
	 * Returns the certificate associated with this credential.
	 *
	 * @return the certificate associated with this credential.
	 */
	X509Certificate getCertificate();

	/**
	 * Returns the certificate chain associated with this credential.
	 *
	 * @return the certificate chain associated with this credential.
	 */
	X509Certificate[] getCertificateChain();

	PrivateKey getPrivateKey();

	/**
	 * Returns the key id associated with this credential, if any. Only locally
	 * generated credentials have a key ID.
	 *
	 * @return Returns the key id associated with this credential, if any.
	 */
	IJp2pID getKeyID();

	/**
	 * Get a Signature object based upon the private key associated with this
	 * credential.
	 *
	 * @param algorithm the signing algorithm to use.
	 * @return Signature.
	 */
	Signature getSigner(String algorithm) throws NoSuchAlgorithmException;

	/**
	 * /**
	 * Get a Signature verifier object based upon the certificate associated
	 * with this credential.
	 *
	 * @param algorithm the signing algorithm to use.
	 * @return Signature.
	 */
	Signature getSignatureVerifier(String algorithm) throws NoSuchAlgorithmException;

	X509Certificate[] generateServiceCertificate(IJp2pID assignedID)
			throws IOException, KeyStoreException, InvalidKeyException, SignatureException;

	IPSECredential getServiceCredential(IJp2pID assignedID)
			throws IOException, Jp2pPeerGroupException, InvalidKeyException, SignatureException;

}