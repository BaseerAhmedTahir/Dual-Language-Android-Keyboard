package com.example.urduenglishkeyboard;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010 \n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\"H\u0002J\u0018\u0010#\u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\"2\u0006\u0010$\u001a\u00020\u001cH\u0002J\b\u0010%\u001a\u00020\u001aH\u0016J\b\u0010&\u001a\u00020\u001cH\u0016J\b\u0010\'\u001a\u00020\u001aH\u0016J\b\u0010(\u001a\u00020\u001aH\u0016J\b\u0010)\u001a\u00020\u001aH\u0002J\b\u0010*\u001a\u00020\u001aH\u0002J\b\u0010+\u001a\u00020\u001aH\u0002J\b\u0010,\u001a\u00020\u001aH\u0002J\u0016\u0010-\u001a\u00020\u001a2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u001f0/H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00060\u0006j\u0002`\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/example/urduenglishkeyboard/UrduEnglishKeyboardService;", "Landroid/inputmethodservice/InputMethodService;", "()V", "coroutineScope", "Lkotlinx/coroutines/CoroutineScope;", "currentComposingText", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "isEmoji", "", "isEnglish", "isNumbers", "isShifted", "keyboardView", "Lcom/example/urduenglishkeyboard/ui/CustomKeyboardView;", "popupWindow", "Landroid/widget/PopupWindow;", "serviceJob", "Lkotlinx/coroutines/CompletableJob;", "suggestion1", "Landroid/widget/TextView;", "suggestion2", "suggestion3", "wordDao", "Lcom/example/urduenglishkeyboard/data/WordDao;", "applyTheme", "", "view", "Landroid/view/View;", "commitSuggestion", "word", "", "handleKeyClick", "keyData", "Lcom/example/urduenglishkeyboard/keyboard/KeyData;", "handleKeyLongClick", "keyView", "onCreate", "onCreateInputView", "onDestroy", "onFinishInput", "querySuggestions", "seedDatabaseMockData", "setupSuggestionListeners", "updateKeyboardLayout", "updateSuggestions", "words", "", "app_debug"})
public final class UrduEnglishKeyboardService extends android.inputmethodservice.InputMethodService {
    private com.example.urduenglishkeyboard.ui.CustomKeyboardView keyboardView;
    private android.widget.TextView suggestion1;
    private android.widget.TextView suggestion2;
    private android.widget.TextView suggestion3;
    private boolean isEnglish = true;
    private boolean isShifted = false;
    private boolean isEmoji = false;
    private boolean isNumbers = false;
    @org.jetbrains.annotations.Nullable()
    private android.widget.PopupWindow popupWindow;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CompletableJob serviceJob = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope coroutineScope = null;
    private com.example.urduenglishkeyboard.data.WordDao wordDao;
    @org.jetbrains.annotations.NotNull()
    private java.lang.StringBuilder currentComposingText;
    
    public UrduEnglishKeyboardService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateInputView() {
        return null;
    }
    
    private final void applyTheme(android.view.View view) {
    }
    
    private final void setupSuggestionListeners() {
    }
    
    private final void seedDatabaseMockData() {
    }
    
    @java.lang.Override()
    public void onFinishInput() {
    }
    
    private final void handleKeyLongClick(com.example.urduenglishkeyboard.keyboard.KeyData keyData, android.view.View keyView) {
    }
    
    private final void handleKeyClick(com.example.urduenglishkeyboard.keyboard.KeyData keyData) {
    }
    
    private final void querySuggestions() {
    }
    
    private final void updateSuggestions(java.util.List<java.lang.String> words) {
    }
    
    private final void commitSuggestion(java.lang.String word) {
    }
    
    private final void updateKeyboardLayout() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
}