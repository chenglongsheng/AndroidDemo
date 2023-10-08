# 目录

[TOC]

# 一、Android系统架构

官网指导文档https://developer.android.com/guide?hl=zh-cn

![Android 软件堆栈](https://developer.android.com/static/guide/platform/images/android-stack_2x.png?hl=zh-cn)

DVM和JVM区别：

1. 实现不同
    - Dalvik 基于寄存器
    - JVM 基于栈
2. 字节码不同
    - Dalvik 执行**.dex**格式的字节码，是对**.class**文件进行压缩后产生的，文件变小
    - JVM 执行**.class**格式的字节码
3. 运行环境不同
    - Dalvik 一个应用启动都运行一个**单独的虚拟机**运行在一个单独的进程中
    - JVM 只能**运行一个实例**，也就是所有应用都运行在同一个JVM中

目录结构

R.java文件中的string、drawable...对应res目录中的各个文件夹，自动生成对应的名字

drawable-xxx为了适配不同分辨率的手机屏幕

.apk文件本质是一个压缩包，classes.dex源码编译后的字节码文件，AndroidManifest.xml功能清单文件，以及res文件夹包含的资源文件

Android系统文件目录

`/` 根目录

`/data/app/` 存放第三方的apk文件

`/system/app/` 系统应用的文件

`/data/data/packagename/` 对应应用的文件，卸载时会删除

`/storage/sdcard/` sd卡的目录

sdk文件目录结构

/docs 文档

/platforms 这个版本运行需要的jar

/platforms-tools 开发工具，如adb

/samples 样例工程

/source 包含部分系统源码

/tools 开发工具

# 尺寸单位

尺寸：屏幕对角线长度，单位 英寸

分辨率：屏幕显示的像素点

像素密度：每英寸显示的像素数量，越大越精细，计算方式：屏幕对角线的像素点数除以屏幕尺寸

手机密度：以160ppi为基准（像素密度）

# Activity

## 组件的特点

- 它的类必须实现特定接口或**继承特定类**
- 必须在配置文件**配置全类名**
- 它的对象不是通过new来创建的，而是**系统自动创建**的
- 它的对象具有一定的生命周期，它的类中有对应的生命周期**回调函数**

**Activity用来提供一个能够让用户操作并与之交互的界面**

对比Activity和Servlet

|           | Servlet                                                       | Activity                                                            |
|-----------|:--------------------------------------------------------------|---------------------------------------------------------------------|
| 组件        | 服务器端组件                                                        | Android客户端组件                                                        |
| 规范定义的接口或类 | Servlet接口                                                     | Activity类                                                           |
| 注册        | web.xml                                                       | AndroidManifest.xml                                                 |
| 生命周期函数    | `init()`<br/>service()<br/>doGet()<br/>doPost()<br/>destory() | onCreate()<br />onStart()<br />onResume()<br />...<br />onDestroy() |
| 请求的发出源    | 浏览器/移动设备                                                      | 手机屏幕                                                                |

# Intent

直译”意图“，是*Activity*、*Service*和*BroadcastReceiver*三个组件之间通信的**信使**

可以携带数据

分类

- 显示意图 明确定制目标意图
    - 创建 `Intent(Context context, Class clazz)`
    - 使用 操作自己应用组件
- 隐式意图 没有明确指定目标意图
    - 创建 `Intent(String action)`
    - 操作其他应用的组件

# 启动Activity的流程



# Activity的任务栈

在Android中，系统使用Task Stack（Back Stack）结构来存储管理启动的Activity对象

启动应用会创建一个对应的任务栈管理activity对象

只有最上面的任务栈的栈顶才能显示在窗口中



## 启动模式

- standard

  每次start就会产生一个新的实例进栈

- singleTop

  如果存在实例位于栈顶时，不会产生新的实例；不位于栈顶会产生一个新的实例

- singleTask

  只有一个实例默认在当前的栈中

- singleInstance

  只有一个实例，创建时会新建一个栈，且栈中不能有其他实例
