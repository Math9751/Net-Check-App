package com.example.netcheckapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Chamando a nossa interface em Kotlin
            TelaPrincipal()
        }
    }
}

@Composable
fun TelaPrincipal() {

    val context = LocalContext.current
    val networkHelper = remember { NetworkHelper(context) }

    // Variáveis que o Kotlin usa para atualizar a tela automaticamente
    var statusRede by remember { mutableStateOf("Clique para verificar") }

    // Componente de texto na tela
    Text(text = "Status: $statusRede")

    // Componente de botão na tela
    Button(onClick = {
        val tipoRede = networkHelper.getNetworkType()
        val modeloAparelho = networkHelper.getDeviceModel()

        // Dispara o envio para a URL cadastrada lá no NetworkHelper
        networkHelper.enviarRelatorio(device = modeloAparelho, network = tipoRede)
    }) {
        Text("Enviar Dados ao Servidor")
    }
}