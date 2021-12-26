package com.github.funczz.kotlin.option

import java.util.*

/**
 * 入れ子になっている Option を一段階アンラップする
 * @return <code>Option<Option<T>></code> を アンラップした <code>Option<T></code>
 */
fun <T : Any> Option<Option<T>>.flatten(): Option<T> = when (this) {
    is None<Option<T>> -> Option.none()
    is Some<Option<T>> -> value
}

/**
 * None なら関数 none を適用し、
 * Some なら関数 some を適用する。
 * @param none 引数を持たず、戻り値のない関数
 * @param some 引数として型 T を持ち、戻り値のない関数
 */
inline fun <T : Any> Option<T>.match(none: () -> Unit = {}, some: (T) -> Unit) = when (this) {
    is None<T> -> none()
    is Some<T> -> some(value)
}

/**
 * None なら関数 none を適用し、
 * Some なら関数 some を適用する。
 * @param none 引数を持たず、型 R を返す関数
 * @param some 引数として型 T を持ち、型 R を返す関数
 * @return R
 */
inline fun <T : Any, R : Any> Option<T>.fold(none: () -> R, some: (T) -> R): R = when (this) {
    is None<T> -> none()
    is Some<T> -> some(value)
}

/**
 * Optional を Option に変換する。
 * @return Option
 */
fun <T : Any> Optional<T>.toOption(): Option<T> = when {
    this.isEmpty -> Option.none()
    else -> Option.tee { get() }
}

/**
 * Option を Optional に変換する。
 * @return Optional
 */
fun <T : Any> Option<T>.toOptional(): Optional<T> {
    return fold(
        none = { Optional.empty() },
        some = { Optional.of(it) }
    )
}