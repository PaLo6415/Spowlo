package com.bobbyesp.spowlo.ui.pages.settings.appearance

import android.os.Build
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bobbyesp.library.dto.Song
import com.bobbyesp.spowlo.R
import com.bobbyesp.spowlo.ui.common.LocalDarkTheme
import com.bobbyesp.spowlo.ui.common.LocalDynamicColorSwitch
import com.bobbyesp.spowlo.ui.common.LocalPaletteStyleIndex
import com.bobbyesp.spowlo.ui.common.LocalSeedColor
import com.bobbyesp.spowlo.ui.common.Route
import com.bobbyesp.spowlo.ui.components.BackButton
import com.bobbyesp.spowlo.ui.components.ConfirmButton
import com.bobbyesp.spowlo.ui.components.DismissButton
import com.bobbyesp.spowlo.ui.components.LargeTopAppBar
import com.bobbyesp.spowlo.ui.components.PreferenceItem
import com.bobbyesp.spowlo.ui.components.PreferenceSwitch
import com.bobbyesp.spowlo.ui.components.PreferenceSwitchWithDivider
import com.bobbyesp.spowlo.ui.components.SingleChoiceItem
import com.bobbyesp.spowlo.ui.components.songs.SongCard
import com.bobbyesp.spowlo.ui.theme.DEFAULT_SEED_COLOR
import com.bobbyesp.spowlo.utils.DarkThemePreference.Companion.FOLLOW_SYSTEM
import com.bobbyesp.spowlo.utils.DarkThemePreference.Companion.OFF
import com.bobbyesp.spowlo.utils.DarkThemePreference.Companion.ON
import com.bobbyesp.spowlo.utils.PreferencesUtil
import com.bobbyesp.spowlo.utils.getLanguageDesc
import com.bobbyesp.spowlo.utils.palettesMap
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.color.DynamicColors
import com.kyant.monet.Hct
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.PaletteStyle
import com.kyant.monet.TonalPalettes
import com.kyant.monet.TonalPalettes.Companion.toTonalPalettes
import com.kyant.monet.a1
import com.kyant.monet.a2
import com.kyant.monet.a3
import com.kyant.monet.toColor

val colorList = listOf(
    Color(DEFAULT_SEED_COLOR),
    Color.Blue,
    Hct(60.0, 100.0, 70.0).toSrgb().toColor(),
    Hct(125.0, 50.0, 60.0).toSrgb().toColor(),
    Color.Cyan,
    Color.Red,
    Color.Yellow,
    Color.Magenta
)

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPagerApi::class
)
@Composable
fun AppearancePage(
    navController: NavHostController
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )
    var showDarkThemeDialog by remember { mutableStateOf(false) }
    val darkTheme = LocalDarkTheme.current
    var darkThemeValue by remember { mutableStateOf(darkTheme.darkThemeValue) }
    val image by remember {
        mutableStateOf(
            listOf(
                R.drawable.sample,
                R.drawable.sample1,
                R.drawable.sample2,
                R.drawable.sample3
            ).random()
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.display),
                    )
                }, navigationIcon = {
                    BackButton {
                        navController.popBackStack()
                    }
                }, scrollBehavior = scrollBehavior
            )
        }, content = {
            Column(
                Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                SongCard(
                    song = Song(
                        name = "Shut Up",
                        artist = "Alan Walker",
                        explicit = true,
                        cover_url = "https://i.scdn.co/image/ab67616d0000b273a152de6438e748b4c0cddff7",
                        duration = 132.954
                    ), modifier = Modifier.padding(16.dp)
                )
                val pagerState =
                    rememberPagerState(
                        initialPage = colorList.indexOf(Color(LocalSeedColor.current))
                            .run { if (equals(-1)) 1 else this })
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clearAndSetSemantics { }, state = pagerState,
                    count = colorList.size, contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Row() { ColorButtons(colorList[it]) }
                }
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    pageCount = colorList.size,
                    modifier = Modifier
                        .clearAndSetSemantics { }
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 12.dp),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = MaterialTheme.colorScheme.outlineVariant,
                    indicatorHeight = 6.dp,
                    indicatorWidth = 6.dp
                )

                if (DynamicColors.isDynamicColorAvailable()) {
                    PreferenceSwitch(
                        title = stringResource(id = R.string.dynamic_color),
                        description = stringResource(
                            id = R.string.dynamic_color_desc
                        ),
                        icon = Icons.Outlined.Palette,
                        isChecked = LocalDynamicColorSwitch.current,
                        onClick = {
                            PreferencesUtil.switchDynamicColor()
                        }
                    )
                }
                val isDarkTheme = LocalDarkTheme.current.isDarkTheme()
                PreferenceSwitchWithDivider(
                    title = stringResource(id = R.string.dark_theme),
                    icon = Icons.Outlined.DarkMode,
                    isChecked = isDarkTheme,
                    description = LocalDarkTheme.current.getDarkThemeDesc(),
                    onChecked = { PreferencesUtil.modifyDarkThemePreference(if (isDarkTheme) OFF else ON) },
                    onClick = { navController.navigate(Route.APP_THEME) }
                )
                //todo: add the languages page
                if (Build.VERSION.SDK_INT >= 24)
                    PreferenceItem(
                        title = stringResource(R.string.language),
                        icon = Icons.Outlined.Language,
                        description = getLanguageDesc()
                    ) { navController.navigate(Route.LANGUAGES) }
            }
        })
    if (showDarkThemeDialog)
        AlertDialog(onDismissRequest = {
            showDarkThemeDialog = false
            darkThemeValue = darkTheme.darkThemeValue
        }, confirmButton = {
            ConfirmButton {
                showDarkThemeDialog = false
                PreferencesUtil.modifyDarkThemePreference(darkThemeValue)
            }
        }, dismissButton = {
            DismissButton {
                showDarkThemeDialog = false
                darkThemeValue = darkTheme.darkThemeValue
            }
        }, icon = { Icon(Icons.Outlined.DarkMode, null) },
            title = { Text(stringResource(R.string.dark_theme)) }, text = {
                Column {
                    SingleChoiceItem(
                        text = stringResource(R.string.follow_system),
                        selected = darkThemeValue == FOLLOW_SYSTEM
                    ) {
                        darkThemeValue = FOLLOW_SYSTEM
                    }
                    SingleChoiceItem(
                        text = stringResource(R.string.on),
                        selected = darkThemeValue == ON
                    ) {
                        darkThemeValue = ON
                    }
                    SingleChoiceItem(
                        text = stringResource(R.string.off),
                        selected = darkThemeValue == OFF
                    ) {
                        darkThemeValue = OFF
                    }
                }
            })
}

@Composable
fun RowScope.ColorButtons(color: Color) {
    palettesMap.forEach {
        ColorButton(color = color, index = it.key, tonalStyle = it.value)
    }
}

@Composable
fun RowScope.ColorButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    index: Int = 0,
    tonalStyle: PaletteStyle = PaletteStyle.TonalSpot,
) {
    val tonalPalettes = color.toTonalPalettes(tonalStyle)
    val isSelect =
        !LocalDynamicColorSwitch.current && LocalSeedColor.current == color.toArgb() && LocalPaletteStyleIndex.current == index
    ColorButtonImpl(
        modifier = modifier,
        tonalPalettes = tonalPalettes,
        isSelected = { isSelect }
    ) {
        PreferencesUtil.switchDynamicColor(enabled = false)
        PreferencesUtil.modifyThemeSeedColor(color.toArgb(), index)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.ColorButtonImpl(
    modifier: Modifier = Modifier,
    isSelected: () -> Boolean = { false },
    tonalPalettes: TonalPalettes,
    onClick: () -> Unit = {}
) {

    CompositionLocalProvider(LocalTonalPalettes provides tonalPalettes) {
        val color1 = 80.a1
        val color2 = 90.a2
        val color3 = 60.a3

        val containerSize by animateDpAsState(targetValue = if (isSelected.invoke()) 28.dp else 0.dp)
        val iconSize by animateDpAsState(targetValue = if (isSelected.invoke()) 16.dp else 0.dp)
        val containerColor = MaterialTheme.colorScheme.primaryContainer

        Surface(modifier = modifier
            .padding(4.dp)
            .sizeIn(maxHeight = 80.dp, maxWidth = 80.dp, minHeight = 64.dp, minWidth = 64.dp)
            .weight(1f, false)
            .aspectRatio(1f),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            onClick = { onClick() }) {
            Box(Modifier.fillMaxSize()) {
                Box(
                    modifier = modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .drawBehind { drawCircle(color1) }
                        .align(Alignment.Center)
                ) {
                    Surface(
                        color = color2,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .size(24.dp)
                    ) {}
                    Surface(
                        color = color3,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(24.dp)
                    ) {}
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .size(containerSize)
                            .drawBehind { drawCircle(containerColor) },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            modifier = Modifier
                                .size(iconSize)
                                .align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                }
            }
        }
    }
}