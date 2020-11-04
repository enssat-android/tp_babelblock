package fr.enssat.babelblock.tools

import android.content.Context
import fr.enssat.babelblock.tools.impl.TextToSpeechHandler
import fr.enssat.babelblock.tools.impl.TranslatorHandler
import java.util.Locale

interface TextToSpeechTool {
    fun speak(text: String)
    fun stop()
    fun close()
}

interface TranslationTool {
    fun translate(text: String, callback: (String) -> Unit)
    fun close()
}

class BlockService(val context: Context) {

    fun textToSpeech():TextToSpeechTool {
        val locale = Locale.getDefault()
        return TextToSpeechHandler(context.applicationContext, locale)
    }

    fun translator(from: Locale, to: Locale): TranslationTool =
        TranslatorHandler(context.applicationContext, from, to)

}

