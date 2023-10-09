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

Handler

```java
public final class ActivityThread extends ClientTransactionHandler
        implements ActivityThreadInternal {
    public static void main(String[] args) {
        // 省略...

        // 实例化主线程的Looper实例
        Looper.prepareMainLooper();

        // 省略...
        ActivityThread thread = new ActivityThread();
        // 通过binder连接ActivityManager服务
        thread.attach(false, startSeq);

        if (sMainThreadHandler == null) {
            sMainThreadHandler = thread.getHandler();
        }

        // 省略...
        // 开启死循环处理消息
        Looper.loop();

        throw new RuntimeException("Main thread loop unexpectedly exited");
    }
}
```

在Looper中`prepareMainLooper()`函数是由程序启动创建ActivityThread时在main方法调用去实例化Looper。
`loopOnce`方法中核心是在队列中取消息，若是没有消息就结束，然后分发消息，最后回收消息。分三步：
1. `me.mQueue.next();`
2. `msg.target.dispatchMessage(msg);`
3. `msg.recycleUnchecked();`

Looper在实例化的时候创建属于自己的消息队列，并且把当前的线程作为自己的处理线程

```java
public final class Looper {
    private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);
        mThread = Thread.currentThread();
    }

    @Deprecated
    public static void prepareMainLooper() {
        prepare(false);
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }

    public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        if (me.mInLoop) {
            Slog.w(TAG, "Loop again would have the queued messages be executed" + " before this one completed.");
        }

        me.mInLoop = true;

        // Make sure the identity of this thread is that of the local process,
        // and keep track of what that identity token actually is.
        Binder.clearCallingIdentity();
        final long ident = Binder.clearCallingIdentity();

        // Allow overriding a threshold with a system prop. e.g.
        // adb shell 'setprop log.looper.1000.main.slow 1 && stop && start'
        final int thresholdOverride = SystemProperties.getInt("log.looper." + Process.myUid() + "." + Thread.currentThread().getName() + ".slow", 0);

        me.mSlowDeliveryDetected = false;

        // 进入死循环 当false的时候退出循环
        for (; ; ) {
            if (!loopOnce(me, ident, thresholdOverride)) {
                return;
            }
        }
    }

    private static boolean loopOnce(final Looper me, final long ident, final int thresholdOverride) {
        Message msg = me.mQueue.next(); // might block
        if (msg == null) {
            // 没有消息时结束死循环，loop()方法也结束
            return false;
        }

        // This must be in a local variable, in case a UI event sets the logger
        final Printer logging = me.mLogging;
        if (logging != null) {
            logging.println(">>>>> Dispatching to " + msg.target + " " + msg.callback + ": " + msg.what);
        }
        // Make sure the observer won't change while processing a transaction.
        final Observer observer = sObserver;

        final long traceTag = me.mTraceTag;
        long slowDispatchThresholdMs = me.mSlowDispatchThresholdMs;
        long slowDeliveryThresholdMs = me.mSlowDeliveryThresholdMs;
        if (thresholdOverride > 0) {
            slowDispatchThresholdMs = thresholdOverride;
            slowDeliveryThresholdMs = thresholdOverride;
        }
        final boolean logSlowDelivery = (slowDeliveryThresholdMs > 0) && (msg.when > 0);
        final boolean logSlowDispatch = (slowDispatchThresholdMs > 0);

        final boolean needStartTime = logSlowDelivery || logSlowDispatch;
        final boolean needEndTime = logSlowDispatch;

        if (traceTag != 0 && Trace.isTagEnabled(traceTag)) {
            Trace.traceBegin(traceTag, msg.target.getTraceName(msg));
        }

        final long dispatchStart = needStartTime ? SystemClock.uptimeMillis() : 0;
        final long dispatchEnd;
        Object token = null;
        if (observer != null) {
            token = observer.messageDispatchStarting();
        }
        long origWorkSource = ThreadLocalWorkSource.setUid(msg.workSourceUid);
        try {
            // 队列中的下一个msg交由handler分发
            msg.target.dispatchMessage(msg);
            if (observer != null) {
                observer.messageDispatched(token, msg);
            }
            dispatchEnd = needEndTime ? SystemClock.uptimeMillis() : 0;
        } catch (Exception exception) {
            if (observer != null) {
                observer.dispatchingThrewException(token, msg, exception);
            }
            throw exception;
        } finally {
            ThreadLocalWorkSource.restore(origWorkSource);
            if (traceTag != 0) {
                Trace.traceEnd(traceTag);
            }
        }
        if (logSlowDelivery) {
            if (me.mSlowDeliveryDetected) {
                if ((dispatchStart - msg.when) <= 10) {
                    Slog.w(TAG, "Drained");
                    me.mSlowDeliveryDetected = false;
                }
            } else {
                if (showSlowLog(slowDeliveryThresholdMs, msg.when, dispatchStart, "delivery", msg)) {
                    // Once we write a slow delivery log, suppress until the queue drains.
                    me.mSlowDeliveryDetected = true;
                }
            }
        }
        if (logSlowDispatch) {
            showSlowLog(slowDispatchThresholdMs, dispatchStart, dispatchEnd, "dispatch", msg);
        }

        if (logging != null) {
            logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
        }

        // Make sure that during the course of dispatching the
        // identity of the thread wasn't corrupted.
        final long newIdent = Binder.clearCallingIdentity();
        if (ident != newIdent) {
            Log.wtf(TAG, "Thread identity changed from 0x" + Long.toHexString(ident) + " to 0x" + Long.toHexString(newIdent) + " while dispatching to " + msg.target.getClass().getName() + " " + msg.callback + " what=" + msg.what);
        }

        msg.recycleUnchecked();

        // 完成一次消息分发后继续下一次循环
        return true;
    }
}
```

消息队列在Looper被创建的时候被实例化，记录能否退出，以及native层初始化。
在Looper的loopOnce方法中会调用`next()`函数

```java
public final class MessageQueue {
    MessageQueue(boolean quitAllowed) {
        mQuitAllowed = quitAllowed;
        mPtr = nativeInit();
    }

    Message next() {
        // 退出了循环
        final long ptr = mPtr;
        if (ptr == 0) {
            return null;
        }

        int pendingIdleHandlerCount = -1; // -1 only during first iteration
        int nextPollTimeoutMillis = 0;
        // 进入死循环
        for (;;) {
            if (nextPollTimeoutMillis != 0) {
                Binder.flushPendingCommands();
            }

            nativePollOnce(ptr, nextPollTimeoutMillis);

            synchronized (this) {
                // Try to retrieve the next message.  Return if found.
                final long now = SystemClock.uptimeMillis();
                Message prevMsg = null;
                Message msg = mMessages;
                if (msg != null && msg.target == null) {
                    // Stalled by a barrier.  Find the next asynchronous message in the queue.
                    do {
                        prevMsg = msg;
                        msg = msg.next;
                    } while (msg != null && !msg.isAsynchronous());
                }
                if (msg != null) {
                    if (now < msg.when) {
                        // Next message is not ready.  Set a timeout to wake up when it is ready.
                        nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                    } else {
                        // Got a message.
                        mBlocked = false;
                        if (prevMsg != null) {
                            prevMsg.next = msg.next;
                        } else {
                            mMessages = msg.next;
                        }
                        msg.next = null;
                        if (DEBUG) Log.v(TAG, "Returning message: " + msg);
                        msg.markInUse();
                        return msg;
                    }
                } else {
                    // No more messages.
                    nextPollTimeoutMillis = -1;
                }

                // Process the quit message now that all pending messages have been handled.
                if (mQuitting) {
                    dispose();
                    return null;
                }

                // If first time idle, then get the number of idlers to run.
                // Idle handles only run if the queue is empty or if the first message
                // in the queue (possibly a barrier) is due to be handled in the future.
                if (pendingIdleHandlerCount < 0
                        && (mMessages == null || now < mMessages.when)) {
                    pendingIdleHandlerCount = mIdleHandlers.size();
                }
                if (pendingIdleHandlerCount <= 0) {
                    // No idle handlers to run.  Loop and wait some more.
                    mBlocked = true;
                    continue;
                }

                if (mPendingIdleHandlers == null) {
                    mPendingIdleHandlers = new IdleHandler[Math.max(pendingIdleHandlerCount, 4)];
                }
                mPendingIdleHandlers = mIdleHandlers.toArray(mPendingIdleHandlers);
            }

            // Run the idle handlers.
            // We only ever reach this code block during the first iteration.
            for (int i = 0; i < pendingIdleHandlerCount; i++) {
                final IdleHandler idler = mPendingIdleHandlers[i];
                mPendingIdleHandlers[i] = null; // release the reference to the handler

                boolean keep = false;
                try {
                    keep = idler.queueIdle();
                } catch (Throwable t) {
                    Log.wtf(TAG, "IdleHandler threw exception", t);
                }

                if (!keep) {
                    synchronized (this) {
                        mIdleHandlers.remove(idler);
                    }
                }
            }

            // Reset the idle handler count to 0 so we do not run them again.
            pendingIdleHandlerCount = 0;

            // While calling an idle handler, a new message could have been delivered
            // so go back and look again for a pending message without waiting.
            nextPollTimeoutMillis = 0;
        }
    }
}
```