package com.kola.otdr.analysis;

import java.util.List;
import java.util.Map;

/**
 * @Title: OtdrData.java
 * @Description:
 * @Package: com.kola.otdr.analysis
 * @author: xingyun－zhanghuijun
 * @date: 2022-04-12 12:15
 */
public class OtdrData {
    private Map<String,Object> params;
    private List<String> tracedata;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<String> getTracedata() {
        return tracedata;
    }

    public void setTracedata(List<String> tracedata) {
        this.tracedata = tracedata;
    }
}
