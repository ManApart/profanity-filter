import kotlin.test.Test
import kotlin.test.assertEquals

class StringCleanerTest {

    @Test
    fun doThing(){
        val swears = listOf(Regex("love"))
        val dirty = "a story of love"
        val expected = "a story of  "
        assertEquals(expected, dirty.clean(swears))
    }

}