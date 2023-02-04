package com.bobbyesp.spowlo.ui.pages.settings.format

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AudioFile
import androidx.compose.material.icons.outlined.HighQuality
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.bobbyesp.spowlo.R
import com.bobbyesp.spowlo.ui.components.BackButton
import com.bobbyesp.spowlo.ui.components.LargeTopAppBar
import com.bobbyesp.spowlo.ui.components.PreferenceInfo
import com.bobbyesp.spowlo.ui.components.PreferenceItem
import com.bobbyesp.spowlo.ui.components.PreferenceSubtitle
import com.bobbyesp.spowlo.utils.AUDIO_FORMAT
import com.bobbyesp.spowlo.utils.AUDIO_QUALITY
import com.bobbyesp.spowlo.utils.CUSTOM_COMMAND
import com.bobbyesp.spowlo.utils.PreferencesUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsFormatsPage(onBackPressed: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )

    var audioFormat by remember { mutableStateOf(PreferencesUtil.getAudioFormatDesc()) }
    var audioQuality by remember { mutableStateOf(PreferencesUtil.getAudioQualityDesc()) }

    var showAudioFormatDialog by remember { mutableStateOf(false) }
    var showAudioQualityDialog by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.format),
                    )
                }, navigationIcon = {
                    BackButton {
                        onBackPressed()
                    }
                }, scrollBehavior = scrollBehavior
            )
        }, content = {
            val isCustomCommandEnabled by remember {
                mutableStateOf(
                    PreferencesUtil.getValue(CUSTOM_COMMAND)
                )
            }
            LazyColumn(Modifier.padding(it)) {
                if (isCustomCommandEnabled)
                    item {
                        PreferenceInfo(text = stringResource(id = R.string.custom_command_enabled_hint))
                    }
                item {
                    PreferenceSubtitle(text = stringResource(id = R.string.audio))
                }
                item {
                    PreferenceItem(
                        title = stringResource(R.string.audio_format),
                        description = audioFormat,
                        icon = Icons.Outlined.AudioFile,
                        enabled = !isCustomCommandEnabled
                    ) { showAudioFormatDialog = true }
                }
                item {
                    PreferenceItem(
                        title = stringResource(R.string.audio_quality),
                        description = audioQuality,
                        icon = Icons.Outlined.HighQuality,
                        enabled = !isCustomCommandEnabled
                    ) { showAudioQualityDialog = true }
                }
            }
        })
    if(showAudioFormatDialog){
        AudioFormatDialog(
            onDismissRequest = { showAudioFormatDialog = false }
        ) {
            audioFormat = PreferencesUtil.getAudioFormatDesc()
        }
    }
    if(showAudioQualityDialog){
        AudioQualityDialog(
            onDismissRequest = { showAudioQualityDialog = false }
        ) {
            audioQuality = PreferencesUtil.getAudioQualityDesc()
        }
    }
}