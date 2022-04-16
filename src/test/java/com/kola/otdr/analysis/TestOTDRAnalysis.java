package com.kola.otdr.analysis;


import com.kola.otdr.util.JsonUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class TestOTDRAnalysis {
    private Logger logger = LoggerFactory.getLogger("TestOTDRAnalysis");
    @Test
    public  void batchAnalysis() {
        try {
            OTDRAnalysis analysis = new OTDRAnalysis();
            String fileName = "";
            OtdrData otdrData = null;
            String path = "data";
            for (int i = 5; i < 49; i++) {
                fileName = String.format("%02d", i);
                String filepath=path + "/" + fileName + ".SOR";
                otdrData = analysis.read(filepath);
                analysis.writeFileJson(path + "/" + fileName, otdrData.getDumpData());
                analysis.writeFileData(path + "/" + fileName, otdrData.getTraceData());
            }
        }catch (Exception e){
        }
    }

    @Test
    public  void batchAnalysis2() {
        try {
            OTDRAnalysis analysis = new OTDRAnalysis();
            String fileName = "";
            OtdrData otdrData = null;
            String path = "data";
            File f = new File(path);//获取路径  F:\测试目录
            if (!f.exists()) {
                logger.info(path + " not exists");//不存在就输出
                return;
            }
            File fa[] = f.listFiles();//用数组接收  F:\笔记总结\C#, F:\笔记总结\if语句.txt
            for (int i = 0; i < fa.length; i++) {//循环遍历
                File fs = fa[i];//获取数组中的第i个
                if (fs.isDirectory()) {
                    logger.info(fs.getName() + " [目录]");//如果是目录就输出
                } else {
                    fileName = fs.getName();
                    if(fileName.endsWith(".sor")){
                        logger.info(fs.getName()+"[处理sor文件]");//
                        String filepath=path + "/" + fileName;
                        otdrData = analysis.read(filepath);
                        analysis.writeFileJson(path + "/" + fileName, otdrData.getDumpData());
                        analysis.writeFileData(path + "/" + fileName, otdrData.getTraceData());
                    }else{
                        logger.info(fs.getName()+"[忽略非sor文件]");//
                    }

//
                }
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
            logger.info("tracedata:" + otdrData.getTraceData());
            logger.info("dump-data:" + ":" + JsonUtils.toJson(otdrData.getDumpData()));
            analysis.writeFileJson(fileName, otdrData.getDumpData());
            analysis.writeFileData(fileName, otdrData.getTraceData());
        }catch (Exception e){
            logger.error(""+e);
        }
    }
}
