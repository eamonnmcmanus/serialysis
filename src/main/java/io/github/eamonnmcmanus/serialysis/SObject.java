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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>A representation of a general Java object appearing in a serial stream.
 * This class is used for all objects except arrays, primitives, and strings,
 * which are represented respectively by {@link SArray}, {@link SPrim},
 * and {@link SString}.</p>
 *
 * <p>The fields accessed by the {@link #getField getField} and {@link #setField
 * setField} methods are serial fields.  Usually these are actual fields in
 * the original Java class of the object, but a class that has a
 * {@code serialPersistentFields} definition, as described in the
 * <a href="http://java.sun.com/javase/6/docs/platform/serialization/spec/class.html">
 * serialization specification</a> can have serial fields that are unrelated
 * to its Java fields.</p>
 *
 * <p>A serialized enumeration constant is represented as an SObject
 * with a synthetic field called {@code "<name>"} of type {@link SString}.</p>
 */
public class SObject extends SEntity {

    SObject(String type) {
        super(type);
    }

    /**
     * Change the representation of the serial field with the given name
     * in this serialized object.
     */
    void setField(String name, SEntity value) {
        fields.put(name, value);
    }

    void addAnnotation(SEntity annot) {
        annots.add(annot);
    }

    /**
     * Get the representation of the serial field with the given name in
     * this serialized object.
     */
    public SEntity getField(String name) {
        return fields.get(name);
    }

    /**
     * Get the names of the serial fields in this serialized object.
     */
    public Set<String> getFieldNames() {
        return fields.keySet();
    }

    /**
     * Get any additional data written to the serial stream by a
     * writeObject method in the class or any of its ancestors.
     */
    @SuppressWarnings("unchecked")
    public List<SEntity> getAnnotations() {
        return (List<SEntity>) annots.clone();
    }

    String kind() {
        return "SObject";
    }

    String contents() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Map.Entry<String, SEntity> entry : fields.entrySet()) {
            indent(sb);
            sb.append(entry.getKey()).append(" = ")
            .append(entry.getValue()).append("\n");
        }
        if (annots.size() > 0) {
            indent(sb);
            sb.append("-- data written by class's writeObject:\n");
            for (SEntity annot : annots) {
                indent(sb);
                sb.append(annot + "\n");
            }
        }
        return sb.toString();
    }

    public String getType() {
        return super.getType();
    }

    private final Map<String, SEntity> fields = new LinkedHashMap<String, SEntity>();
    private final ArrayList<SEntity> annots = new ArrayList<SEntity>();
}
