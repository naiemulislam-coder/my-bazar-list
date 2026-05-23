package com.naiemul.mybazarlist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BazarDao {

    @Insert
    void insert(BazarItem item);

    @Update
    void update(BazarItem item);

    @Delete
    void delete(BazarItem item);


    @Query("SELECT DISTINCT bazarName FROM bazar_table")
    LiveData<List<String>> getAllBazarNames();

    @Query("SELECT * FROM bazar_table WHERE bazarName = :name")
    LiveData<List<BazarItem>> getItemsByBazarName(String name);

    @Query("SELECT * FROM bazar_table ORDER BY id DESC")
    LiveData<List<BazarItem>> getAllItems();


    // @Query("SELECT * FROM bazar_table WHERE bazarName != 'Note' GROUP BY bazarName ORDER BY id DESC")
    //    LiveData<List<BazarItem>> getUniqueBazarLists();
    // বাজার লিস্টের জন্য যেখানে নাম 'Note' নয়

     @Query("SELECT * FROM bazar_table WHERE bazarName != 'Note' " +
            "AND id IN (SELECT MAX(id) FROM bazar_table GROUP BY bazarName) " +
            "ORDER BY id DESC")
     LiveData<List<BazarItem>> getUniqueBazarLists();

        // পুরো বাজার ডিলিট করার জন্য
        @Query("DELETE FROM bazar_table WHERE bazarName = :name")
        void deleteBazarByName(String name);

    @Query("SELECT SUBSTR(date, 4, 7) as month, SUM(CAST(price AS DOUBLE)) as totalCost, COUNT(DISTINCT bazarName) as bazarCount " +
            "FROM bazar_table GROUP BY month ORDER BY month ASC") // ORDER BY যোগ করা হয়েছে
    LiveData<List<MonthlySummary>> getMonthlySummary();

    @Query("SELECT COUNT(DISTINCT bazarName) FROM bazar_table")
    LiveData<Integer> getTotalBazarCountOverall();

    // শুধু নোটগুলো দেখার জন্য (যেখানে bazarName = 'Note')
    @Query("SELECT * FROM bazar_table WHERE bazarName = 'Note' ORDER BY id DESC")
    LiveData<List<BazarItem>> getOnlyNotes();

    // MonthlySummary POJO (এটি ডাও ফাইলের বাইরে বা নিচে রাখতে পারেন)
    // BazarDao এর ভেতরে এটি এভাবে লিখুন
    public static class MonthlySummary {
        public String month;
        public double totalCost;
        public int bazarCount;

        // রুম ডাটাবেসের জন্য একটি খালি কনস্ট্রাক্টর প্রয়োজন
        public MonthlySummary() {
        }
    }
}
