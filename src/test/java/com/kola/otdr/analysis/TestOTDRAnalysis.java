package com.kola.otdr.analysis;


import com.kola.otdr.util.JsonUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestOTDRAnalysis {
    private Logger logger = LoggerFactory.getLogger("TestOTDRAnalysis");
    @Test
    public  void batchAnalysis() {
        try {
            OTDRAnalysis analysis = new OTDRAnalysis();
            String fileName = "";
            OtdrData otdrData = null;
            String path = "/Users/xingyun/project/j-otdr/饶氏宾馆至东部开发区";
            for (int i = 5; i < 49; i++) {
                fileName = String.format("%02d", i);
                otdrData = analysis.read(path + "/" + fileName + ".SOR");
                analysis.writeFileJson(path + "/" + fileName, otdrData.getDump());
                analysis.writeFileData(path + "/" + fileName, otdrData.getTracedata());
            }
        }catch (Exception e){

        }
    }
    @Test
    public void simpleAnalysis(){
        try {
            OTDRAnalysis analysis = new OTDRAnalysis();
            String sorFileName = "test.sor";
            String fileName = sorFileName.split("\\.")[0];
            OtdrData otdrData = analysis.read(sorFileName);
            logger.info("tracedata:" + otdrData.getTracedata());
            logger.info(sorFileName + ":" + JsonUtils.toJson(otdrData.getDump()));
            analysis.writeFileJson(fileName, otdrData.getDump());
            analysis.writeFileData(fileName, otdrData.getTracedata());
        }catch (Exception e){
            logger.error(""+e);
        }
    }
}
