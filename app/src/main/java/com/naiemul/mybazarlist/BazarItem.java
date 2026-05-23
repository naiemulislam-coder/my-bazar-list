package com.naiemul.mybazarlist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bazar_table")
public class BazarItem {

   @PrimaryKey(autoGenerate = true)
    public int id;

   public String itemName;
   public String bazarName;
    public String quantity;  // পরিমাণ (যেমন: ২ কেজি)
    public String price;
   public String date;
   public String time;
   public boolean isChecked;


   public BazarItem (String itemName, String bazarName, String quantity, String price, String date, String time, boolean isChecked){
       this.itemName=itemName;
       this.bazarName=bazarName;
       this.quantity=quantity;
       this.price=price;
       this.date=date;
       this.time=time;
       this.isChecked=isChecked;

   }



}
