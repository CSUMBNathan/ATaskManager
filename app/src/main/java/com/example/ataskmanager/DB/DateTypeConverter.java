package com.example.ataskmanager.DB;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateTypeConverter {
    @TypeConverter
    public long convertDateToLong(Date date){
        return date.getTime();
    }
    @TypeConverter
    public Date convertLongToDate(Long epoch){
        return new Date(epoch);
    }
}
