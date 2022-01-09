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
            val origin = RoOption.none<String>()
            val actual = origin.dump().load() as RoOption<*>

            actual.javaClass shouldBe origin.javaClass
            actual shouldNotBeSameInstanceAs origin
            actual.isNone shouldBe expected
        }

        "None: toString" {
            RoOption.none<String>().toString() shouldBe "None"
        }

        "None: hashCode" {
            RoOption.none<String>().hashCode() shouldBe RoOption.none<String>().hashCode()
            RoOption.none<String>().hashCode() shouldBe RoOption.none<Int>().hashCode()
            RoOption.none<String>().hashCode() shouldNotBe RoOption.some { "hello world." }.hashCode()
            RoOption.none<String>().hashCode() shouldNotBe "hello world.".hashCode()
        }

        "None: equals" {
            RoOption.none<String>().equals(RoOption.none<String>()) shouldBe true
            RoOption.none<String>().equals(RoOption.none<Int>()) shouldBe true
            RoOption.none<String>().equals(0) shouldBe false
        }

        "None: isNone" {
            RoOption.none<String>().isNone shouldBe true
        }

        "None: isSome" {
            RoOption.none<String>().isSome shouldBe false
        }

        "None: getOrElse" {
            val expected = "hello world."
            val option = RoOption.none<String>()
            val actual = option.getOrElse { "hello world." }

            actual shouldBe expected
        }

        "None: getOrNull" {
            val expected = null
            val option = RoOption.none<String>()
            val actual = option.getOrNull()

            actual shouldBe expected
        }

        "None: getOrThrow" {
            val expected = "None.getOrThrow"
            val option = RoOption.none<String>()
            val actual = shouldThrow<RoOptionException> {
                option.getOrThrow()
            }.message

            actual shouldBe expected
        }

        "None: Optional.toRoOption" {
            val expected = RoOption.none<String>()
            val optional = Optional.empty<String>()
            val actual = optional.toRoOption()

            actual shouldBe expected
        }

        "None: toOptional" {
            val expected = Optional.empty<String>()
            val option = RoOption.none<String>()
            val actual = option.toOptional()

            actual shouldBe expected
        }

        "None: fold" {
            val expected = "None"
            val option = RoOption.none<String>()
            val actual = option.fold(
                none = { expected },
                some = { it }
            )

            actual shouldBe expected
        }

        "None: match" {
            val expected = "None"
            val option = RoOption.none<String>()
            var actual = ""
            option.match({ actual = "None" }) {}

            actual shouldBe expected
        }

        "None: filter" {
            val expected = true
            val option = RoOption.none<String>()
            val actual = option.filter { it == "hello world." }

            actual.isNone shouldBe expected
        }

        "None: map" {
            val expected = "None"
            val option = RoOption.none<String>()
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
            val option = RoOption.none<String>()
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
            val option = RoOption.none<String>()
            var actual = ""
            option.andThen {
                RoOption.tee { it.uppercase() }
            }.match(
                none = { actual = "None" },
                some = {}
            )

            actual shouldBe expected
        }

        "None: orElse" {
            val expected = "orElse"
            val option = RoOption.none<String>()
            var actual = ""
            option.orElse {
                RoOption.tee { expected }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "None: andThenOrElse" {
            val expected = "andThenOrElse"
            val option = RoOption.none<Int>()
            var actual = ""
            option.andThenOrElse(
                fn = { RoOption.tee { "%s".format(it) } },
                or = { RoOption.tee { expected } }
            ).match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "None, None -> None: xor" {
            val expected = "xor"
            val option = RoOption.none<String>()
            var actual = ""
            option.xor {
                RoOption.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "None, Some -> Some: xor" {
            val expected = "xor"
            val option = RoOption.none<String>()
            var actual = ""
            option.xor {
                RoOption.tee { expected }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "None, None -> None: zip" {
            val expected = "zip"
            val option = RoOption.none<String>()
            var actual = ""
            option.zip {
                RoOption.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "None, Some -> None: zip" {
            val expected = "zip"
            val option = RoOption.none<String>()
            var actual = ""
            option.zip {
                RoOption.tee { expected }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

    }

}