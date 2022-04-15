OTDR 测量文件 SOR文件解析
OTDR 标准格式文件是遵守Bellcore 标准的

概要【文件名称、测量时间、光缆标识、纤芯标识、起点、终点、设备型号、设备序列号等】
测量结果【长度、总损耗、平均损耗、平均接头损耗、最大接头损耗、跨段光回损等】
测量参数【测量范围、波长、脉宽、持续时间、是否高分辨率、折射率、分辨率、背向散射、余长系数等】
阈值设定【单项接头损耗、单项连接器损耗、反射率、光纤区段衰减、跨段长度、光回损等】
测量事件【事件位置、跨段损耗、反射率、事件类型、事件状态（回波、分离）事件起点索引、事件终点索引】
区段统计【累计损耗、衰减系数、区段长度、起点索引、终点索引、起点位置、终点位置】
测量点信息【double类型、UINT类型】

一般来讲，所有的解析文件都是显示这些信息
[原文链接](https://blog.csdn.net/qq_28207461/article/details/102466618)


## OTDR Sor File Data format

### Block Details

#### Map block


- 0、blockId 默认为"map\0" ；从v2.0开始有此部分，v1.0版本从version开始。
- 0-1、version 版本信息；长度2个字节
- 2-5、blockSize MapBlock块字节长度； 4个字节
- 6-7、blockNum  MapBlock块数量；2个字节
- :: blocks
- ::1 blockName block块名称,string,以\0结束
- ::2 blockVer  block块版本, 2个字节
- ::3 blockSize block块字节长度，4个字节，据此在结合MapBlock块字节长度计算块的起始位置；



#### Gen Params

ver.2
- 1、 block ID block块名称，默认值"GenParams\0" 
- 2、 Language Code 语言编码，长度2个字节
- 3、 cable ID 光缆id，string类型，自定义长度，以\0结束
- 4、 fiber ID 纤芯id，string类型，自定义长度，以\0结束
- 5、 fiber type 光纤类型，长度2个字节，无符号整数
- 6、 wavelength 波长，长度2个字节，无符号整数
- 7、 location A 起点位置，string类型，自定义长度，以\0结束
- 8、 location B 终点位置，string类型，自定义长度，以\0结束
- 9、 cable code 光缆编号，string类型，自定义长度，以\0结束
- 10、build condition 构建条件，2 字节字符
- 11、user offset 用户补偿，无符号整数，4个字节
- 12、user offset distance  补偿长度，无符号整数，4个字节
- 13、operator 运算符，string类型，自定义长度，以\0结束
- 14、comments 备注，string类型，自定义长度，以\0结束

ver.1
- 1、Language Code 语言编码，长度2个字节
- 2、cable ID, 光缆id，string类型，自定义长度，以\0结束
- 3、fiber ID, 纤芯id，string类型，自定义长度，以\0结束
- 4、wavelength,  波长，长度2个字节，无符号整数
- 5、location A, 起点位置，string类型，自定义长度，以\0结束
- 6、location B, 终点位置，string类型，自定义长度，以\0结束
- 7、cable code/fiber type, 光缆编号/光纤类型 string类型，自定义长度，以\0结束
- 8、build condition,  构建条件，2 字节字符
- 9、user offset,  用户补偿，无符号整数，4个字节
- 10、operator, 运算符，string类型，自定义长度，以\0结束
- 11、comments, 备注，string类型，自定义长度，以\0结束

#### Sup Params 

- 1、block ID block块名称，默认值"SupParams\0" 
- 2、supplier, 供应商 string类型，自定义长度，以\0结束
- 3、OTDR,  OTDR机器信息，string类型，自定义长度，以\0结束
- 4、OTDR S/N,OTDR机器序列号，string类型，自定义长度，以\0结束
- 5、optical module, 光学模块信息 ，string类型，自定义长度，以\0结束
- 6、optical module S/N, 光学模块序列号，string类型，自定义长度，以\0结束
- 7、software version,  软件版本 ，string类型，自定义长度，以\0结束
- 8、other,  关于设备的其他信息，string类型，自定义长度，以\0结束

#### fxd params

Ver.2
-   0-3：date/time : 日期/时间：整数，4字节
-   4-5：unit : 距离单位（km,mt,ft,kf,mi）：2字节
-   6-7：wavelength : 实际波长：整数，2字节
-  8-11：acquisition offset: 获得补偿，整数，4个字节; 
- 12-15：acquisition offset distance：获得补偿，整数，4个字节; 
- 16-17：number of pulse width entries：输入脉冲宽度总数，整数，2个字节; 
- 18-19：pulse-width : 脉冲宽度：2字节
- 20-23：sample spacing:  样本（数据）间距：4字节
- 24-27：number of data points : 跟踪中的数据点数：4 字节
- 28-31：index of refraction : 折射率：4字节
- 32-33：backscattering coefficient  : 后向散射系数：2字节
- 34-37：number of averages : 平均数：4字节
- 38-39：averaging time in seconds：平均时间（每秒），整数，2个字节; 
- 40-43：acquisition range (km): 采集范围：4 个字节
- 44-47：acquisition range distance：采集距离 ；整数，4个字节;
- 48-51：front panel offset ：前面板偏移量；整数，4个字节;
- 52-53：noise floor level ：噪音水平；2个字节; 
- 54-55：noise floor scaling factor：噪音比例因子：整数，2个字节; 
- 56-57：power offset first point：首次功率偏移点；2个字节; 
- 58-59：loss threshold:  : 丢失阈值：2字节
- 60-61：reflection threshold:  : 反射阈值：2字节
- 62-63：end-of-transmission threshold : 传输结束阈值：2字节
- 64-65：trace type:  : 跟踪类型(ST,RT,DT, or RF)：2 个字符
- 66-81：window coordinates  : 窗口坐标：16 字节，两组 （x1，y1，x2,y2)
    

#### key events


#### data points block


#### checksum block

- block ID block块名称，默认值"Cksum\0"  
- 0-01: checksum : 校验码 


