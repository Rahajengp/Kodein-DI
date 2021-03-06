package org.kodein.di.erased

import org.kodein.di.Kodein
import org.kodein.di.Multi2
import org.kodein.di.erased
import org.kodein.di.erasedComp2
import org.kodein.di.test.FixMethodOrder
import org.kodein.di.test.MethodSorters
import kotlin.test.Test
import kotlin.test.assertEquals

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ErasedTests_21_Description {

    @Test
    fun test_00_SimpleKeySimpleDescription() {
        val key = Kodein.Key(
                contextType = erased<Any>(),
                argType = erased<Unit>(),
                type = erased<String>(),
                tag = null
        )

        assertEquals("bind<String>()", key.bindDescription)
        assertEquals("bind<String>() with ? { ? }", key.description)
    }

    @Test
    fun test_01_ComplexKeySimpleDescription() {
        val key = Kodein.Key(
                contextType = erased<String>(),
                argType = erasedComp2<Multi2<String, String>, String, String>(),
                type = erased<IntRange>(),
                tag = "tag"
        )

        assertEquals("bind<IntRange>(tag = \"tag\")", key.bindDescription)
        assertEquals("bind<IntRange>(tag = \"tag\") with ?<String>().? { Multi2<String, String> -> ? }", key.description)
    }

}
