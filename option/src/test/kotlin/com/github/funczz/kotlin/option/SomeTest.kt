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
            val origin = RoOption.some { "hello world." }
            val actual = origin.dump().load() as RoOption<*>

            actual.javaClass shouldBe origin.javaClass
            actual shouldNotBeSameInstanceAs origin
            actual.getOrThrow() shouldBe expected
        }

        "Some: toString" {
            RoOption.some { "hello world." }.toString() shouldBe "Some(hello world.)"
            RoOption.some { 0 }.toString() shouldBe "Some(0)"
        }

        "Some: hashCode" {
            RoOption.some { "hello world." }.hashCode() shouldBe RoOption.some { "hello world." }.hashCode()
            RoOption.some { "hello world." }.hashCode() shouldNotBe RoOption.some { "HELLO WORLD." }.hashCode()
            RoOption.some { "hello world." }.hashCode() shouldNotBe RoOption.some { 0 }.hashCode()
            RoOption.some { "hello world." }.hashCode() shouldNotBe "hello world.".hashCode()
        }

        "Some: equals" {
            RoOption.some { "hello world." }.equals(RoOption.some { "hello world." }) shouldBe true
            RoOption.some { "hello world." }.equals(RoOption.some { "HELLO WORLD." }) shouldBe false
            RoOption.some { "hello world." }.equals(RoOption.some { 0 }) shouldBe false
        }

        "Some: isNone" {
            RoOption.some { "hello world." }.isNone shouldBe false
        }

        "Some: isSome" {
            RoOption.some { "hello world." }.isSome shouldBe true
        }

        "Some: getOrElse" {
            val expected = "hello world."
            val option = RoOption.some { "hello world." }
            val actual = option.getOrElse { "HELLO WORLD." }

            actual shouldBe expected
        }

        "Some: getOrNull" {
            val expected = "hello world."
            val option = RoOption.some { "hello world." }
            val actual = option.getOrNull()

            actual shouldBe expected
        }

        "Some: getOrThrow" {
            val expected = "hello world."
            val option = RoOption.some { "hello world." }
            val actual = option.getOrThrow()

            actual shouldBe expected
        }

        "Some: Optional.toRoOption" {
            val expected = RoOption.some { "hello world." }
            val optional = Optional.of("hello world.")
            val actual = optional.toRoOption()

            actual shouldBe expected
        }

        "Some: toOptional" {
            val expected = Optional.of("hello world.")
            val option = RoOption.some { "hello world." }
            val actual = option.toOptional()

            actual shouldBe expected
        }

        "Some: match" {
            val expected = "hello world."
            val option = RoOption.some { "hello world." }
            var actual = ""
            option.match { actual = it }

            actual shouldBe expected
        }

        "Some: filter" {
            val expected = true
            val option = RoOption.some { "hello world." }
            val actual = option.filter { it == "hello world." }

            actual.isSome shouldBe expected
        }

        "Some -> None: filter" {
            val expected = false
            val option = RoOption.some { "hello world." }
            val actual = option.filter { it == "HELLO WORLD." }

            actual.isSome shouldBe expected
        }

        "Some: fold" {
            val expected = "hello world."
            val option = RoOption.some { expected }
            val actual = option.fold(
                none = { "None" },
                some = { it }
            )

            actual shouldBe expected
        }

        "Some: map" {
            val expected = "HELLO WORLD."
            val option = RoOption.some { "hello world." }
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
            val option = RoOption.some { "hello world." }
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
            val option = RoOption.some { "hello world." }
            var actual = ""
            option.andThen {
                RoOption.tee { it.uppercase() }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some -> None: andThen" {
            val expected = "None"
            val option = RoOption.some { "hello world." }
            var actual = ""
            option.andThen {
                RoOption.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "Some: orElse" {
            val expected = "hello world."
            val option = RoOption.some { expected }
            var actual = ""
            option.orElse {
                RoOption.tee { "orElse" }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some: andThenOrElse" {
            val expected = "hello world."
            val option = RoOption.some { 0 }
            var actual = ""
            option.andThenOrElse(
                fn = { RoOption.tee { (0..it).joinToString { expected } } },
                or = { RoOption.tee { "andThenOrElse" } }
            ).match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some, None -> Some: xor" {
            val expected = "hello world."
            val option = RoOption.some { "hello world." }
            var actual = ""
            option.xor {
                RoOption.tee { null }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some, Some -> None: xor" {
            val expected = "None"
            val option = RoOption.some { "hello world." }
            var actual = ""
            option.xor {
                RoOption.tee { "xor" }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "Some, None -> None: zip" {
            val expected = "None"
            val option = RoOption.some { "hello world." }
            var actual = ""
            option.zip {
                RoOption.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "Some, Some -> Some: zip" {
            val expected = "hello world. zip"
            val option = RoOption.some { "hello world." }
            var actual = ""
            option.zip {
                RoOption.tee { "zip" }
            }.match(
                none = {},
                some = { actual = "${it.first} ${it.second}" }
            )

            actual shouldBe expected
        }

    }

}