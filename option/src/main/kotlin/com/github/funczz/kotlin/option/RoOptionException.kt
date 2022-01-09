package com.github.funczz.kotlin.option

class RoOptionException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)
