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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.google.gson.*;

/**
 * A class implementing Jackson's {@link com.fasterxml.jackson.databind.Module} in order to setup the library for given object mapper.
 *
 * @see com.fasterxml.jackson.databind.Module
 * @see com.fasterxml.jackson.databind.ObjectMapper
 *
 * @author Voigon (Lior S.)
 */
public class GsonModule extends SimpleModule {

    public static final Version
            VERSION = new Version(1, 0, 0, null, "dev.voigon", "gson-jackson-interop");

    public static final String
            NAME = "GsonModule";

    /**
     * Construct a new instance of this module
     */
    public GsonModule() {
        super(NAME, VERSION);
    }

    @Override
    public void setupModule(SetupContext context) {
        SimpleDeserializers simpleDeserializers = new SimpleDeserializers();
        JsonElementDeserializer jsonElementDeserializer = new JsonElementDeserializer();
        simpleDeserializers.addDeserializer(JsonElement.class, jsonElementDeserializer);
        context.addDeserializers(simpleDeserializers);

        SimpleSerializers simpleSerializers = new SimpleSerializers();
        simpleSerializers.addSerializer(new JsonElementSerializer<>(JsonElement.class));
        simpleSerializers.addSerializer(new JsonElementSerializer<>(JsonArray.class));
        simpleSerializers.addSerializer(new JsonElementSerializer<>(JsonObject.class));
        simpleSerializers.addSerializer(new JsonElementSerializer<>(JsonPrimitive.class));
        simpleSerializers.addSerializer(new JsonElementSerializer<>(JsonNull.class));
        context.addSerializers(simpleSerializers);
    }

    @Override
    public int hashCode() {
        return NAME.hashCode();
    }

    @Override
    public String toString() {
        return NAME;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GsonModule;
    }
}
