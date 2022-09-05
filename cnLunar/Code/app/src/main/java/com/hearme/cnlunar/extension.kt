package com.hearme.cnlunar

object extension {

    // extension
    val Int.boolean
        get() = this != 0
    val Boolean.int
        get() = if (this) 1 else 0
}