package com.amora.pokeku.ui.utils

class NameRenamer(private val firstName: String?, private val prevNum: Int?) {

	fun rename(): String {
		val newName = "${firstName}-${getNextFibonacci()}"
		return newName
	}

	private fun generateFibonacci(n: Int): List<Int> {
		val fibonacciList = mutableListOf<Int>()

		var a = 0
		var b = 1

		repeat(n) {
			fibonacciList.add(a)
			val sum = a + b
			a = b
			b = sum
		}

		return fibonacciList
	}

	private fun getNextFibonacci(): Int {
		val listFibonacci = generateFibonacci(1000)
		val index = listFibonacci.indexOfLast { it == prevNum }
		return listFibonacci[index + 1]
	}
}