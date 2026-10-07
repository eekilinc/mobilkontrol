package com.mobilkontrol.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDao_Impl implements AppDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DailyUsageEntity> __insertionAdapterOfDailyUsageEntity;

  private final EntityInsertionAdapter<UsageHistoryEntity> __insertionAdapterOfUsageHistoryEntity;

  public AppDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDailyUsageEntity = new EntityInsertionAdapter<DailyUsageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `daily_usage` (`date`,`usedMinutes`,`bonusMinutes`,`manualLock`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailyUsageEntity entity) {
        statement.bindString(1, entity.getDate());
        statement.bindLong(2, entity.getUsedMinutes());
        statement.bindLong(3, entity.getBonusMinutes());
        final int _tmp = entity.getManualLock() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__insertionAdapterOfUsageHistoryEntity = new EntityInsertionAdapter<UsageHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `usage_history` (`date`,`totalUsageMinutes`,`dailyLimitMinutes`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UsageHistoryEntity entity) {
        statement.bindString(1, entity.getDate());
        statement.bindLong(2, entity.getTotalUsageMinutes());
        statement.bindLong(3, entity.getDailyLimitMinutes());
      }
    };
  }

  @Override
  public Object upsertDailyUsage(final DailyUsageEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailyUsageEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertHistory(final UsageHistoryEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUsageHistoryEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDailyUsage(final String date,
      final Continuation<? super DailyUsageEntity> $completion) {
    final String _sql = "SELECT * FROM daily_usage WHERE date = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DailyUsageEntity>() {
      @Override
      @Nullable
      public DailyUsageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfUsedMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "usedMinutes");
          final int _cursorIndexOfBonusMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "bonusMinutes");
          final int _cursorIndexOfManualLock = CursorUtil.getColumnIndexOrThrow(_cursor, "manualLock");
          final DailyUsageEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpUsedMinutes;
            _tmpUsedMinutes = _cursor.getInt(_cursorIndexOfUsedMinutes);
            final int _tmpBonusMinutes;
            _tmpBonusMinutes = _cursor.getInt(_cursorIndexOfBonusMinutes);
            final boolean _tmpManualLock;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfManualLock);
            _tmpManualLock = _tmp != 0;
            _result = new DailyUsageEntity(_tmpDate,_tmpUsedMinutes,_tmpBonusMinutes,_tmpManualLock);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<DailyUsageEntity> observeDailyUsage(final String date) {
    final String _sql = "SELECT * FROM daily_usage WHERE date = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_usage"}, new Callable<DailyUsageEntity>() {
      @Override
      @Nullable
      public DailyUsageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfUsedMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "usedMinutes");
          final int _cursorIndexOfBonusMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "bonusMinutes");
          final int _cursorIndexOfManualLock = CursorUtil.getColumnIndexOrThrow(_cursor, "manualLock");
          final DailyUsageEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpUsedMinutes;
            _tmpUsedMinutes = _cursor.getInt(_cursorIndexOfUsedMinutes);
            final int _tmpBonusMinutes;
            _tmpBonusMinutes = _cursor.getInt(_cursorIndexOfBonusMinutes);
            final boolean _tmpManualLock;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfManualLock);
            _tmpManualLock = _tmp != 0;
            _result = new DailyUsageEntity(_tmpDate,_tmpUsedMinutes,_tmpBonusMinutes,_tmpManualLock);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<UsageHistoryEntity>> observeHistory(final String start) {
    final String _sql = "SELECT * FROM usage_history WHERE date >= ? ORDER BY date ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, start);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"usage_history"}, new Callable<List<UsageHistoryEntity>>() {
      @Override
      @NonNull
      public List<UsageHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTotalUsageMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUsageMinutes");
          final int _cursorIndexOfDailyLimitMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLimitMinutes");
          final List<UsageHistoryEntity> _result = new ArrayList<UsageHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UsageHistoryEntity _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpTotalUsageMinutes;
            _tmpTotalUsageMinutes = _cursor.getInt(_cursorIndexOfTotalUsageMinutes);
            final int _tmpDailyLimitMinutes;
            _tmpDailyLimitMinutes = _cursor.getInt(_cursorIndexOfDailyLimitMinutes);
            _item = new UsageHistoryEntity(_tmpDate,_tmpTotalUsageMinutes,_tmpDailyLimitMinutes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<UsageHistoryEntity>> observeLast7() {
    final String _sql = "SELECT * FROM usage_history ORDER BY date DESC LIMIT 7";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"usage_history"}, new Callable<List<UsageHistoryEntity>>() {
      @Override
      @NonNull
      public List<UsageHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTotalUsageMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUsageMinutes");
          final int _cursorIndexOfDailyLimitMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLimitMinutes");
          final List<UsageHistoryEntity> _result = new ArrayList<UsageHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UsageHistoryEntity _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpTotalUsageMinutes;
            _tmpTotalUsageMinutes = _cursor.getInt(_cursorIndexOfTotalUsageMinutes);
            final int _tmpDailyLimitMinutes;
            _tmpDailyLimitMinutes = _cursor.getInt(_cursorIndexOfDailyLimitMinutes);
            _item = new UsageHistoryEntity(_tmpDate,_tmpTotalUsageMinutes,_tmpDailyLimitMinutes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
