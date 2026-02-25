package com.example.urduenglishkeyboard.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0006\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ \u0010\u001b\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\u001e\u001a\u00020\u000bH\u0002J,\u0010\u001f\u001a\u00020\u00102\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0!0!2\u0006\u0010\u001d\u001a\u00020\u000b2\b\b\u0002\u0010\u001e\u001a\u00020\u000bJ\u001a\u0010\"\u001a\u00020\u00102\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000eJ \u0010$\u001a\u00020\u00102\u0018\u0010#\u001a\u0014\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00100\u0012J\u000e\u0010%\u001a\u00020\u00102\u0006\u0010&\u001a\u00020\u000bR\u000e\u0010\t\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u0011\u001a\u0016\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/example/urduenglishkeyboard/ui/CustomKeyboardView;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "currentLayoutHashCode", "isDarkTheme", "", "isRepeating", "keyClickListener", "Lkotlin/Function1;", "Lcom/example/urduenglishkeyboard/keyboard/KeyData;", "", "keyLongClickListener", "Lkotlin/Function2;", "Landroid/widget/TextView;", "repeatHandler", "Landroid/os/Handler;", "repeatRunnable", "Ljava/lang/Runnable;", "repeatingKey", "urduTypeface", "Landroid/graphics/Typeface;", "createKeyView", "key", "isShifted", "isUrdu", "renderLayout", "rows", "", "setOnKeyClickListener", "listener", "setOnKeyLongClickListener", "setTheme", "isDark", "app_debug"})
public final class CustomKeyboardView extends android.widget.LinearLayout {
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function1<? super com.example.urduenglishkeyboard.keyboard.KeyData, kotlin.Unit> keyClickListener;
    private boolean isDarkTheme = false;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function2<? super com.example.urduenglishkeyboard.keyboard.KeyData, ? super android.widget.TextView, kotlin.Unit> keyLongClickListener;
    @org.jetbrains.annotations.Nullable()
    private android.graphics.Typeface urduTypeface;
    private boolean isRepeating = false;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler repeatHandler = null;
    @org.jetbrains.annotations.Nullable()
    private com.example.urduenglishkeyboard.keyboard.KeyData repeatingKey;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Runnable repeatRunnable = null;
    private int currentLayoutHashCode = 0;
    
    @kotlin.jvm.JvmOverloads()
    public CustomKeyboardView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
    
    public final void setTheme(boolean isDark) {
    }
    
    public final void setOnKeyClickListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.urduenglishkeyboard.keyboard.KeyData, kotlin.Unit> listener) {
    }
    
    public final void setOnKeyLongClickListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.example.urduenglishkeyboard.keyboard.KeyData, ? super android.widget.TextView, kotlin.Unit> listener) {
    }
    
    public final void renderLayout(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends java.util.List<com.example.urduenglishkeyboard.keyboard.KeyData>> rows, boolean isShifted, boolean isUrdu) {
    }
    
    private final android.widget.TextView createKeyView(com.example.urduenglishkeyboard.keyboard.KeyData key, boolean isShifted, boolean isUrdu) {
        return null;
    }
    
    @kotlin.jvm.JvmOverloads()
    public CustomKeyboardView(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public CustomKeyboardView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
}