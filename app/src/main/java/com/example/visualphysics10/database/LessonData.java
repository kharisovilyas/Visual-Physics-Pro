package com.example.visualphysics10.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//TODO: Database written using ORM ROOM
// using annotations we declare fields, this class contains only fields and equals and hashCode

@Entity
public class LessonData {
    //key for code generate
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "surname")
    public String surname;
    @ColumnInfo(name = "image")
    public byte[] image;
    @ColumnInfo(name = "emailTeacher")
    public String emailTeacher;
    @ColumnInfo(name = "progressL1")
    public int progressL1;
    @ColumnInfo(name = "progressL2")
    public int progressL2;
    @ColumnInfo(name = "progressL3")
    public int progressL4;
    @ColumnInfo(name = "progressL5")
    public int progressL5;
}
