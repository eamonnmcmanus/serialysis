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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.OutputStream;
import java.io.ObjectOutputStream;

/**
 * <p>A representation of arbitrary binary data included in a serial stream.
 * As well as objects written with {@link ObjectOutputStream#writeObject},
 * a serial stream can contain binary data written with any of the methods
 * that {@link ObjectOutputStream} inherits from {@link OutputStream} and
 * {@link DataOutput}.  The format of such data needs to be agreed on by
 * writer and reader.  Each chunk of such data in the stream is represented
 * by an instance of this class.</p>
 */
public class SBlockData extends SEntity {
    
    SBlockData(byte[] data) {
        super("blockdata");
        this.data = data;
    }
    
    /**
     * Get the binary data.
     */
    public byte[] getValue() {
        return data.clone();
    }
    
    /**
     * Get a DataInputStream that can read the binary data.
     */
    public DataInputStream getDataInputStream() {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        return new DataInputStream(bin);
    }
    
    String kind() {
        return "SBlockData";
    }
    
    String contents() {
        return data.length + " byte" + (data.length == 1 ? "" : "s") +
                " of binary data";
    }
    
    private final byte[] data;
}
