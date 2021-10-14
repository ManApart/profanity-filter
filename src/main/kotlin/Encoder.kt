import java.io.File
import java.util.*

private const val encode = false
private const val base = "./src/main/resources/profanity-base.txt"
private const val encoded = "./src/main/resources/profanity.txt"

fun main(){
    if (encode) encodeProfanity() else decodeProfanity()
}

private fun encodeProfanity(){
    val text = File(base).readText()
    val encodedString: String = Base64.getEncoder().encodeToString(text.toByteArray())
    File(encoded).writeText(encodedString)
}

private fun decodeProfanity(){
    val text = File(encoded).readText()
    val decodedString = String(Base64.getDecoder().decode(text))
    File(base).writeText(decodedString)
}