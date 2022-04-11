javaOTDR 

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

[OTDR文件格式说明](https://github-wiki-see.page/m/sid5432/pubOTDR/wiki/The-OTDR-%28Optical-Time-Domain-Reflectometer%29-Data-Format)
[jsOTDR](https://github.com/sid5432/jsOTDR)
[pyOTDR](https://github.com/sid5432/pyOTDR)
