package org.kodein.di.generic

import org.kodein.di.Kodein
import org.kodein.di.findAllBindings
import org.kodein.di.test.FixMethodOrder
import org.kodein.di.test.MethodSorters
import kotlin.test.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GenericJvmTests_22_Search {

    @Test
    fun test_00_SearchTagged() {
        val kodein = Kodein {
            bind<String>(tag = "foo") with provider { "String-foo" }
            bind<String>(tag = "bar") with provider { "String-bar" }
            bind<Int>(tag = "foo") with provider { 42 }
            bind<Int>(tag = "bar") with provider { 21 }
        }

        val bindings = kodein.container.tree.findAllBindings {
            +tag("foo")
        }

        assertEquals(2, bindings.size)

        val values = bindings.map { (key, _) ->
            @Suppress("UNCHECKED_CAST")
            kodein.container.factory(key as Kodein.Key<Any?, Any?, Any>, null).invoke(Unit)
        }

        assertTrue("String-foo" in values)
        assertTrue(42 in values)
    }

    @Test
    fun test_01_SearchArgument() {
        val kodein = Kodein {
            bind<String>() with provider { "String-foo" }
            bind<String>() with factory { name: String -> "String-$name" }
            bind<Int>() with provider { 42 }
            bind<Int>() with factory { i: Int -> 21 + i }
        }

        val bindings = kodein.container.tree.findAllBindings {
            +argument<Unit>()
        }

        assertEquals(2, bindings.size)

        val values = bindings.map { (key, _) ->
            @Suppress("UNCHECKED_CAST")
            kodein.container.factory(key as Kodein.Key<Any?, Unit, Any>, null).invoke(Unit)
        }

        assertTrue("String-foo" in values)
        assertTrue(42 in values)
    }

    @Test
    fun test_02_SearchContext() {
        val kodein = Kodein {
            bind<String>() with provider { "String-foo" }
            bind<String>() with contexted<String>().provider { "String-$context" }
            bind<Int>() with provider { 42 }
            bind<Int>() with contexted<String>().provider { 21 + context.length }
        }

        val bindings = kodein.container.tree.findAllBindings {
            +context<Any?>()
        }

        assertEquals(2, bindings.size)

        val values = bindings.map { (key, _) ->
            @Suppress("UNCHECKED_CAST")
            kodein.container.factory(key as Kodein.Key<Any?, Any?, Any>, null).invoke(Unit)
        }

        assertTrue("String-foo" in values)
        assertTrue(42 in values)
    }

}
