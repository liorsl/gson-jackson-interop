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

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author Voigon (Lior S.)
 */
@ApiStatus.Internal
public class JsonGeneratorWriter {

    public static void write(JsonGenerator jsonGenerator, JsonElement jsonElement) throws IOException {
        if (jsonElement instanceof JsonArray) {
            jsonGenerator.writeStartArray();
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray)
                write(jsonGenerator, element);
            jsonGenerator.writeEndArray();
        } else if (jsonElement instanceof JsonObject) {
            jsonGenerator.writeStartObject();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                jsonGenerator.writeFieldName(entry.getKey());
                write(jsonGenerator, entry.getValue());
            }
            jsonGenerator.writeEndObject();
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isBoolean()) {
                jsonGenerator.writeBoolean(jsonPrimitive.getAsBoolean());
            } else if (jsonPrimitive.isString()) {
                jsonGenerator.writeString(jsonElement.getAsString());
            } else if (jsonPrimitive.isNumber()) {
                writeNumber(jsonGenerator, jsonElement.getAsNumber());
            } else {
                throw new RuntimeException(String.format("Unsupported GSON primitive: %s", jsonPrimitive));
            }
        } else if (jsonElement.isJsonNull()) {
            jsonGenerator.writeNull();
        } else {
            throw new RuntimeException(String.format("Unsupported GSON type %s", jsonElement.getClass().getName()));
        }
    }

    public static void writeNumber(JsonGenerator jsonGenerator, Number number) throws IOException {
        if (number instanceof Byte)
            jsonGenerator.writeNumber(number.byteValue());
        else if (number instanceof Short)
            jsonGenerator.writeNumber(number.shortValue());
        else if (number instanceof Integer)
            jsonGenerator.writeNumber(number.intValue());
        else if (number instanceof Long)
            jsonGenerator.writeNumber(number.longValue());
        else if (number instanceof BigInteger)
            jsonGenerator.writeNumber((BigInteger) number);
        else if (number instanceof Float)
            jsonGenerator.writeNumber(number.floatValue());
        else if (number instanceof Double)
            jsonGenerator.writeNumber(number.doubleValue());
        else if (number instanceof BigDecimal)
            jsonGenerator.writeNumber((BigDecimal) number);
        else if (number instanceof LazilyParsedNumber)
            jsonGenerator.writeNumber(number.doubleValue());
        else
            throw new RuntimeException(String.format("Unsupported number type %s", number.getClass().getName()));
    }

}
