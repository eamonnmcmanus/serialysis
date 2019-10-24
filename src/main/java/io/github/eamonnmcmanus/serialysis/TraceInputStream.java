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
import java.io.InputStream;

import static io.github.eamonnmcmanus.serialysis.Trace.enter;
import static io.github.eamonnmcmanus.serialysis.Trace.exit;
import static io.github.eamonnmcmanus.serialysis.Trace.show;

class TraceInputStream extends InputStream {
    TraceInputStream(InputStream in) {
        this.in = in;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int n = in.read(b, off, len);
        if (n < 0)
            show("is.read", n);
        else
            show("is.read", b, off, n);
        return n;
    }

    public int read(byte[] b) throws IOException {
        int n = in.read(b);
        if (n < 0)
            show("is.read", n);
        else
            show("is.read", b, 0, n);
        return n;
    }

    public long skip(long n) throws IOException {
        long ret = in.skip(n);
        show("is.skip(" + n + ") -> " + ret);
        return ret;
    }

    public int read() throws IOException {
        int n = in.read();
        if (n < 0)
            show("is.read", n);
        else
            show("is.read", new byte[] {(byte) n});
        return n;
    }

    private final InputStream in;
}
