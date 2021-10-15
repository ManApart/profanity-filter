import nl.siegmann.epublib.epub.EpubReader
import nl.siegmann.epublib.epub.EpubWriter
import java.io.File

//TODO - use coroutines for per file replacements (or chunk text)

//private const val inputFile = "./src/main/resources/test.txt"
private const val inputFile = "./src/main/resources/sample.epub"


fun main() {
    val swears = File(swearsFile).readText().parseSwears()
    val sourceFile = File(inputFile)
    val outFile = File("./out/${sourceFile.path}").also { it.parentFile.mkdirs() }
    cleanFile(sourceFile, outFile, swears)
}

private fun cleanFile(file: File, outFile: File, swears: List<Regex>) {
    when {
        file.path.endsWith(".epub") -> cleanBook(file, outFile, swears)
        file.path.endsWith(".txt") -> cleanTextFile(file, outFile, swears)
        file.isDirectory -> file.listFiles()!!.forEach { cleanFile(it, outFile, swears) }
        else -> println("Unable to clean file ${file.path}")
    }
}

fun String.parseSwears(): List<Regex> {
    return this.decode()
        .split("\n")
        .flatMap { listOf(it, "${it}ed", "${it}s", "${it}ing") }
        .map { buildRegex(it) }
}

private fun buildRegex(word: String): Regex {
    /*
    Starts with none to one new lines or spaces
    Has the word
    Ends with a space, newline, or period
     */
    return Regex("[ \n]?(?i)$word(?=[. \n])")
}

private fun cleanBook(file: File, outFile: File, swears: List<Regex>) {
    val book = EpubReader().readEpub(file.inputStream())

    book.contents.forEach { chapter ->
        chapter.data = chapter.reader.readText().clean(swears).toByteArray()
    }

    EpubWriter().write(book, outFile.outputStream())
}

private fun cleanTextFile(file: File, outFile: File, swears: List<Regex>) {
    val text = file.readText()
    val cleaned = text.clean(swears)
    outFile.writeText(cleaned)
}

fun String.clean(swears: List<Regex>): String {
    var newText = this
    swears.forEach { swear ->
        newText = newText.replace(swear, "")
    }
    return newText
}
