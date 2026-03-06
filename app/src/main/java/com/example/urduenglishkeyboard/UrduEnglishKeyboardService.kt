package com.example.urduenglishkeyboard

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.PopupWindow
import android.widget.LinearLayout
import android.view.ViewGroup
import android.view.Gravity
import android.widget.ImageView
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.content.Intent
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.urduenglishkeyboard.data.AppDatabase
import com.example.urduenglishkeyboard.data.WordDao
import com.example.urduenglishkeyboard.data.WordEntity
import com.example.urduenglishkeyboard.keyboard.KeyData
import com.example.urduenglishkeyboard.keyboard.KeyboardLayouts
import com.example.urduenglishkeyboard.ui.CustomKeyboardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.view.HapticFeedbackConstants
import android.media.AudioManager
import android.content.res.Configuration
import android.graphics.Color
import kotlinx.coroutines.withContext

class UrduEnglishKeyboardService : InputMethodService() {
    
    private lateinit var keyboardView: CustomKeyboardView
    private lateinit var suggestion1: TextView
    private lateinit var suggestion2: TextView
    private lateinit var suggestion3: TextView
    
    private var isEnglish = true
    private var isShifted = false
    private var isEmoji = false
    private var isNumbers = false
    private var currentEmojiCategory = KeyboardLayouts.CODE_EMOJI_SMILEYS
    
    private var popupWindow: PopupWindow? = null
    
    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var keyboardLayoutContainer: View
    private lateinit var inlineVoiceLayout: View
    private lateinit var suggestionBarLayout: View
    private lateinit var voicePromptText: TextView
    private lateinit var voiceMicIcon: ImageView
    
    private val serviceJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var suggestionJob: Job? = null
    private lateinit var wordDao: WordDao
    
    private var currentComposingText = StringBuilder()

    override fun onCreate() {
        super.onCreate()
        CrashLogger.init(this)
        wordDao = AppDatabase.getDatabase(this).wordDao()
        setupVoiceRecognition()
        seedDatabaseMockData()
    }

    override fun onCreateInputView(): View {
        val view = layoutInflater.inflate(R.layout.keyboard_view, null)
        keyboardLayoutContainer = view.findViewById(R.id.keyboard_layout_container)
        
        inlineVoiceLayout = view.findViewById(R.id.inline_voice_layout)
        suggestionBarLayout = view.findViewById(R.id.suggestion_bar_layout)
        inlineVoiceLayout.visibility = View.GONE
        
        voicePromptText = view.findViewById(R.id.inline_voice_prompt_text)
        voiceMicIcon = view.findViewById(R.id.inline_voice_mic_icon)
        
        val closeBtn = view.findViewById<ImageView>(R.id.inline_voice_close_btn)
        closeBtn.setOnClickListener { stopVoiceInput() }
        voiceMicIcon.setOnClickListener { startListeningIntent() }
        
        keyboardView = view.findViewById(R.id.custom_keyboard_view)
        suggestion1 = view.findViewById(R.id.suggestion1)
        suggestion2 = view.findViewById(R.id.suggestion2)
        suggestion3 = view.findViewById(R.id.suggestion3)
        
        applyTheme(view)
        
        keyboardView.setOnKeyClickListener { keyData ->
            handleKeyClick(keyData)
        }
        
        keyboardView.setOnKeyLongClickListener { keyData, keyViewTextView ->
            handleKeyLongClick(keyData, keyViewTextView)
        }
        
        keyboardView.setOnCursorMoveListener { direction ->
            val ic = currentInputConnection ?: return@setOnCursorMoveListener
            val keyEventCode = if (direction > 0) KeyEvent.KEYCODE_DPAD_RIGHT else KeyEvent.KEYCODE_DPAD_LEFT
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode))
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keyEventCode))
        }

        keyboardView.setOnSwipeDeleteListener {
            val ic = currentInputConnection ?: return@setOnSwipeDeleteListener
            if (currentComposingText.isNotEmpty()) {
                currentComposingText.clear()
                ic.setComposingText("", 0)
                updateSuggestions(emptyList())
            } else {
                val textBefore = ic.getTextBeforeCursor(100, 0)
                if (!textBefore.isNullOrEmpty()) {
                    val lastSpaceIndex = textBefore.trimEnd().lastIndexOf(' ')
                    val deleteLength = if (lastSpaceIndex == -1) textBefore.length else textBefore.length - lastSpaceIndex - 1
                    val totalDelete = deleteLength + (textBefore.length - textBefore.trimEnd().length)
                    ic.deleteSurroundingText(maxOf(1, totalDelete), 0)
                }
            }
            performHapticFeedback(keyboardView, HapticFeedbackConstants.KEYBOARD_TAP)
        }
        
        setupSuggestionListeners()
        updateKeyboardLayout()
        return view
    }

    private fun isHapticFeedbackEnabled(): Boolean {
        val prefs = getSharedPreferences("keyboard_prefs", android.content.Context.MODE_PRIVATE)
        return prefs.getBoolean("vibration_enabled", true)
    }

    private fun isSoundEnabled(): Boolean {
        val prefs = getSharedPreferences("keyboard_prefs", android.content.Context.MODE_PRIVATE)
        return prefs.getBoolean("sound_enabled", true)
    }

    private fun playKeyClickSound() {
        if (isSoundEnabled()) {
            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }

    private fun performHapticFeedback(view: View, constant: Int) {
        if (isHapticFeedbackEnabled()) {
            view.performHapticFeedback(constant)
        }
    }

    private fun applyTheme(view: View) {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDark = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        
        val bgColor = if (isDark) Color.parseColor("#121212") else Color.parseColor("#E0E0E0")
        val suggestionBgColor = if (isDark) Color.parseColor("#1A1A1A") else Color.parseColor("#D0D0D0")
        val textColor = if (isDark) Color.WHITE else Color.BLACK

        view.setBackgroundColor(bgColor)
        view.findViewById<View>(R.id.suggestion_bar_layout)?.setBackgroundColor(suggestionBgColor)
        
        suggestion1.setTextColor(textColor)
        suggestion2.setTextColor(textColor)
        suggestion3.setTextColor(textColor)
        
        keyboardView.setTheme(isDark)
    }

    private fun setupSuggestionListeners() {
        val listener = View.OnClickListener { v ->
            val text = (v as TextView).text.toString()
            if (text.isNotEmpty()) {
                commitSuggestion(text)
            }
        }
        suggestion1.setOnClickListener(listener)
        suggestion2.setOnClickListener(listener)
        suggestion3.setOnClickListener(listener)
    }

    private fun seedDatabaseMockData() {
        coroutineScope.launch(Dispatchers.IO) {
            val testSuggestions = wordDao.getSuggestions("t", "en")
            if (testSuggestions.isEmpty()) {
                val words = mutableListOf<WordEntity>()
                // Common English words
                val englishWords = listOf("the", "to", "and", "that", "this", "they", "there", "their", "then", "them", "these", "those",
                       "time", "today", "tomorrow", "thanks", "think", "thing", "take", "tell", "talk", "try",
                       "hello", "hi", "how", "have", "has", "had", "here", "help", "hope", "happy", "home",
                       "you", "your", "yours", "yes", "yeah", "year", "yesterday",
                       "we", "will", "what", "where", "when", "why", "who", "which", "with", "would", "want", "work", "well",
                       "are", "about", "all", "also", "any", "after", "always", "around", "ask", "answer",
                       "is", "it", "its", "if", "in", "into", "can", "could", "call", "come", "good", "great",
                       "for", "from", "find", "first", "friend", "not", "now", "new", "no", "never", "need",
                       "be", "of", "a", "I", "for", "on", "he", "as", "at", "but", "his", "by", "say", "her", "she",
                       "or", "an", "my", "one", "so", "up", "out", "get", "go", "me", "make", "like", "just", "him", "know",
                       "people", "some", "see", "other", "than", "look", "only", "over", "back", "use", "two", "our", "way",
                       "even", "because", "give", "day", "most", "us", "okay", "ok", "sure", "please", "love", "much", "many", "more", "little")
                englishWords.forEach {
                    words.add(WordEntity(word = it, frequency = 100, language = "en"))
                }
                
                // Common Urdu words
                val urduWords = listOf("کیا", "کیوں", "کب", "کیسے", "کہاں", "کون", "کچھ", "کوئی", "کہہ", "کر", "کرو", "کریں", "کرنا",
                       "ہے", "ہیں", "ہو", "ہوں", "ہاں", "ہوتا", "ہوتی", "ہوتے", "ہمارا", "ہماری", "ہمارے", "ہم", "ہر",
                       "اور", "اب", "اس", "ان", "اپنا", "اپنی", "اپنے", "اچھا", "اچھی", "اچھے", "آج", "آؤ", "آئیں", "آپ",
                       "یہ", "یہاں", "یاد", "یقین", "یا", "تھا", "تھی", "تھے", "تم", "تیرا", "تیری", "تیرے", "تمہارا", "تو", "تک",
                       "میں", "میرا", "میری", "میرے", "مجھے", "مت", "مزید", "بہت", "بات", "بھی", "باہر", "بار", "بعد",
                       "نہیں", "نہ", "نام", "نے", "نظر", "دے", "دیں", "دو", "دیا", "دن", "دیر", "دیکھ", "دیکھو",
                       "سلام", "شکریہ", "وقت", "وہ", "وہاں", "واپس", "لو", "لیں", "لیا", "لگتا", "لگتی", "لگتے",
                       "کہ", "سے", "کو", "کی", "کے", "پر", "ایک", "جو", "لیے", "ساتھ", "جس", "اپنی", "جائے", "کرنے",
                       "گیا", "کام", "ہوا", "جب", "جیسے", "ایسے", "کہا", "کس", "اگر", "طرح", "مگر", "کرتا", "وہی", "پاس",
                       "سب", "ہوگا", "تھوڑا", "بھائی", "دوست", "اللہ", "حافظ", "انشاءاللہ", "ماشاءاللہ", "الحمدللہ", "جزاک",
                       "جلدی", "صبح", "رات", "شام", "خدا")
                urduWords.forEach {
                    words.add(WordEntity(word = it, frequency = 100, language = "ur"))
                }
                wordDao.insertWords(words)
            }
        }
    }

    override fun onStartInputView(info: android.view.inputmethod.EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        
        // Reset keyboard state when opening
        isShifted = false
        isEmoji = false
        isNumbers = false
        currentComposingText.clear()
        
        if (::keyboardView.isInitialized) {
            val actionId = info?.imeOptions?.and(android.view.inputmethod.EditorInfo.IME_MASK_ACTION)
            val enterLabel = when(actionId) {
                android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH -> "🔍"
                android.view.inputmethod.EditorInfo.IME_ACTION_GO -> "➔"
                android.view.inputmethod.EditorInfo.IME_ACTION_SEND -> "📤"
                android.view.inputmethod.EditorInfo.IME_ACTION_DONE -> "✓"
                android.view.inputmethod.EditorInfo.IME_ACTION_NEXT -> "⇥"
                else -> "↵"
            }
            keyboardView.setEnterKeyLabel(enterLabel)
            
            updateKeyboardLayout()
            updateSuggestions(emptyList())
        }
    }

    override fun onFinishInput() {
        super.onFinishInput()
        currentComposingText.clear()
        updateSuggestions(emptyList())
        popupWindow?.dismiss()
        stopVoiceInput()
    }
    
    override fun onUpdateSelection(
        oldSelStart: Int, oldSelEnd: Int,
        newSelStart: Int, newSelEnd: Int,
        candidatesStart: Int, candidatesEnd: Int
    ) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd)
        // If the user taps away from the composing text, reset the composing buffer
        if (currentComposingText.isNotEmpty() && (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
            currentComposingText.clear()
            updateSuggestions(emptyList())
            currentInputConnection?.finishComposingText()
        }
    }

    private fun handleKeyLongClick(keyData: KeyData, keyView: View) {
        if (keyData.longPressOptions.isEmpty()) return
        
        performHapticFeedback(keyboardView, HapticFeedbackConstants.LONG_PRESS)
        
        val popupView = layoutInflater.inflate(R.layout.popup_key_options, null) as LinearLayout
        
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDark = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        val textColor = if (isDark) Color.WHITE else Color.parseColor("#E0E0E0")
        
        val bgDrawable = android.graphics.drawable.GradientDrawable().apply {
            setColor(if (isDark) Color.parseColor("#4A4D51") else Color.parseColor("#202124"))
            cornerRadius = 24f
            setStroke(2, if (isDark) Color.parseColor("#5A5D61") else Color.parseColor("#3C4043"))
        }
        popupView.background = bgDrawable

        for (option in keyData.longPressOptions) {
            val btn = TextView(this).apply {
                text = option
                textSize = if (option == "Clear All") 20f else 28f
                setPadding(36, 24, 36, 24)
                setTextColor(textColor)
                typeface = if (option.any { it in '\u0600'..'\u06FF' }) keyboardView.getUrduTypeface() else android.graphics.Typeface.DEFAULT
                val typedArray = obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground))
                background = typedArray.getDrawable(0)
                typedArray.recycle()
                
                setOnClickListener {
                    if (option == "Clear All") {
                        currentComposingText.clear()
                        currentInputConnection?.deleteSurroundingText(10000, 10000)
                        updateSuggestions(emptyList())
                    } else {
                        val commitStr = option.replace("◌", "")
                        currentInputConnection?.commitText(commitStr, 1)
                    }
                    popupWindow?.dismiss()
                }
            }
            popupView.addView(btn)
        }

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false // Must be false so it doesn't steal focus from input connection!
        ).apply {
            elevation = 16f
            setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))
            isOutsideTouchable = true
            showAsDropDown(keyView, (keyView.width - popupView.measuredWidth) / 2, -keyView.height * 2 - 20)
        }
    }

    private fun handleKeyClick(keyData: KeyData) {
        val inputConnection = currentInputConnection ?: return
        
        performHapticFeedback(keyboardView, HapticFeedbackConstants.KEYBOARD_TAP)
        playKeyClickSound()
        
        when (keyData.code) {
            KeyboardLayouts.CODE_DELETE -> {
                val selectedText = inputConnection.getSelectedText(0)
                if (!selectedText.isNullOrEmpty()) {
                    // Fast sentence/selection deletion: If text is highlighted natively, delete the selection.
                    inputConnection.commitText("", 1)
                } else if (currentComposingText.isNotEmpty()) {
                    currentComposingText.deleteCharAt(currentComposingText.length - 1)
                    if (currentComposingText.isNotEmpty()) {
                        inputConnection.setComposingText(currentComposingText.toString(), 1)
                        querySuggestions()
                    } else {
                        inputConnection.commitText("", 1)
                        updateSuggestions(emptyList())
                    }
                } else {
                    inputConnection.deleteSurroundingText(1, 0)
                }
            }
            KeyboardLayouts.CODE_SHIFT -> {
                isShifted = !isShifted
                updateKeyboardLayout()
            }
            KeyboardLayouts.CODE_LANGUAGE_SWITCH -> {
                isEnglish = !isEnglish
                isShifted = false
                isEmoji = false
                isNumbers = false
                currentComposingText.clear()
                inputConnection.finishComposingText()
                updateSuggestions(emptyList())
                updateKeyboardLayout()
            }
            KeyboardLayouts.CODE_EMOJI -> {
                if (isEmoji) {
                    isEmoji = false
                    // Maintain previous language/number state
                } else {
                    isEmoji = true
                    isNumbers = false
                }
                updateKeyboardLayout()
            }
            KeyboardLayouts.CODE_VOICE -> {
                startVoiceInput()
            }
            KeyboardLayouts.CODE_SPACE -> {
                if (currentComposingText.isNotEmpty()) {
                    saveWordToDictionary(currentComposingText.toString())
                    // We clear composing text cleanly before writing out the raw input
                    inputConnection.setComposingText("", 0)
                    inputConnection.commitText(currentComposingText.toString() + " ", 1)
                    currentComposingText.clear()
                    updateSuggestions(emptyList())
                } else {
                    inputConnection.commitText(" ", 1)
                }
            }
            KeyboardLayouts.CODE_ENTER -> {
                if (currentComposingText.isNotEmpty()) {
                    saveWordToDictionary(currentComposingText.toString())
                    inputConnection.commitText(currentComposingText.toString(), 1)
                    currentComposingText.clear()
                    updateSuggestions(emptyList())
                }
                inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
            }
            KeyboardLayouts.CODE_NUMBERS -> {
                if (isNumbers) {
                    isNumbers = false
                    isEmoji = false
                } else {
                    isNumbers = true
                    isEmoji = false
                }
                updateKeyboardLayout()
            }
            KeyboardLayouts.CODE_EMOJI_SMILEYS,
            KeyboardLayouts.CODE_EMOJI_ANIMALS,
            KeyboardLayouts.CODE_EMOJI_FOOD,
            KeyboardLayouts.CODE_EMOJI_TRAVEL,
            KeyboardLayouts.CODE_EMOJI_OBJECTS,
            KeyboardLayouts.CODE_EMOJI_SYMBOLS -> {
                currentEmojiCategory = keyData.code
                updateKeyboardLayout()
            }
            else -> {
                if (keyData.code == 0) {
                    // It's an Emoji or sticker. Just commit it directly.
                    inputConnection.commitText(keyData.label, 1)
                    return
                }
                
                var textToCommit = if (isShifted && keyData.shiftLabel.isNotEmpty()) keyData.shiftLabel else keyData.label
                
                if (isEnglish && !isNumbers && currentComposingText.isEmpty() && textToCommit.length == 1 && textToCommit[0].isLetter()) {
                    val textBeforeCursor = inputConnection.getTextBeforeCursor(2, 0)
                    if (textBeforeCursor.isNullOrEmpty() || textBeforeCursor.toString().endsWith(". ") || textBeforeCursor.toString().endsWith("\n")) {
                        textToCommit = textToCommit.uppercase()
                    }
                }

                currentComposingText.append(textToCommit)
                inputConnection.setComposingText(currentComposingText.toString(), 1)
                querySuggestions()
                
                if (isShifted) {
                    isShifted = false
                    updateKeyboardLayout()
                }
            }
        }
    }

    private fun querySuggestions() {
        val prefix = currentComposingText.toString()
        if (prefix.isEmpty()) {
            updateSuggestions(emptyList())
            return
        }
        
        val lang = if (isEnglish) "en" else "ur"
        suggestionJob?.cancel() // Cancel previous in-flight database request
        suggestionJob = coroutineScope.launch {
            // Slight delay to debounce rapid typing
            kotlinx.coroutines.delay(50) 
            val suggestions = withContext(Dispatchers.IO) {
                wordDao.getSuggestions(prefix.lowercase(), lang)
            }
            updateSuggestions(suggestions.map { it.word })
        }
    }

    private fun updateSuggestions(words: List<String>) {
        if (::suggestion1.isInitialized) {
            suggestion1.text = words.getOrNull(1) ?: ""
            suggestion2.text = words.getOrNull(0) ?: "" // Top suggestion in the middle 
            suggestion3.text = words.getOrNull(2) ?: ""
        }
    }

    private fun commitSuggestion(word: String) {
        val inputConnection = currentInputConnection ?: return
        if (currentComposingText.isNotEmpty()) {
            // Safely clear the buggy prefix before committing suggestion
            inputConnection.setComposingText("", 0)
        }
        saveWordToDictionary(word, 3) // Boost frequency highly since user picked it
        inputConnection.commitText("$word ", 1)
        currentComposingText.clear()
        updateSuggestions(emptyList())
    }
    
    private fun saveWordToDictionary(word: String, boostValue: Int = 1) {
        if (word.isBlank() || isEmoji || isNumbers) return
        val lang = if (isEnglish) "en" else "ur"
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                wordDao.upsertWord(word.lowercase(), lang, boostValue)
            }
        }
    }

    private fun updateKeyboardLayout() {
        val layout = if (isEmoji) {
            KeyboardLayouts.buildEmojiLayout(currentEmojiCategory)
        } else if (isNumbers) {
            if (isEnglish) KeyboardLayouts.numberSymbolLayout else KeyboardLayouts.urduNumberSymbolLayout
        } else if (isEnglish) {
            KeyboardLayouts.englishQwerty
        } else {
            KeyboardLayouts.urduPhonetic
        }
        val requiresNastaliq = !isEnglish && !isEmoji && !isNumbers
        keyboardView.renderLayout(layout, isShifted, requiresNastaliq)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    // Voice Input Integration
    private fun setupVoiceRecognition() {
        try {
            if (SpeechRecognizer.isRecognitionAvailable(this)) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
                speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    if (::voicePromptText.isInitialized) {
                        voicePromptText.text = "Listening..."
                        voiceMicIcon.alpha = 1.0f
                    }
                }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {
                    if (::voiceMicIcon.isInitialized) {
                        val scale = 1.0f + (rmsdB / 10f).coerceIn(0f, 0.3f)
                        voiceMicIcon.scaleX = scale
                        voiceMicIcon.scaleY = scale
                    }
                }
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    if (::voicePromptText.isInitialized) {
                        voicePromptText.text = "Processing..."
                        voiceMicIcon.scaleX = 1f
                        voiceMicIcon.scaleY = 1f
                    }
                }
                override fun onError(error: Int) {
                    if (::voicePromptText.isInitialized) {
                        val msg = when(error) {
                            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> {
                                if (!isEnglish) {
                                    "Urdu voice needs Wi-Fi (No offline model)"
                                } else {
                                    "Network Error"
                                }
                            }
                            SpeechRecognizer.ERROR_NO_MATCH -> "Didn't catch that. Tap to try again."
                            else -> "Error ($error). Tap mic to try again."
                        }
                        voicePromptText.text = msg
                        voiceMicIcon.alpha = 0.5f
                    }
                }
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        val spokenText = matches[0]
                        currentInputConnection?.commitText("$spokenText ", 1)
                    }
                    stopVoiceInput()
                }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Gracefully handle lack of speech recognizer
        }
    }

    private fun startVoiceInput() {
        // Stop any active components
        currentInputConnection?.finishComposingText()
        currentComposingText.clear()
        updateSuggestions(emptyList())

        // Check hardware/OS support
        if (speechRecognizer == null) {
            currentInputConnection?.commitText("[Voice Not Supported]", 1)
            return
        }

        // Check Microphone Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            currentInputConnection?.commitText(" [Opening Settings for Mic Permission] ", 1)
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("request_mic_permission", true)
            }
            startActivity(intent)
            return
        }

        // Show Inline Voice UI, hide standard suggestions
        suggestionBarLayout.visibility = View.GONE
        inlineVoiceLayout.visibility = View.VISIBLE
        
        startListeningIntent()
    }

    private fun startListeningIntent() {
        try {
            voicePromptText.text = "Initializing..."
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                val lang = if (isEnglish) "en-US" else "ur-PK"
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            }
            speechRecognizer?.startListening(intent)
        } catch (e: SecurityException) {
            voicePromptText.text = "Permission Denied: Enable Mic!"
            currentInputConnection?.commitText("\n[Please enable Microphone permission for the keyboard in App Settings!]\n", 1)
        } catch (e: Exception) {
            voicePromptText.text = "Voice Error occurred."
            e.printStackTrace()
        }
    }

    private fun stopVoiceInput() {
        speechRecognizer?.stopListening()
        if (::inlineVoiceLayout.isInitialized) {
            inlineVoiceLayout.visibility = View.GONE
        }
        if (::suggestionBarLayout.isInitialized) {
            suggestionBarLayout.visibility = View.VISIBLE
        }
        if (::keyboardLayoutContainer.isInitialized) {
            keyboardLayoutContainer.visibility = View.VISIBLE
        }
    }
}
