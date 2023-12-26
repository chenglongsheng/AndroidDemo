// IMessage.aidl
package com.buyehou.message;

// Declare any non-default types here with import statements

interface IMessage {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /** 发送消息 */
    void send(String message);
}