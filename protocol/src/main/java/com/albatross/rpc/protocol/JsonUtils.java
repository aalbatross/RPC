/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.protocol;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 *
 * @author iamrp
 * Utility class to generate Service Schema from java interface
 */
public class JsonUtils {

    public static String NAMESPACE = "namespace";
    public static String SERVICENAME = "servicename";
    public static String SERVICEDEPENDENCIES = "dependencies";
    public static String SERVICE_DEPENDENCYNAME = "name";
    public static String SERVICE_DEPENDENCYFIELD = "fields";
    public static String METHODS = "methods";
    public static String METHOD_NAME = "name";
    public static String METHOD_RETURNTYPE = "returntype";
    public static String METHOD_ARGS = "args";

    public static Schema generateProtocolFromInterface(Class clazz) throws IOException {
       
        if(!clazz.isInterface())
            throw new RuntimeException("Must bei interface");
        JsonFactory jsonFactory = new JsonFactory();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator jsongen = jsonFactory.createGenerator(baos, JsonEncoding.UTF8);

        //started main json object
        jsongen.writeStartObject();

        //namespace
        jsongen.writeStringField(NAMESPACE, clazz.getCanonicalName().replace("." + clazz.getSimpleName(), "").toLowerCase());
        //service name
        jsongen.writeStringField(SERVICENAME, clazz.getSimpleName());

        //dependency
        jsongen.writeArrayFieldStart(SERVICEDEPENDENCIES);

        Class[] classes = clazz.getDeclaredClasses();
        for (Class clas : classes) {
            jsongen.writeStartObject();
            jsongen.writeStringField(NAMESPACE, clas.getCanonicalName().replace("."+clas.getSimpleName(), ""));
            jsongen.writeStringField(SERVICE_DEPENDENCYNAME, clas.getSimpleName());
            jsongen.writeArrayFieldStart(SERVICE_DEPENDENCYFIELD);
            Field[] fields = clas.getDeclaredFields();
            for (Field field : fields) {
                
                jsongen.writeString(ProtocolDataTypeMap.javaToProtoDataType(field.getType()));
            }
            jsongen.writeEndArray();
            jsongen.writeEndObject();

        }
        jsongen.writeEndArray();

        //methods
        jsongen.writeArrayFieldStart(METHODS);

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            jsongen.writeStartObject();
            jsongen.writeStringField(METHOD_NAME, method.getName());
            jsongen.writeStringField(METHOD_RETURNTYPE, ProtocolDataTypeMap.javaToProtoDataType(method.getReturnType()));
            jsongen.writeArrayFieldStart(METHOD_ARGS);
            Class[] params = method.getParameterTypes();
            for (Class param : params) {
                jsongen.writeString(ProtocolDataTypeMap.javaToProtoDataType(param));
            }
            jsongen.writeEndArray();
            jsongen.writeEndObject();

        }
        jsongen.writeEndArray();
        jsongen.writeEndObject();

        jsongen.close();
        System.out.println(baos.toString());
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(baos.toString(), Schema.class);
       
    }

    

    
public static void generateJavaClassesFromProtocol(String protocol,File file) throws IOException{
    ObjectMapper mapper = new ObjectMapper();
    if(!file.exists()){
        file.mkdirs();
    }
    JsonNode node =mapper.readTree(protocol);
    Map map=mapper.readValue(protocol, Map.class);
    
    System.out.println(map.toString());
    
    //create package
    String namespace= map.get(NAMESPACE).toString();
    String folders=namespace.replace(".", File.separator);
    String namespace_creator= file.getCanonicalPath().concat(File.separator).concat("src").concat(File.separator).concat(folders);
    System.out.println(namespace_creator);
    Files.createDirectories(new File(namespace_creator).toPath());
    String service=map.get(SERVICENAME).toString();
    Path createFile = Files.createFile(new File(namespace_creator.concat(File.separator).concat(service+".java")).toPath());
    
    //create File
    JsonNode methods= node.get(METHODS);
    //PrintWriter pw = new PrintWriter(createFile.toFile());
    
    
}
    

    
}
