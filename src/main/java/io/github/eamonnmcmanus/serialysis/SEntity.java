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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * A representation of a serialized object.  Scanning a serial stream
 * produces a sequence of SEntity instances.  The subclass of each depends on the
 * type of the real Java object that would be produced when deserializing
 * the input stream.  Essentially, SEntity and its subclasses define a
 * parallel type hierarchy that represents the original Java type hierarchy.
 * An instance of an SEntity subclass represents an instance of a Java
 * object or primitive.
 */
public abstract class SEntity {

    SEntity(String type) {
        this.type = type;
    }

    abstract String kind();
    abstract String contents();

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(kind()).append("(").append(type).append("){");
        Stack<SEntity> stack = stringThings.get();
        if (stack.contains(this)) {
            sb.append("...");
        } else {
            stack.push(this);
            sb.append(contents());
            stack.pop();
        }
        if (sb.charAt(sb.length() - 1) == '\n')
            indent(sb);
        sb.append("}");
        return sb.toString();
    }

    String getType() {
        return type;
    }

    static void indent(StringBuilder sb) {
        Stack<SEntity> stack = stringThings.get();
        for (int i = stack.size(); i > 0; i--)
            sb.append("  ");
    }

    private final String type;
    private static final ThreadLocal<Stack<SEntity>> stringThings =
            new ThreadLocal<Stack<SEntity>>() {
                protected Stack<SEntity> initialValue() {
                    return new Stack<SEntity>();
                }
            };
}
