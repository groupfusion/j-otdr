package com.kola.otdr.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: KeyEvents.java
 * @Description:
 * @Package: com.kola.otdr.analysis
 * @author: xingyun－zhanghuijun
 * @date: 2022-04-11 16:47
 */
public class KeyEvents {
    private static Logger logger= LoggerFactory.getLogger("KeyEvents");

    public static Map<String, Object> process(int format, byte[] content) {
        String bName = "KeyEvents";
        int offset = 0;
        if (format == 2) {
            String blockId = Parts.readStringSpaceZero(content, offset);
            if (!bName.equals(blockId)) {
                return null;
            } else {
                offset += blockId.getBytes().length + 1;
            }
        }

        return processKeyevents(content,offset);

    }

    private static Map<String,Object> processKeyevents(byte[] content,int offset){
        Map<String, Object> block = new HashMap<>();
        //number of events
        int nev = Parts.readInt(content,offset, 2);
        logger.info(" {} events", nev);
        block.put("num events", nev);
        offset += 2;

//        Float factor = 1e-4 * Parts.SOL / Float.parseFloat(results["FxdParams"]["index"]);
//
//        pat = re.compile("(.)(.)9999LS")

                return block;
    }
}
