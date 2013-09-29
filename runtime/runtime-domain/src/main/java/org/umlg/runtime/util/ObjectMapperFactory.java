package org.umlg.runtime.util;


import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Date: 2013/09/29
 * Time: 2:25 PM
 */
public class ObjectMapperFactory {

    public static final ObjectMapperFactory INSTANCE = new ObjectMapperFactory();
    private ObjectMapper objectMapper = new ObjectMapper();

    private ObjectMapperFactory() {
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }
}
