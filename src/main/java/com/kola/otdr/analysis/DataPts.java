package com.kola.otdr.analysis;

import com.kola.otdr.exception.QuantityMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Title: DataPts.java
 * @Description:
 * @Package: com.kola.otdr.analysis
 * @author: xingyun－zhanghuijun
 * @date: 2022-04-12 10:55
 */
public class DataPts {
    private static Logger logger= LoggerFactory.getLogger("DataPts");

    public static Map<String, Object> process(List<String> tracedata, int format,String model,Double dx, int numPonit,byte[] content) throws QuantityMismatchException {
        String bName = "DataPts";
        int offset = 0;
        if (format == 2) {
            String blockId = Parts.readStringSpaceZero(content, offset);
            if (!bName.equals(blockId)) {
                return null;
            } else {
                offset += blockId.getBytes().length + 1;
            }
        }
        return processDataPts(tracedata,model,dx,numPonit,content,offset);

    }

    private static Map<String, Object> processDataPts(List<String> tracedata,String model,Double dx,int numPonit, byte[] content, int offset) throws QuantityMismatchException {
        Map<String, Object> block = new HashMap<>();
// special case:
        // old Noyes/AFL OFL250 model is off by factor of 10
        Map<String,Object> datapts = new HashMap<>();
        if ("OFL250".equals(model)) {
            datapts.put("xscaling", 0.1);
            datapts.put("offset", "AFL");
        }else{
            datapts.put("xscaling", 1);
            datapts.put("offset", "STV");
        }
        block.put("_datapts_params",datapts);
        int ndp = Parts.readInt(content, offset, 4);
//        System.out.println("Number of Data Points : " + ndp);
        if ( ndp != numPonit) {
            throw new QuantityMismatchException("!!! WARNING !!! block says number of data points is "+ndp+" instead of results['FxdParams']['num data points']＝"+numPonit);
        }
        block.put("num data points",ndp);

        offset += 4;
        int tnsfu = Parts.readInt(content, offset, 2);
//        System.out.println("Total Number Scale Factors Used : " + tnsfu);
        block.put("num traces",tnsfu);
        if ( tnsfu > 1 ) {
            throw new QuantityMismatchException("WARNING!!!: Cannot handle multiple traces ("+tnsfu+"); aborting");
        }

        offset += 2;
        int number = Parts.readInt(content, offset, 4);
//        System.out.println("Total Data Points Using Scale Factors : " + number);
        block.put("Using data points",number);

        offset += 4;
        int scaleFactor = Parts.readInt(content, offset, 2);
//        System.out.println("    Scale Factor : " + scaleFactor*0.001);
        block.put("scaling factor",scaleFactor*0.001);

        offset += 2;
        List<Double> dlist = new ArrayList();
        for (int i = 0; i < number; i++) {
            dlist.add((double) Parts.readInt(content, offset, 2));
            offset += 2;
        }
        // .........................................
        Double ymax = Collections.max( dlist );
        Double ymin = Collections.min( dlist );
        double fsx = 0.001* scaleFactor;
        Double disp_min = ymin * fsx;
        Double disp_max = ymax * fsx;
        block.put("max before offset",String.format("%.3f",disp_max));
        block.put("min before offset",String.format("%.3f",disp_min));


        // .........................................
        // save to file
        String xoffset = datapts.get("offset").toString();
        Double xscaling = Double.parseDouble(datapts.get("xscaling").toString());

        // convert/scale to dB
        List<Double> nlist=new ArrayList<>();
        if ("STV".equals(xoffset)) {
            dlist.forEach((x)->{
                x = (ymax - x )*fsx;
                nlist.add(x);
            });
        }else if ("AFL".equals(offset)) {
            dlist.forEach((x)->{
                x=(ymin - x )*fsx;
                nlist.add(x);
            });
        }else{ // invert
            dlist.forEach((x)->{
                x=(-x*fsx);
                nlist.add(x);
            });
        }

        for(int i=0; i<number; i++) {
            // more work but (maybe) less rounding issues
            Double x = dx*i*xscaling / 1000.0; // output in km
            tracedata.add( String.format("%.6f",x) +"\t"+String.format("%.6f",nlist.get(i)*0.001) );
        }
        return block;
    }
}
