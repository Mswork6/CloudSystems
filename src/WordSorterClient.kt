import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

fun main() {
    val host = "127.0.0.1" // Адрес сервера
    val port = 5000

    // Подключаемся к серверу
    Socket(host, port).use { socket ->
        println("Клиент запущен. Подключение к серверу: $host:$port")

        val serverReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val clientWriter = PrintWriter(socket.getOutputStream(), true)
        val consoleReader = BufferedReader(InputStreamReader(System.`in`))

        while (true) {
            print("Введите текст (или пустую строку/'exit' для выхода): ")
            val userInput = consoleReader.readLine() ?: break

            // Если пустая строка или 'exit' -> завершаем
            if (userInput.isBlank() || userInput.equals("exit", ignoreCase = true)) {
                clientWriter.println(userInput)
                println("Завершение работы клиента.")
                break
            }

            // Отправляем строку на сервер
            clientWriter.println(userInput)

            // Ожидаем ответ от сервера. Ответ состоит из нескольких строк + строка "<END>"
            println("Ответ сервера:")

            val answer = serverReader.readLine()
            println(answer)

            while (true) {
                val line = serverReader.readLine() ?: break
                if (line == "<END>") {
                    // Когда дошли до "<END>", значит ответ полностью получен
                    break
                }
                println(line)
            }
            println() // пустая строка для разделения ответов
        }
    }
}
