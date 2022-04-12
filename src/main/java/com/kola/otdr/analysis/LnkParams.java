package com.kola.otdr.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

/**
 * @Title: LnkParams.java
 * @Description:
 * @Package: com.kola.otdr.analysis
 * @author: xingyun－zhanghuijun
 * @date: 2022-04-12 10:47
 */
public class LnkParams {
    private static Logger logger= LoggerFactory.getLogger("LnkParams");
    public static Map<String, Object> process(int format, byte[] content) {
        String bName = "LnkParams";
        int offset = 0;
        if (format == 2) {
            String blockId = Parts.readStringSpaceZero(content, offset);
            if (!bName.equals(blockId)) {
                return null;
            } else {
                offset += blockId.getBytes().length + 1;
            }
        }
        return processLnkParams(format,content,offset);

    }

    private static Map<String, Object> processLnkParams(int format, byte[] content, int offset) {


        int number = Parts.readInt(content, offset, 2);
        System.out.println("Total Number of Landmarks : " + number);

        offset += 2;
        System.out.println("[");
        for (int i = 0; i < number; i++) {
            System.out.println("\t{");
            System.out.println("\t\tLandmark Number : " + Parts.readInt(content, offset, 2));

            offset += 2;
            System.out.println("\t\tLandmark Code : " + Parts.readString(content, offset, 2));

            offset += 2;
            System.out.println("\t\tLandmark Location : " + Parts.readInt(content, offset, 4));

            offset += 4;
            System.out.println("\t\tRelated Event Number : " + Parts.readInt(content, offset, 2));

            offset += 4;
            int[] gps = new int[2];
            for (int j = 0; j < gps.length; j++) {
                gps[j] = Parts.readInt(content, offset, 4);
                offset += 4;
            }
            System.out.println("\t\tGPS Information : " + Arrays.toString(gps));

            System.out.println("\t\tFiber Correction Factor Lead-in Fiber : " + Parts.readInt(content, offset, 2) / 100F + "%");

            offset += 2;
            System.out.println("\t\tSheath Marker Entering Landmark : " + Parts.readInt(content, offset, 4));

            offset += 4;
            System.out.println("\t\tSheath Marker Leaving Landmark : " + Parts.readInt(content, offset, 4));

            offset += 4;
            System.out.println("\t\tUnits of Sheath Marker Leaving Landmark : " + Parts.readString(content, offset, 2));

            offset += 2;
            System.out.println("\t\tMode Field Diameter Leaving Landmark : " + Parts.readString(content, offset, 2));

            offset += 2;
            System.out.println("\t\tComment : " + Parts.readStringSpaceZero(content, offset));

            System.out.println("\t}");
        }
        System.out.println("]");
        return null;
    }
}
