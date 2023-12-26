package com.buyehou.message

/**
 * @author Rosen
 */
class MessageStub : IMessage.Stub() {


    override fun basicTypes(
        anInt: Int,
        aLong: Long,
        aBoolean: Boolean,
        aFloat: Float,
        aDouble: Double,
        aString: String?
    ) {
    }

    override fun send(message: String?) {
    }
}