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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * A representation of a serialized primitive object such as an int or
 * boolean.
 */
public class SPrim extends SEntity {

    private final Object value;

    /**
     * Create a representation of the given wrapped primitive object.
     *
     * @param x a wrapped primitive object, for example an Integer if
     * the represented primitive is an int.
     */
    SPrim(Object x) {
        super(wrappedClassToPrimName.get(x.getClass()));
        this.value = x;
    }

    String kind() {
        return "SPrim";
    }

    String contents() {
        return value.toString();
    }

    /**
     * The value of the primitive object, wrapped in the corresponding
     * wrapper type, for example Integer if the primitive is an int.
     */
    public Object getValue() {
        return value;
    }
    
    private static final Map<Class<?>, String>
            wrappedClassToPrimName = new HashMap<Class<?>, String>();
    static {
        for (Class<?> c : new Class<?>[] {
            Boolean.class, Byte.class, Character.class, Double.class,
            Float.class, Integer.class, Long.class, Short.class
        }) {
            try {
                Field type = c.getField("TYPE");
                Class<?> prim = (Class<?>) type.get(null);
                wrappedClassToPrimName.put(c, prim.getName());
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }
}
