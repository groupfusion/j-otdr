package com.kola.otdr.analysis;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

/**
 * @Title: OTDRAnalysis
 * @date: 2022-03-29 14:02
 */
public class OTDRAnalysis {
    private static Logger logger = Logger.getLogger("OTDRAnalysis");
    private static int LENGTH_SHORT = 2;
    private static int LENGTH_LONG = 4;

    public static void main(String[] args) throws IOException {
        read("sample1310_lowDR.sor");
//        read("demo_ab.sor");
    }
    /**
     * 读取OTDR文件内容
     *
     * @param fileName 文件名
     * @return 区块集合信息
     */
    public static void read(String fileName) throws IOException {
        read(new FileInputStream(fileName),fileName);
    }

    /**
     * 读取OTDR输入流内容
     *
     * @param input 文件输入流
     * @return 区块集合信息
     */
    public static void read(InputStream input,String fileName) throws IOException {
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
         read(content,fileName);
    }

    public static Map<String, Object> read(byte[] content,String fileName) {
//        List<Map<String, Object>> blocks = new ArrayList<>();
        String tracedata[];

        int offset = 0;
        //创建Map块
        Map<String, Object> results = new HashMap<>();
        results.put("filename",fileName);

        String mapBlockId = Parts.readStringSpaceZero(content, offset);
        System.out.println("Map Block ID : " + mapBlockId);
        int format=2;
        if(mapBlockId.equals("Map")){
            offset += mapBlockId.getBytes().length + 1;
            format=2;
        }else{
            offset = 0;
            format=1;
        }
        String status = Mapblock.process(results,content,offset);
        if(!"ok".equals(status)){
//            return ;
        }

        Map<String, Object> mapblock = (Map)results.get("mapblock");
        int mapLength = (int)mapblock.get("nbytes");
        System.out.println(mapLength);
        //设置区块字节内容
//        byte[] mapContent = new byte[mapLength];
//        System.arraycopy(content, 0, mapContent, 0, mapLength);
//        results.put("content", Arrays.toString(mapContent));
//        blocks.add(results);

//        offset += LENGTH_SHORT;
//        int contentOffset = mapLength;

        Map<String,Object> blocks=(Map)results.get("blocks");
        for(String bName:blocks.keySet()){
            Map<String, Object> block = new HashMap<>();
            Map<String,Object> ref = (Map)blocks.get(bName);
            String bname = ref.get("name").toString();
            int length = Integer.parseInt(ref.get("size").toString());
            int startOffset = Integer.parseInt(ref.get("pos").toString());
            //设置区块字节内容
            byte[] blockContent = new byte[length];
            System.arraycopy(content, startOffset, blockContent, 0, length);
            System.out.println(bname+"："+Arrays.toString(blockContent));;

            switch (String.valueOf(bname)) {
                case "GenParams":
                    block = GenParams.process(format, blockContent);
                    break;
                case "SupParams":
                    block =  SupParams.process(format, blockContent);
                    break;
                case "FxdParams":
                    block =  FxdParams.process(format, blockContent);
                    break;
                case "KeyEvents":
    //                    readKeyEvents(block, blockContent);
                    break;
                case "LnkParams":
    //                    readLnkParams(block, blockContent);
                    break;
                case "DataPts":
    //                    readDataPts(block, blockContent);
                    break;
                case "Cksum":
                    block =  Checksum.process(format, blockContent,content);
                    break;
            }
            System.out.println(bname+":"+block);
            results.put(bname,block);

        }
//        System.out.println(results);
        return results;
    }

}
