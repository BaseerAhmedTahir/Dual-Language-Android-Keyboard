package com.example.urduenglishkeyboard.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\bJ&\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u001c\u0010\u0011\u001a\u00020\u000f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0013J(\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\nH\u0097@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u0015"}, d2 = {"Lcom/example/urduenglishkeyboard/data/WordDao;", "", "getSuggestions", "", "Lcom/example/urduenglishkeyboard/data/WordEntity;", "prefix", "", "lang", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "incrementWordFrequency", "", "word", "amount", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertWord", "", "(Lcom/example/urduenglishkeyboard/data/WordEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertWords", "words", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertWord", "app_debug"})
@androidx.room.Dao()
public abstract interface WordDao {
    
    @androidx.room.Query(value = "SELECT * FROM words WHERE language = :lang AND word LIKE :prefix || \'%\' ORDER BY frequency DESC LIMIT 3")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSuggestions(@org.jetbrains.annotations.NotNull()
    java.lang.String prefix, @org.jetbrains.annotations.NotNull()
    java.lang.String lang, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.urduenglishkeyboard.data.WordEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertWord(@org.jetbrains.annotations.NotNull()
    com.example.urduenglishkeyboard.data.WordEntity word, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertWords(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.urduenglishkeyboard.data.WordEntity> words, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE words SET frequency = frequency + :amount WHERE word = :word AND language = :lang")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementWordFrequency(@org.jetbrains.annotations.NotNull()
    java.lang.String word, @org.jetbrains.annotations.NotNull()
    java.lang.String lang, int amount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertWord(@org.jetbrains.annotations.NotNull()
    java.lang.String word, @org.jetbrains.annotations.NotNull()
    java.lang.String lang, int amount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        @androidx.room.Transaction()
        @org.jetbrains.annotations.Nullable()
        public static java.lang.Object upsertWord(@org.jetbrains.annotations.NotNull()
        com.example.urduenglishkeyboard.data.WordDao $this, @org.jetbrains.annotations.NotNull()
        java.lang.String word, @org.jetbrains.annotations.NotNull()
        java.lang.String lang, int amount, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
    }
}