package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.gson.utils.FeatureUtils;
import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.github.filosganga.geogson.model.positions.Properties;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;


public class FeatureCollectionAdapterTest {

    private Gson gson;

    @Before
    public void initToTest() {
        gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
    }

    @Test
    public void shouldHandleEmptyFeatureCollection() {
        FeatureCollection collection = new FeatureCollection(Collections.<Feature>emptyList());
        FeatureCollection parsed = gson.fromJson(gson.toJson(collection), FeatureCollection.class);
        assertThat(parsed, equalTo(collection));
    }

    @Test
    public void shouldHandleFeatureCollectionWithFeatures() {
        Properties expectedProperties = new Properties();
        expectedProperties.setName("name_test1");
        expectedProperties.setAddress("address_test1");
        expectedProperties.put("hello", "world");

        List<Feature> features = ImmutableList.of(
                FeatureUtils.featureWithId("test1"),
                FeatureUtils.featureWithId("test2")
                        .withProperties(expectedProperties)
        );

        FeatureCollection collection = new FeatureCollection(features);

        // Convert to a JSON string and then parse back to an object:
        String jsonString = gson.toJson(collection);
        FeatureCollection parsedFeatures = gson.fromJson(jsonString, FeatureCollection.class);
        assertThat(parsedFeatures.features(), hasSize(2));

        // Feature test1 should be present:
        assertThat(parsedFeatures.features().get(0).id().get(), equalTo("test1"));

        // Feature test2 should be present and have properties:
        Feature featureTest2 = parsedFeatures.features().get(1);
        assertThat(featureTest2.id().get(), equalTo("test2"));
        // Get the properties:
        Properties parsedProperties = featureTest2.getProperties();
        assertEquals(expectedProperties.size(), parsedProperties.size());
        assertEquals("name_test1", parsedProperties.getName());
        assertEquals("world", parsedProperties.getString("hello"));

    }

    @Test
    public void shouldParseFeatureCollection() {
        String json = "{ \"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"} }, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ] }, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0 } }, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ] }, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"} } } ] }";

        FeatureCollection parsed = gson.fromJson(json, FeatureCollection.class);

        assertThat(parsed.features(), hasSize(3));

    }

    @Test
    public void shouldParseRealFeatureCollection() throws IOException {
        String json = Resources.toString(Resources.getResource("feature-collection.json"), Charsets.UTF_8);

        FeatureCollection parsed = gson.fromJson(json, FeatureCollection.class);

        assertThat(parsed.features(), hasSize(202));

    }


}
