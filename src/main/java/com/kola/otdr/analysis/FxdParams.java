package com.kola.otdr.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析fxd 参数信息
 * @author fusionGroup
 * @Date 2022-04-11 16:47
 */
public class FxdParams {
    private static Logger logger= LoggerFactory.getLogger("FxdParams");

    private static Map<String,String> unitMap = Map.of(
            "mt", " (meters)",
            "km", " (kilometers)",
            "mi", " (miles)",
            "kf", " (kilo-ft)"
    );

    private static Map<String,String> tracetype = Map.of(
            "ST", " [standard trace]",
            "RT", " [reverse trace]",
            "DT", " [difference trace]",
            "RF", " [reference]"
            );

    public static Map<String, Object> process(int format, byte[] content){
        String bName="FxdParams";
        int offset = 0;
        if (format == 2) {
            String blockId = Parts.readStringSpaceZero(content, offset);
            if(!bName.equals(blockId)) {
                return null;
            }else{
                offset += blockId.getBytes().length + 1;
            }
        }

        Object[][] plist;
        if ( format == 1 ) {
              plist = new Object[][]{// name, start-pos, length (bytes), type, multiplier, precision, units
                    // type: display type: 'v' (value) or 'h' (hexidecimal) or 's' (string)
                    {"date/time", 0, 4, "v", "", "", ""}, // ............... 0-3 seconds in Unix time
                    {"unit", 4, 2, "s", "", "", ""}, // .................... 4-5 distance units, 2 char (km,mt,ft,kf,mi)
                    {"wavelength", 6, 2, "v", 0.1, 1, "nm"}, // ............ 6-7 wavelength (nm)

                    // from Andrew Jones
                    {"acquisition offset", 8, 4, "i", "", "", ""}, // .............. 8-11 acquisition offset; units?
                    {"number of pulse width entries", 12, 2, "v", "", "", ""}, // .. 12-13 number of pulse width entries

                    {"pulse width", 14, 2, "v", "", 0, "ns"},  // .......... 14-15 pulse width (ns)
                    {"sample spacing", 16, 4, "v", 1e-8, "", "usec"}, // .. 16-19 sample spacing (in usec)
                    {"num data points", 20, 4, "v", "", "", ""}, // ....... 20-23 number of data points
                    {"index", 24, 4, "v", 1e-5, 6, ""}, // ................ 24-27 index of refraction
                    {"BC", 28, 2, "v", -0.1, 2, "dB"}, // ................. 28-29 backscattering coeff
                    {"num averages", 30, 4, "v", "", "", ""}, // .......... 30-33 number of averages
                    {"range", 34, 4, "v", 2e-5, 6, "km"}, // .............. 34-37 range (km)

                    // from Andrew Jones
                    {"front panel offset", 38, 4, "i", "", "", ""}, // ................ 38-41
                    {"noise floor level", 42, 2, "v", "", "", ""}, // ................. 42-43 unsigned
                    {"noise floor scaling factor", 44, 2, "i", "", "", ""}, // ........ 44-45
                    {"power offset first point", 46, 2, "v", "", "", ""}, // .......... 46-47 unsigned

                    {"loss thr", 48, 2, "v", 0.001, 3, "dB"}, // .......... 48-49 loss threshold
                    {"refl thr", 50, 2, "v", -0.001, 3, "dB"}, // ......... 50-51 reflection threshold
                    {"EOT thr", 52, 2, "v", 0.001, 3, "dB"}, // ............ 52-53 end-of-transmission threshold
            };
        }else{
            plist = new Object[][]{// name, start-pos, length (bytes), type, multiplier, precision, units
                    // value: display type: 'v' (value) or 'h' (hexidecimal) or 's' (string)
                    {"date/time", 0, 4, "v", "", "", ""},  // ............... 0-3 seconds in Unix time
                    {"unit", 4, 2, "s", "", "", ""},  // .................... 4-5 distance units, 2 char (km,mt,ft,kf,mi)
                    {"wavelength", 6, 2, "v", 0.1, 1, "nm"},  // ............ 6-7 wavelength (nm)
                    //  from Andrew Jones
                    {"acquisition offset", 8, 4, "i", "", "", ""}, // .............. 8-11 acquisition offset; units?
                    {"acquisition offset distance", 12, 4, "i", "", "", ""},  // .... 12-15 acquisition offset distance; units?
                    {"number of pulse width entries", 16, 2, "v", "", "", ""},  // .. 16-17 number of pulse width entries

                    {"pulse width", 18, 2, "v", "", 0, "ns"},  // .......... 18-19 pulse width (ns)
                    {"sample spacing", 20, 4, "v", 1e-8, "", "usec"},  // .. 20-23 sample spacing (usec)
                    {"num data points", 24, 4, "v", "", "", ""},  // ....... 24-27 number of data points
                    {"index", 28, 4, "v", 1e-5, 6, ""},  // ................ 28-31 index of refraction
                    {"BC", 32, 2, "v", -0.1, 2, "dB"},  // ................. 32-33 backscattering coeff

                    {"num averages", 34, 4, "v", "", "", ""},  // .......... 34-37 number of averages

                    // from Dmitry Vaygant:
                    {"averaging time", 38, 2, "v", 0.1, 0, "sec"},  // ..... 38-39 averaging time in seconds
                    {"acquisition range", 40, 4, "v", 2e-5, 6, "km"},  // .............. 40-43 range (km); note x2

                    // from Andrew Jones
                    {"acquisition range distance", 44, 4, "i", "", "", ""},  // ........ 44-47 acquisition range distance
                    {"front panel offset", 48, 4, "i", "", "", ""},  // ................ 48-51 front panel offset
                    {"noise floor level", 52, 2, "v", "", "", ""},  // ................ 52-53 noise floor level ；unsigned
                    {"noise floor scaling factor", 54, 2, "i", "", "", ""},  // ........ 54-55 noise floor scaling factor
                    {"power offset first point", 56, 2, "v", "", "", ""},  // ......... 56-57 power offset first point ；unsigned
                    {"loss thr", 58, 2, "v", 0.001, 3, "dB"},  // .......... 58-59 loss threshold
                    {"refl thr", 60, 2, "v", -0.001, 3, "dB"},  // ......... 60-61 reflection threshold
                    {"EOT thr", 62, 2, "v", 0.001, 3, "dB"},  // ............ 62-63 end-of-transmission threshold
                    {"trace type", 64, 2, "s", "", "", ""},  // ............. 64-65 trace type (ST,RT,DT, or RF)

                    // from Andrew Jones
                    {"X1", 66, 4, "i", "", "", ""},  // ............. 66-69
                    {"Y1", 70, 4, "i", "", "", ""},  // ............. 70-73
                    {"X2", 74, 4, "i", "", "", ""},  // ............. 74-77
                    {"Y2", 78, 4, "i", "", "", ""},  // ............. 78-81
            };
        }
        return processFxdParam(plist,content, offset);
    }

    private static Map<String, Object> processFxdParam(Object[][] plist,byte[] content, int offset) {
        Map<String, Object> block = new HashMap<>();
        for(Object[] field : plist) {

            String name  = (String)field[0];
            int fsize = Integer.parseInt(field[2].toString());
            Object ftype = field[3];
            Object scale = field[4];
            Object dgt   = field[5];
            Object unit  = field[6];

            Object xstr  = "";
            BigDecimal val=new BigDecimal(0);

            if ("i".equals(ftype)) {
                val = new BigDecimal(Parts.readInt(content,offset, fsize));
                xstr = val;
            }else if ("v".equals(ftype)) {
                val = new BigDecimal(Parts.readInt(content,offset, fsize));
                if (scale != "") {
                    val = val.multiply(new BigDecimal(scale.toString()));
                }
                if (!"".equals(dgt)) {
                    xstr = String.format("%."+dgt+"f",val);
                }else{
                    xstr = val;
                }
            }else if ("h".equals(ftype)) {
                //xstr = Parts.rget_hex(fh, fsize);
            }else if ("s".equals(ftype)) {
                xstr = Parts.readString(content,offset,fsize);
            }else{
                val = new BigDecimal(Parts.readInt(content,offset,fsize));
                xstr = val.toString();
            }

            // .................................
            if ("date/time".equals(name)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                xstr =  format.format(new Date(val.longValue()*1000));
                xstr = xstr +"("+val.longValue()+" sec)";
                logger.debug("date/time:: "+xstr +"("+val.longValue()+")");
                // console.log("............... got "+xstr);2019-06-17 16:01:21
            }else if ("unit".equals(name)) {
                xstr = xstr + unitMap.get(xstr);
            }else if ("trace type".equals(name)) {
                xstr += tracetype.get(xstr);
            }

            // don't bother even trying if there are multiple pulse width entries; too lazy
            // to restructure code to handle this case
            if ("number of pulse width entries".equals(name) && val.intValue()>1) {
                logger.warn("WARNING!!!: Cannot handle multiple pulse width entries ({}); aborting",val.intValue());
                return null;
            }

            // .................................

            if ( unit=="" ) {
                block.put(name,xstr);
            }else{
                block.put(name, xstr+" "+unit);
            }
            offset +=fsize;
        }

        // corrrections/adjustment:
        var ior = Float.parseFloat(block.get("index").toString());
        var ss = block.get("sample spacing").toString().split(" ")[0];
        var dx  = Float.parseFloat( ss ) * Parts.SOL / ior;
        block.put("range",dx * Integer.parseInt(block.get("num data points").toString()));
        block.put("resolution", dx * 1000.0); // in meters
//      设置光折射率
        Parts.refraction=Double.parseDouble(block.get("index").toString());
        return block;
    }


}
