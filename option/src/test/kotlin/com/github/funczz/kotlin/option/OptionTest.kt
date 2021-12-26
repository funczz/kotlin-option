package com.github.funczz.kotlin.option

import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class OptionTest : StringSpec(), ISerializableUtil {

    init {

        "Option.none" {
            val expected = true
            val actual = Option.none<String>()

            actual.isNone shouldBe expected
        }

        "Option.some" {
            val expected = true
            val actual = Option.some { "hello world." }

            actual.isSome shouldBe expected
        }

        "Option.tee -> None" {
            val expected = true
            val actual = Option.tee { null }

            actual.isNone shouldBe expected
        }

        "Option.tee -> Some" {
            val expected = true
            val actual = Option.tee { "hello world." }

            actual.isSome shouldBe expected
        }

        "flatten" {
            val expected = "hello world."
            val option = Option.some { Option.some { expected } }
            val actual = option.flatten()

            actual.getOrThrow() shouldBe expected
        }

    }

}