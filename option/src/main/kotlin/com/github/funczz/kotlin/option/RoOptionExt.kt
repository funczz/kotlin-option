package com.github.funczz.kotlin.option

import java.util.*

/**
 * 入れ子になっている RoOption を一段階アンラップする
 * @return <code>RoOption<RoOption<T>></code> を アンラップした <code>RoOption<T></code>
 */
fun <T : Any> RoOption<RoOption<T>>.flatten(): RoOption<T> = when (this) {
    is None<RoOption<T>> -> RoOption.none()
    is Some<RoOption<T>> -> value
}

/**
 * None なら関数 none を適用し、
 * Some なら関数 some を適用する。
 * @param none 引数を持たず、戻り値のない関数
 * @param some 引数として型 T を持ち、戻り値のない関数
 */
inline fun <T : Any> RoOption<T>.match(none: () -> Unit = {}, some: (T) -> Unit) = when (this) {
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
inline fun <T : Any, R : Any> RoOption<T>.fold(none: () -> R, some: (T) -> R): R = when (this) {
    is None<T> -> none()
    is Some<T> -> some(value)
}

/**
 * Optional を RoOption に変換する。
 * @return RoOption
 */
fun <T : Any> Optional<T>.toRoOption(): RoOption<T> = when {
    this.isEmpty -> RoOption.none()
    else -> RoOption.tee { get() }
}

/**
 * RoOption を RoOptional に変換する。
 * @return Optional
 */
fun <T : Any> RoOption<T>.toOptional(): Optional<T> {
    return fold(
        none = { Optional.empty() },
        some = { Optional.of(it) }
    )
}