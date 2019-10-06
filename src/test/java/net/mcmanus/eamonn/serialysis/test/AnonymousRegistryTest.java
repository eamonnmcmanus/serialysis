/*
 * Copyright 2007 Éamonn McManus
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.mcmanus.eamonn.serialysis.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObject;
import java.util.List;
import junit.framework.*;
import net.mcmanus.eamonn.serialysis.SBlockData;
import net.mcmanus.eamonn.serialysis.SObject;
import net.mcmanus.eamonn.serialysis.SEntity;
import net.mcmanus.eamonn.serialysis.SerialScan;

public class AnonymousRegistryTest extends TestCase {

    public AnonymousRegistryTest(String testName) {
        super(testName);
    }

    /*
     * Test that we can determine what port an RMI Registry is running on
     * when it was created using an anonymous port number (port 0).
     * The serialized form of an RMI registry stub is described at
     * http://java.sun.com/javase/6/docs/api/serialized-form.html#java.rmi.server.RemoteObject
     * We don't have any socket factories, so we expect it to use the
     * documented UnicastRef form.
     */
    public void testAnonymousRegistry() throws Exception {
        // Make the registry
        Registry reg = LocateRegistry.createRegistry(0);
        
        // Convert it to a stub
        Remote stub = RemoteObject.toStub(reg);

        // Analyze the serial form of the stub
        SObject sstub = (SObject) SerialScan.examine(stub); // (SObject) ss.readObject();
        System.out.println(sstub);

        // The interesting data is in the "annotations", i.e. the data
        // written by the writeObject method as documented.
        List<SEntity> annots = sstub.getAnnotations();
        assertEquals(1, annots.size());
        SBlockData sdata = (SBlockData) annots.get(0);
        DataInputStream din = sdata.getDataInputStream();

        // Read the UnicastRef encoding

        // Type string
        String unicastref = din.readUTF();
        assertEquals("UnicastRef", unicastref);

        // Host address
        String host = din.readUTF();
        // Test that this is indeed a local address by creating a ServerSocket.
        // That will fail if this is not a valid address or if it is remote.
        InetAddress addr = InetAddress.getByName(host);
        ServerSocket socket = new ServerSocket(0, 0, addr);
        socket.close();

        // Port (this is what we would be after if we were using this to
        // discover the port number assigned by the kernel).
        int port = din.readInt();
        assertTrue(port != 0);
        System.out.println("The port is " + port);

        // ObjID, should be the registry id.
        ObjectInput idin = new DataObjectInput(din);
        ObjID objID = ObjID.read(idin);
        assertEquals(new ObjID(ObjID.REGISTRY_ID), objID);

        // Boolean value false
        boolean b = din.readBoolean();
        assertFalse(b);

        // Check we've exhausted the data
        assertEquals(-1, din.read());
    }

    /* Annoyingly, ObjID.read takes an ObjectInput parameter even though
     * it never calls ObjectInput.readObject().  It could just as easily
     * declare the parent interface DataInput, and we wouldn't be obliged
     * to have the hack below.
     */
    private static class DataObjectInput extends DataInputStream
            implements ObjectInput {
        DataObjectInput(InputStream in) {
            super(in);
        }

        public Object readObject() {
            throw new UnsupportedOperationException();
        }
    }
}
