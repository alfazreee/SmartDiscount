package com.mahesa0004.smartdiscount.ui.screen

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mahesa0004.smartdiscount.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    } ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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

    var harga by rememberSaveable { mutableStateOf("") }
    var diskon by rememberSaveable { mutableStateOf("") }
    var isPersen by rememberSaveable { mutableStateOf(true) }
    var hasil by rememberSaveable { mutableStateOf(0.0) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val options = listOf("%", "Rp")
    var selectOption by rememberSaveable { mutableStateOf(options[0]) }
    var hargaError by rememberSaveable { mutableStateOf(false) }
    var diskonError by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
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
            modifier = Modifier.width(120.dp)
        ) {
            Text(stringResource(R.string.hitung))
        }
        Text(stringResource(R.string.hasil))

        Text(
            text = "${stringResource(R.string.harga_akhir)}: Rp ${hasil.toInt()}",
            style = MaterialTheme.typography.titleLarge
        )
        if (hasil > 0.0) {
            val message = stringResource(R.string.bagikan_template, harga,
                if (isPersen) "$diskon%" else "Rp $diskon",
                "Rp ${hasil.toInt()}"
                )
            Button(
                onClick = {shareData(context, message)},
                modifier = Modifier.width(120.dp)
            ) {
                Text(text = stringResource(R.string.bagikan))
            }
        }
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

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    SmartDiscountTheme {
        MainScreen(rememberNavController())
    }
}
