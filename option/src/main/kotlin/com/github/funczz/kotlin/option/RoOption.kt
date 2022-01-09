package com.github.funczz.kotlin.option

import java.io.Serializable
import java.util.*

/**
 * RoOption シールドクラス:
 * 実装クラスに None と Some を持つ。
 */
sealed class RoOption<T : Any> : Serializable {

    /**
     * None かどうかを返す。
     * @return None なら true、
     *         Some なら false。
     */
    val isNone: Boolean
        get() = this is None

    /**
     * Some かどうかを返す。
     * @return Some なら true、
     *         None なら false。
     */
    val isSome: Boolean
        get() = this is Some

    /**
     * Some の値を返す。
     * @param fn 型 T を返す関数
     * @return Some なら Some 値、
     *         None なら 関数 fn の戻り値。
     */
    abstract fun getOrElse(fn: () -> T): T

    /**
     * Some の値を返す。
     * @return Some なら Some の 値、
     *         None なら null 。
     */
    abstract fun getOrNull(): T?

    /**
     * Some の値を返す。
     * @return Some なら Some の 値、
     *         None なら <code>RoOptionException</code> をスロー。
     * @throws RoOptionException
     */
    abstract fun getOrThrow(): T

    /**
     * Some かつ関数 fn の戻り値が true なら Some を返し、
     * それ以外は None を返す。
     * @param fn 引数に 値 T を持ち、型 Boolean を返す関数
     * @return RoOption
     */
    abstract fun filter(fn: (T) -> Boolean): RoOption<T>

    /**
     * Some なら関数 fn の戻り値を持つ Some を返す
     * @param fn 引数に 値 T を持ち、型 U を返す関数
     * @return RoOption
     */
    abstract fun <U : Any> map(fn: (T) -> U): RoOption<U>

    /**
     * Some なら関数 fn の戻り値を持つ Some を返し、
     * None なら関数 or の戻り値を持つ Some を返す。
     * @param fn 引数に 値 T を持ち、型 U を返す関数
     * @param or 型 U を返す関数
     * @return RoOption
     */
    abstract fun <U : Any> mapOrElse(fn: (T) -> U, or: () -> U): RoOption<U>

    /**
     * Some なら関数 fn の戻り値を返す。
     * Some から None の変換が可能。
     * @param fn 引数に 値 T を持ち、RoOption を返す関数
     * @return RoOption
     */
    abstract fun <U : Any> andThen(fn: (T) -> RoOption<U>): RoOption<U>

    /**
     * None なら関数 fn の戻り値を返す。
     * None から Some の変換が可能。
     * @param fn RoOption を返す関数
     * @return RoOption
     */
    abstract fun orElse(fn: () -> RoOption<T>): RoOption<T>

    /**
     * Some なら関数 fn の戻り値を返し、
     * None なら関数 or の戻り値を返す。
     * Some から None と None から Some の変換が可能。
     * @param fn 引数に 値 T を持ち、RoOption を返す関数
     * @param or RoOption を返す関数
     * @return RoOption
     */
    abstract fun <U : Any> andThenOrElse(fn: (T) -> RoOption<U>, or: () -> RoOption<U>): RoOption<U>

    /**
     * 自身と関数 fn の戻り値が、
     * Some と None またはその逆なら Some を返し、
     * Some 同士や None 同士なら None を返す。
     * @param fn RoOption を返す関数
     * @return RoOption
     */
    abstract fun xor(fn: () -> RoOption<T>): RoOption<T>

    /**
     * 自身と関数 fn の戻り値がSome 同士なら、
     * それらの値を持つ Pair を値とする Some 返す。
     * @param fn RoOption を返す関数
     * @return RoOption
     */
    abstract fun <U : Any> zip(fn: () -> RoOption<U>): RoOption<Pair<T, U>>

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    abstract override fun toString(): String

    companion object {

        /**
         * None を返す。
         * @return RoOption
         */
        @JvmStatic
        fun <T : Any> none(): RoOption<T> = None()

        /**
         * 関数 fn の戻り値を値とする Some を返す。
         * @param fn 型 T を返す関数
         * @return RoOption
         */
        @JvmStatic
        fun <T : Any> some(fn: () -> T): RoOption<T> = Some(value = fn())

        /**
         * 関数 fn の戻り値が null なら None を返し、
         * それ以外は戻り値を値とする Some を返す。
         * @param fn 型 T? を返す関数
         * @return RoOption
         */
        @JvmStatic
        fun <T : Any> tee(fn: () -> T?): RoOption<T> = when (val result = fn()) {
            null -> none()
            else -> some { result }
        }

    }

}

/**
 * None クラス: 値を持たない RoOption クラス
 */
class None<T : Any> : RoOption<T>() {

    override fun getOrElse(fn: () -> T): T = fn()

    override fun getOrNull(): T? = null

    override fun getOrThrow(): T = throw RoOptionException(message = "None.getOrThrow")

    override fun filter(fn: (T) -> Boolean): RoOption<T> = this

    override fun <U : Any> map(fn: (T) -> U): RoOption<U> = none()

    override fun <U : Any> mapOrElse(fn: (T) -> U, or: () -> U): RoOption<U> = some { or() }

    override fun <U : Any> andThen(fn: (T) -> RoOption<U>): RoOption<U> = none()

    override fun orElse(fn: () -> RoOption<T>): RoOption<T> = fn()

    override fun <U : Any> andThenOrElse(fn: (T) -> RoOption<U>, or: () -> RoOption<U>) = or()

    override fun xor(fn: () -> RoOption<T>): RoOption<T> = when (val result = fn()) {
        is None<T> -> none()
        is Some<T> -> result
    }

    override fun <U : Any> zip(fn: () -> RoOption<U>): RoOption<Pair<T, U>> = none()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is None<*> -> true
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(javaClass)
    }

    override fun toString(): String {
        return String.format("None")
    }

    companion object {
        private const val SerialVersionUID: Long = 1L
    }

}

/**
 * Some クラス: 値を持つ RoOption クラス
 */
class Some<T : Any>(

    /**
     * Some クラス が保持する値
     */
    val value: T

) : RoOption<T>() {

    override fun getOrElse(fn: () -> T): T = value

    override fun getOrNull(): T = value

    override fun getOrThrow(): T = value

    override fun filter(fn: (T) -> Boolean): RoOption<T> = when (fn(value)) {
        true -> this
        else -> none()
    }

    override fun <U : Any> map(fn: (T) -> U): RoOption<U> = some { fn(value) }

    override fun <U : Any> mapOrElse(fn: (T) -> U, or: () -> U): RoOption<U> = some { fn(value) }

    override fun <U : Any> andThen(fn: (T) -> RoOption<U>): RoOption<U> = fn(value)

    override fun orElse(fn: () -> RoOption<T>): RoOption<T> = this

    override fun <U : Any> andThenOrElse(fn: (T) -> RoOption<U>, or: () -> RoOption<U>): RoOption<U> = fn(value)

    override fun xor(fn: () -> RoOption<T>): RoOption<T> = when (fn()) {
        is None<T> -> this
        is Some<T> -> none()
    }

    override fun <U : Any> zip(fn: () -> RoOption<U>): RoOption<Pair<T, U>> = when (val result = fn()) {
        is None<U> -> none()
        is Some<U> -> some { Pair(value, result.value) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is Some<*> -> value == other.value
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(javaClass, value)
    }

    override fun toString(): String {
        return String.format("Some(%s)", this.value)
    }

    companion object {
        private const val SerialVersionUID: Long = 1L
    }

}
