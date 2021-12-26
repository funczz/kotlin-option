package com.github.funczz.kotlin.option

import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import java.util.*

internal class NoneTest : StringSpec(), ISerializableUtil {

    init {

        "None: Serialization" {
            val expected = true
            val origin = Option.none<String>()
            val actual = origin.dump().load() as Option<*>

            actual.javaClass shouldBe origin.javaClass
            actual shouldNotBeSameInstanceAs origin
            actual.isNone shouldBe expected
        }

        "None: toString" {
            Option.none<String>().toString() shouldBe "None"
        }

        "None: hashCode" {
            Option.none<String>().hashCode() shouldBe Option.none<String>().hashCode()
            Option.none<String>().hashCode() shouldBe Option.none<Int>().hashCode()
            Option.none<String>().hashCode() shouldNotBe Option.some { "hello world." }.hashCode()
            Option.none<String>().hashCode() shouldNotBe "hello world.".hashCode()
        }

        "None: equals" {
            Option.none<String>().equals(Option.none<String>()) shouldBe true
            Option.none<String>().equals(Option.none<Int>()) shouldBe true
            Option.none<String>().equals(0) shouldBe false
        }

        "None: isNone" {
            Option.none<String>().isNone shouldBe true
        }

        "None: isSome" {
            Option.none<String>().isSome shouldBe false
        }

        "None: getOrElse" {
            val expected = "hello world."
            val option = Option.none<String>()
            val actual = option.getOrElse { "hello world." }

            actual shouldBe expected
        }

        "None: getOrNull" {
            val expected = null
            val option = Option.none<String>()
            val actual = option.getOrNull()

            actual shouldBe expected
        }

        "None: getOrThrow" {
            val expected = "None.getOrThrow"
            val option = Option.none<String>()
            val actual = shouldThrow<OptionException> {
                option.getOrThrow()
            }.message

            actual shouldBe expected
        }

        "None: Optional.toOption" {
            val expected = Option.none<String>()
            val optional = Optional.empty<String>()
            val actual = optional.toOption()

            actual shouldBe expected
        }

        "None: toOptional" {
            val expected = Optional.empty<String>()
            val option = Option.none<String>()
            val actual = option.toOptional()

            actual shouldBe expected
        }

        "None: fold" {
            val expected = "None"
            val option = Option.none<String>()
            val actual = option.fold(
                none = { expected },
                some = { it }
            )

            actual shouldBe expected
        }

        "None: match" {
            val expected = "None"
            val option = Option.none<String>()
            var actual = ""
            option.match({ actual = "None" }) {}

            actual shouldBe expected
        }

        "None: filter" {
            val expected = true
            val option = Option.none<String>()
            val actual = option.filter { it == "hello world." }

            actual.isNone shouldBe expected
        }

        "None: map" {
            val expected = "None"
            val option = Option.none<String>()
            var actual = ""
            option.map {
                it.uppercase()
            }.match(
                none = { actual = "None" },
                some = {}
            )

            actual shouldBe expected
        }

        "None: mapOrElse" {
            val expected = "mapOrElse"
            val option = Option.none<String>()
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

        "None: andThen" {
            val expected = "None"
            val option = Option.none<String>()
            var actual = ""
            option.andThen {
                Option.tee { it.uppercase() }
            }.match(
                none = { actual = "None" },
                some = {}
            )

            actual shouldBe expected
        }

        "None: orElse" {
            val expected = "orElse"
            val option = Option.none<String>()
            var actual = ""
            option.orElse {
                Option.tee { expected }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "None: andThenOrElse" {
            val expected = "andThenOrElse"
            val option = Option.none<Int>()
            var actual = ""
            option.andThenOrElse(
                fn = { Option.tee { "%s".format(it) } },
                or = { Option.tee { expected } }
            ).match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "None, None -> None: xor" {
            val expected = "xor"
            val option = Option.none<String>()
            var actual = ""
            option.xor {
                Option.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "None, Some -> Some: xor" {
            val expected = "xor"
            val option = Option.none<String>()
            var actual = ""
            option.xor {
                Option.tee { expected }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "None, None -> None: zip" {
            val expected = "zip"
            val option = Option.none<String>()
            var actual = ""
            option.zip {
                Option.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "None, Some -> None: zip" {
            val expected = "zip"
            val option = Option.none<String>()
            var actual = ""
            option.zip {
                Option.tee { expected }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

    }

}