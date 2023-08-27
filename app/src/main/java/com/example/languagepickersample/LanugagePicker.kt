package com.example.languagepickersample

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


data class LanguageOption(
    val icon: ImageVector,
    val name: String,
    val contentDescription: String? = null,
    val code: String,
)

@Stable interface LanguagePickerColors {
    val contentColor: Color
    val onContentColor: Color
    val selectedColor: Color
    val onSelectedColor: Color
    val dismissColor: Color
    val onDismissColor: Color
    val onApplyColor: Color
    val applyColor: Color
    val barsColor: Color
    val onBarsColor: Color
}

@Immutable
private class DefaultLanguagePickerColors(
    override val contentColor: Color,
    override val onContentColor: Color,
    override val selectedColor: Color,
    override val onSelectedColor: Color,
    override val dismissColor: Color,
    override val onDismissColor: Color,
    override val onApplyColor: Color,
    override val applyColor: Color,
    override val barsColor: Color,
    override val onBarsColor: Color,
): LanguagePickerColors

@Composable
fun languagePickerColors(
    contentColor: Color = MaterialTheme.colors.surface,
    onContentColor: Color = MaterialTheme.colors.onSurface ,
    selectedColor: Color = MaterialTheme.colors.secondary,
    onSelectedColor: Color = MaterialTheme.colors.onSecondary,
    dismissColor: Color = MaterialTheme.colors.surface,
    onDismissColor: Color = MaterialTheme.colors.onSurface,
    onApplyColor: Color = MaterialTheme.colors.onPrimary,
    applyColor: Color = MaterialTheme.colors.primary,
    barsColor: Color = MaterialTheme.colors.secondary,
    onBarsColor: Color = MaterialTheme.colors.onSecondary,
): LanguagePickerColors = DefaultLanguagePickerColors(
    contentColor,
    onContentColor,
    selectedColor,
    onSelectedColor,
    dismissColor,
    onDismissColor,
    onApplyColor,
    applyColor,
    barsColor,
    onBarsColor,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LanguagePicker(
    headerText: String = "",
    languageOptions: List<LanguageOption>,
    onDismissRequest: () -> Unit = {},
    onApplyEvent: ((String) -> Unit)? = null,
    padding: PaddingValues = PaddingValues(15.dp, 10.dp),
    colors: LanguagePickerColors = languagePickerColors(),
    currentLanguage: String
) {

    var selected by remember {
        mutableStateOf(currentLanguage)
    }
    Dialog(
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            ),
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .padding(padding)
                .width(400.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(colors.barsColor),
            ) {
                Header(headerText, padding, colors)
                LanguagesLazyColumn(
                    languageOptions,
                    padding,
                    onValueChange = {selected = it},
                    selected = selected,
                    modifier = Modifier.weight(1f, false),
                    colors
                )
                DecisionButtons(onDismissRequest, padding, onApplyEvent, selected, colors)
            }
        }
    }
}
@Composable
fun LanguagesLazyColumn(
    languageOptions: List<LanguageOption>,
    padding: PaddingValues,
    onValueChange: (String) -> Unit,
    selected: String,
    modifier: Modifier = Modifier,
    colors: LanguagePickerColors
    ) {
    val spacerSize = padding.calculateTopPadding()
    LazyColumn(
        modifier = Modifier
            .padding(0.dp)
            .then(modifier)
            .background(colors.contentColor),

        ) { item {
        for(languageOption in languageOptions) {
            Spacer(Modifier.height(spacerSize))
            LanguageButton(
                selected = selected == languageOption.code,
                onClick = { onValueChange(languageOption.code)},
                languageOption
            )
            }
        Spacer(Modifier.height(spacerSize))
        }
    }
}

@Composable
fun DecisionButtons(
    onDismissRequest: () -> Unit = {},
    padding: PaddingValues,
    onApplyEvent: ((String) -> Unit)? = null,
    selected: String,
    colors: LanguagePickerColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.barsColor),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(1.dp, 60.dp)
                .padding(padding),
            onClick = onDismissRequest,

            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.dismissColor
            )

        ) {
            Text(
                style = MaterialTheme.typography.button,
                text = stringResource(R.string.cancel),
                color = colors.onDismissColor,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            modifier =
            Modifier
                .weight(1f)
                .defaultMinSize(1.dp, 60.dp)
                .padding(padding),
            onClick =  {
                if(onApplyEvent != null)
                    onApplyEvent(selected)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.applyColor
            ),
        ) {
            Text(
                style = MaterialTheme.typography.button,
                text = stringResource(R.string.apply),
                color = colors.onApplyColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Header(
    text: String,
    padding: PaddingValues,
    colors: LanguagePickerColors,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.barsColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.h5,
            text = text,
            color = colors.onBarsColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun LanguageButton(
    selected: Boolean,
    onClick: () -> Unit,
    languageOption: LanguageOption,
    padding: PaddingValues = PaddingValues(10.dp),
    colors: LanguagePickerColors = languagePickerColors()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (!selected)
                    Modifier.background(Color.Transparent)
                else
                    Modifier.background(colors.selectedColor)
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.padding(padding))
        Image(
            languageOption.icon, languageOption.contentDescription,
            modifier = Modifier
                .size(70.dp)
                .padding(padding)
        )
        Text(
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f),
            text = languageOption.name,
            color = if(selected) colors.onSelectedColor else colors.onContentColor
        )
        if(selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = colors.onSelectedColor,
                modifier = Modifier
                    .size(50.dp)
                    .padding(padding)
            )
            Spacer(modifier = Modifier.padding(padding))
        }
    }
}

