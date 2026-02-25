package com.example.urduenglishkeyboard.keyboard

object KeyboardLayouts {
    const val CODE_SHIFT = -1
    const val CODE_DELETE = -5
    const val CODE_SPACE = 32
    const val CODE_ENTER = 10
    const val CODE_LANGUAGE_SWITCH = -2
    const val CODE_NUMBERS = -3
    const val CODE_EMOJI = -4
    const val CODE_EMOJI_NEXT_PAGE = -6
    const val CODE_EMOJI_PREV_PAGE = -7

    val englishQwerty = listOf(
        listOf(
            KeyData('q'.code, "q", "Q", longPressOptions = listOf("1")), KeyData('w'.code, "w", "W", longPressOptions = listOf("2")), KeyData('e'.code, "e", "E", longPressOptions = listOf("3", "è", "é", "ê", "ë", "ē")), 
            KeyData('r'.code, "r", "R", longPressOptions = listOf("4")), KeyData('t'.code, "t", "T", longPressOptions = listOf("5", "þ")), KeyData('y'.code, "y", "Y", longPressOptions = listOf("6")), 
            KeyData('u'.code, "u", "U", longPressOptions = listOf("7", "ù", "ú", "û", "ü", "ū")), KeyData('i'.code, "i", "I", longPressOptions = listOf("8", "ì", "í", "î", "ï", "ī")), KeyData('o'.code, "o", "O", longPressOptions = listOf("9", "ò", "ó", "ô", "õ", "ö", "ō", "œ", "ø")), 
            KeyData('p'.code, "p", "P", longPressOptions = listOf("0"))
        ),
        listOf(
            KeyData('a'.code, "a", "A", longPressOptions = listOf("@", "à", "á", "â", "ä", "æ", "ã", "å", "ā")), KeyData('s'.code, "s", "S", longPressOptions = listOf("#", "ß", "š")), KeyData('d'.code, "d", "D", longPressOptions = listOf("&", "ð")), 
            KeyData('f'.code, "f", "F", longPressOptions = listOf("*")), KeyData('g'.code, "g", "G", longPressOptions = listOf("-")), KeyData('h'.code, "h", "H", longPressOptions = listOf("+")), 
            KeyData('j'.code, "j", "J", longPressOptions = listOf("=")), KeyData('k'.code, "k", "K", longPressOptions = listOf("(")), KeyData('l'.code, "l", "L", longPressOptions = listOf(")"))
        ),
        listOf(
            KeyData(CODE_SHIFT, "⇧", "⇧", true, 1.5f),
            KeyData('z'.code, "z", "Z", longPressOptions = listOf("ž", "ź")), KeyData('x'.code, "x", "X"), KeyData('c'.code, "c", "C", longPressOptions = listOf("ç", "ć", "č")), 
            KeyData('v'.code, "v", "V"), KeyData('b'.code, "b", "B"), KeyData('n'.code, "n", "N", longPressOptions = listOf("ñ", "ń")), 
            KeyData('m'.code, "m", "M"),
            KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
        ),
        listOf(
            KeyData(CODE_NUMBERS, "?123", "?123", true, 1.2f),
            KeyData(CODE_EMOJI, "😀", "😀", true, 1f),
            KeyData(CODE_LANGUAGE_SWITCH, "\uD83C\uDF10", "\uD83C\uDF10", true, 1.2f),
            KeyData(CODE_SPACE, " ", " ", true, 3.5f),
            KeyData('.'.code, ".", ".", longPressOptions = listOf(",", "?", "!", ":", ";", "'", "\"")),
            KeyData(CODE_ENTER, "↵", "↵", true, 1.2f)
        )
    )

    val urduPhonetic = listOf(
        listOf(
            KeyData('ق'.code, "ق", "ْ", longPressOptions = listOf("1", "۱")), KeyData('و'.code, "و", "ّ", longPressOptions = listOf("2", "۲", "ؤ")), KeyData('ع'.code, "ع", "ٰ", longPressOptions = listOf("3", "۳", "أ", "إ")), 
            KeyData('ر'.code, "ر", "ڑ", longPressOptions = listOf("4", "۴")), KeyData('ت'.code, "ت", "ٹ", longPressOptions = listOf("5", "۵")), KeyData('ے'.code, "ے", "َ", longPressOptions = listOf("6", "۶")), 
            KeyData('ء'.code, "ء", "ُ", longPressOptions = listOf("7", "۷")), KeyData('ی'.code, "ی", "ِ", longPressOptions = listOf("8", "۸", "ئ")), KeyData('ہ'.code, "ہ", "ۃ", longPressOptions = listOf("9", "۹", "ھ", "ح")), 
            KeyData('پ'.code, "پ", "ٌ", longPressOptions = listOf("0", "۰"))
        ),
        listOf(
            KeyData('ا'.code, "ا", "آ"), KeyData('س'.code, "س", "ص", longPressOptions = listOf("ث")), KeyData('د'.code, "د", "ڈ", longPressOptions = listOf("ذ")), 
            KeyData('ف'.code, "ف", "ض"), KeyData('گ'.code, "گ", "غ"), KeyData('ح'.code, "ح", "ھ"), 
            KeyData('ج'.code, "ج", "ض", longPressOptions = listOf("ژ")), KeyData('ک'.code, "ک", "خ", longPressOptions = listOf("ق")), KeyData('ل'.code, "ل", "ل")
        ),
        listOf(
            KeyData(CODE_SHIFT, "⇧", "⇧", true, 1.5f),
            KeyData('ز'.code, "ز", "ذ", longPressOptions = listOf("ض", "ظ")), KeyData('ش'.code, "ش", "ژ"), KeyData('چ'.code, "چ", "ث"), 
            KeyData('ط'.code, "ط", "ظ"), KeyData('ب'.code, "ب", "ب"), KeyData('ن'.code, "ن", "ں", longPressOptions = listOf("ڻ")), 
            KeyData('م'.code, "م", "م"),
            KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
        ),
        listOf(
            KeyData(CODE_NUMBERS, "?123", "?123", true, 1.2f),
            KeyData(CODE_EMOJI, "😀", "😀", true, 1f),
            KeyData(CODE_LANGUAGE_SWITCH, "\uD83C\uDF10", "\uD83C\uDF10", true, 1.2f),
            KeyData(CODE_SPACE, " ", " ", true, 3.5f),
            KeyData('۔'.code, "۔", ".", longPressOptions = listOf("،", "؟", "!", ":", "؛")),
            KeyData(CODE_ENTER, "↵", "↵", true, 1.2f)
        )
    )

    val numberSymbolLayout = listOf(
        listOf(
            KeyData('1'.code, "1", "1", longPressOptions = listOf("¹", "½", "⅓", "¼")), KeyData('2'.code, "2", "2", longPressOptions = listOf("²", "⅔")), KeyData('3'.code, "3", "3", longPressOptions = listOf("³", "¾")), KeyData('4'.code, "4", "4", longPressOptions = listOf("⁴")), KeyData('5'.code, "5", "5", longPressOptions = listOf("⁵")),
            KeyData('6'.code, "6", "6", longPressOptions = listOf("⁶")), KeyData('7'.code, "7", "7", longPressOptions = listOf("⁷")), KeyData('8'.code, "8", "8", longPressOptions = listOf("⁸", "∞")), KeyData('9'.code, "9", "9", longPressOptions = listOf("⁹")), KeyData('0'.code, "0", "0", longPressOptions = listOf("ⁿ", "∅"))
        ),
        listOf(
            KeyData('@'.code, "@", "@"), KeyData('#'.code, "#", "#"), KeyData('$'.code, "$", "$", longPressOptions = listOf("€", "£", "¥", "¢", "₹")), KeyData('%'.code, "%", "%", longPressOptions = listOf("‰")), KeyData('&'.code, "&", "&"),
            KeyData('-'.code, "-", "-", longPressOptions = listOf("_", "—", "–")), KeyData('+'.code, "+", "+", longPressOptions = listOf("±")), KeyData('('.code, "(", "(", longPressOptions = listOf("<", "{", "[")), KeyData(')'.code, ")", ")", longPressOptions = listOf(">", "}", "]")), KeyData('/'.code, "/", "/", longPressOptions = listOf("\\"))
        ),
        listOf(
            KeyData('*'.code, "*", "*", longPressOptions = listOf("★", "†", "‡")), KeyData('"'.code, "\"", "\"", longPressOptions = listOf("“", "”", "«", "»")), KeyData('\''.code, "'", "'", longPressOptions = listOf("‘", "’")), KeyData(':'.code, ":", ":"), KeyData(';'.code, ";", ";"),
            KeyData('!'.code, "!", "!", longPressOptions = listOf("¡")), KeyData('?'.code, "?", "?", longPressOptions = listOf("¿")), KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
        ),
        listOf(
            KeyData(CODE_NUMBERS, "ABC", "ABC", true, 1.2f),
            KeyData(CODE_EMOJI, "😀", "😀", true, 1f),
            KeyData(','.code, ",", ","),
            KeyData(CODE_SPACE, " ", " ", true, 3.5f),
            KeyData('.'.code, ".", ".", longPressOptions = listOf("…")),
            KeyData(CODE_ENTER, "↵", "↵", true, 1.2f)
        )
    )

    val urduNumberSymbolLayout = listOf(
        listOf(
            KeyData('۱'.code, "۱", "۱", longPressOptions = listOf("1")), KeyData('۲'.code, "۲", "۲", longPressOptions = listOf("2")), KeyData('۳'.code, "۳", "۳", longPressOptions = listOf("3")), KeyData('۴'.code, "۴", "۴", longPressOptions = listOf("4")), KeyData('۵'.code, "۵", "۵", longPressOptions = listOf("5")),
            KeyData('۶'.code, "۶", "۶", longPressOptions = listOf("6")), KeyData('۷'.code, "۷", "۷", longPressOptions = listOf("7")), KeyData('۸'.code, "۸", "۸", longPressOptions = listOf("8")), KeyData('۹'.code, "۹", "۹", longPressOptions = listOf("9")), KeyData('۰'.code, "۰", "۰", longPressOptions = listOf("0"))
        ),
        listOf(
            KeyData('@'.code, "@", "@"), KeyData('#'.code, "#", "#"), KeyData('$'.code, "$", "$", longPressOptions = listOf("€", "£", "¥", "¢", "₹")), KeyData('%'.code, "%", "%", longPressOptions = listOf("٪")), KeyData('&'.code, "&", "&"),
            KeyData('-'.code, "-", "-", longPressOptions = listOf("_", "—", "–")), KeyData('+'.code, "+", "+", longPressOptions = listOf("±")), KeyData('('.code, "(", "(", longPressOptions = listOf("<", "{", "[")), KeyData(')'.code, ")", ")", longPressOptions = listOf(">", "}", "]")), KeyData('/'.code, "/", "/", longPressOptions = listOf("\\"))
        ),
        listOf(
            KeyData('*'.code, "*", "*", longPressOptions = listOf("★")), KeyData('"'.code, "\"", "\"", longPressOptions = listOf("“", "”", "«", "»")), KeyData('\''.code, "'", "'", longPressOptions = listOf("‘", "’")), KeyData(':'.code, ":", ":"), KeyData('؛'.code, "؛", "؛"),
            KeyData('!'.code, "!", "!", longPressOptions = listOf("¡")), KeyData('؟'.code, "؟", "؟"), KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
        ),
        listOf(
            KeyData(CODE_NUMBERS, "ابپ", "ابپ", true, 1.2f),
            KeyData(CODE_EMOJI, "😀", "😀", true, 1f),
            KeyData('،'.code, "،", "،"),
            KeyData(CODE_SPACE, " ", " ", true, 3.5f),
            KeyData('۔'.code, "۔", ".", longPressOptions = listOf("…")),
            KeyData(CODE_ENTER, "↵", "↵", true, 1.2f)
        )
    )

    val emojiLayoutPages = listOf(
        listOf(
            listOf(KeyData(0, "😀", "😀"), KeyData(0, "😃", "😃"), KeyData(0, "😄", "😄"), KeyData(0, "😁", "😁"), KeyData(0, "😆", "😆"), KeyData(0, "😅", "😅"), KeyData(0, "😂", "😂"), KeyData(0, "🤣", "🤣"), KeyData(0, "🥲", "🥲")),
            listOf(KeyData(0, "🥹", "🥹"), KeyData(0, "☺️", "☺️"), KeyData(0, "😊", "😊"), KeyData(0, "😇", "😇"), KeyData(0, "🙂", "🙂"), KeyData(0, "🙃", "🙃"), KeyData(0, "😉", "😉"), KeyData(0, "😌", "😌"), KeyData(0, "😍", "😍")),
            listOf(KeyData(0, "🥰", "🥰"), KeyData(0, "😘", "😘"), KeyData(0, "😗", "😗"), KeyData(0, "😙", "😙"), KeyData(0, "😚", "😚"), KeyData(0, "😋", "😋"), KeyData(0, "😛", "😛"), KeyData(0, "😝", "😝"), KeyData(0, "😜", "😜")),
            listOf(KeyData(0, "🤪", "🤪"), KeyData(0, "🤨", "🤨"), KeyData(0, "🧐", "🧐"), KeyData(0, "🤓", "🤓"), KeyData(0, "😎", "😎"), KeyData(0, "🥸", "🥸"), KeyData(0, "🤩", "🤩"), KeyData(0, "🥳", "🥳"), KeyData(0, "😏", "😏")),
            listOf(
                KeyData(CODE_EMOJI, "ABC", "ABC", true, 1.5f),
                KeyData(CODE_EMOJI_PREV_PAGE, "◀", "◀", true, 1f),
                KeyData(CODE_SPACE, " ", " ", true, 3f),
                KeyData(CODE_EMOJI_NEXT_PAGE, "▶", "▶", true, 1f),
                KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
            )
        ),
        listOf(
            listOf(KeyData(0, "😒", "😒"), KeyData(0, "😞", "😞"), KeyData(0, "😔", "😔"), KeyData(0, "😟", "😟"), KeyData(0, "😕", "😕"), KeyData(0, "🙁", "🙁"), KeyData(0, "😣", "😣"), KeyData(0, "😖", "😖"), KeyData(0, "😫", "😫")),
            listOf(KeyData(0, "😩", "😩"), KeyData(0, "🥺", "🥺"), KeyData(0, "😢", "😢"), KeyData(0, "😭", "😭"), KeyData(0, "😮‍💨", "😮‍💨"), KeyData(0, "😤", "😤"), KeyData(0, "😠", "😠"), KeyData(0, "😡", "😡"), KeyData(0, "🤬", "🤬")),
            listOf(KeyData(0, "🤯", "🤯"), KeyData(0, "😳", "😳"), KeyData(0, "🥵", "🥵"), KeyData(0, "🥶", "🥶"), KeyData(0, "😱", "😱"), KeyData(0, "😨", "😨"), KeyData(0, "😰", "😰"), KeyData(0, "😥", "😥"), KeyData(0, "😓", "😓")),
            listOf(KeyData(0, "🤗", "🤗"), KeyData(0, "🤔", "🤔"), KeyData(0, "🫣", "🫣"), KeyData(0, "🤭", "🤭"), KeyData(0, "🤫", "🤫"), KeyData(0, "🤥", "🤥"), KeyData(0, "😶", "😶"), KeyData(0, "😐", "😐"), KeyData(0, "😑", "😑")),
            listOf(
                KeyData(CODE_EMOJI, "ABC", "ABC", true, 1.5f),
                KeyData(CODE_EMOJI_PREV_PAGE, "◀", "◀", true, 1f),
                KeyData(CODE_SPACE, " ", " ", true, 3f),
                KeyData(CODE_EMOJI_NEXT_PAGE, "▶", "▶", true, 1f),
                KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
            )
        ),
        listOf(
            listOf(KeyData(0, "🐶", "🐶"), KeyData(0, "🐱", "🐱"), KeyData(0, "🐭", "🐭"), KeyData(0, "🐹", "🐹"), KeyData(0, "🐰", "🐰"), KeyData(0, "🦊", "🦊"), KeyData(0, "🐻", "🐻"), KeyData(0, "🐼", "🐼"), KeyData(0, "🐻‍❄️", "🐻‍❄️")),
            listOf(KeyData(0, "🙈", "🙈"), KeyData(0, "🙉", "🙉"), KeyData(0, "🙊", "🙊"), KeyData(0, "🐵", "🐵"), KeyData(0, "🐔", "🐔"), KeyData(0, "🐧", "🐧"), KeyData(0, "🐦", "🐦"), KeyData(0, "🐤", "🐤"), KeyData(0, "🦆", "🦆")),
            listOf(KeyData(0, "🌲", "🌲"), KeyData(0, "🌱", "🌱"), KeyData(0, "🌷", "🌷"), KeyData(0, "🌹", "🌹"), KeyData(0, "🌺", "🌺"), KeyData(0, "🌸", "🌸"), KeyData(0, "🌼", "🌼"), KeyData(0, "🌻", "🌻"), KeyData(0, "🌞", "🌞")),
            listOf(KeyData(0, "🌎", "🌎"), KeyData(0, "🌙", "🌙"), KeyData(0, "⭐", "⭐"), KeyData(0, "🔥", "🔥"), KeyData(0, "💧", "💧"), KeyData(0, "⚡", "⚡"), KeyData(0, "❄️", "❄️"), KeyData(0, "🌩️", "🌩️"), KeyData(0, "✨", "✨")),
            listOf(
                KeyData(CODE_EMOJI, "ABC", "ABC", true, 1.5f),
                KeyData(CODE_EMOJI_PREV_PAGE, "◀", "◀", true, 1f),
                KeyData(CODE_SPACE, " ", " ", true, 3f),
                KeyData(CODE_EMOJI_NEXT_PAGE, "▶", "▶", true, 1f),
                KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
            )
        ),
        listOf(
            listOf(KeyData(0, "👋", "👋"), KeyData(0, "🤚", "🤚"), KeyData(0, "🖐️", "🖐️"), KeyData(0, "✋", "✋"), KeyData(0, "🖖", "🖖"), KeyData(0, "👌", "👌"), KeyData(0, "🤌", "🤌"), KeyData(0, "🤏", "🤏"), KeyData(0, "✌️", "✌️")),
            listOf(KeyData(0, "🤞", "🤞"), KeyData(0, "🫰", "🫰"), KeyData(0, "🤟", "🤟"), KeyData(0, "🤘", "🤘"), KeyData(0, "🤙", "🤙"), KeyData(0, "👈", "👈"), KeyData(0, "👉", "👉"), KeyData(0, "👆", "👆"), KeyData(0, "👇", "👇")),
            listOf(KeyData(0, "❤️", "❤️"), KeyData(0, "🧡", "🧡"), KeyData(0, "💛", "💛"), KeyData(0, "💚", "💚"), KeyData(0, "💙", "💙"), KeyData(0, "💜", "💜"), KeyData(0, "🤎", "🤎"), KeyData(0, "🖤", "🖤"), KeyData(0, "🤍", "🤍")),
            listOf(KeyData(0, "💔", "💔"), KeyData(0, "❤️‍🔥", "❤️‍🔥"), KeyData(0, "❤️‍🩹", "❤️‍🩹"), KeyData(0, "❣️", "❣️"), KeyData(0, "💕", "💕"), KeyData(0, "👍", "👍"), KeyData(0, "👎", "👎"), KeyData(0, "👏", "👏"), KeyData(0, "🙌", "🙌")),
            listOf(
                KeyData(CODE_EMOJI, "ABC", "ABC", true, 1.5f),
                KeyData(CODE_EMOJI_PREV_PAGE, "◀", "◀", true, 1f),
                KeyData(CODE_SPACE, " ", " ", true, 3f),
                KeyData(CODE_EMOJI_NEXT_PAGE, "▶", "▶", true, 1f),
                KeyData(CODE_DELETE, "⌫", "⌫", true, 1.5f)
            )
        )
    )
}
