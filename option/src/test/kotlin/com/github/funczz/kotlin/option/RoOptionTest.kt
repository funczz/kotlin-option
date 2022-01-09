package com.github.funczz.kotlin.option

import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class RoOptionTest : StringSpec(), ISerializableUtil {

    init {

        "RoOption.none" {
            val expected = true
            val actual = RoOption.none<String>()

            actual.isNone shouldBe expected
        }

        "RoOption.some" {
            val expected = true
            val actual = RoOption.some { "hello world." }

            actual.isSome shouldBe expected
        }

        "RoOption.tee -> None" {
            val expected = true
            val actual = RoOption.tee { null }

            actual.isNone shouldBe expected
        }

        "RoOption.tee -> Some" {
            val expected = true
            val actual = RoOption.tee { "hello world." }

            actual.isSome shouldBe expected
        }

        "flatten" {
            val expected = "hello world."
            val option = RoOption.some { RoOption.some { expected } }
            val actual = option.flatten()

            actual.getOrThrow() shouldBe expected
        }

    }

}