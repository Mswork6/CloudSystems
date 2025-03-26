object WordSorter {
    /**
     * Принимает строку текста, разбивает на слова, удаляет повторы,
     * сортирует в алфавитном порядке и возвращает список слов.
     */
    fun sortWords(textBlock: String): List<String> {

        // Удаляем всё, что не буква / не цифра / не пробел
        val cleanedText = textBlock.replace("[^\\p{L}\\p{Nd}\\s]+".toRegex(), " ")

        // Разделение текста на слова (убираем пунктуацию, пробелы, переносы)
        val words = cleanedText
            .split("\\s+".toRegex())
            .map { it.trim().lowercase() }
            .filter { it.isNotBlank() }

        // Удаление повторов и сортировка
        return words.distinct().sorted()
    }
}