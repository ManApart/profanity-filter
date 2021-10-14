import java.io.File
import java.util.*

//TODO pass in string, file or files
//TODO - use coroutines for per file replacements (or chunk text)

private const val inputFile = "./src/main/resources/test.txt"


fun main(){
    val swears = File(swearsFile).readText().parseSwears()
    cleanFile(inputFile, swears)
}

fun String.parseSwears(): List<Regex>{
    return this.decode()
        .split("\n")
        .flatMap { listOf(it, "${it}ed", "${it}s", "${it}ing") }
            //case insensitive
        .map { Regex("(?i) $it ") }
}

fun cleanFile(path: String, swears: List<Regex>) {
    val text = File(path).readText()
    val cleaned = text.clean(swears)
    File("./out/$path").also { it.parentFile.mkdirs() }.writeText(cleaned)
}

fun String.clean(swears: List<Regex>): String {
    var newText = this
    swears.forEach { swear ->
        newText = newText.replace(swear, " ")
    }
    return newText
}
