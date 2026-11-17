package com.example.expensemanager.feature.voice.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensemanager.R

@Composable
fun VoiceRecognitionScreen(
    modifier: Modifier = Modifier,
    viewModel: VoiceRecognitionViewModel
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.startListening() else viewModel.onPermissionDenied()
    }

    // Stop the mic when leaving the screen.
    DisposableEffect(Unit) { onDispose { viewModel.stopListening() } }

    val onMicClick = {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (state.isListening || granted) viewModel.toggleListening()
        else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.voice_title),
            style = MaterialTheme.typography.headlineLarge
        )

        LanguagePicker(
            selected = state.language,
            onSelected = viewModel::selectLanguage
        )

        IconButton(
            onClick = onMicClick,
            modifier = Modifier.size(96.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = if (state.isListening) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                painter = painterResource(if (state.isListening) R.drawable.ic_stop else R.drawable.ic_mic),
                contentDescription = stringResource(
                    if (state.isListening) R.string.voice_stop_listening
                    else R.string.voice_start_listening
                ),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(48.dp)
            )
        }

        Text(
            text = stringResource(
                if (state.isListening) R.string.voice_listening else R.string.voice_tap_to_start
            ),
            style = MaterialTheme.typography.bodyMedium
        )

        if (state.isListening) {
            // SpeechRecognizer reports a relative level (about -2 to 10 dB), not calibrated SPL.
            LinearProgressIndicator(
                progress = { ((state.levelDb + 2f) / 12f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.voice_level, state.levelDb),
                style = MaterialTheme.typography.bodySmall
            )
        }

        state.error?.let {
            Text(
                text = stringResource(
                    when (it) {
                        VoiceError.NOT_AVAILABLE -> R.string.voice_not_available
                        VoiceError.NO_SPEECH -> R.string.voice_no_speech
                        VoiceError.PERMISSION_DENIED -> R.string.voice_permission_denied
                        VoiceError.RECOGNITION_FAILED -> R.string.voice_error
                    }
                ),
                color = MaterialTheme.colorScheme.error
            )
        }

        Text(
            text = state.text.ifEmpty { stringResource(R.string.voice_placeholder) },
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = if (state.text.isEmpty()) MaterialTheme.colorScheme.outline
            else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun LanguagePicker(
    selected: VoiceLanguage,
    onSelected: (VoiceLanguage) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(onClick = { expanded = true }) {
            Text("${stringResource(R.string.voice_language)}: ${selected.displayName}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            VoiceLanguage.entries.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.displayName) },
                    onClick = {
                        expanded = false
                        onSelected(language)
                    }
                )
            }
        }
    }
}
