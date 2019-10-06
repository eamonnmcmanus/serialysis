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
package net.mcmanus.eamonn.serialysis;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import static net.mcmanus.eamonn.serialysis.Trace.enter;
import static net.mcmanus.eamonn.serialysis.Trace.exit;
import static net.mcmanus.eamonn.serialysis.Trace.show;

class TraceObjectInputStream extends ObjectInputStream {
    public TraceObjectInputStream(InputStream is) throws IOException {
        super(is);
    }

    protected Object resolveObject(Object obj) throws IOException {
        enter("ois.resolveObject", obj);
        Object retValue;
        
        retValue = super.resolveObject(obj);
        exit(retValue);
        return retValue;
    }

    protected Class<?> resolveClass(java.io.ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        enter("ois.resolveClass", desc);
        Class retValue;
        
        retValue = super.resolveClass(desc);
        exit(retValue);
        return retValue;
    }

    protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
        enter("ois.resolveProxyClass", Arrays.toString(interfaces));
        Class retValue;
        
        retValue = super.resolveProxyClass(interfaces);
        exit(retValue);
        return retValue;
    }

    public void readFully(byte[] buf, int off, int len) throws IOException {
        enter("ois.readFully", "byte[" + buf.length + "]", off, len);
        super.readFully(buf, off, len);
        showBuf(buf, off, len);
        exit();
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        enter("ois.read", "byte[" + buf.length + "]", off, len);
        int retValue;
        
        retValue = super.read(buf, off, len);
        showBuf(buf, off, retValue);
        exit(retValue);
        return retValue;
    }

    public void readFully(byte[] buf) throws IOException {
        enter("ois.readFully", "byte[" + buf.length + "]");
        super.readFully(buf);
        showBuf(buf, 0, buf.length);
        exit();
    }

    public int read(byte[] b) throws IOException {
        enter("ois.read");
        int retValue;
        
        retValue = super.read(b);
        showBuf(b, 0, retValue);
        return retValue;
    }

    public int skipBytes(int len) throws IOException {
        enter("ois.skipBytes", len);
        int retValue;
        
        retValue = super.skipBytes(len);
        exit(retValue);
        return retValue;
    }

    public long skip(long n) throws IOException {
        enter("ois.skip", n);
        long retValue;
        
        retValue = super.skip(n);
        exit(retValue);
        return retValue;
    }

    public void registerValidation(java.io.ObjectInputValidation obj, int prio) throws java.io.NotActiveException, java.io.InvalidObjectException {
        enter("ois.registerValidation", obj, prio);
        super.registerValidation(obj, prio);
        exit();
    }

    public int readInt() throws IOException {
        enter("ois.readInt");
        int retValue;
        
        retValue = super.readInt();
        exit(retValue);
        return retValue;
    }

    public float readFloat() throws IOException {
        enter("ois.readFloat");
        float retValue;
        
        retValue = super.readFloat();
        exit(retValue);
        return retValue;
    }

    public java.io.ObjectInputStream.GetField readFields() throws IOException, ClassNotFoundException {
        enter("ois.readFields");
        java.io.ObjectInputStream.GetField retValue;
        
        retValue = super.readFields();
        exit(retValue);
        return retValue;
    }

    public double readDouble() throws IOException {
        enter("ois.readDouble");
        double retValue;
        
        retValue = super.readDouble();
        exit(retValue);
        return retValue;
    }

    protected java.io.ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        enter("ois.readClassDescriptor");
        java.io.ObjectStreamClass retValue;
        
        retValue = super.readClassDescriptor();
        exit(retValue);
        return retValue;
    }

    public char readChar() throws IOException {
        enter("ois.readChar");
        char retValue;
        
        retValue = super.readChar();
        exit(retValue);
        return retValue;
    }

    public byte readByte() throws IOException {
        enter("ois.readByte");
        byte retValue;
        
        retValue = super.readByte();
        exit(retValue);
        return retValue;
    }

    public boolean readBoolean() throws IOException {
        enter("ois.readBoolean");
        boolean retValue;
        
        retValue = super.readBoolean();
        exit(retValue);
        return retValue;
    }

    public int read() throws IOException {
        enter("ois.read");
        int retValue;
        
        retValue = super.read();
        exit(retValue);
        return retValue;
    }

    @Deprecated
    public String readLine() throws IOException {
        enter("ois.readLine");
        String retValue;
        
        retValue = super.readLine();
        exit(retValue);
        return retValue;
    }

    public long readLong() throws IOException {
        enter("ois.readLong");
        long retValue;
        
        retValue = super.readLong();
        exit(retValue);
        return retValue;
    }

    protected Object readObjectOverride() throws IOException, ClassNotFoundException {
        enter("ois.readObjectOverride");
        Object retValue;
        
        retValue = super.readObjectOverride();
        exit(retValue);
        return retValue;
    }

    public short readShort() throws IOException {
        enter("ois.readShort");
        short retValue;
        
        retValue = super.readShort();
        exit(retValue);
        return retValue;
    }

    protected void readStreamHeader() throws IOException, java.io.StreamCorruptedException {
        enter("ois.readStreamHeader");
        super.readStreamHeader();
        exit();
    }

    public String readUTF() throws IOException {
        enter("ois.readUTF");
        String retValue;
        
        retValue = super.readUTF();
        exit(retValue);
        return retValue;
    }

    public Object readUnshared() throws IOException, ClassNotFoundException {
        enter("ois.readUnshared");
        Object retValue;
        
        retValue = super.readUnshared();
        exit(retValue);
        return retValue;
    }

    public int readUnsignedByte() throws IOException {
        enter("ois.readUnsignedByte");
        int retValue;
        
        retValue = super.readUnsignedByte();
        exit(retValue);
        return retValue;
    }

    public int readUnsignedShort() throws IOException {
        enter("ois.readUnsignedShort");
        int retValue;
        
        retValue = super.readUnsignedShort();
        exit(retValue);
        return retValue;
    }
    
    private static void showBuf(byte[] b, int off, int len) {
        byte[] copy = new byte[len];
        System.arraycopy(b, off, copy, 0, len);
        show(Arrays.toString(copy));
    }
}
