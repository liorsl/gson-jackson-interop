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

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.google.gson.*;
import com.google.gson.internal.LazilyParsedNumber;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class to convert {@link JsonElement} to {@link JsonNode} and vice-versa.
 * @author Voigon (Lior S.)
 */
public class NodeConverter {

    /**
     * Convert Gson's {@link JsonElement} object to a Jackson's {@link JsonNode}, with the same value
     * @param jsonElement Gson's JsonElement instance, may not be null
     * @return JsonNode instance with the same content as jsonElement
     * @see NodeConverter#gsonToJackson(JsonNodeFactory, JsonElement)
     */
    public static @NotNull JsonNode gsonToJackson(@NotNull JsonElement jsonElement) {
        return gsonToJackson(JsonNodeFactory.instance, jsonElement);
    }

    /**
     * Convert Gson's {@link JsonElement} object to a Jackson's {@link JsonNode}, with the same value
     * @param jsonNodeFactory json node factory instance, may not be null
     * @param jsonElement Gson's JsonElement instance, may not be null
     * @return JsonNode instance with the same content as jonElement
     * @throws RuntimeException if Gson type is unsupported
     * @see NodeConverter#gsonToJackson(JsonElement)
     */
    public static @NotNull JsonNode gsonToJackson(@NotNull JsonNodeFactory jsonNodeFactory, @NotNull JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            ArrayNode arrayNode = jsonNodeFactory.arrayNode(jsonArray.size());
            for (JsonElement element : jsonArray)
                arrayNode.add(gsonToJackson(jsonNodeFactory, element));
            return arrayNode;
        } else if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            ObjectNode objectNode = jsonNodeFactory.objectNode();
            for (Map.Entry<String, JsonElement> content : jsonObject.entrySet())
                objectNode.set(content.getKey(), gsonToJackson(jsonNodeFactory, content.getValue()));
            return objectNode;
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isBoolean()) {
                return jsonNodeFactory.booleanNode(jsonPrimitive.getAsBoolean());
            } else if (jsonPrimitive.isString()) {
                return jsonNodeFactory.textNode(jsonPrimitive.getAsString());
            } else if (jsonPrimitive.isNumber()) {
                return valueNodeFromNumber(jsonNodeFactory, jsonPrimitive.getAsNumber());
            }
        } else if (jsonElement.isJsonNull()) {
            return jsonNodeFactory.nullNode();
        }
        throw new RuntimeException(String.format("Unsupported GSON type %s", jsonElement.getClass().getName()));
    }

    /**
     * Create a Jackson {@link ValueNode} instance from given generic {@link Number}.
     * If number is an instance of {@link LazilyParsedNumber}, return an instance representing its double value
     * @param number given generic number, may not be null
     * @return representation of given number as a {@link ValueNode}
     * @throws RuntimeException if number type is unsupported
     */
    public static @NotNull ValueNode valueNodeFromNumber(@NotNull Number number) {
        return valueNodeFromNumber(JsonNodeFactory.instance, number);
    }

    /**
     * Create a Jackson ValueNode instance from given generic number.
     * If number is an instance of {@link LazilyParsedNumber}, return an instance representing its double value
     * @param jsonNodeFactory json node factory instance, may not be null
     * @param number given generic number, may not be null
     * @return representation of given number as a {@link ValueNode}
     * @throws RuntimeException if number type is unsupported
     */
    public static @NotNull ValueNode valueNodeFromNumber(@NotNull JsonNodeFactory jsonNodeFactory, @NotNull Number number) {
        if (number instanceof Byte)
            return jsonNodeFactory.numberNode(number.byteValue());
        else if (number instanceof Short)
            return jsonNodeFactory.numberNode(number.shortValue());
        else if (number instanceof Integer)
            return jsonNodeFactory.numberNode(number.intValue());
        else if (number instanceof Long)
            return jsonNodeFactory.numberNode(number.longValue());
        else if (number instanceof BigInteger)
            return jsonNodeFactory.numberNode((BigInteger) number);
        else if (number instanceof Float)
            return jsonNodeFactory.numberNode(number.floatValue());
        else if (number instanceof Double)
            return jsonNodeFactory.numberNode(number.doubleValue());
        else if (number instanceof BigDecimal)
            return jsonNodeFactory.numberNode((BigDecimal) number);
        else if (number instanceof LazilyParsedNumber)
            return jsonNodeFactory.numberNode(number.doubleValue());

        throw new RuntimeException(String.format("Unsupported number type %s", number.getClass().getName()));
    }

    /**
     * Convert Jackson's {@link TreeNode} into a {@link JsonElement} with the same value
     * @param jsonNode jackson's tree node instance
     * @return a {@link JsonElement} with the same value
     */
    public static @NotNull JsonElement jacksonToGson(@NotNull TreeNode jsonNode) {
        if (jsonNode instanceof ArrayNode) {
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < jsonNode.size(); i++)
                jsonArray.add(jacksonToGson(jsonNode.get(i)));
            return jsonArray;
        } else if (jsonNode instanceof ObjectNode) {
            JsonObject jsonObject = new JsonObject();
            Iterator<String> iterator = jsonNode.fieldNames();
            while (iterator.hasNext()) {
                String key = iterator.next();
                jsonObject.add(key, jacksonToGson(jsonNode.get(key)));
            }
            return jsonObject;
        } else if (jsonNode instanceof NumericNode) {
            return new JsonPrimitive(((NumericNode) jsonNode).numberValue());
        } else if (jsonNode instanceof BooleanNode) {
            return new JsonPrimitive(((BooleanNode) jsonNode).asBoolean());
        } else if (jsonNode instanceof TextNode) {
            return new JsonPrimitive(((TextNode) jsonNode).asText());
        } else if (jsonNode instanceof NullNode) {
            return JsonNull.INSTANCE;
        } else {
            throw new RuntimeException("Unsupported JSON Node type " + jsonNode.getClass().getName());
        }
    }

}
