package com.example.expensemanager.feature.voice.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class VoiceError { NOT_AVAILABLE, NO_SPEECH, PERMISSION_DENIED, RECOGNITION_FAILED }

enum class VoiceLanguage(val tag: String, val displayName: String) {
    ENGLISH("en-US", "English"),
    FRENCH("fr-FR", "Français"),
    JAPANESE("ja-JP", "日本語"),
    RUSSIAN("ru-RU", "Русский")
}

data class VoiceUiState(
    val language: VoiceLanguage = VoiceLanguage.ENGLISH,
    val isListening: Boolean = false,
    val text: String = "",
    val levelDb: Float = 0f,
    val error: VoiceError? = null
)

@HiltViewModel
class VoiceRecognitionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private var recognizer: SpeechRecognizer? = null

    private val listener = object : RecognitionListener {
        override fun onPartialResults(partialResults: Bundle?) {
            firstResult(partialResults)?.let { text -> _uiState.update { it.copy(text = text) } }
        }

        override fun onResults(results: Bundle?) {
            _uiState.update {
                it.copy(isListening = false, levelDb = 0f, text = firstResult(results) ?: it.text)
            }
        }

        override fun onError(error: Int) {
            Log.w("VoiceRecognition", "Recognition error code: $error")
            val voiceError = when (error) {
                SpeechRecognizer.ERROR_CLIENT -> null // our own stop/cancel
                SpeechRecognizer.ERROR_NO_MATCH,
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> VoiceError.NO_SPEECH

                else -> VoiceError.RECOGNITION_FAILED
            }
            _uiState.update { it.copy(isListening = false, levelDb = 0f, error = voiceError) }
        }

        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {
            _uiState.update { it.copy(levelDb = rmsdB) }
        }

        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    private fun firstResult(bundle: Bundle?): String? =
        bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
            ?.takeIf { it.isNotBlank() }

    fun toggleListening() {
        if (_uiState.value.isListening) stopListening() else startListening()
    }

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _uiState.update { it.copy(error = VoiceError.NOT_AVAILABLE) }
            return
        }
        val speechRecognizer = recognizer ?: SpeechRecognizer.createSpeechRecognizer(context)
            .also {
                it.setRecognitionListener(listener)
                recognizer = it
            }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, _uiState.value.language.tag)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, _uiState.value.language.tag)
        }
        _uiState.update { VoiceUiState(language = it.language, isListening = true) }
        speechRecognizer.startListening(intent)
    }

    fun selectLanguage(language: VoiceLanguage) {
        if (_uiState.value.isListening) {
            recognizer?.cancel()
        }
        _uiState.update { it.copy(language = language, isListening = false, levelDb = 0f, error = null) }
    }

    fun stopListening() {
        // Results are delivered via onResults after stopListening.
        recognizer?.stopListening()
        _uiState.update { it.copy(isListening = false, levelDb = 0f) }
    }

    fun onPermissionDenied() {
        _uiState.update { it.copy(isListening = false, error = VoiceError.PERMISSION_DENIED) }
    }

    override fun onCleared() {
        recognizer?.destroy()
        recognizer = null
    }
}
