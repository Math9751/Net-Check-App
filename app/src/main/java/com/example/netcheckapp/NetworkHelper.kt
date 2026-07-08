package com.example.netcheckapp

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class NetworkHelper(private val context: Context) {

    private val BASE_URL = "http://192.168.15.4:5000/api/report"
    // Função para checar o tipo de conexão atual
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun getNetworkType(): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // No Android moderno, pegamos a rede ativa e suas capacidades
        val activeNetwork = connectivityManager.activeNetwork ?: return "Sem Conexão"
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return "Sem Conexão"

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Dados Móveis (Operadora)"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            else -> "Outra Conexão"
        }
    }

    // Função para pegar dados básicos do aparelho (bom para o relatório)
    fun getDeviceModel(): String {
        val fabricante = Build.MANUFACTURER
        val modelo = Build.MODEL
        return "$fabricante $modelo (Android ${Build.VERSION.RELEASE})"
    }
    // Faz o envio do JSON para o servidor Python
    fun enviarRelatorio(device: String, network: String) {
        // Abrimos uma Thread separada exigida pelo Android para operações de rede
        Thread {
            try {
                val url = URL(BASE_URL)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json; utf-8")
                connection.setRequestProperty("Accept", "application/json")
                connection.doOutput = true

                // Monta o esqueleto do JSON manualmente (para evitar bibliotecas extras agora)
                val jsonInputString = "{\"device\": \"$device\", \"network\": \"$network\"}"

                // Envia os dados
                OutputStreamWriter(connection.outputStream, "UTF-8").use { writer ->
                    writer.write(jsonInputString)
                    writer.flush()
                }

                // Lê a resposta do seu servidor Python
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("NetCheck", "Sucesso! Servidor Python recebeu os dados.")
                } else {
                    Log.e("NetCheck", "Erro no servidor: $responseCode")
                }
                connection.disconnect()

            } catch (e: Exception) {
                Log.e("NetCheck", "Erro de conexão: ${e.message}")
                e.printStackTrace()
            }
        }.start()
    }
}