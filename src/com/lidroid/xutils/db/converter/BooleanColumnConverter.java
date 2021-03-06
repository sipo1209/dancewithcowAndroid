/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import android.text.TextUtils;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ 
/*    */ public class BooleanColumnConverter
/*    */   implements ColumnConverter<Boolean>
/*    */ {
/*    */   public Boolean getFieldValue(Cursor cursor, int index)
/*    */   {
/* 15 */     return cursor.isNull(index) ? null : Boolean.valueOf(cursor.getInt(index) == 1);
/*    */   }
/*    */ 
/*    */   public Boolean getFieldValue(String fieldStringValue)
/*    */   {
/* 20 */     if (TextUtils.isEmpty(fieldStringValue)) return null;
/* 21 */     return Boolean.valueOf(fieldStringValue.length() == 1 ? "1".equals(fieldStringValue) : Boolean.valueOf(fieldStringValue).booleanValue());
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(Boolean fieldValue)
/*    */   {
/* 26 */     if (fieldValue == null) return null;
/* 27 */     return Integer.valueOf(fieldValue.booleanValue() ? 1 : 0);
/*    */   }
/*    */ 
/*    */   public ColumnDbType getColumnDbType()
/*    */   {
/* 32 */     return ColumnDbType.INTEGER;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.BooleanColumnConverter
 * JD-Core Version:    0.6.0
 */