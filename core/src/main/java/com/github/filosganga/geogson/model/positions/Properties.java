package com.github.filosganga.geogson.model.positions;

import com.github.filosganga.geogson.model.GeoJsonConst;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Properties {

    private final Map<String, JsonElement> registeredProperties;

    public static Properties from(Map<String, JsonElement> propertyMap) {
        Properties properties = new Properties();
        properties.registeredProperties.putAll(propertyMap);
        return properties;
    }

    public Properties() {
        this.registeredProperties = new HashMap<>();
    }

    public void put(String key, String value) {
        registeredProperties.put(key, new JsonPrimitive(value));
    }

    public void put(String key, JsonElement value) {
        registeredProperties.put(key, value);
    }

    @Nullable
    public String getString(String key) {
        JsonElement mappedValue = registeredProperties.get(key);
        if(mappedValue != null && mappedValue.isJsonPrimitive() &&
                ((JsonPrimitive) mappedValue).isString()) {
            return mappedValue.getAsString();
        }
        else {
            return null;
        }
    }

    // -----------------------------------------------------------------------------------------------

    // Name and Address are commonly-used properties and so we add convenience getters and setters for them.
    // While this could be considered an unnecessary complication they are nevertheless quite useful.

    public Properties setName(String name) {
        put(GeoJsonConst.NAME, name);
        return this;
    }

    public Properties setAddress(String address) {
        put(GeoJsonConst.ADDRESS, address);
        return this;
    }

    public String getName() {
        return getString(GeoJsonConst.NAME);
    }

    public String getAddress() {
        return getString(GeoJsonConst.ADDRESS);
    }

    // -----------------------------------------------------------------------------------------------

    public int size() {
        return registeredProperties.size();
    }

    public ImmutableMap<String, JsonElement> toImmutableMap() {
        return ImmutableMap.copyOf(registeredProperties);
    }

}
