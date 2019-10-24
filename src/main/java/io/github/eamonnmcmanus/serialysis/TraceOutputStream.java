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
package io.github.eamonnmcmanus.serialysis;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static io.github.eamonnmcmanus.serialysis.Trace.enter;
import static io.github.eamonnmcmanus.serialysis.Trace.exit;

class TraceOutputStream extends OutputStream {

    public TraceOutputStream(OutputStream os) {
        this.os = os;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        enter("os.write", Arrays.toString(b), off, len);
        os.write(b, off, len);
        exit();
    }

    public void write(byte[] b) throws IOException {
        enter("os.write", Arrays.toString(b));
        os.write(b);
        exit();
    }

    public void write(int b) throws IOException {
        enter("os.write", b);
        os.write(b);
        exit();
    }

    public void flush() throws IOException {
        enter("os.flush");
        os.flush();
        exit();
    }

    public void close() throws IOException {
        enter("os.close");
        os.close();
        exit();
    }

    private final OutputStream os;
}
