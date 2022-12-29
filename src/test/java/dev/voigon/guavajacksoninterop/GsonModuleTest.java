/*
 * Copyright 2022 Apartium
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dev.voigon.guavajacksoninterop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Voigon (Lior S.)
 */
class GsonModuleTest {

    @Test
    void testSerializeAndDeserializeObject() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GsonModule());
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("text"));
        jsonArray.add(new JsonPrimitive(5));
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("value", jsonArray);

        TestType object = new TestType(jsonObject);

        String value = objectMapper.writeValueAsString(object);
        assertEquals("{\"value\":[\"text\",5]}", value);
        System.out.println(value);
        TestType result = objectMapper.readValue(value, TestType.class);
        assertEquals(jsonObject, result.jsonElement);

    }

    @Test
    void testSerializeAndDeserializeArray() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GsonModule());
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("text"));
        jsonArray.add(new JsonPrimitive(5));
        jsonArray.add(JsonNull.INSTANCE);
        TestType object = new TestType(jsonArray);

        String value = objectMapper.writeValueAsString(object);
        assertEquals("[\"text\",5,null]", value);
        TestType result = objectMapper.readValue(value, TestType.class);
        assertEquals(jsonArray, result.jsonElement);

    }

    static class TestType {

        @JsonValue
        private JsonElement jsonElement;

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public TestType(JsonElement jsonElement) {
            this.jsonElement = jsonElement;
        }
    }
}
