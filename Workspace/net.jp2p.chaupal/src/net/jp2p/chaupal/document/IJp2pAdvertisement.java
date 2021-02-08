/**
 * The JXTA protocols often need to refer to peers, peer groups, pipes and other JXTA resources. These references are presented in
 * the protocols as JXTA IDs. JXTA IDs are a means for uniquely identifying specific peer groups, peers, pipes, codat and service
 * instances. JXTA IDs provide unambiguous references to the various JXTA entities. There are six types of JXTA entities which
 * have JXTA ID types defined: peergroups, peers, pipes, codats, module classes and module specifications. Additional JXTA ID
 * types may be defined in the future.
 * JXTA IDs are normally presented as URNs. URNs are a form of URI that ‘... are intended to serve as persistent, locationindependent,
 * resource identifiers’. Like other forms of URI, JXTA IDs are presented as text. See IETF RFC 2141 RFC2141 for
 * more information on URNs.
 * 
 * @See: JXTA v2.0 Protocols Specification, Chapter 1
 * @author keesp
 * @Organisation: chaupal.org 
 * 
 * Copyright 2020: Apache 2.0 License
 *
*/
package net.jp2p.chaupal.document;

import org.w3c.dom.Document;

import javax.ws.rs.core.MediaType;
import javax.xml.crypto.dsig.XMLSignature;

import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.membership.pse.IPSECredential;

public interface IJp2pAdvertisement extends Cloneable{

	/**
	 *  Returns the identifying type of this Advertisement. Unlike
	 *  {@link #getAdvertisementType()} this method will return the correct
	 *  runtime type of an Advertisement object.
	 *  <p/>
	 *  This implementation is provided for existing advertisements which do not
	 *  provide their own implementation. In most cases you should provide your
	 *  own implementation for efficiency reasons.
	 *
	 *  @since JXSE 2.1.1
	 *  @return The identifying type of this Advertisement.
	 */
	String getAdvType();

	/**
	 *  Write this advertisement into a document of the requested type. Two 
	 *  standard document forms are defined. <code>"text/plain"</code> encodes 
	 *  the document in a "pretty-print" format for human viewing and 
	 *  <code>"text/xml"<code> which provides an XML format.
	 *
	 *  @param asMimeType MimeMediaType format representation requested.
	 *  @return The {@code Advertisement} represented as a {@code Document} of
	 *  the requested MIME Media Type.
	 */
	Document getDocument(MediaType asMimeType);

	/**
	 *  Returns an ID which identifies this {@code Advertisement} as uniquely as 
	 *  possible. This ID is typically used as the primary key for indexing of
	 *  the Advertisement within databases. 
	 *  <p/>
	 *  Each advertisement sub-class must choose an appropriate implementation
	 *  which returns canonical and relatively unique ID values for it's
	 *  instances. Since this ID is commonly used for indexing, the IDs returned
	 *  must be as unique as possible to avoid collisions. The value for the ID 
	 *  returned can either be:
	 *  <p/>
	 *  <ul>
	 *      <li>An ID which is already part of the advertisement definition
	 *      and is relatively unique between advertisements instances. For
	 *      example, the Peer Advertisement returns the Peer ID.</li>
	 *
	 *      <li>A static CodatID which is generated via some canonical process
	 *      which will produce the same value each time and different values for
	 *      different advertisements of the same type.</li>
	 *
	 *      <li>ID.nullID for advertisement types which are not readily indexed.
	 *      </li>
	 *  </ul>
	 *  <p/>
	 *  For Advertisement types which normally return non-ID.nullID values
	 *  no ID should be returned when asked to generate an ID while the
	 *  Advertisement is an inconsistent state (example: uninitialized index
	 *  fields). Instead {@link java.lang.IllegalStateException} should be
	 *  thrown.
	 *
	 *  @return An ID that relatively uniquely identifies this advertisement 
	 *  or {@code ID.nullID} if this advertisement is of a type that is not 
	 *  normally indexed.
	 */
	IJp2pID getID();

	/**
	 * Returns the element names on which this advertisement should be indexed.
	 *
	 * @return The element names on which this advertisement should be indexed.
	 */
	String[] getIndexFields();

	/**
	 * This method returns the original document appended with an XML signature element, if it exists.
	 * Before an advertisement is signed, the method returns the original document. After the
	 * advertisement is signed successfully, the method returns the original document appended with
	 * a valid XML signature element. For a newly discovered advertisement, the method may return
	 * the original document with or without an XML signature element attached, depending on whether
	 * the original publisher has signed it or not. If there is an XML signature attached, the verify()
	 * method must return true before you can trust the advertisement.
	 *
	 * When the XML signature element is not attached, the method is equivalent to
	 * getDocument(MimeMediaType.XMLUTF8).
	 *
	 * The abstract method getDocument(MimeMediaType asMimeType) is used only for the subclass to
	 * implement the advertisement. The getSignedDocument() is used whenever you want to get the
	 * document out of the advertisement.
	 *
	 * @return The document with an XML signature, if it exists.
	 */
	Document getSignedDocument();

	/**
	 * A valid signature when an advertisement is signed or verified successfully. Otherwise, it
	 * returns null.
	 *
	 * @return A valid signature or null.
	 */
	XMLSignature getSignature();

	/**
	 * A successful sign() or verify() makes isAuthenticated() return true.
	 *
	 * @return true, when sign() or verify() succeeds. Otherwise, false.
	 */
	boolean isAuthenticated();

	/**
	 * Signing peer is member of PSEMemebershipService default keystore
	 * 
	 * @return true if signing peer is member of PSEMemebershipService default keystore
	 */
	boolean isMember();

	/**
	 * Wrapped publickey in advertisement matches the public key of the peerid in
	 * the PSEMemebershipService keystore
	 *
	 * @return true if wrapped key matches the public key of the peerid
	 */
	boolean isCorrectMembershipKey();

	/**
	 * The public key in the certificate and the private key are used to sign the advertisement.
	 *
	 * @param paraCert The signer's certificate (public key)
	 * @param paraPrivateKey The signer's private key.
	 * @return true, if the signing succeeds. Otherwise, true.
	 */
	boolean sign(IPSECredential pseCredential, boolean includePublicKey, boolean includePeerID);

	/**
	 * This method is used on a newly discovered advertisement, which may or may not bear a
	 * a valid or invalid signature. If the signature and/or the advertisement has been tampered with,
	 * the method returns false. If the advertisement is intact after it is signed and published, the method
	 * is supposed to return true.
	 *
	 * @return true, when the signature is verified. Otherwise, false.
	 */
	boolean verify(IPSECredential pseCredential, boolean verifyKeyWithKeystore);

}