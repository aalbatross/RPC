/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.protocol;

/**
 *
 * @author iamrp
 * Datatype converter java to protocol and protocol to java
 */
public class ProtocolDataTypeMap {
   
    public static final String PROTO_INTEGER="int";
    public static final String PROTO_LONG="long";
    public static final String PROTO_DOUBLE="double";
    public static final String PROTO_CHAR="char";
    public static final String PROTO_STRING="string";
    public static final String PROTO_BOOLEAN="boolean";
    /**
     * converts java types to protocol data types
     * @param clazz
     * @return 
     */
    public static String javaToProtoDataType(Class clazz){
        String jdt = clazz.getCanonicalName();
        String result = jdt;
        
        if(jdt.equalsIgnoreCase(Integer.class.getCanonicalName()))
            return PROTO_INTEGER;
        else if(jdt.equalsIgnoreCase(Long.class.getCanonicalName()))
            return PROTO_LONG;
        else if(jdt.equalsIgnoreCase(Double.class.getCanonicalName()))
            return PROTO_DOUBLE;
        else if(jdt.equalsIgnoreCase(Character.class.getCanonicalName()))
            return PROTO_CHAR;
        else if(jdt.equalsIgnoreCase(String.class.getCanonicalName()))
            return PROTO_STRING;
        else if(jdt.equalsIgnoreCase(Boolean.class.getCanonicalName()))
            return PROTO_BOOLEAN;        
        return result;
    }
    /**
     * converts protocol datatypes to java types
     * @param proto
     * @return
     * @throws ClassNotFoundException 
     */
    public static Class ProtoDataTypeToJava(String proto) throws ClassNotFoundException{
        String jdt = proto;

        if(jdt.equalsIgnoreCase(PROTO_INTEGER))
            return Integer.class;
        else if(jdt.equalsIgnoreCase(PROTO_LONG))
            return long.class;
        else if(jdt.equalsIgnoreCase(PROTO_DOUBLE))
            return Double.class;
        else if(jdt.equalsIgnoreCase(PROTO_CHAR))
            return Character.class;
        else if(jdt.equalsIgnoreCase(PROTO_STRING))
            return String.class;
        else if(jdt.equalsIgnoreCase(PROTO_BOOLEAN))
            return Boolean.class;        
        return Class.forName(jdt);
    }
        
}
