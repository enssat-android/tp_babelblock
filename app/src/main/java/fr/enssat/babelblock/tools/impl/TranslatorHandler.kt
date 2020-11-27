package fr.enssat.babelblock.tools.impl

import android.content.Context
import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import fr.enssat.babelblock.tools.TranslationTool
import java.util.Locale

class TranslatorHandler(context: Context, from: Locale, to: Locale): TranslationTool {

    private val options = TranslatorOptions.Builder()
                                .setSourceLanguage(from.language)
                                .setTargetLanguage(to.language)
                                .build()

    private val translator = Translation.getClient(options)

    private val conditions = DownloadConditions.Builder()
                                .requireWifi()
                                .build()

    private val modelManager = RemoteModelManager.getInstance()

    private val model =  TranslateRemoteModel.Builder(from.toString()).build()

    init {  translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener { Log.d("Translation", "download completed") }
                .addOnFailureListener {
                    e -> Log.e("Translation", "Download failed ", e)
                    //remove any previous model downloaded
                    modelManager.deleteDownloadedModel(model)
                    //retry
                    modelManager.download(model,conditions)
                }
    }

    override fun translate(text: String, callback: (String) -> Unit) {
        translator.translate(text)
            .addOnSuccessListener(callback)
            .addOnFailureListener { e -> Log.e("Translation", "Translation falied", e) }
    }

    override fun close() {
        translator.close()
    }
}