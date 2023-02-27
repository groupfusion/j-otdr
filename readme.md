jOTDR：Simple OTDR SOR file parse written in Java 

jOTDR 支持jdk11以上版本。

OTDR java版本实现主要参考
[OTDR文件格式说明](https://github-wiki-see.page/m/sid5432/pubOTDR/wiki/The-OTDR-%28Optical-Time-Domain-Reflectometer%29-Data-Format)
[jsOTDR](https://github.com/sid5432/jsOTDR)
[pyOTDR](https://github.com/sid5432/pyOTDR)

这里引用[sid5432 Hsin-Yu Sidney Li](https://github.com/sid5432/)说明
```text
The SOR ("Standard OTDR Record") data format is used to store OTDR (optical time-domain reflectometer ) fiber data. The format is defined by the Telcordia SR-4731, issue 2 standard. While it is a standard, it is unfortunately not open, in that the specifics of the data format are not openly available. You can buy the standards document from Telcordia for $750 US (as of this writing), but this was beyond my budget. (And likely comes with all sorts of licensing restrictions. I wouldn't know; I have never seen the document!)

There are several freely available OTDR trace readers available for download on the web, but most do not allow exporting the trace curve into, say, a CSV file for further analysis, and only one that I've found that runs natively on Linux (but without source code; although some of these do work in the Wine emulator). There have been requests on various Internet forums asking for information on how to extract the trace data, but I am not aware of anyone providing any answers beyond pointing to the free readers and the Telcordia standard.

Fortunately the data format is not particularly hard to decipher. The table of contents on the Telcordia SR-4731, issue 2 page provides several clues, as does the Wikipedia page on optical time-domain reflectometer.

Using a binary-file editor/viewer and comparing the outputs from some free OTDR SOR file readers, I was able to piece together most of the encoding in the SOR data format and written a simple program (in Python) that parses the SOR file and dumps the trace data into a file. (For a more detailed description, other than reading the source code, see my blog post).

Presented here for your entertainment are my findings, in the hope that it will be useful to other people. But be aware that the information provided here is based on guess work from looking at a limited number of sample files. I can not guarantee that there are no mistakes, or that I have uncovered all possible exceptions to the rules that I have deduced from the sample files. use it at your own risk! You have been warned!

```
Java版本实现从sid5432的pyOTDR和jsOTDR移植而来，并参考了[developer-yong/OTDRAnalysis](https://github.com/developer-yong/OTDRAnalysis)的OTDR文件解析

Java版本的OTDR SOR文件解析提供了以下方法：

```java
OTDRAnalysis analysis = new OTDRAnalysis();
String sorFileName="demo_ab.sor";
String fileName=sorFileName.split(".")[0];
OtdrData otdrData = analysis.read(sorFileName);//读取并解析OTDR sor文件
analysis.writeFileJson(fileName,otdrData.getDump());//将sor文件的摘要信息写入json文件
analysis.writeFileData(fileName,otdrData.getTracedata());//将sor文件的数据写入data文件,这里默认采用"\t"分割
```



