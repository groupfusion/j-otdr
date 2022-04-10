package com.kola.otdr.analysis;

import java.util.HashMap;
import java.util.Map;

public class Checksum {
    public static Map<String, Object> process(int format, byte[] blockContent,byte[] content){
        String bName="Cksum";
        int offset = 0;
        if (format == 2) {
            String blockId = Parts.readStringSpaceZero(blockContent, offset);
            if(!bName.equals(blockId)) {
                return null;
            }else{
                String name = Parts.readStringSpaceZero(blockContent, offset);
                offset += name.getBytes().length + 1;
            }
        }
        Map<String, Object> block = new HashMap<>();
        int csum=Parts.readInt(blockContent, offset, 2);
        block.put("csum",csum);
        int digest=ourDigest(content);
        block.put("checksum_ours",digest);
        if(digest==csum){
            block.put("match",true);
        }else{
            block.put("match",false);
        }

        return block;
    }

    private static int ourDigest(byte[] content){
        int size = content.length;
        byte[] buffer = new byte[size-2];
        System.arraycopy(content, 0, buffer, 0, size-2);
        System.out.println(CRC16Util.calcCrc16(buffer));//crc校验码不正确
        System.out.println(CRC16Util.crc16ccitt(buffer));//crc校验码不正确
        int digest = CRC16.crc16(buffer); //    //crc16-ccitt-false
        return digest;
    }

}
