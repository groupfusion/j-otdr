package com.kola.otdr.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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

        return processKeyevents(format,content,offset);

    }

    private static Map<String,Object> processKeyevents(int format,byte[] content,int offset){
        Map<String, Object> block = new HashMap<>();
        //number of events
        int eventCount = Parts.readInt(content,offset, 2);
        logger.info(" {} events", eventCount);
        block.put("num events", eventCount);
        offset += 2;

//        Float factor = 1e-4 * Parts.SOL / Float.parseFloat(results["FxdParams"]["index"]);
//
        for (int i = 0; i < eventCount; i++) {
            Map<String, Object> event = new HashMap<>();

            int xid= Parts.readInt(content, offset, 2);           // 00-01: event number
            offset += 2;
            int dist = Parts.readInt(content, offset, 4);   // 02-05: time-of-travel; need to convert to distance
            offset += 4;
            Float slope  = Parts.readInt(content, offset, 2) * 0.001f; // 06-07: slope  dB/Km
            offset += 2;
            Float splice = Parts.readInt(content, offset,  2) * 0.001f; // 08-09: splice loss   dB
            offset += 2;
            Float refl   = Parts.readInt(content, offset, 4) * 0.001f; // 10-13: reflection loss dB
            offset += 4;
            String xtype="";
            String subtype = Parts.readString(content, offset, 6);                 // 14-21: event type
            offset += 6;
            String manual = Parts.readString(content, offset, 2);
            offset += 2;
            if ("A".equals(manual)) {
                xtype = subtype +" {manual}";
            }else{
                xtype = subtype +" {auto}";
            }
            if ("1".equals(subtype)) {
                xtype += " reflection";
            }else if ("0".equals(subtype)) {
                xtype += " loss/drop/gain";
            }else if ("2".equals(subtype)) {
                xtype += " multiple";
            }else{
                xtype += " unknown '"+subtype+"'";
            }


            if (format == 2) {
                var end_prev   = Parts.readInt(content, offset, 4) ; // 22-25: end of previous event
                var start_curr = Parts.readInt(content, offset, 4) ; // 26-29: start of current event
                var end_curr   = Parts.readInt(content, offset, 4) ; // 30-33: end of current event
                var start_next = Parts.readInt(content, offset, 4) ; // 34-37: start of next event
                var pkpos      = Parts.readInt(content, offset, 4) ; // 38-41: peak point of event
            }

            int[] markerLocations = new int[5];
            for (int j = 0; j < markerLocations.length; j++) {
                markerLocations[j] = Parts.readInt(content, offset, 4);
                offset += 4;
            }

            logger.info("        Marker Locations : " + Arrays.toString(markerLocations));

            String comments = Parts.readStringSpaceZero(content, offset);
            logger.info("        Comment : " + comments);
            offset += comments.getBytes().length + 1;

            event.put("type", xtype);
            event.put("distance", dist);
            event.put("slope", slope);
            event.put("splice loss",splice);
            event.put("refl loss", refl);
            event.put("comments", comments);
            block.put("event "+i,event);
        }

//        System.out.println("End-to-End Loss : " + readInt(content, offset, 4) / 1000F + " dB");
//        offset += 4;
//        System.out.println("End-to-End Marker Positions start : " + readInt(content, offset, 4));
//        offset += 4;
//        System.out.println("End-to-End Marker Positions end : " + readInt(content, offset, 4));
//        offset += 4;
//        System.out.println("Optical Return Loss : " + readInt(content, offset, 2) / 1000F + " ORL");
//        offset += 2;
//        System.out.println("Optical Return Loss Marker Position start : " + readInt(content, offset, 4));
//        offset += 4;
//        System.out.println("Optical Return Loss Marker Position end : " + readInt(content, offset, 4));

                return block;
    }
}
