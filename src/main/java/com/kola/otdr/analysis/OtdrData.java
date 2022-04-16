package com.kola.otdr.analysis;

import java.util.List;
import java.util.Map;

/**
 * 定义otdr实体
 * @author fusionGroup
 * @date: 2022-04-12 12:15
 */
public class OtdrData {
    private Map<String,Object> dumpData;
    private List<String> traceData;

    public Map<String, Object> getDumpData() {
        return dumpData;
    }

    public void setDumpData(Map<String, Object> dumpData) {
        this.dumpData = dumpData;
    }

    public List<String> getTraceData() {
        return traceData;
    }

    public void setTraceData(List<String> traceData) {
        this.traceData = traceData;
    }
}
