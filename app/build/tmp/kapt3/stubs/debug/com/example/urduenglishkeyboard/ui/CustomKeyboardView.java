package com.example.urduenglishkeyboard.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\b\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ,\u0010\u001a\u001a\u00020\u000f2\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u001c0\u001c2\u0006\u0010\u001d\u001a\u00020\n2\b\b\u0002\u0010\u001e\u001a\u00020\nJ\u001a\u0010\u001f\u001a\u00020\u000f2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\rJ \u0010!\u001a\u00020\u000f2\u0018\u0010 \u001a\u0014\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u000f0\u0011J\u000e\u0010\"\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\nR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u000f\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/example/urduenglishkeyboard/ui/CustomKeyboardView;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "isDarkTheme", "", "isRepeating", "keyClickListener", "Lkotlin/Function1;", "Lcom/example/urduenglishkeyboard/keyboard/KeyData;", "", "keyLongClickListener", "Lkotlin/Function2;", "Landroid/widget/TextView;", "repeatHandler", "Landroid/os/Handler;", "repeatRunnable", "Ljava/lang/Runnable;", "repeatingKey", "urduTypeface", "Landroid/graphics/Typeface;", "renderLayout", "rows", "", "isShifted", "isUrdu", "setOnKeyClickListener", "listener", "setOnKeyLongClickListener", "setTheme", "isDark", "app_debug"})
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