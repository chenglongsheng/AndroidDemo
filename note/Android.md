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

# 存储方式

## sp存储

kv存储在xml中

## 内部文件存储

- 读取文件 `FileInputStream fis = openFileInput("xxx");`
- 保存文件 `FileOutputStream fos = openFileOutput("xxx", MODE_PRIVATE);`
- 得到files文件夹对象 `File filesDir = getFileDir();`
- 得到asserts下的文件 `InputStream is = context.getAssets().open("xxx");`
- 加载图片文件 `Bitmap b = BitmapFactory.decodeFile("");`

## 外部文件存储

数据保存的路径：

- /storage/sdcard/Android/data/packageName/files/ 其它应用可以访问，应用卸载时删除
- /storage/sdcard/xxx/ 其它应用可以访问，应用卸载时不会删除

Environment：操作SD卡的工具类
得到SD卡的状态：`Environment.getExternalStorageState()`
得到SD卡的路径：`Environment.getExternalStorageDirectory()`
SD卡可读写的挂载状态值：`Environment.MEDIA_MOUNTED`
context.getExternalFilesDir():
/mnt/sdcard/Android/data/pageckage_name/files/xxx.txt
操作SD卡的权限：
`android.permission.WRITE_EXTERNAL_STORAGE`

## SQLite数据库存储

有一定结构的数据，关系型数据库

文件类型是`.db`

路径：/data/data/包名/databases/xxx.db

默认情况其他应用不可以访问，通过ContentProvider提供数据访问

### 相关API

SQLiteOpenHelper

SQLiteDatabase

## 服务器存储

使用网络库处理HTTP请求

# Handler消息机制

摘选关键源码分析

## Message

本类顾名思义作为消息载体

```java
public final class Message implements Parcelable {
    public int what; // 消息的标识，用于区分的id
    public long when; // 消息发生的目标时间
    Handler target; // 用于处理该消息的Handler对象引用
    Runnable callback; // Message的回调函数
    Message next; // 下一个消息的引用，Message是作为单链表的节点角色用来组成消息复用池，消息缓存池本质是单链表，每个节点的数据字段都置空或归零
    private static Message sPool; // 永远指向头节点，表明消息池有缓存数据

    /**
     * 生产消息，sPool不为空表明消息池也就是链表有节点，取头节点作为本次消息的载体，sPool指针后移。若没有生产过消息，则new一个消息
     */
    public static Message obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                Message m = sPool;
                sPool = m.next;
                m.next = null;
                m.flags = 0; // clear in-use flag
                sPoolSize--;
                return m;
            }
        }
        return new Message();
    }

    /**
     * 回收消息，把所有字段重置，在链表中接为头节点。链表最大数量小于50个
     */
    void recycleUnchecked() {
        // Mark the message as in use while it remains in the recycled object pool.
        // Clear out all other details.
        flags = FLAG_IN_USE;
        what = 0;
        arg1 = 0;
        arg2 = 0;
        obj = null;
        replyTo = null;
        sendingUid = UID_NONE;
        workSourceUid = UID_NONE;
        when = 0;
        target = null;
        callback = null;
        data = null;

        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool;
                sPool = this;
                sPoolSize++;
            }
        }
    }
}
```

创建消息实例应该使用`Message.obtain()`
函数从缓存池中复用以减少内存消耗。消息缓存池实现原理是数量小于50个节点的单链表，消息是单链表中的节点。获取消息和回收消息操作的都是头节点

## Handler

发送消息、分发处理消息、移除未处理的消息

常用的`post(Runnable r)`和`sendMessage(Message msg)`以及一系列带时间延迟的重载函数，
最终调用的都是`sendMessageAtTime(Message msg, long uptimeMillis)`
函数，而这个函数是执行`enqueueMessage`函数。
在调用`sendMessageAtTime(Message msg, long uptimeMillis)`函数之前，分两类函数：

- xxxAtTime：内部没有使用`SystemClock.uptimeMillis()`，实参没有加上这个值会打乱Message的优先级
- xxxDelayed：行参被加上`SystemClock.uptimeMillis()`，会按照`when`值升序插入队列

```java
public class Handler {
    final Looper mLooper;
    final MessageQueue mQueue;
    final Callback mCallback;

    public void handleMessage(@NonNull Message msg) {
    }

    // 消息分发有三个走向，消息有Runnable就只处理消息带的；否则handler的回调存在就执行，
    // 这个回调需要返回true就不会再执行我们重写handler的handleMessage
    public void dispatchMessage(@NonNull Message msg) {
        if (msg.callback != null) {
            handleCallback(msg); // 1
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) { // 2
                    return;
                }
            }
            handleMessage(msg); // 3
        }
    }

    private boolean enqueueMessage(@NonNull MessageQueue queue, @NonNull Message msg,
                                   long uptimeMillis) {
        msg.target = this; // 在经由handler发送消息时指明处理消息的handler，this指向自己
        msg.workSourceUid = ThreadLocalWorkSource.getUid();

        if (mAsynchronous) {
            msg.setAsynchronous(true);
        }
        return queue.enqueueMessage(msg, uptimeMillis); // 消息进队列由MessageQueue消费消息
    }
}
```

## MessageQueue

MessageQueue主要作用处理**消息入队**和**取消息**，本身和队列没有什么关联。

在`enqueueMessage(Message msg, long when)`函数中，实现对未处理消息链表查询到合适位置插入本次新增的节点

`next()`是对在链表中取元素，有三个关键点：

- nativePollOnce(ptr, nextPollTimeoutMillis);
-

```java
public final class MessageQueue {
    Message mMessages; // 取名意为消息集合，但是类型是Message，作用是代指链表的头节点。存在头节点则链表存在数据

    boolean enqueueMessage(Message msg, long when) {
        if (msg.target == null) {
            throw new IllegalArgumentException("Message must have a target.");
        }

        synchronized (this) {
            // 容错处理
            if (msg.isInUse()) {
                throw new IllegalStateException(msg + " This message is already in use.");
            }

            if (mQuitting) {
                IllegalStateException e = new IllegalStateException(
                        msg.target + " sending message to a Handler on a dead thread");
                Log.w(TAG, e.getMessage(), e);
                msg.recycle();
                return false;
            }

            msg.markInUse();
            msg.when = when; // 预期处理时间
            Message p = mMessages; // 记录当前头节点
            boolean needWake;
            if (p == null || when == 0 || when < p.when) {
                // 第一次插入链表以及后续按优先级插入到头节点
                msg.next = p;
                mMessages = msg; // 前移指向当前的头节点
                needWake = mBlocked;
            } else {
                // 在链表中插入节点，新插入节点的when >= 当前头节点的when
                needWake = mBlocked && p.target == null && msg.isAsynchronous();
                Message prev; // 记录指针的前一个节点
                for (; ; ) { // 遍历链表找到合适插入的位置
                    prev = p;
                    p = p.next; // 指针后移
                    if (p == null || when < p.when) { // 到达尾节点或者排序到合适位置后停止
                        break;
                    }
                    if (needWake && p.isAsynchronous()) {
                        needWake = false;
                    }
                }
                // 排序完成插入节点
                msg.next = p;
                prev.next = msg;
            }

            // We can assume mPtr != 0 because mQuitting is false.
            if (needWake) {
                nativeWake(mPtr); // 本地方法，唤醒CPU给予调度
            }
        }
        return true;
    }

    Message next() {
        // Return here if the message loop has already quit and been disposed.
        // This can happen if the application tries to restart a looper after quit
        // which is not supported.
        final long ptr = mPtr;
        if (ptr == 0) {
            return null;
        }

        int pendingIdleHandlerCount = -1; // -1 only during first iteration
        int nextPollTimeoutMillis = 0;
        // 进入阻塞状态取消息
        for (; ; ) {
            if (nextPollTimeoutMillis != 0) {
                Binder.flushPendingCommands();
            }

            nativePollOnce(ptr, nextPollTimeoutMillis); // 本地方法 唤醒调度

            synchronized (this) {
                // Try to retrieve the next message.  Return if found.
                final long now = SystemClock.uptimeMillis();
                Message prevMsg = null; // 指针前一个节点
                Message msg = mMessages; // 指针
                // 头节点没有Handler处理，就去找异步消息
                // 头节点没有handler来处理消息？在Handler的enqueueMessage中有msg.target = this;。所以这类消息的target不可能为空，疑问点
                if (msg != null && msg.target == null) {
                    do { // 指针后移
                        prevMsg = msg;
                        msg = msg.next;
                    } while (msg != null && !msg.isAsynchronous()); // 遇到异步消息停止后移
                }
                if (msg != null) {
                    if (now < msg.when) { // 没有到达消息的预期处理时间点，记录下这个消息的预期被处理到现在的时间差
                        nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                    } else {
                        // Got a message.
                        mBlocked = false;
                        if (prevMsg != null) {
                            prevMsg.next = msg.next; // 从链表中移除这个节点
                        } else {
                            mMessages = msg.next; // 这个就是头节点
                        }
                        msg.next = null; // 从链表中移除这个节点
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

## Looper

Looper作用是让当前线程阻塞从MessageQueue中获取消息给Handler分发Message

通过`prepare()`函数实例化一个Looper

```java
public final class Looper {
    // 通过ThreadLocal确保一个处理线程对一个Looper
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }

    private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);
        mThread = Thread.currentThread();
    }
}
```

调用`loop()`函数执行死循环获取消息

```
for (;;) {
    if (!loopOnce(me, ident, thresholdOverride)) {
        return;
    }
}
```

在`loopOnce`中取消息

```java
public final class Looper {
    private static boolean loopOnce(final Looper me,
                                    final long ident, final int thresholdOverride) {
        Message msg = me.mQueue.next(); // 当取出msg为空的时候，应用应该是退出
        if (msg == null) {
            // No message indicates that the message queue is quitting.
            return false;
        }

        // This must be in a local variable, in case a UI event sets the logger
        final Printer logging = me.mLogging;
        if (logging != null) {
            logging.println(">>>>> Dispatching to " + msg.target + " "
                    + msg.callback + ": " + msg.what);
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
            msg.target.dispatchMessage(msg); // 把消息给handler去分发消息，1、msg的回调 2、handler的回调 3、重写的handleMessage
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
                if (showSlowLog(slowDeliveryThresholdMs, msg.when, dispatchStart, "delivery",
                        msg)) {
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
            Log.wtf(TAG, "Thread identity changed from 0x"
                    + Long.toHexString(ident) + " to 0x"
                    + Long.toHexString(newIdent) + " while dispatching to "
                    + msg.target.getClass().getName() + " "
                    + msg.callback + " what=" + msg.what);
        }

        msg.recycleUnchecked(); // 回收消息，重置所属消息的变量

        return true;
    }
}
```

主线程Handler的启动

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

## 屏障消息

在上述中，MessageQueue的next中有处理Message的target为空的情况，意味着这个Message是没有Handler处理的。
而在后续逻辑中会寻找链表中标记为**异步**的Message，而后返回这个异步Message。
Message分成同步消息、屏障消息、异步消息，当插入屏障消息就会跳过所有同步消息

通过`postSyncBarrier()`
函数发出没有handler的屏障消息，这个消息也是有序的，调用重载函数`postSyncBarrier(SystemClock.uptimeMillis());`

```java
public final class MessageQueue {
    public int postSyncBarrier() {
        return postSyncBarrier(SystemClock.uptimeMillis());
    }

    // 本质是enqueueMessage入队的操作
    private int postSyncBarrier(long when) {
        // Enqueue a new sync barrier token.
        // We don't need to wake the queue because the purpose of a barrier is to stall it.
        synchronized (this) {
            final int token = mNextBarrierToken++;
            final Message msg = Message.obtain();
            msg.markInUse();
            msg.when = when;
            msg.arg1 = token;

            Message prev = null;
            Message p = mMessages;
            if (when != 0) {
                while (p != null && p.when <= when) {
                    prev = p;
                    p = p.next;
                }
            }
            if (prev != null) { // invariant: p == prev.next
                msg.next = p;
                prev.next = msg;
            } else {
                msg.next = p;
                mMessages = msg;
            }
            return token;
        }
    }

    public void removeSyncBarrier(int token) {
        // Remove a sync barrier token from the queue.
        // If the queue is no longer stalled by a barrier then wake it.
        synchronized (this) {
            Message prev = null;
            Message p = mMessages;
            while (p != null && (p.target != null || p.arg1 != token)) { // 跳过非屏障消息和非目标屏障消息
                prev = p;
                p = p.next;
            }
            if (p == null) {
                throw new IllegalStateException("The specified message queue synchronization "
                        + " barrier token has not been posted or has already been removed.");
            }
            // 重链表中移除这个屏障消息
            final boolean needWake;
            if (prev != null) {
                prev.next = p.next;
                needWake = false;
            } else {
                mMessages = p.next;
                needWake = mMessages == null || mMessages.target != null;
            }
            p.recycleUnchecked();

            // If the loop is quitting then it is already awake.
            // We can assume mPtr != 0 when mQuitting is false.
            if (needWake && !mQuitting) {
                nativeWake(mPtr);
            }
        }
    }
}
```