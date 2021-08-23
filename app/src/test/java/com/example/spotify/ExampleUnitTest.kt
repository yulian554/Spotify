package com.example.spotify

import com.example.spotify.data.model.Song
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ExampleUnitTest {
    lateinit var song: Song

    @Before
    fun setUp(){
        song = Song("1","title1","subtitle1","cancion1","image1",false)
    }
    @Test
    fun thisIsATest() {
        assertEquals(song.mediaId, "1")
    }
    @Test
    fun thisIsAnotherTest(){
        assertEquals(song.inLibrary, false)
    }
}