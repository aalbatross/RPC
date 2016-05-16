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
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author iamrp 
 * Utility class for RPC
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

    private static final Logger logger = Logger.getLogger(JsonUtils.class);

    /**
     * generate protocol Schema from java interface, use wrapper classes instead
     * of primitive types in service interface
     *
     * @param clazz
     * @return
     * @throws IOException
     */
    public static Schema generateProtocolFromInterface(Class clazz) throws IOException {

        if (logger.isInfoEnabled()) {
            logger.info("Generating protocol from interface " + clazz.getCanonicalName());
        }

        if (!clazz.isInterface()) {
            logger.error("Exception: Must be an interface");
            throw new RuntimeException("Must be an interface");
        }
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
            jsongen.writeStringField(NAMESPACE, clas.getCanonicalName().replace("." + clas.getSimpleName(), ""));
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
        if (logger.isInfoEnabled()) {
            logger.info("Generated schema: " + baos.toString());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(baos.toString(), Schema.class);

    }

    /**
     * generate Java Service interface from Schema
     *
     * @param protocol
     * @return
     * @throws IOException
     */
    public static void generateInterfaceFromProtocol(Schema protocol) throws IOException, ClassNotFoundException {
        String namespace = protocol.getNamespace();
        String serviceName = protocol.getServicename();
        if (logger.isInfoEnabled()) {
            logger.info("Generating interface");
        }
        PrintWriter pw = new PrintWriter(serviceName + ".java");
        //package definition
        pw.println("package " + namespace + ";");
        //defining interface
        pw.println("public interface " + serviceName + "{");

        Dependency[] dependencies = protocol.getDependencies();
        for (Dependency dependency : dependencies) {
            pw.println(dependencyAsJavaClassString(dependency, namespace, serviceName));
        }

        com.albatross.rpc.protocol.Method[] methods = protocol.getMethods();
        for (com.albatross.rpc.protocol.Method method : methods) {
            if (method.getReturntype().contains(namespace)) {
                String[] splits = method.getReturntype().split("\\.");
                //fields[i] = fields[i].replace("."+splits[splits.length-1],"$"+splits[splits.length-1]);
                method.setReturntype(splits[splits.length - 1]);
                //System.out.println("trabsformation: "+fields[i]);
            } else if (method.getReturntype().equals("void")) {
                method.setReturntype("void");
            } else {
                method.setReturntype(ProtocolDataTypeMap.ProtoDataTypeToJava(method.getReturntype()).getCanonicalName());
            }
            pw.print("public " + method.getReturntype() + " " + method.getName() + "(");
            String[] arg = method.getArgs();
            if (arg.length == 0) {
                pw.println(");");
                continue;
            }

            for (int i = 0; i < arg.length; i++) {
                if (arg[i].contains(namespace)) {
                    String[] splits = arg[i].split("\\.");
                    //fields[i] = fields[i].replace("."+splits[splits.length-1],"$"+splits[splits.length-1]);
                    arg[i] = (splits[splits.length - 1]);
                    //System.out.println("trabsformation: "+fields[i]);
                } else {
                    arg[i] = ProtocolDataTypeMap.ProtoDataTypeToJava(arg[i]).getCanonicalName();
                }
            }

            for (int i = 0; i < arg.length - 1; i++) {

                pw.print(arg[i] + " arg" + i + ",");
            }

            pw.println(arg[arg.length - 1] + " arg" + (arg.length - 1) + ");\n");
        }
        pw.println("}");
        pw.close();
        if (logger.isInfoEnabled()) {
            logger.info("Generated interface at: " + new File(serviceName + ".java").toPath());
        }
        //return  mapper.readValue(baos.toString(), Schema.class);

    }

    private static String dependencyAsJavaClassString(Dependency dependency, String namespace, String sname) throws ClassNotFoundException {
        StringBuffer sb = new StringBuffer();
        String name = dependency.getName();
        String[] fields = dependency.getFields();
        sb.append("class " + name + " {\n");

        sb.append(name + "(){}\n");

        for (int i = 0; i < fields.length; i++) {

            if (fields[i].contains(namespace)) {
                String[] splits = fields[i].split("\\.");
                //fields[i] = fields[i].replace("."+splits[splits.length-1],"$"+splits[splits.length-1]);
                fields[i] = splits[splits.length - 1];
                //System.out.println("trabsformation: "+fields[i]);
            } else {
                fields[i] = ProtocolDataTypeMap.ProtoDataTypeToJava(fields[i]).getCanonicalName();
            }
            String fld = fields[i];
            //String fld=ProtocolDataTypeMap.ProtoDataTypeToJava(fields[i]).getCanonicalName();
            sb.append("private " + fld + " field" + i + ";\n");

            sb.append("public " + fld + " getField" + i + "(){\n");
            sb.append("return this.field" + i + ";\n");
            sb.append("}\n");

            sb.append("public void setField" + i + "(" + fld + " field" + i + "){\n");
            sb.append("this.field" + i + "= this.field" + i + ";\n");
            sb.append("}\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * WIP
     *
     * @param protocol
     * @param file
     * @throws IOException
     */
    public static void generateJavaClassesFromProtocol(String protocol, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (!file.exists()) {
            file.mkdirs();
        }
        JsonNode node = mapper.readTree(protocol);
        Map map = mapper.readValue(protocol, Map.class);

        System.out.println(map.toString());

        //create package
        String namespace = map.get(NAMESPACE).toString();
        String folders = namespace.replace(".", File.separator);
        String namespace_creator = file.getCanonicalPath().concat(File.separator).concat("src").concat(File.separator).concat(folders);
        System.out.println(namespace_creator);
        Files.createDirectories(new File(namespace_creator).toPath());
        String service = map.get(SERVICENAME).toString();
        Path createFile = Files.createFile(new File(namespace_creator.concat(File.separator).concat(service + ".java")).toPath());

        //create File
        JsonNode methods = node.get(METHODS);
        //PrintWriter pw = new PrintWriter(createFile.toFile());

    }

}
