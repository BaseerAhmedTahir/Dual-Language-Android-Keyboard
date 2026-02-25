package com.example.urduenglishkeyboard.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WordDao_Impl implements WordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WordEntity> __insertionAdapterOfWordEntity;

  private final EntityInsertionAdapter<WordEntity> __insertionAdapterOfWordEntity_1;

  private final SharedSQLiteStatement __preparedStmtOfIncrementWordFrequency;

  public WordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWordEntity = new EntityInsertionAdapter<WordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `words` (`id`,`word`,`frequency`,`language`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WordEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getWord() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getWord());
        }
        statement.bindLong(3, entity.getFrequency());
        if (entity.getLanguage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLanguage());
        }
      }
    };
    this.__insertionAdapterOfWordEntity_1 = new EntityInsertionAdapter<WordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `words` (`id`,`word`,`frequency`,`language`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WordEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getWord() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getWord());
        }
        statement.bindLong(3, entity.getFrequency());
        if (entity.getLanguage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLanguage());
        }
      }
    };
    this.__preparedStmtOfIncrementWordFrequency = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE words SET frequency = frequency + ? WHERE word = ? AND language = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertWord(final WordEntity word, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWordEntity.insert(word);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertWords(final List<WordEntity> words,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWordEntity_1.insert(words);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertWord(final String word, final String lang, final int amount,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> WordDao.DefaultImpls.upsertWord(WordDao_Impl.this, word, lang, amount, __cont), $completion);
  }

  @Override
  public Object incrementWordFrequency(final String word, final String lang, final int amount,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementWordFrequency.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, amount);
        _argIndex = 2;
        if (word == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, word);
        }
        _argIndex = 3;
        if (lang == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, lang);
        }
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementWordFrequency.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getSuggestions(final String prefix, final String lang,
      final Continuation<? super List<WordEntity>> $completion) {
    final String _sql = "SELECT * FROM words WHERE language = ? AND word LIKE ? || '%' ORDER BY frequency DESC LIMIT 3";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (lang == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, lang);
    }
    _argIndex = 2;
    if (prefix == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, prefix);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WordEntity>>() {
      @Override
      @NonNull
      public List<WordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final List<WordEntity> _result = new ArrayList<WordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WordEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWord;
            if (_cursor.isNull(_cursorIndexOfWord)) {
              _tmpWord = null;
            } else {
              _tmpWord = _cursor.getString(_cursorIndexOfWord);
            }
            final int _tmpFrequency;
            _tmpFrequency = _cursor.getInt(_cursorIndexOfFrequency);
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            _item = new WordEntity(_tmpId,_tmpWord,_tmpFrequency,_tmpLanguage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
