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
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL CHAUPAL 
 *  MICROSYSTEMS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * @See: JXTA v2.0 Protocols Specification, Chapter 1
 * @author keesp
 * @Organisation: chaupal.org 
 * 
 *******************************************************************************
 * Copyright (c) 2014-2021 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************
*/
package net.jp2p.chaupal.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class maintains a {@link java.util.List} of all possible {@link
 * java.lang.Throwable causes} that may be generated as a part of the
 * configuration process.
 *
 * @author james todd [gonzo at jxta dot org]
 */

public class Jp2pConfiguratorException extends Exception {
	private static final long serialVersionUID = 1L;
	private List<Throwable> causes = null;

    /**
     * Constucts a {@link JxtaException} with no specified details.
     */
    public Jp2pConfiguratorException() {
        super();
    }

    /**
     * Constructs a {@link JxtaException} with the specified message.
     *
     * @param msg message
     */
    public Jp2pConfiguratorException(String msg) {
        super(msg);
    }

    /**
     * Constructs a {@link JxtaException} with the specified {@link
     * java.lang.Throwable cause}.
     *
     * @param ex cause
     */
    public Jp2pConfiguratorException(Throwable ex) {
        super();

        addCause(ex);
    }

    /**
     * Constructs a {@link JxtaException} with the specified message and {@link
     * java.lang.Throwable cause}.
     *
     * @param msg message
     * @param ex  cause
     */
    public Jp2pConfiguratorException(String msg, Throwable ex) {
        super(msg);

        addCause(ex);
    }

    /**
     * Constructs a {@link JxtaException} with the specified {@link
     * java.util.List} of {@link java.lang.Throwable causes}.
     *
     * @param ex causes
     */
    public Jp2pConfiguratorException(List<Throwable> ex) {
        super();

        addCauses(ex);
    }

    /**
     * Constructs a {@link JxtaException} with the specified message in
     * addition to the {@link java.util.List} of {@link java.lang.Throwable
     * causes}.
     *
     * @param msg message
     * @param ex  causes
     */
    public Jp2pConfiguratorException(String msg, List<Throwable> ex) {
        super(msg);

        addCauses(ex);
    }

    /**
     * Retrieve the {@link java.lang.Throwable causes} as a {@link
     * java.util.List}.
     *
     * @return The causes
     */
    public List<Throwable> getCauses() {
        return this.causes != null ? this.causes : Collections.<Throwable>emptyList();
    }

    /**
     * Add a cause of type {@link java.lang.Throwable}.
     *
     * @param c The cause
     */
    public void addCause(Throwable c) {
        if (c != null) {
            if (this.causes == null) {
                this.causes = new ArrayList<Throwable>();
            }

            this.causes.add(c);
        }
    }

    /**
     * Add a {@link java.util.List} of {@link java.lang.Throwable causes}.
     *
     * @param c The causes
     */
    public void addCauses(List<Throwable> c) {
        if (c != null) {
            for (Object aC : c) {
                addCause((Throwable) aC);
            }
        }
    }

    /**
     * @inheritDoc <p/>Overload printStackTrace() to support multiple {@link java.lang.Throwable causes}.
     */
    @Override
    public void printStackTrace() {
        super.printStackTrace();

        for (Object o : getCauses()) {
            ((Throwable) o).printStackTrace();
        }
    }

    /**
     * @param ps Description of the Parameter
     * @inheritDoc <p/>Overload printStackTrace({@link java.io.PrintStream}) to support
     * multiple {@link java.lang.Throwable causes}.
     */
    @Override
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);

        ps.println("Caused by:");
        int count = 1;

        for (Object o : getCauses()) {
            Throwable t = (Throwable) o;

            ps.print("Cause #" + count++ + " : ");

            t.printStackTrace(ps);
        }
    }

    /**
     * @param pw Description of the Parameter
     * @inheritDoc <p/>Overload printStackTrace({@link java.io.PrintWriter}) to support
     * multiple {@link java.lang.Throwable causes}.
     */
    @Override
    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);

        pw.println("Caused by:");
        int count = 1;

        for (Object o : getCauses()) {
            Throwable t = (Throwable) o;

            pw.print("Cause #" + count++ + " : ");

            t.printStackTrace(pw);
        }
    }
}

