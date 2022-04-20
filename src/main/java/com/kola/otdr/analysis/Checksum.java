package com.kola.otdr.analysis;

import com.kola.otdr.util.CRC16;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 校验码
 * @author fusionGroup
 */
public class Checksum {
    private static Logger logger = LoggerFactory.getLogger("Checksum");

    public static Map<String, Object> process(int format, byte[] blockContent,byte[] content){
        String bName="Cksum";
        int offset = 0;
        if (format == 2) {
            String blockId = Parts.readStringSpaceZero(blockContent, offset);
            if(!bName.equals(blockId)) {
                return null;
            }else{
                offset += blockId.getBytes().length + 1;
            }
        }
        Map<String, Object> block = new HashMap<>();
        int csum=Parts.readInt(blockContent, offset, 2);
        block.put("csum",csum);
        int digest=ourDigest(content,offset+2);

        block.put("checksum_ours",digest);
        if(digest==csum){
            block.put("match",true);
        }else{
            block.put("match",false);
        }
        logger.info("校验码："+csum +",重新校验结构："+digest +",是否匹配："+block.get("match"));
        return block;
    }

    private static int ourDigest(byte[] content,int cLength){
        int size = content.length;
        byte[] buffer = new byte[size-cLength];
        System.arraycopy(content, 0, buffer, 0, size-cLength);
//        System.out.println(CRC16Util.calcCrc16(buffer));//crc校验码不正确
//        System.out.println(CRC16Util.crc16ccitt(buffer));//crc校验码不正确
//        System.out.println("crc_16_CCITT_False: "+CrcUtil.crc_16_CCITT_False(buffer,0));//
        int digest = CRC16.crc16(buffer); //    //crc16-ccitt-false
//        String bName="Cksum";
//        size=size-cLength;
//        byte[] buffer2 = new byte[size];
//        System.arraycopy(content, 0, buffer2, 0, size);
//        System.out.println("crc16:"+CRC16.crc16(buffer2)); //    //crc16-ccitt-false
//        System.out.println("crc_16_CCITT_False: "+CrcUtil.crc_16_CCITT_False(buffer2,0));
        return digest;
    }

}
