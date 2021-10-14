import java.io.File
import java.util.*

//TODO pass in string, file or files
//TODO - use coroutines for per file replacements (or chunk text)

private const val inputFile = "./src/main/resources/test.txt"

private val swears = parseSwears()

fun main(){
    cleanFile(inputFile)
}

private fun parseSwears(): List<Regex>{
    val text = File(swearsFile).readText()
    return String(Base64.getDecoder().decode(text))
        .split("\n")
        .flatMap { listOf(it, "${it}ed", "${it}s", "${it}ing") }
            //case insensitive
        .map { Regex("(?i) $it ") }
}

fun cleanFile(path: String) {
    val text = File(path).readText()
    val cleaned = cleanText(text)
    File("./out/$path").also { it.parentFile.mkdirs() }.writeText(cleaned)
}

fun cleanText(text: String): String {
    var newText = text
    swears.forEach { swear ->
        newText = newText.replace(swear, " ")
    }
    return newText
}
