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

        logger.debug("blockId : " + results.get("blockId"));
        if(mapBlockId.equals("Map")){
            offset += mapBlockId.getBytes().length + 1;
            results.put("format", 2);
        }else{
            offset = 0;
            results.put("format", 1);
        }
        logger.debug("format : " + results.get("format"));
        //读取并设置Map区块版本
        String version = String.format("%.2f",Parts.readInt(content, offset, Parts.LENGTH_SHORT)*0.01);
        results.put("version", version);
        logger.debug("version : " + version);
        Map<String,Object> mapBlock = new HashMap<>();
        offset += Parts.LENGTH_SHORT;
        //读取并设置Map区块长度
        int nbytes = Parts.readInt(content, offset, Parts.LENGTH_LONG);
        mapBlock.put("nbytes", nbytes);
        logger.debug("nbytes : " + nbytes);
        offset += Parts.LENGTH_LONG;
        //读取并设置区块数
        int nblocks = Parts.readInt(content, offset, Parts.LENGTH_SHORT)-1;
        mapBlock.put("nblocks", nblocks);
        logger.debug("nblocks : " + nblocks);
        results.put("mapblock",mapBlock);
        int startpos=nbytes;
        offset += Parts.LENGTH_SHORT;
        Map<String,Object> blocks = new LinkedHashMap<>();
        for(int i=0;i<nblocks;i++){
            String bname= Parts.readStringSpaceZero(content,offset);
            offset += bname.getBytes().length + 1;
            String bver = String.format("%.2f",Parts.readInt(content, offset, Parts.LENGTH_SHORT)*0.01);
            offset += Parts.LENGTH_SHORT;
            int bsize = Parts.readInt(content, offset,Parts.LENGTH_LONG);
            offset += Parts.LENGTH_LONG;
            Map ref = Map.of("name", bname,
                    "version",bver,
                    "size",bsize,
                    "pos",startpos,
                    "order", i);
            startpos += bsize;
            logger.debug("blocks:" + i+" : "+ref.toString());
            blocks.put(bname,ref);
        }
        results.put("blocks",blocks);

        return status;
    }
}
