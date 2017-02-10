package com.github.filosganga.geogson.model.positions;

import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
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

    public static Properties withName(String name) {
        return new Properties().setName(name);
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
        if (mappedValue != null && mappedValue.isJsonPrimitive() &&
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

    @Nullable
    public String getName() {
        return getString(GeoJsonConst.NAME);
    }

    @Nullable
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

    public static class PropertyIndex {
        private final Map<String, Feature> propertyNameMap;

        private PropertyIndex() {
            propertyNameMap = new HashMap<>();
        }

        public static PropertyIndex parse(FeatureCollection featureCollection) {
            PropertyIndex finder = new PropertyIndex();
            for (Feature feature : featureCollection.features()) {
                String featureName = feature.getProperties().getName();
                if(featureName != null) {
                    finder.propertyNameMap.put(featureName, feature);
                }
            }
            return finder;
        }

        @Nullable
        public Feature findFeatureWithName(String name) {
          return propertyNameMap.get(name);
        }


    }

}
