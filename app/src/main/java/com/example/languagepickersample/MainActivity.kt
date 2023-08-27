package com.example.languagepickersample

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.example.languagepickersample.ui.theme.LanguagePickerSampleTheme
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isPickingLanguage by remember {
                mutableStateOf(false)
            }


            val options: List<LanguageOption> = listOf (
                LanguageOption(
                    icon = ImageVector.vectorResource(id = R.drawable.great_britain),
                    name = "English",
                    code = "en"
                ),
                LanguageOption(
                    icon = ImageVector.vectorResource(id = R.drawable.poland),
                    name = "Polski",
                    code = "pl"
                ),
                LanguageOption(
                    icon = ImageVector.vectorResource(id = R.drawable.germany),
                    name = "Deutsch",
                    code = "de"
                )
            )
            LanguagePickerSampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.flag),
                            null,
                            modifier = Modifier.clickable(onClick = {isPickingLanguage = !isPickingLanguage})
                        )
                        Text(
                            text = getString(R.string.language_code)
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = getString(R.string.hello_world),
                            style = MaterialTheme.typography.h3
                        )
                        if(isPickingLanguage) {
                            LanguagePicker(
                                getString(R.string.select_language),
                                options,
                                onDismissRequest = {isPickingLanguage = !isPickingLanguage},
                                onApplyEvent = {
                                    isPickingLanguage = !isPickingLanguage
                                    AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.create(
                                        Locale(it)
                                    )
                                )

                                recreate()
                                },
                                currentLanguage = getString(R.string.language_code).lowercase()
                            )
                        }
                    }
                }
            }
        }
    }
}

