package com.kola.otdr.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

//        Float factor = 1e-4 * Parts.SOL / Float.parseFloat("refraction");//refraction 折射率
        Pattern pat = Pattern.compile("(.)(.)9999LS");
        for (int i = 0; i < eventCount; i++) {
            Map<String, Object> event = new HashMap<>();
            int xid= Parts.readInt(content, offset, 2);                 // 00-01: event number
            offset += 2;
            int dist = Parts.readInt(content, offset, 4); // * factor  // 02-05: time-of-travel; need to convert to distance
            offset += 4;
            Float slope  = Parts.readInt(content, offset, 2) * 0.001f; // 06-07: slope  dB/Km
            offset += 2;
            Float splice = Parts.readInt(content, offset,  2) * 0.001f; // 08-09: splice loss   dB
            offset += 2;
            Float refl   = Parts.readInt(content, offset, 4) * 0.001f;  // 10-13: reflection loss dB
            offset += 4;
            String xtype= Parts.readString(content, offset, 8);                 // 14-21: event type
            offset += 8;
            Matcher mresults = pat.matcher(xtype);
            if(mresults.find()){
                String subtype = mresults.group(1);
                String manual  = mresults.group(2);
                if ("A".equals(manual)) {
                    xtype += " {manual}";
                } else {
                    xtype += " {auto}";
                }
                if ("1".equals(subtype)) {
                    xtype += " reflection";
                } else if ("0".equals(subtype)) {
                    xtype += " loss/drop/gain";
                } else if ("2".equals(subtype)) {
                    xtype += " multiple";
                } else {
                    xtype += " unknown '" + subtype + "'";
                }
            }else{
                xtype += " [unknown type "+xtype+"]";
            }

            if (format == 2) {
                var end_prev   = Parts.readInt(content, offset, 4) ;// * factor // 22-25: end of previous
                offset+=4;
                var start_curr = Parts.readInt(content, offset, 4) ;// * factor // 26-29: start of current event
                offset+=4;
                var end_curr   = Parts.readInt(content, offset, 4) ;// * factor // 30-33: end of current event
                offset+=4;
                var start_next = Parts.readInt(content, offset, 4) ;// * factor // 34-37: start of next event
                offset+=4;
                var pkpos      = Parts.readInt(content, offset, 4) ;// * factor // 38-41: peak point of event
                offset+=4;

                event.put("end of prev", end_prev);
                event.put("start of curr", start_curr);
                event.put("end of curr", end_curr);
                event.put("start of next", start_next);
                event.put("peak", pkpos);
                logger.info("        end of prev :{}", end_prev);
                logger.info("        start of curr :{}", start_curr);
                logger.info("        end of curr :{}", end_curr);
                logger.info("        start of next :{}", start_next);
                logger.info("        peak :{}", pkpos);
            }

            int[] markerLocations = new int[5];
            for (int j = 0; j < markerLocations.length; j++) {
                markerLocations[j] = Parts.readInt(content, offset, 4);
                offset += 4;
            }
            logger.info("        Marker Locations : " + Arrays.toString(markerLocations));
            String comments = Parts.readStringSpaceZero(content, offset);

            offset += comments.getBytes().length + 1;

            logger.info("        EventNum : " +xid);
            logger.info("        Type : " + xtype);
            logger.info("        distance : " + dist);
            logger.info("        slope : " + slope);
            logger.info("        splice loss : " + splice);
            logger.info("        refl loss: " + refl);

            logger.info("        Comment : " + comments);
            event.put("type", xtype);
            event.put("distance", dist);
            event.put("slope", slope);
            event.put("splice loss",splice);
            event.put("refl loss", refl);
            event.put("comments", comments);
            block.put("event "+xid,event);
        }

// ...................................................
        var total      = Parts.readInt(content, offset, 4) * 0.001;  // 00-03: total loss
        offset +=4;
        var loss_start = Parts.readInt(content, offset, 4); // 04-07: loss start position  (* factor)
        offset +=4;
        var loss_finish= Parts.readInt(content, offset, 4);   // 08-11: loss finish position  (* factor)
        offset +=4;
        var orl        = Parts.readInt(content, offset, 2) * 0.001f;    // 12-13: optical return loss (ORL)
        offset +=2;
        var orl_start  = Parts.readInt(content, offset, 4); // 14-17: ORL start position  (* factor)
        offset +=4;
        var orl_finish = Parts.readInt(content, offset, 4);   // 18-21: ORL finish position    (* factor)
        Map<String, Object> summary = new HashMap<>();
        summary.put("total loss",total);
        summary.put("ORL"       ,orl);
        summary.put("loss start",loss_start);
        summary.put("loss end"  ,loss_finish);
        summary.put("ORL start" ,orl_start);
        summary.put("ORL finish",orl_finish);
        block.put("Summary",summary);
        logger.info("event block: {}",block);
        return block;
    }
}
