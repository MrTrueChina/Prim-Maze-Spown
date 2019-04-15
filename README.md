# Prim-Maze-Spown
[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

## 简介：
普里姆（Prim）迷宫生成算法：一种常用的迷宫生成算法。其优点是速度快、美观、易于理解、易于书写。美中不足的是它只能生成生成长宽都是单数的迷宫，而且从终点向起点反走难度比正走低很多。<br/>
<br/>

## 文件夹内容：
|文件夹|内容|
| -------- | :--------|
|src/maze/|生成算法|
|src/display|可视化部分|
|src/save/|保存部分|
<br/>

## 普里姆迷宫生成算法：
0.和寻路算法有些像，普里姆算法有一个开表、一个闭表和大量的二维的节点。开表用来存储准备打通的节点，闭表用来存储已经打通的节点。打通这个动作在迷宫生成里叫做“雕刻”。<br/>
<br/>
1.普里姆算法从第一步就和别的迷宫生成算法不一样，他先把整个迷宫填满，之后隔一个格子打一个洞，看起来就像这样：<br/>
<br>
墙墙墙墙墙墙墙<br/>
墙　墙　墙　墙<br/>
墙墙墙墙墙墙墙<br/>
墙　墙　墙　墙<br/>
墙墙墙墙墙墙墙<br/>
墙　墙　墙　墙<br/>
墙墙墙墙墙墙墙<br/>
<br/>
2.选择一个洞作为起点，我选择左上角的那个洞作为起点，起点视为已经打通放入闭表，把和起点相邻的两个点放进开表。<br/>
<br/>
3.最精彩的地方来了！迷宫生成看起来和寻路很像，但他是反着来的——不是路去找节点，而是节点来找路。<br/>
在开表里随机找一个点，然后在这个点的相邻节点里随机找出一个在闭表里的点，打穿这两个点。新打穿的这个点移到闭表，相邻的点加入开表。<br/>
<br/>
4.循环3.直到开表耗竭，此时所有的节点都打穿了，迷宫也就填满了整个区域，完美迷宫完成。<br/>
<br/>
<b>详细内容还请结合代码理解</b>
