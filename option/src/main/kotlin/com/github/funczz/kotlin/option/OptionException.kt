package com.github.funczz.kotlin.option

class OptionException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)
