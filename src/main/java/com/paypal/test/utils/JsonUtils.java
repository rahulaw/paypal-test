package com.paypal.test.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by rahulaw
 */
public enum JsonUtils {

    DEFAULT(new ObjectMapper()),
    PRETTY(new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT));

    public final ObjectMapper mapper;

    JsonUtils(ObjectMapper mapper) {
        this.mapper = mapper;
        mapper.findAndRegisterModules();
    }

    public <T> String toJson(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> byte[] toJsonBinary(T object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) {
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String jsonString, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public <T> T fromJson(byte[] json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(byte[] json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> asMap(String jsonString) {
        try {
            final TypeReference<LinkedHashMap<String, Object>> typeRef = new
                    TypeReference<LinkedHashMap<String, Object>>() {
                    };
            return mapper.readValue(jsonString, typeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> asStringMap(String jsonString) {
        Map<String,String> map = Maps.newHashMap();
        asMap(jsonString).forEach((s, o) -> map.put(s, String.valueOf(o)));
        return map;
    }

    public Map<String, Object> asMap(byte[] jsonString) {
        try {
            final TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String,
                    Object>>() {
            };
            return mapper.readValue(jsonString, typeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

