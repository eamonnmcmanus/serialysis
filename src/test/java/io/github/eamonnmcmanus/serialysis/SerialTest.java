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
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Collections;
import junit.framework.*;
import io.github.eamonnmcmanus.serialysis.SArray;
import io.github.eamonnmcmanus.serialysis.SObject;
import io.github.eamonnmcmanus.serialysis.SEntity;
import io.github.eamonnmcmanus.serialysis.SPrim;
import io.github.eamonnmcmanus.serialysis.SString;
import io.github.eamonnmcmanus.serialysis.SerialScan;

public class SerialTest extends TestCase {

    public SerialTest(String testName) {
        super(testName);
    }

    public void testInteger() throws Exception {
        SObject so = (SObject) scan(Integer.valueOf(5));
        assertEquals(Integer.class.getName(), so.getType());
        assertEquals(Collections.singleton("value"), so.getFieldNames());
        SPrim value = (SPrim) so.getField("value");
        Integer x = (Integer) value.getValue();
        assertEquals(5, x.intValue());
    }

    public void testString() throws Exception {
        SString so = (SString) scan("noddy");
        assertEquals("noddy", so.getValue());
    }

    public void testLongString() throws Exception {
        StringBuilder sb = new StringBuilder("x");
        for (int i = 0; i < 20; i++)
            sb.append(sb);
        String s = sb.toString();
        assertTrue(s.length() > 1000000);
        SString ss = (SString) scan(s);
        assertEquals(s, ss.getValue());
    }

    public void testIntegerArray() throws Exception {
        SArray so = (SArray) scan(new Integer[] {1, 2, 3});
        SEntity[] a = so.getValue();
        assertEquals(3, a.length);
        for (int i = 0; i < a.length; i++) {
            SObject x = (SObject) a[i];
            SPrim p = (SPrim) x.getField("value");
            assertEquals(i + 1, p.getValue());
        }
    }

    public void testIntArray() throws Exception {
        SArray so = (SArray) scan(new int[] {1, 2, 3});
        SEntity[] a = so.getValue();
        assertEquals(3, a.length);
        for (int i = 0; i < a.length; i++) {
            SPrim p = (SPrim) a[i];
            assertEquals(i + 1, p.getValue());
        }
    }

    public void testStringArray() throws Exception {
        String[] strings = {"seacht", "ocht", "naoi"};
        SArray so = (SArray) scan(strings);
        SEntity[] a = so.getValue();
        assertEquals(strings.length, a.length);
        for (int i = 0; i < a.length; i++) {
            SString s = (SString) a[i];
            assertEquals(strings[i], s.getValue());
        }
    }

    public void testEnum() throws Exception {
        Enum en = ElementType.LOCAL_VARIABLE;  // random enum from Java SE
        SObject so = (SObject) scan(en);
        assertEquals(en.getClass().getName(), so.getType());
        SString name = (SString) so.getField("<name>");
        assertEquals(en.name(), name.getValue());
    }

    public void testSelfRef() throws Exception {
        Holder x = new Holder();
        x.held = x;
        SObject so = (SObject) scan(x);
        assertSame(so.getField("held"), so);
        // Make sure toString() doesn't get into infinite recursion
        System.out.println(so);
    }

    public void testObjectArrayContainingObjectArray() throws Exception {
        Object[] objects = {new Object[0]};
        SArray so = (SArray) scan(objects);
        SEntity[] a = so.getValue();
        assertEquals(objects.length, a.length);
        SArray aa = (SArray) a[0];
        assertEquals(0, aa.getValue().length);
    }

    public void testObjectArrayContainingSelf() throws Exception {
        Object[] objects = new Object[1];
        objects[0] = objects;
        SArray so = (SArray) scan(objects);
        SEntity[] a = so.getValue();
        assertEquals(objects.length, a.length);
        assertSame(so, a[0]);
        // Make sure toString() doesn't get into infinite recursion
        System.out.println(so);
    }

    public void testMultipleObjects() throws Exception {
        Object[] objects = {5, new int[] {5}, new Integer[] {5}, "noddy"};
        SEntity[] sos = scanMultiple(objects);
        SString noddy = (SString) sos[sos.length - 1];
        assertEquals("noddy", noddy.getValue());
    }

    private static class Holder implements Serializable {
        private static final long serialVersionUID = 6922605819566649377L;

        Object held;
    }

    private SEntity scan(Object x) throws IOException {
        return scanMultiple(new Object[] {x})[0];
    }

    private SEntity[] scanMultiple(Object[] xs) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        for (Object x : xs)
            oout.writeObject(x);
        oout.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bout.toByteArray());
        SerialScan ss = new SerialScan(bis);
        SEntity[] sos = new SEntity[xs.length];
        for (int i = 0; i < xs.length; i++)
            sos[i] = ss.readObject();
        assertTrue(bis.read() == -1);  // should have exhausted input stream
        return sos;
    }
}
