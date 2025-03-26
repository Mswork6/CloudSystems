import java.net.ServerSocket
import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.io.File
import kotlin.concurrent.thread

fun main() {
    val port = 6000
    val serverSocket = ServerSocket(port)
    println("Сервер доски объявлений запущен на порту $port.")

    // Файл, где будут храниться объявления:
    val adsFile = File("ads.txt")
    if (!adsFile.exists()) {
        adsFile.createNewFile()
    }

    while (true) {
        val clientSocket = serverSocket.accept()
        println("Подключился новый клиент: ${clientSocket.inetAddress.hostAddress}")

        // Запускаем каждое подключение в отдельном потоке
        thread {
            handleAdsClient(clientSocket, adsFile)
        }
    }
}

private fun handleAdsClient(socket: Socket, adsFile: File) {
    socket.use {
        val input = BufferedReader(InputStreamReader(socket.getInputStream()))
        val output = PrintWriter(socket.getOutputStream(), true)

        while (true) {
            val clientMessage = input.readLine() ?: break
            // Если строка пустая, разрываем соединение
            if (clientMessage.isBlank()) {
                println("Клиент ${socket.inetAddress.hostAddress} завершил сеанс.")
                break
            }

            if (clientMessage.equals("LIST", ignoreCase = true)) {
                // Отправляем клиенту все объявления, объединённые через ";"
                val ads = adsFile.readLines()
                val response = ads.joinToString("; ")
                output.println(response)
            } else {
                // Добавляем новое объявление в файл
                adsFile.appendText(clientMessage + System.lineSeparator())
                // И отправляем подтверждение
                output.println("Message added: \"$clientMessage\"")
            }
        }
    }
}
