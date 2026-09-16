fun main() {
    val calculator = mathhh()

    println("Введите пример:")
    val primer = readln()
    try {
        val result = calculator.calc(primer)
        val formatResult = if (result % 1.0 == 0.0) {
            result.toInt().toString()
        } else {
            String.format("%.4f", result).trimEnd('0').trimEnd('.')
        }
        println(formatResult)
    } catch (e: Exception) {
        println("Ошибка: ${e.message ?: "Некорректное выражение"}")
    }
}