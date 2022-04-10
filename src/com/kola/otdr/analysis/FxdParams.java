package com.kola.otdr.analysis;

import java.util.Map;

public class FxdParams {
    public static Map<String, Object> process(int format, byte[] content){
        String bName="SupParams";
        int offset = 0;
        if (format == 2) {
            String blockId = Parts.readStringSpaceZero(content, offset);
            if(!bName.equals(blockId)) {
                return null;
            }
        }
        return processFxdParam(content, offset);
    }

    private static Map<String, Object> processFxdParam(byte[] content, int offset) {
        return null;
    }
}
