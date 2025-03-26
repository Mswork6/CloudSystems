import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

fun main() {
    val host = "127.0.0.1"
    val port = 6000

    Socket(host, port).use { socket ->
        println("Подключились к серверу доски объявлений $host:$port")

        val serverReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val clientWriter = PrintWriter(socket.getOutputStream(), true)

        val consoleReader = BufferedReader(InputStreamReader(System.`in`))

        while (true) {
            print("Введите команду (LIST / текст / пустая строка для выхода): ")
            val userInput = consoleReader.readLine() ?: break

            // Если пользователь ввёл пустую строку, завершаем соединение
            if (userInput.isBlank()) {
                clientWriter.println(userInput)
                println("Завершение работы клиента.")
                break
            }

            // Отправляем команду/строку серверу
            clientWriter.println(userInput)

            // Читаем ответ от сервера
            val response = serverReader.readLine() ?: break
            if (response.isNotBlank()) {
                println("Ответ сервера: $response")
            }
        }
    }
}
