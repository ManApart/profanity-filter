import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.siegmann.epublib.epub.EpubReader
import nl.siegmann.epublib.epub.EpubWriter
import java.io.File

private const val inputPath = "./src/main/resources/"

fun main() {
    val swears = File(swearsFile).readText().parseSwears()
    val sourceFile = File(inputPath)
    val root = if (sourceFile.isDirectory) sourceFile.path else sourceFile.parentFile.path
    runBlocking {
        launch {
            cleanFile(this, sourceFile, root, swears)
        }
    }
}

private suspend fun cleanFile(scope: CoroutineScope, file: File, root: String, swears: List<Regex>) {
    when {
        file.path.endsWith(".epub") -> cleanBook(file, root, swears)
        file.path.endsWith(".txt") -> cleanTextFile(file, root, swears)
        file.isDirectory -> cleanDirectory(scope, file, root, swears)
        else -> println("Unable to clean file ${file.path}")
    }
}

private suspend fun cleanDirectory(scope: CoroutineScope, file: File, root: String, swears: List<Regex>) {
    if (file.path == "$root\\out") return
    file.listFiles()!!.forEach { file ->
        scope.async {
            cleanFile(scope, file, root, swears)
        }
    }
}

fun String.parseSwears(): List<Regex> {
    return this.decode()
        .split("\n")
        .flatMap { listOf(it, "${it}ed", "${it}s", "${it}ing") }
        .flatMap { buildRegex(it) }
}

private fun buildRegex(word: String): List<Regex> {
    return listOf(
        /*
        Optionally start with > but don't capture it
        Starts with none to one new lines or spaces
        Has the word
        Ends with a space, newline, period or <
         */
        Regex("(?<=>)?[ \n]?(?i)$word(?=[^a-zA-Z])"),
        /*
        Same as above but match end of string instead.
         */
        Regex("[ \n>](?i)$word$")
    )
}

private fun cleanBook(file: File, root: String, swears: List<Regex>) {
    println("Cleaning ${file.path}")
    val book = EpubReader().readEpub(file.inputStream())

    book.contents.forEach { chapter ->
        chapter.data = chapter.reader.readText().clean(swears).toByteArray()
    }

    EpubWriter().write(book, createOutFile(file, root).outputStream())
}

private fun cleanTextFile(file: File, root: String, swears: List<Regex>) {
    println("Cleaning ${file.path}")
    val text = file.readText()
    val cleaned = text.clean(swears)
    createOutFile(file, root).writeText(cleaned)
}

private fun createOutFile(file: File, root: String): File {
    val relativePath = file.path.substring(root.length, file.path.length)
    val newPath = "$root/out/$relativePath"
    return File(newPath).also { it.parentFile.mkdirs() }
}

fun String.clean(swears: List<Regex>): String {
    var newText = this
    swears.forEach { swear ->
        newText = newText.replace(swear, "")
    }
    return newText
}
