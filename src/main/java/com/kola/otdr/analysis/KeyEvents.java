package com.kola.otdr.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: KeyEvents.java
 * @Description: 解析事件信息
 * @author fusionGroup
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
        logger.debug(" {} events，content：{}", eventCount,content);
        block.put("num events", eventCount);
        offset += 2;

//        Float factor = 1e-4 * Parts.SOL / Float.parseFloat("refraction");//refraction 折射率
        Pattern pat = Pattern.compile("(.)(.)9999LS");
        for (int i = 0; i < eventCount; i++) {
            Map<String, Object> event = new LinkedHashMap<>();
            int xid= Parts.readInt(content, offset, 2);                 // 00-01: event number
            offset += 2;
            Double dist = Parts.readInt(content, offset, 4) * Parts.factor(); // * factor  // 02-05: time-of-travel; need to convert to distance
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

            logger.debug("  event num : " +xid);
            logger.debug("        Type : " + xtype);
            logger.debug("        distance : " + String.format("%.3f",dist));
            logger.debug("        slope : " + String.format("%.3f",slope));
            logger.debug("        splice loss : " + String.format("%.3f",splice));
            logger.debug("        refl loss: " + String.format("%.3f",refl));

            if (format == 2) {
                var end_prev   = Parts.readInt(content, offset, 4) * Parts.factor();// * factor // 22-25: end of previous
                offset+=4;
                var start_curr = Parts.readInt(content, offset, 4) * Parts.factor();// * factor // 26-29: start of current event
                offset+=4;
                var end_curr   = Parts.readInt(content, offset, 4) * Parts.factor();// * factor // 30-33: end of current event
                offset+=4;
                var start_next = Parts.readInt(content, offset, 4) * Parts.factor();// * factor // 34-37: start of next event
                offset+=4;
                var pkpos      = Parts.readInt(content, offset, 4) * Parts.factor();// * factor // 38-41: peak point of event
                offset+=4;
                event.put("end of prev", String.format("%.3f",end_prev));
                event.put("start of curr", String.format("%.3f",start_curr));
                event.put("end of curr", String.format("%.3f",end_curr));
                event.put("start of next", String.format("%.3f",start_next));
                event.put("peak", String.format("%.3f",pkpos));
                logger.debug("        end of prev :{}", String.format("%.3f",end_prev));
                logger.debug("        start of curr :{}", String.format("%.3f",start_curr));
                logger.debug("        end of curr :{}", String.format("%.3f",end_curr));
                logger.debug("        start of next :{}", String.format("%.3f",start_next));
                logger.debug("        peak :{}", String.format("%.3f",pkpos));
            }
//            int[] markerLocations = new int[5];
//            for (int j = 0; j < markerLocations.length; j++) {
//                markerLocations[j] = Parts.readInt(content, offset, 4);
//                offset += 4;
//            }
//            logger.info("        Marker Locations : " + Arrays.toString(markerLocations));

            String comments = Parts.readStringSpaceZero(content, offset);
            offset += comments.getBytes().length + 1;
            logger.debug("        Comment : " + comments);

            event.put("type", xtype);
            event.put("distance", String.format("%.3f",dist));
            event.put("slope", String.format("%.3f",slope));
            event.put("splice loss",String.format("%.3f",splice));
            event.put("refl loss", String.format("%.3f",refl));
            event.put("comments", comments);
            block.put("event "+xid,event);
        }

// ...................................................
        var total      = Parts.readInt(content, offset, 4) * 0.001;  // 00-03: total loss
        offset +=4;
        var loss_start = Parts.readInt(content, offset, 4) * Parts.factor(); // 04-07: loss start position  (* factor)
        offset +=4;
        var loss_finish= Parts.readInt(content, offset, 4) * Parts.factor();   // 08-11: loss finish position  (* factor)
        offset +=4;
        var orl        = Parts.readInt(content, offset, 2) * 0.001f;    // 12-13: optical return loss (ORL)
        offset +=2;
        var orl_start  = Parts.readInt(content, offset, 4) * Parts.factor(); // 14-17: ORL start position  (* factor)
        offset +=4;
        var orl_finish = Parts.readInt(content, offset, 4) * Parts.factor();   // 18-21: ORL finish position    (* factor)
        Map<String, Object> summary = new HashMap<>();
        summary.put("total loss",String.format("%.3f",total));
        summary.put("ORL"       ,String.format("%.3f",orl));
        summary.put("loss start",String.format("%.6f",loss_start));
        summary.put("loss end"  ,String.format("%.6f",loss_finish));
        summary.put("ORL start" ,String.format("%.6f",orl_start));
        summary.put("ORL finish",String.format("%.6f",orl_finish));
        block.put("Summary",summary);
        logger.debug("event block: {}",block);
        return block;
    }
}
