package com.rajesh.contentproviderkotlincompose.viewmodel

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class MediaViewModelTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun decoratorPattern() {
        val map = HappyMap<String, String>()
        val result = mapOf(
            map["A"] to "B",
            map["B"] to "C",
            map["A"] to "C",
        )
        map.remove("A")
        map["A"] to "C"
        assertEquals(mapOf("A" to "C", "B" to "C"), map.map)
        assertEquals(listOf("Yay! A", "Yay! B", "Yay! A"), (result))
    }
}

class HappyMap<K, V>(val map: MutableMap<K, V> = mutableMapOf()) : MutableMap<K, V> by map {
    override fun put(key: K, value: V): V? {
        return map.put(key, value).apply {
            if (this == null) {
                println("Yay Testing decorator : by")
            }
        }
    }

}