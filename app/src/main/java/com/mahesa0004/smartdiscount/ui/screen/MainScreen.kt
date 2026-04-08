package com.mahesa0004.smartdiscount.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahesa0004.smartdiscount.R
import com.mahesa0004.smartdiscount.ui.theme.SmartDiscountTheme
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) {
            innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(modifier: Modifier = Modifier) {

    var harga by remember { mutableStateOf("") }
    var diskon by remember { mutableStateOf("") }
    var isPersen by remember { mutableStateOf(true) }
    var hasil by remember { mutableStateOf(0.0) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("%", "Rp")
    var selectOption by remember { mutableStateOf(options[0]) }
    var hargaError by remember { mutableStateOf(false) }
    var diskonError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.discount),
            contentDescription = stringResource(R.string.logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(132.dp)
        )
        Text(
            text = stringResource(R.string.app_Intro)
        )
        OutlinedTextField(
            value = harga,
            onValueChange = {
              text -> harga = text.filter { it.isDigit() }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(stringResource(R.string.harga)) },
            supportingText = {ErrorHint(hargaError)},
            isError = hargaError,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = diskon,
                onValueChange = {
                    text -> diskon = text.filter { it.isDigit() }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {Text(stringResource(R.string.diskon)) },
                supportingText = {ErrorHint(diskonError)},
                isError = diskonError,
                modifier = Modifier.weight(1f)
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {expanded = !expanded}
            ) {
                OutlinedTextField(
                    value = selectOption,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Tipe") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier.width(90.dp).menuAnchor()
                )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                options.forEach { options ->
                    DropdownMenuItem(
                        text = {Text(options)},
                        onClick = {
                            selectOption = options
                            isPersen = options == "%"
                            expanded = false
                            }
                        )
                    }
                }
            }
        }
        Button(
            onClick = {
                hargaError = (harga == "" || harga == "0")
                diskonError = (diskon == "" || diskon == "0")
                if (hargaError || diskonError)return@Button

                val hargaValue = harga.toDoubleOrNull() ?: 0.0
                val diskonValue = diskon.toDoubleOrNull() ?:0.0

                hasil = if (isPersen) {
                    hargaValue - (hargaValue * diskonValue / 100)
                } else {
                    hargaValue - diskonValue
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.hitung))
        }
        Text(stringResource(R.string.hasil))

        Text(
            text = "${stringResource(R.string.harga_akhir)}: Rp ${hasil.toInt()}",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError){
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean){
    if (isError){
        Text(text = stringResource(R.string.input_invalid))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    SmartDiscountTheme {
        MainScreen()
    }
}
