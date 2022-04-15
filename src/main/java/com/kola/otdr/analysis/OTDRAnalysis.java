package com.kola.otdr.analysis;

import com.kola.otdr.exception.FormatException;
import com.kola.otdr.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @Title: OTDRAnalysis
 * OTDR sor file prase
 * @author fusionGroup
 * @date: 2022-03-29 14:02
 */
public class OTDRAnalysis {
    private static Logger logger = LoggerFactory.getLogger("OTDRAnalysis");


    /**
     * 读取并解析OTDR sor文件内容
     *
     * @param fileName 文件名
     * @return 区块集合信息
     */
    public  OtdrData read(String fileName) throws Exception {
       return read(new FileInputStream(fileName),fileName);
    }

    /**
     * 将sor文件的摘要信息写入json文件
     * @param fileName
     * @param dump
     */
    public void writeFileJson(String fileName,Map<String,Object> dump){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName+"_dump.json"));
            out.write(JsonUtils.toJson(dump));
            out.close();
            logger.debug(fileName+"_dump.json 文件创建成功！");
        } catch (IOException e) {
        }
    }

    /**
     * 将sor文件的数据写入data文件
     * @param fileName
     * @param tracedata
     */
    public void writeFileData(String fileName,List<String> tracedata){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName+"_trace.data"));
            for(String hang:tracedata) {
                out.write(hang +"\n");
            }
            out.close();
            logger.debug(fileName+"_trace.data 文件创建成功！");
        } catch (IOException e) {
        }
    }

    /**
     * 读取OTDR输入流内容
     *
     * @param input 文件输入流
     * @return 区块集合信息
     */
    public  OtdrData read(InputStream input,String fileName) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        byte[] content = out.toByteArray();
        out.close();
        input.close();
        //获取字节内容
        OtdrData results = read(content,fileName);
        return results;
    }

    public OtdrData read(byte[] content, String fileName) throws Exception {
        OtdrData data = new OtdrData();
        List<String> tracedata=new ArrayList();
        //创建Map块
        Map<String, Object> results = new LinkedHashMap<>();
        results.put("filename",fileName);

        logger.debug("===============Mapblock===============");
        String status = Mapblock.process(results,content);
        logger.debug("===============Mapblock end==========");
        if(!"ok".equals(status)){
            throw new FormatException("解析 "+fileName+" 文件格式异常");
        }
        int format = Integer.parseInt(results.get("format").toString());

        Map<String,Object> blocks=(Map)results.get("blocks");
        for(String bName:blocks.keySet()){
            Map<String,Object> ref = (Map)blocks.get(bName);
            Map<String, Object> block = new HashMap<>();
            String bname = ref.get("name").toString();
            int length = Integer.parseInt(ref.get("size").toString());
            int startOffset = Integer.parseInt(ref.get("pos").toString());
            //设置区块字节内容
            byte[] blockContent = new byte[length];
            System.arraycopy(content, startOffset, blockContent, 0, length);
            //            logger.info(bname+" original data："+ Arrays.toString(blockContent));
            logger.debug("==============="+bname+"===============");
            switch (String.valueOf(bname)) {
                case "GenParams":
                    block = GenParams.process(format, blockContent);
                    break;
                case "SupParams":
                    block =  SupParams.process(format, blockContent);
                    setSubOtdr(block.get("OTDR").toString());
                    break;
                case "FxdParams":
                    block =  FxdParams.process(format, blockContent);
                    setNumDataPoint(Integer.parseInt(block.get("num data points").toString()));
                    setResolution(Double.parseDouble(block.get("resolution").toString()));
                    break;
                case "KeyEvents":
                    block =  KeyEvents.process(format, blockContent);
                    break;
                case "LnkParams":
//                    block =  LnkParams.process(format, blockContent);
                    break;
                case "DataPts":
                    block =  DataPts.process(tracedata,format,getSubOtdr(),getResolution(),getNumDataPoint(), blockContent);
                    break;
                case "Cksum":
                    block =  Checksum.process(format, blockContent,content);
                    break;
            }
            logger.debug(bname+" data:: "+block);
            logger.debug("==============="+bname+" end===============");
            if(!block.isEmpty()) {
                results.put(bname, block);
            }

        }
        data.setDump(results);
        data.setTracedata(tracedata);
        return data;
    }



    private int numDataPoint=0;
    private String subOtdr;
    private Double resolution;

    public int getNumDataPoint() {
        return numDataPoint;
    }

    public void setNumDataPoint(int numDataPoint) {
        this.numDataPoint = numDataPoint;
    }
    public String getSubOtdr() {
        return subOtdr;
    }

    public void setSubOtdr(String subOtdr) {
        this.subOtdr = subOtdr;
    }

    public Double getResolution() {
        return resolution;
    }

    public void setResolution(Double resolution) {
        this.resolution = resolution;
    }
}
