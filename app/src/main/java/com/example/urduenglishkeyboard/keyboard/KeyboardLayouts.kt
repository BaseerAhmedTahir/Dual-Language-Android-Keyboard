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
            KeyData('q'.code, "q", "Q", longPressOptions = listOf("1")), KeyData('w'.code, "w", "W", longPressOptions = listOf("2")), KeyData('e'.code, "e", "E", longPressOptions = listOf("3", "è", "é", "ê", "ë")), 
            KeyData('r'.code, "r", "R", longPressOptions = listOf("4")), KeyData('t'.code, "t", "T", longPressOptions = listOf("5")), KeyData('y'.code, "y", "Y", longPressOptions = listOf("6")), 
            KeyData('u'.code, "u", "U", longPressOptions = listOf("7", "ù", "ú", "û", "ü")), KeyData('i'.code, "i", "I", longPressOptions = listOf("8", "ì", "í", "î", "ï")), KeyData('o'.code, "o", "O", longPressOptions = listOf("9", "ò", "ó", "ô", "õ", "ö")), 
            KeyData('p'.code, "p", "P", longPressOptions = listOf("0"))
        ),
        listOf(
            KeyData('a'.code, "a", "A", longPressOptions = listOf("@", "à", "á", "â", "ä", "æ", "ã", "å")), KeyData('s'.code, "s", "S", longPressOptions = listOf("#", "ß")), KeyData('d'.code, "d", "D", longPressOptions = listOf("&")), 
            KeyData('f'.code, "f", "F", longPressOptions = listOf("*")), KeyData('g'.code, "g", "G", longPressOptions = listOf("-")), KeyData('h'.code, "h", "H", longPressOptions = listOf("+")), 
            KeyData('j'.code, "j", "J", longPressOptions = listOf("=")), KeyData('k'.code, "k", "K", longPressOptions = listOf("(")), KeyData('l'.code, "l", "L", longPressOptions = listOf(")"))
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
            KeyData('ق'.code, "ق", "ْ", longPressOptions = listOf("1", "۱")), KeyData('و'.code, "و", "ّ", longPressOptions = listOf("2", "۲", "ؤ")), KeyData('ع'.code, "ع", "ٰ", longPressOptions = listOf("3", "۳")), 
            KeyData('ر'.code, "ر", "ڑ", longPressOptions = listOf("4", "۴")), KeyData('ت'.code, "ت", "ٹ", longPressOptions = listOf("5", "۵")), KeyData('ے'.code, "ے", "َ", longPressOptions = listOf("6", "۶")), 
            KeyData('ء'.code, "ء", "ُ", longPressOptions = listOf("7", "۷")), KeyData('ی'.code, "ی", "ِ", longPressOptions = listOf("8", "۸", "ئ")), KeyData('ہ'.code, "ہ", "ۃ", longPressOptions = listOf("9", "۹", "ھ")), 
            KeyData('پ'.code, "پ", "ٌ", longPressOptions = listOf("0", "۰"))
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
