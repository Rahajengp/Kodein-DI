package org.kodein.di.erased

import org.kodein.di.*
import org.kodein.di.test.*
import kotlin.test.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ErasedTests_18_MultiBindings {

    @Test
    fun test_00_MultiSet() {
        val kodein = Kodein {
            bind() from setBinding<IPerson>()

            bind<IPerson>().inSet() with singleton { Person("Salomon") }
            bind<IPerson>().inSet() with provider { Person("Laila") }

            Bind<List<IPerson>>(erasedList()) with provider { Instance<Set<IPerson>>(erasedSet(), null).toList() }
        }

        val persons1: Set<IPerson> by kodein.Instance(erasedSet())

        assertTrue(Person("Salomon") in persons1)
        assertTrue(Person("Laila") in persons1)

        val persons2: Set<IPerson> by kodein.Instance(erasedSet())

        val salomon1 = persons1.first { it.name == "Salomon" }
        val salomon2 = persons2.first { it.name == "Salomon" }

        val laila1 = persons1.first { it.name == "Laila" }
        val laila2 = persons2.first { it.name == "Laila" }

        assertSame(salomon1, salomon2)
        assertNotSame(laila1, laila2)

        val list: List<IPerson> by kodein.Instance(erasedList())
        assertEquals(persons1.toList(), list)
    }

    @Test
    fun test_01_MultiMap() {
        val kodein = Kodein {
            bind() from setBinding<PersonEntry>()

            bind<PersonEntry>().inSet() with singleton { "so" to Person("Salomon") }
            bind<PersonEntry>().inSet() with provider { "loulou" to Person("Laila") }

            Bind<Map<String, Person>>(erasedMap()) with provider { Instance<PersonEntries>(erasedSet(), null).toMap() }
        }

        val persons: Map<String, Person> = kodein.direct.Instance(erasedMap(), null)

        assertEquals(Person("Salomon"), persons["so"])
        assertEquals(Person("Laila"), persons["loulou"])
    }

}
