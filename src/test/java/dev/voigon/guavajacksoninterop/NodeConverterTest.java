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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.*;
import com.google.gson.internal.LazilyParsedNumber;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Voigon (Lior S.)
 */
class NodeConverterTest {

    @Test
    void gsonToJackson() {
        assertEquals(jacksonObject(JsonNodeFactory.instance), NodeConverter.gsonToJackson(gsonObject()));
        assertThrows(IllegalArgumentException.class, () -> {
            NodeConverter.gsonToJackson(null) ;
        });
        assertThrows(RuntimeException.class, () -> {
            NodeConverter.gsonToJackson(new JsonElement() {
                @Override
                public JsonElement deepCopy() {
                    return null;
                }
            }) ;
        });

    }


    @Test
    void jacksonToGson() {
        assertEquals(gsonObject(), NodeConverter.jacksonToGson(jacksonObject(JsonNodeFactory.instance)));
        assertThrows(IllegalArgumentException.class, () -> {
            NodeConverter.jacksonToGson(null) ;
        });
        assertThrows(RuntimeException.class, () -> {
            NodeConverter.jacksonToGson(new TreeNode() {
                @Override
                public JsonToken asToken() {
                    return null;
                }

                @Override
                public JsonParser.NumberType numberType() {
                    return null;
                }

                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isValueNode() {
                    return false;
                }

                @Override
                public boolean isContainerNode() {
                    return false;
                }

                @Override
                public boolean isMissingNode() {
                    return false;
                }

                @Override
                public boolean isArray() {
                    return false;
                }

                @Override
                public boolean isObject() {
                    return false;
                }

                @Override
                public TreeNode get(String s) {
                    return null;
                }

                @Override
                public TreeNode get(int i) {
                    return null;
                }

                @Override
                public TreeNode path(String s) {
                    return null;
                }

                @Override
                public TreeNode path(int i) {
                    return null;
                }

                @Override
                public Iterator<String> fieldNames() {
                    return null;
                }

                @Override
                public TreeNode at(JsonPointer jsonPointer) {
                    return null;
                }

                @Override
                public TreeNode at(String s) throws IllegalArgumentException {
                    return null;
                }

                @Override
                public JsonParser traverse() {
                    return null;
                }

                @Override
                public JsonParser traverse(ObjectCodec objectCodec) {
                    return null;
                }
            });
        });

    }

    @Test
    void valueNodeFromNumber() {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

        assertEquals(nodeFactory.numberNode((byte) 1), NodeConverter.valueNodeFromNumber((byte) 1));
        assertEquals(nodeFactory.numberNode(2), NodeConverter.valueNodeFromNumber(2));
        assertEquals(nodeFactory.numberNode(3.5), NodeConverter.valueNodeFromNumber(3.5));
        assertEquals(nodeFactory.numberNode((short) 10), NodeConverter.valueNodeFromNumber((short) 10));
        assertEquals(nodeFactory.numberNode((float) 10.55), NodeConverter.valueNodeFromNumber((float) 10.55));
        assertEquals(nodeFactory.numberNode(Long.MAX_VALUE), NodeConverter.valueNodeFromNumber(Long.MAX_VALUE));
        assertEquals(nodeFactory.numberNode(BigInteger.TEN), NodeConverter.valueNodeFromNumber(BigInteger.TEN));
        assertEquals(nodeFactory.numberNode(BigDecimal.TEN), NodeConverter.valueNodeFromNumber(BigDecimal.TEN));
        assertEquals(nodeFactory.numberNode(10.777), NodeConverter.valueNodeFromNumber(new LazilyParsedNumber("10.777")));
        assertThrows(RuntimeException.class, () -> {
            NodeConverter.valueNodeFromNumber(new Number() {
                @Override
                public int intValue() {
                    return 0;
                }

                @Override
                public long longValue() {
                    return 0;
                }

                @Override
                public float floatValue() {
                    return 0;
                }

                @Override
                public double doubleValue() {
                    return 0;
                }
            });
        });
    }

    private JsonObject gsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("key1", new JsonPrimitive("value1"));
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(1L));
        jsonObject.add("key2", jsonArray);
        jsonObject.add("true", new JsonPrimitive(true));
        jsonObject.add("false", new JsonPrimitive(false));
        jsonObject.add("nill", JsonNull.INSTANCE);
        return jsonObject;
    }

    private TreeNode jacksonObject(JsonNodeFactory jsonNodeFactory) {
        ObjectNode objectNode = jsonNodeFactory.objectNode();
        objectNode.set("key1", jsonNodeFactory.textNode("value1"));
        ArrayNode arrayNode = jsonNodeFactory.arrayNode();
        arrayNode.add(1L);
        objectNode.set("key2", arrayNode);
        objectNode.set("true", BooleanNode.TRUE);
        objectNode.set("false", BooleanNode.FALSE);
        objectNode.set("nill", jsonNodeFactory.nullNode());
        return objectNode;
    }
}