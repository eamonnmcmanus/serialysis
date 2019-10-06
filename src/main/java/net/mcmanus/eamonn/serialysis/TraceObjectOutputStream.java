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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static net.mcmanus.eamonn.serialysis.Trace.enter;
import static net.mcmanus.eamonn.serialysis.Trace.exit;

class TraceObjectOutputStream extends ObjectOutputStream {
    public TraceObjectOutputStream(OutputStream os) throws IOException {
        super(os);
    }

    protected Object replaceObject(Object obj) throws IOException {
        enter("oos.replaceObject", obj);
        Object retValue;

        retValue = super.replaceObject(obj);
        exit(retValue);
        return retValue;
    }

    public boolean equals(Object obj) {
        enter("oos.equals", obj);
        boolean retValue;

        retValue = super.equals(obj);
        exit(retValue);
        return retValue;
    }

    protected void writeClassDescriptor(java.io.ObjectStreamClass desc) throws IOException {
        enter("oos.writeClassDescriptor", desc);
        super.writeClassDescriptor(desc);
        exit();
    }

    protected boolean enableReplaceObject(boolean enable) throws SecurityException {
        enter("oos.enableReplaceObject", enable);
        boolean retValue;

        retValue = super.enableReplaceObject(enable);
        exit(retValue);
        return retValue;
    }

    public void writeBoolean(boolean val) throws IOException {
        enter("oos.writeBoolean", val);
        super.writeBoolean(val);
        exit();
    }

    public void writeDouble(double val) throws IOException {
        enter("oos.writeDouble", val);
        super.writeDouble(val);
        exit();
    }

    public void writeFloat(float val) throws IOException {
        enter("oos.writeFloat", val);
        super.writeFloat(val);
        exit();
    }

    public void writeUTF(String str) throws IOException {
        enter("oos.writeUTF", str);
        super.writeUTF(str);
        exit();
    }

    public void writeBytes(String str) throws IOException {
        enter("oos.writeBytes", str);
        super.writeBytes(str);
        exit();
    }

    public void writeChars(String str) throws IOException {
        enter("oos.writeChars", str);
        super.writeChars(str);
        exit();
    }

    public void write(byte[] buf, int off, int len) throws IOException {
        enter("oos.write", Arrays.toString(buf), off, len);
        super.write(buf, off, len);
        exit();
    }

    public void write(byte[] buf) throws IOException {
        enter("oos.write", Arrays.toString(buf));
        super.write(buf);
        exit();
    }

    public void writeShort(int val) throws IOException {
        enter("oos.writeShort", val);
        super.writeShort(val);
        exit();
    }

    public void writeInt(int val) throws IOException {
        enter("oos.writeInt", val);
        super.writeInt(val);
        exit();
    }

    public void useProtocolVersion(int version) throws IOException {
        enter("oos.useProtocolVersion", version);
        super.useProtocolVersion(version);
        exit();
    }

    public void write(int val) throws IOException {
        enter("oos.write", val);
        super.write(val);
        exit();
    }

    public void writeByte(int val) throws IOException {
        enter("oos.writeByte", val);
        super.writeByte(val);
        exit();
    }

    public void writeChar(int val) throws IOException {
        enter("oos.writeChar", val);
        super.writeChar(val);
        exit();
    }

    public void writeLong(long val) throws IOException {
        enter("oos.writeLong", val);
        super.writeLong(val);
        exit();
    }

    protected void writeStreamHeader() throws IOException {
        enter("oos.writeStreamHeader");
        super.writeStreamHeader();
        exit();
    }

    public void writeFields() throws IOException {
        enter("oos.writeFields");
        super.writeFields();
        exit();
    }

    public java.io.ObjectOutputStream.PutField putFields() throws IOException {
        enter("oos.putFields");
        java.io.ObjectOutputStream.PutField retValue;

        retValue = super.putFields();
        exit(retValue);
        return retValue;
    }

    public int hashCode() {
        enter("oos.hashCode");
        int retValue;

        retValue = super.hashCode();
        exit(retValue);
        return retValue;
    }

    public void flush() throws IOException {
        enter("oos.flush");
        super.flush();
        exit();
    }

    protected void drain() throws IOException {
        enter("oos.drain");
        super.drain();
        exit();
    }

    public void defaultWriteObject() throws IOException {
        enter("oos.defaultWriteObject");
        super.defaultWriteObject();
        exit();
    }

    public void close() throws IOException {
        enter("oos.close");
        super.close();
        exit();
    }

    public void reset() throws IOException {
        enter("oos.reset");
        super.reset();
        exit();
    }

    public String toString() {
        enter("oos.toString");
        String retValue;

        retValue = super.toString();
        exit(retValue);
        return retValue;
    }

    public void writeUnshared(Object obj) throws IOException {
        enter("oos.writeUnshared", obj);
        super.writeUnshared(obj);
        exit();
    }

    protected final void writeObjectOverride(Object obj) throws IOException {
        enter("oos.writeObjectOverride", obj);
        super.writeObjectOverride(obj);
        exit();
    }

}
