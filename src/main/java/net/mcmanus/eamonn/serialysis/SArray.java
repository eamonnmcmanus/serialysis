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

/**
 * <p>A representation of a serialized array.  The represented array can be
 * an array of objects (for example String[]) or of primitives (for example
 * int[]).</p>
 */
public class SArray extends SEntity {

    private final SEntity[] array;

    SArray(String type, int size) {
        super(type);
        this.array = new SEntity[size];
    }

    public SEntity[] getValue() {
        return array.clone();
    }

    String kind() {
        return "SArray";
    }

    String contents() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (SEntity obj : array) {
            indent(sb);
            sb.append(obj).append("\n");
        }
        return sb.toString();
    }

    void set(int i, SEntity object) {
        array[i] = object;
    }

}
