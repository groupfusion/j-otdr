package com.kola.otdr.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SupParams {
    private static Logger logger= LoggerFactory.getLogger("SupParams");
    public static Map<String, Object> process(int format, byte[] content){
        String bName="SupParams";
        int offset = 0;
        if (format == 2) {
            String blockId = Parts.readStringSpaceZero(content, offset);
            if(!bName.equals(blockId)) {
                return null;
            }
        }
        return processSupparam(content, offset);
    }

    private static Map<String, Object> processSupparam(byte[] content, int offset){
        Map<String, Object> block = new HashMap<>();
        String [] fields = {
                "supplier",  // ............. 0
                "OTDR",  // ................. 1
                "OTDR S/N",// ............. 2
                "module",  // ...............3
                "module S/N",  // ........... 4
                "software",  // ............. 5
                "other",  // ................ 6
        };

        for(String field :fields){
            String xstr = Parts.readStringSpaceZero(content, offset);
            offset += xstr.getBytes().length+1;
            logger.info(field+" : " + xstr);
            block.put(field,xstr);
        }
        return block;
    }
}
