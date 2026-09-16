import kotlin.math.*

class mathhh : iinterface {

    override fun sum(a: Double, b: Double): Double = a + b
    override fun raz(a: Double, b: Double): Double = a - b
    override fun proiz(a: Double, b: Double): Double = a * b
    override fun del(a: Double, b: Double): Double = if (b != 0.0) a / b else throw ArithmeticException("Деление на ноль")

    private fun toRadians(a: Double): Double = a * PI / 180.0

    override fun sin(a: Double): Double = kotlin.math.sin(toRadians(a))
    override fun cos(a: Double): Double = kotlin.math.cos(toRadians(a))
    override fun tan(a: Double): Double = kotlin.math.tan(toRadians(a))
    override fun cot(a: Double): Double {
        val tanVal = tan(a)
        if (tanVal == 0.0) throw ArithmeticException("Котангенс не определен")
        return 1.0 / tanVal
    }
    override fun sqrt(a: Double): Double {
        if (a < 0) throw ArithmeticException("Корень из отрицательного числа")
        return kotlin.math.sqrt(a)
    }

    private fun getPriority(op: String): Int = when (op) {
        "+", "-" -> 1
        "*", "/" -> 2
        "^" -> 3
        "sin", "cos", "tan", "cot", "sqrt" -> 4
        else -> 0
    }

    private fun applyOp(a: Double, b: Double, op: String): Double = when (op) {
        "+" -> sum(a, b)
        "-" -> raz(a, b)
        "*" -> proiz(a, b)
        "/" -> del(a, b)
        "^" -> a.pow(b)
        else -> 0.0
    }

    private fun applyFunc(a: Double, func: String): Double = when (func) {
        "sin" -> sin(a)
        "cos" -> cos(a)
        "tan" -> tan(a)
        "cot" -> cot(a)
        "sqrt" -> sqrt(a)
        else -> a
    }

    override fun calc(expression: String): Double {
        // Нормализация: добавляем пробелы вокруг операторов и скобок
        // Также отделяем названия функций от скобок, если они слиты (например, sin30 -> sin 30)
        val normalized = expression.lowercase()
            .replace("(", " ( ")
            .replace(")", " ) ")
            .replace("+", " + ")
            .replace("-", " - ")
            .replace("*", " * ")
            .replace("/", " / ")
            .replace("^", " ^ ")
            // Добавляем пробел после названий функций, если его нет
            .replace("sin", " sin ")
            .replace("cos", " cos ")
            .replace("tan", " tan ")
            .replace("cot", " cot ")
            .replace("sqrt", " sqrt ")

        val tokens = normalized.split(" ").filter { it.isNotBlank() }

        val outputQueue = mutableListOf<String>()
        val operatorStack = mutableListOf<String>()
        val functions = listOf("sin", "cos", "tan", "cot", "sqrt")

        for (token in tokens) {
            when {
                // Число
                token.toDoubleOrNull() != null -> outputQueue.add(token)

                // Функция (sin, cos, sqrt...)
                token in functions -> operatorStack.add(token)

                // Оператор
                token in listOf("+", "-", "*", "/", "^") -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != "(" &&
                        getPriority(operatorStack.last()) >= getPriority(token)
                    ) {
                        outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                    }
                    operatorStack.add(token)
                }

                // Открывающая скобка
                token == "(" -> operatorStack.add(token)

                // Закрывающая скобка
                token == ")" -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                        outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                    }
                    if (operatorStack.isNotEmpty()) operatorStack.removeAt(operatorStack.size - 1) // Удаляем "("

                    // Если перед скобкой была функция, выталкиваем её в очередь
                    if (operatorStack.isNotEmpty() && operatorStack.last() in functions) {
                        outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                    }
                }
            }
        }

        while (operatorStack.isNotEmpty()) outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))

        val stack = mutableListOf<Double>()
        for (token in outputQueue) {
            when {
                token.toDoubleOrNull() != null -> stack.add(token.toDouble())
                token in functions -> {
                    val a = stack.removeAt(stack.size - 1)
                    stack.add(applyFunc(a, token))
                }
                else -> {
                    val b = stack.removeAt(stack.size - 1)
                    val a = stack.removeAt(stack.size - 1)
                    stack.add(applyOp(a, b, token))
                }
            }
        }
        return if (stack.isNotEmpty()) stack.first() else 0.0
    }
}