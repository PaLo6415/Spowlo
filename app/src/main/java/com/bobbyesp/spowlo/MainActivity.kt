package com.bobbyesp.spowlo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.bobbyesp.spowlo.App.Companion.context
import com.bobbyesp.spowlo.ui.common.LocalDarkTheme
import com.bobbyesp.spowlo.ui.common.LocalDynamicColorSwitch
import com.bobbyesp.spowlo.ui.common.SettingsProvider
import com.bobbyesp.spowlo.ui.pages.InitialEntry
import com.bobbyesp.spowlo.ui.pages.downloader.DownloaderViewModel
import com.bobbyesp.spowlo.ui.theme.SpowloTheme
import com.bobbyesp.spowlo.utils.PreferencesUtil
import com.bobbyesp.spowlo.utils.matchUrlFromSharedText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val downloaderViewModel: DownloaderViewModel by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            v.setPadding(0, 0, 0, 0)
            insets
        }
        runBlocking {
            if (Build.VERSION.SDK_INT < 33)
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(PreferencesUtil.getLanguageConfiguration())
                )
        }
        context = this.baseContext
        setContent {
            val isUrlSharingTriggered =
                downloaderViewModel.viewStateFlow.collectAsState().value.isUrlSharingTriggered
            val windowSizeClass = calculateWindowSizeClass(this)
            SettingsProvider(windowSizeClass.widthSizeClass) {
                SpowloTheme(
                    darkTheme = LocalDarkTheme.current.isDarkTheme(),
                    isHighContrastModeEnabled = LocalDarkTheme.current.isHighContrastModeEnabled,
                    isDynamicColorEnabled = LocalDynamicColorSwitch.current,
                ) {
                    InitialEntry(
                        downloaderViewModel = downloaderViewModel,
                        isUrlShared = isUrlSharingTriggered
                    )
                }
            }
        }
        handleShareIntent(intent)
    }
    private fun handleShareIntent(intent: Intent) {
        Log.d(TAG, "handleShareIntent: $intent")

        when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.dataString?.let {
                    sharedUrl = it
                    downloaderViewModel.updateUrl(sharedUrl, true)
                }
            }
            Intent.ACTION_SEND -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)
                    ?.let { sharedContent ->
                        intent.removeExtra(Intent.EXTRA_TEXT)
                        matchUrlFromSharedText(sharedContent)
                            .let { matchedUrl ->
                                if (sharedUrl != matchedUrl) {
                                    sharedUrl = matchedUrl
                                    downloaderViewModel.updateUrl(sharedUrl, true)
                                }
                            }
                    }
            }
        }

    }

    companion object {
        private const val TAG = "MainActivity"
        private var sharedUrl = ""

        fun setLanguage(locale: String) {
            Log.d(TAG, "setLanguage: $locale")
            val localeListCompat =
                if (locale.isEmpty()) LocaleListCompat.getEmptyLocaleList()
                else LocaleListCompat.forLanguageTags(locale)
            App.applicationScope.launch(Dispatchers.Main) {
                AppCompatDelegate.setApplicationLocales(localeListCompat)
            }
        }

    }
}