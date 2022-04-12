package com.kola.otdr.analysis;

import java.util.List;
import java.util.Map;

/**
 * @Title: OtdrData.java
 * @Description:
 * @author fusionGroup
 * @date: 2022-04-12 12:15
 */
public class OtdrData {
    private Map<String,Object> dump;
    private List<String> tracedata;

    public Map<String, Object> getDump() {
        return dump;
    }

    public void setDump(Map<String, Object> dump) {
        this.dump = dump;
    }

    public List<String> getTracedata() {
        return tracedata;
    }

    public void setTracedata(List<String> tracedata) {
        this.tracedata = tracedata;
    }
}
