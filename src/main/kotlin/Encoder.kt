import java.io.File
import java.util.*

private const val encode = true
private const val base = "./src/main/resources/profanity-base.txt"
const val swearsFile = "./src/main/resources/profanity.txt"

fun main(){
    if (encode) encodeProfanity() else decodeProfanity()
}

private fun encodeProfanity(){
    val text = File(base).readText()
    File(swearsFile).writeText(text.encode())
}

private fun decodeProfanity(){
    val text = File(swearsFile).readText()
    File(base).writeText(text.decode())
}

fun String.encode(): String {
    return Base64.getEncoder().encodeToString(this.toByteArray())
}

fun String.decode(): String {
    return String(Base64.getDecoder().decode(this))
}