package com.kola.otdr.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Title: Mapblock.java
 * @Description: block块
 * @author: fusionGroup
 * @date: 2022-03-30 16:15
 */
public class Mapblock {
    private static Logger logger = LoggerFactory.getLogger("Mapblock");

    public static String process(Map<String, Object> results, byte[] content){
        String status = "ok";
        int offset = 0;
        //读取并设置Map区块名称
        String mapBlockId = Parts.readStringSpaceZero(content, offset);
        results.put("blockId", mapBlockId);
        logger.info("===============Mapblock===============");
        logger.info("blockId : " + results.get("blockId"));
        if(mapBlockId.equals("Map")){
            offset += mapBlockId.getBytes().length + 1;
            results.put("format", 2);
        }else{
            offset = 0;
            results.put("format", 1);
        }
        logger.info("format : " + results.get("results"));
        //读取并设置Map区块版本
        results.put("version", Parts.readInt(content, offset, Parts.LENGTH_SHORT)*0.01);
        logger.info("version : " + results.get("version"));
        Map<String,Object> mapBlock = new HashMap<>();
        offset += Parts.LENGTH_SHORT;
        //读取并设置Map区块长度
        int mapLength = Parts.readInt(content, offset, Parts.LENGTH_LONG);
        mapBlock.put("nbytes", mapLength);
        logger.info("nbytes : " + mapBlock.get("nbytes"));
        offset += Parts.LENGTH_LONG;
        //读取并设置区块数
        int nblocks = Parts.readInt(content, offset, Parts.LENGTH_SHORT)-1;
        mapBlock.put("nblocks", nblocks);
        logger.info("nblocks : " + mapBlock.get("nblocks"));
        results.put("mapblock",mapBlock);
        int startpos=mapLength;
        offset += Parts.LENGTH_SHORT;
        Map<String,Object> blocks = new LinkedHashMap<>();
        for(int i=0;i<nblocks;i++){
            String bname= Parts.readStringSpaceZero(content,offset);
            offset += bname.getBytes().length + 1;
            double bver = Parts.readInt(content, offset, Parts.LENGTH_SHORT)*0.01;
            offset += Parts.LENGTH_SHORT;
            int bsize = Parts.readInt(content, offset,Parts.LENGTH_LONG);
            offset += Parts.LENGTH_LONG;
            Map ref = Map.of("name", bname,
                    "version",bver,
                    "size",bsize,
                    "pos",startpos,
                    "order", i);
            startpos += bsize;
            logger.info("blocks:" + i+" : "+ref.toString());
            blocks.put(bname,ref);
        }
        results.put("blocks",blocks);
        logger.info("===============Mapblock==end==========");
        return status;
    }
}
