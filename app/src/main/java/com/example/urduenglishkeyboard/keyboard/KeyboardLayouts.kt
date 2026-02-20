package com.example.urduenglishkeyboard.keyboard

object KeyboardLayouts {
    const val CODE_SHIFT = -1
    const val CODE_DELETE = -5
    const val CODE_SPACE = 32
    const val CODE_ENTER = 10
    const val CODE_LANGUAGE_SWITCH = -2
    const val CODE_NUMBERS = -3

    val englishQwerty = listOf(
        listOf(
            KeyData('q'.code, "q", "Q"), KeyData('w'.code, "w", "W"), KeyData('e'.code, "e", "E"), 
            KeyData('r'.code, "r", "R"), KeyData('t'.code, "t", "T"), KeyData('y'.code, "y", "Y"), 
            KeyData('u'.code, "u", "U"), KeyData('i'.code, "i", "I"), KeyData('o'.code, "o", "O"), 
            KeyData('p'.code, "p", "P")
        ),
        listOf(
            KeyData('a'.code, "a", "A"), KeyData('s'.code, "s", "S"), KeyData('d'.code, "d", "D"), 
            KeyData('f'.code, "f", "F"), KeyData('g'.code, "g", "G"), KeyData('h'.code, "h", "H"), 
            KeyData('j'.code, "j", "J"), KeyData('k'.code, "k", "K"), KeyData('l'.code, "l", "L")
        ),
        listOf(
            KeyData(CODE_SHIFT, "⇧", "⇧", true, 1.5f),
            KeyData('z'.code, "z", "Z"), KeyData('x'.code, "x", "X"), KeyData('c'.code, "c", "C"), 
            KeyData('v'.code, "v", "V"), KeyData('b'.code, "b", "B"), KeyData('n'.code, "n", "N"), 
            KeyData('m'.code, "m", "M"),
            KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
        ),
        listOf(
            KeyData(CODE_NUMBERS, "?123", "?123", true, 1.5f),
            KeyData(CODE_LANGUAGE_SWITCH, "\uD83C\uDF10", "\uD83C\uDF10", true, 1f),
            KeyData(CODE_SPACE, " ", " ", true, 4f),
            KeyData('.'.code, ".", "."),
            KeyData(CODE_ENTER, "↵", "↵", true, 1.5f)
        )
    )

    val urduPhonetic = listOf(
        listOf(
            KeyData('ق'.code, "ق", "ْ"), KeyData('و'.code, "و", "ّ"), KeyData('ع'.code, "ع", "ٰ"), 
            KeyData('ر'.code, "ر", "ڑ"), KeyData('ت'.code, "ت", "ٹ"), KeyData('ے'.code, "ے", "َ"), 
            KeyData('ء'.code, "ء", "ُ"), KeyData('ی'.code, "ی", "ِ"), KeyData('ہ'.code, "ہ", "ۃ"), 
            KeyData('پ'.code, "پ", "ٌ")
        ),
        listOf(
            KeyData('ا'.code, "ا", "آ"), KeyData('س'.code, "س", "ص"), KeyData('د'.code, "د", "ڈ"), 
            KeyData('ف'.code, "ف", "فض"), KeyData('گ'.code, "گ", "غ"), KeyData('ح'.code, "ح", "ھ"), 
            KeyData('ج'.code, "ج", "ض"), KeyData('ک'.code, "ک", "خ"), KeyData('ل'.code, "ل", "ل")
        ),
        listOf(
            KeyData(CODE_SHIFT, "⇧", "⇧", true, 1.5f),
            KeyData('ز'.code, "ز", "ذ"), KeyData('ش'.code, "ش", "ژ"), KeyData('چ'.code, "چ", "ث"), 
            KeyData('ط'.code, "ط", "ظ"), KeyData('ب'.code, "ب", "ب"), KeyData('ن'.code, "ن", "ں"), 
            KeyData('م'.code, "م", "م"),
            KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
        ),
        listOf(
            KeyData(CODE_NUMBERS, "?123", "?123", true, 1.5f),
            KeyData(CODE_LANGUAGE_SWITCH, "\uD83C\uDF10", "\uD83C\uDF10", true, 1f),
            KeyData(CODE_SPACE, " ", " ", true, 4f),
            KeyData('۔'.code, "۔", "."),
            KeyData(CODE_ENTER, "↵", "↵", true, 1.5f)
        )
    )
}
