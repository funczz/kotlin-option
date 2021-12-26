package com.github.funczz.kotlin.option

import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import java.util.*

internal class SomeTest : StringSpec(), ISerializableUtil {

    init {

        "Some: Serialization" {
            val expected = "hello world."
            val origin = Option.some { "hello world." }
            val actual = origin.dump().load() as Option<*>

            actual.javaClass shouldBe origin.javaClass
            actual shouldNotBeSameInstanceAs origin
            actual.getOrThrow() shouldBe expected
        }

        "Some: toString" {
            Option.some { "hello world." }.toString() shouldBe "Some(hello world.)"
            Option.some { 0 }.toString() shouldBe "Some(0)"
        }

        "Some: hashCode" {
            Option.some { "hello world." }.hashCode() shouldBe Option.some { "hello world." }.hashCode()
            Option.some { "hello world." }.hashCode() shouldNotBe Option.some { "HELLO WORLD." }.hashCode()
            Option.some { "hello world." }.hashCode() shouldNotBe Option.some { 0 }.hashCode()
            Option.some { "hello world." }.hashCode() shouldNotBe "hello world.".hashCode()
        }

        "Some: equals" {
            Option.some { "hello world." }.equals(Option.some { "hello world." }) shouldBe true
            Option.some { "hello world." }.equals(Option.some { "HELLO WORLD." }) shouldBe false
            Option.some { "hello world." }.equals(Option.some { 0 }) shouldBe false
        }

        "Some: isNone" {
            Option.some { "hello world." }.isNone shouldBe false
        }

        "Some: isSome" {
            Option.some { "hello world." }.isSome shouldBe true
        }

        "Some: getOrElse" {
            val expected = "hello world."
            val option = Option.some { "hello world." }
            val actual = option.getOrElse { "HELLO WORLD." }

            actual shouldBe expected
        }

        "Some: getOrNull" {
            val expected = "hello world."
            val option = Option.some { "hello world." }
            val actual = option.getOrNull()

            actual shouldBe expected
        }

        "Some: getOrThrow" {
            val expected = "hello world."
            val option = Option.some { "hello world." }
            val actual = option.getOrThrow()

            actual shouldBe expected
        }

        "Some: Optional.toOption" {
            val expected = Option.some { "hello world." }
            val optional = Optional.of("hello world.")
            val actual = optional.toOption()

            actual shouldBe expected
        }

        "Some: toOptional" {
            val expected = Optional.of("hello world.")
            val option = Option.some { "hello world." }
            val actual = option.toOptional()

            actual shouldBe expected
        }

        "Some: match" {
            val expected = "hello world."
            val option = Option.some { "hello world." }
            var actual = ""
            option.match { actual = it }

            actual shouldBe expected
        }

        "Some: filter" {
            val expected = true
            val option = Option.some { "hello world." }
            val actual = option.filter { it == "hello world." }

            actual.isSome shouldBe expected
        }

        "Some -> None: filter" {
            val expected = false
            val option = Option.some { "hello world." }
            val actual = option.filter { it == "HELLO WORLD." }

            actual.isSome shouldBe expected
        }

        "Some: fold" {
            val expected = "hello world."
            val option = Option.some { expected }
            val actual = option.fold(
                none = { "None" },
                some = { it }
            )

            actual shouldBe expected
        }

        "Some: map" {
            val expected = "HELLO WORLD."
            val option = Option.some { "hello world." }
            var actual = ""
            option.map {
                it.uppercase()
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some: mapOrElse" {
            val expected = "HELLO WORLD."
            val option = Option.some { "hello world." }
            var actual = ""
            option.mapOrElse(
                fn = { it.uppercase() },
                or = { "mapOrElse" }
            ).match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some: andThen" {
            val expected = "HELLO WORLD."
            val option = Option.some { "hello world." }
            var actual = ""
            option.andThen {
                Option.tee { it.uppercase() }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some -> None: andThen" {
            val expected = "None"
            val option = Option.some { "hello world." }
            var actual = ""
            option.andThen {
                Option.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "Some: orElse" {
            val expected = "hello world."
            val option = Option.some { expected }
            var actual = ""
            option.orElse {
                Option.tee { "orElse" }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some: andThenOrElse" {
            val expected = "hello world."
            val option = Option.some { 0 }
            var actual = ""
            option.andThenOrElse(
                fn = { Option.tee { (0..it).joinToString { expected } } },
                or = { Option.tee { "andThenOrElse" } }
            ).match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some, None -> Some: xor" {
            val expected = "hello world."
            val option = Option.some { "hello world." }
            var actual = ""
            option.xor {
                Option.tee { null }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some, Some -> None: xor" {
            val expected = "None"
            val option = Option.some { "hello world." }
            var actual = ""
            option.xor {
                Option.tee { "xor" }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "Some, None -> None: zip" {
            val expected = "None"
            val option = Option.some { "hello world." }
            var actual = ""
            option.zip {
                Option.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "Some, Some -> Some: zip" {
            val expected = "hello world. zip"
            val option = Option.some { "hello world." }
            var actual = ""
            option.zip {
                Option.tee { "zip" }
            }.match(
                none = {},
                some = { actual = "${it.first} ${it.second}" }
            )

            actual shouldBe expected
        }

    }

}