fun main() {
    val calculator = mathhh()

    println("Введите пример:")
    val input = readln().trim()
    try {
        val result = calculator.calc(input)
        val formattedResult = if (result % 1.0 == 0.0) {
            result.toInt().toString()
        } else {
            String.format("%.4f", result).trimEnd('0').trimEnd('.')
        }
        println(formattedResult)
    } catch (e: Exception) {
        println("Ошибка: ${e.message ?: "Некорректное выражение"}")
    }
}