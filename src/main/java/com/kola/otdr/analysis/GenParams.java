package com.kola.otdr.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * GenParams
 */
public class GenParams {
    private static Logger logger = LoggerFactory.getLogger("GenParams");

    public static Map<String, Object> process(int format, byte[] content){
        String bName="GenParams";
        int offset = 0;
        if (format == 1) {
            return readGenParamsV1(content, offset);
        }else{
            String blockId = Parts.readStringSpaceZero(content, offset);
            if(bName.equals(blockId)) {
                return readGenParamsV2(content, offset);
            }else{
                return null;
            }
        }
    }

    private static Map<String, Object> readGenParamsV1(byte[] content, int offset) {
        Map<String, Object> block = new HashMap<>();
        String [] fields = {
                "cable ID",  // ........... 0
                "fiber ID",  // ........... 1
                "wavelength",  // ............2: fixed 2 bytes value
                "location A",  // ............ 3
                "location B",  // ............ 4
                "cable code/fiber type",  // ............ 5
                "build condition",  // ....... 6: fixed 2 bytes char/string
                "user offset",  // ........... 7: fixed 4 bytes (Andrew Jones)
                "operator",  // ........... 8
                "comments",  // ........... 9
        };
        String language = Parts.readString(content, offset, 2);
        logger.info("Language : " + language);
        block.put("language",language);
        offset += 2;
        int count = 0;
        String xstr;
        for(String field :fields){
            if (field == "build condition") {
                xstr = Parts.readString(content, offset, 2);
                offset += 2;
                logger.info(field+ " : " + xstr);
                block.put(field,xstr);
            }else if(field.equals("wavelength")){
                int xint = Parts.readInt(content, offset, 2);
                offset += 2;
                logger.info(field+" : " + xint);
                block.put(field,xint);
            }else if( field.equals("user offset")){
                int xint = Parts.readInt(content, offset, 4);
                offset += 4;
                logger.info(field+" : " + xint);
                block.put(field,xint);
            }else{
                xstr = Parts.readStringSpaceZero(content, offset);
                offset += xstr.getBytes().length+1;
                logger.info(field+" : " + xstr);
                block.put(field,xstr);
            }
            count += 1;
        }
//        System.out.println("count::"+count);
        return block;
    }

    private static Map<String, Object> readGenParamsV2(byte[] content, int offset) {
        Map<String, Object> block = new HashMap<>();
        String [] fields = {
                "cable ID",  // ........... 0
                "fiber ID",  // ........... 1
                "fiber type",  // ........... 2:fixed 2 bytes value
                "wavelength",  // ............3:fixed 2 bytes value
                "location A",  // ............4
                "location B",  // ............5
                "cable code/fiber type",  // ............6
                "build condition",  // ....... 7:fixed 2 bytes char/string
                "user offset",  // ........... 8:fixed 4 bytes int(Andrew Jones)
                "user offset distance",  // .. 9:fixed 4 bytes int(Andrew Jones)
                "operator",  // ........... 10
                "comments",  // ........... 11
        };
        String name = Parts.readStringSpaceZero(content, offset);
        offset += name.getBytes().length + 1;
        String language = Parts.readString(content, offset, 2);
        logger.info("Language : " + language);
        block.put("language",language);
        offset += 2;
        int count = 0;
        String xstr;
        for(String field :fields){

            if (field == "build condition") {
                xstr = Parts.readString(content, offset, 2);
                offset += 2;
                logger.info(field+ " : " + xstr);
                block.put(field,xstr);
            }else if(field.equals("fiber type")) {
                int xint = Parts.readInt(content, offset, 2);
                offset += 2;
                logger.info(field+" : " + xint);
                block.put(field,xint);
            }else if(field.equals("wavelength")){
                int xint = Parts.readInt(content, offset, 2);
                offset += 2;
                logger.info(field+" : " + xint);
                block.put(field,xint);
            }else if( field.equals("user offset") || field.equals("user offset distance")){
                int xint = Parts.readInt(content, offset, 4);
                offset += 4;
                logger.info(field+" : " + xint);
                block.put(field,xint);
            }else{
                xstr = Parts.readStringSpaceZero(content, offset);
                offset += xstr.getBytes().length+1;
                logger.info(field+" : " + xstr);
                block.put(field,xstr);
            }
            count += 1;
        }
//        System.out.println("count::"+count);
        return block;
    }
}
