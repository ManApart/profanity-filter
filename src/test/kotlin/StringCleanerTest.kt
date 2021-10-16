import kotlin.test.Test
import kotlin.test.assertEquals

class StringCleanerTest {

    @Test
    fun basic(){
        val swears = swears("love")
        val dirty = "a story of love "
        val expected = "a story of "
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun wordPart(){
        val swears = swears("love")
        val dirty = "lovestruck"
        val expected = "lovestruck"
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun wordPartEnd(){
        val swears = swears("love")
        val dirty = "dumblove"
        val expected = "dumblove"
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun endNewline(){
        val swears = swears("love")
        val dirty = "a story of love\n"
        val expected = "a story of\n"
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun endPeriod(){
        val swears = swears("love")
        val dirty = "a story of love."
        val expected = "a story of."
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun endHTML(){
        val swears = swears("love")
        val dirty = "a story of love<"
        val expected = "a story of<"
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun startString(){
        val swears = swears("love")
        val dirty = "love stars"
        val expected = " stars"
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun endString(){
        val swears = swears("love")
        val dirty = "starry love"
        val expected = "starry"
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun startNewline(){
        val swears = swears("love")
        val dirty = "a story of love\n a story of hate"
        val expected = "a story of\n a story of hate"
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun startHTML(){
        val swears = swears("love")
        val dirty = ">love in the moonlight"
        val expected = "> in the moonlight"
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun capitalization(){
        val swears = swears("love")
        val dirty = "a lOve story"
        val expected = "a story"
        assertEquals(expected, dirty.clean(swears))
    }

    @Test
    fun variants(){
        val swears = swears("love")
        val dirty = "a loveed loves loveing story"
        val expected = "a story"
        assertEquals(expected, dirty.clean(swears))
    }


    private fun swears(vararg swear: String): List<Regex>{
        return swear.joinToString("\n").encode().parseSwears()
    }
}