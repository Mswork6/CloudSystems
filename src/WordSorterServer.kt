import java.net.ServerSocket
import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import kotlin.concurrent.thread

fun main() {

    val address = "217.114.9.51"
    val port = 5000
    val serverSocket = ServerSocket(port, 50, InetAddress.getByName(address))
    println("Сервер сортировщика слов запущен на порту $port.")

    while (true) {
        // Ожидаем подключения клиента
        val clientSocket = serverSocket.accept()
        println("Подключился новый клиент: ${clientSocket.inetAddress.hostAddress}")

        // Для каждого клиента — отдельный поток
        thread {
            handleClient(clientSocket)
        }
    }
}

private fun handleClient(socket: Socket) {
    socket.use {
        val input = BufferedReader(InputStreamReader(socket.getInputStream()))
        val output = PrintWriter(socket.getOutputStream(), true)

        while (true) {
            // Считываем строку от клиента
            val clientMessage = input.readLine() ?: break

            // Если клиент отправил пустую строку или 'exit', завершаем работу с этим клиентом
            if (clientMessage.isBlank() || clientMessage.equals("exit", ignoreCase = true)) {
                println("Клиент ${socket.inetAddress.hostAddress} завершил сеанс.")
                break
            }

            // 3) Убираем дубликаты, сортируем
            val sortedUniqueWords = WordSorter.sortWords(clientMessage)

            // 4) Формируем многострочную строку (каждое слово на новой строке)
            val multilineResponse = sortedUniqueWords.joinToString(separator = "\n")

            // Отправляем клиенту все слова. После них отправляем специальную метку <END>
            output.println(multilineResponse)
            output.println("<END>")
        }
    }
}
