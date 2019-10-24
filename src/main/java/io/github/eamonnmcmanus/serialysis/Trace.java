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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Stack;
import javax.management.Query;
import javax.management.QueryExp;

class Trace {
    static final boolean TRACE = false;

    static void enter(String what, Object...args) {
        if (!TRACE) return;
        indent();
        StringBuilder sb = new StringBuilder(what).append("(");
        String sep = "";
        for (Object arg : args) {
            sb.append(sep).append(toString(arg));
            sep = ", ";
        }
        sb.append(")");
        System.out.println(sb);
        stack.push(what);
    }

    static void show(String what) {
        if (!TRACE) return;
        indent();
        System.out.println(what);
    }

    static void show(String what, Object x) {
        if (!TRACE) return;
        if (x instanceof byte[])
            show(what, (byte[]) x, 0, ((byte[]) x).length);
        else {
            indent();
            System.out.println(what + ": " + toString(x));
        }
    }

    static void show(String what, byte[] bytes, int off, int len) {
        if (!TRACE) return;
        indent();
        System.out.print(what + ": ");
        for (int i = 0; i < len; i++)
            System.out.printf(" %02x", bytes[off + i] & 255);
        System.out.println();
    }

    static void exit() {
        exit("");
    }

    static void exit(Object ret) {
        if (!TRACE) return;
        String what = stack.pop();
        indent();
        System.out.println(what + " returns " + toString(ret));
    }

    private static String toString(Object x) {
        if (x == null || !x.getClass().isArray())
            return String.valueOf(x);
        String s = Arrays.deepToString(new Object[] {x});
        return s.substring(1, s.length() - 1);
    }

    private static void indent() {
        for (int i = 0; i < stack.size(); i++)
            System.out.print("| ");
    }

    private static Stack<String> stack = new Stack<String>();
}
