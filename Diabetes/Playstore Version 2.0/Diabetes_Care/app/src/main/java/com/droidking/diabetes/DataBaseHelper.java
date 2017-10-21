package com.droidking.diabetes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nazmul on 4/11/16.
 */
public class DataBaseHelper {



    /** publisher Table Attribute*/
    public static final String PUBLISHER_ID ="Pub_ID";
    public static final String PUBLISHER_NAME ="Pub_name";
    public static final String PUB_ADDRESS ="Pub_Address";
    private static final String PUBLISHER_TABLE="publisher";

    /**author_group Table Attribute*/
    public static final String AUTHOR_ID ="Author_ID";
    public static final String GROUP_ID ="Author_Group_ID";
    private static final String GROUP_TABLE="author_group";

    /** books Table Attribute  */
    public static final String BOOK_ID ="Book_ID";
    public static final String  TITLE = "Title" ;
    public static final String AUTHOR_GROUP ="Author_Group_ID";
    public static final String PUB_ID = "Pub_ID";
    public static final String TOTAL_BOOK ="Total_Book";
    public static final String AVAILABLE = "Available";
    public static final String PRICE ="Price";
    private static final String BOOK_TABLE="books";

    /** author Table Attribute*/

    //public static final String AUTHOR_ID ="Author_ID";
    public static final String AUTHOR_FIRST_NAME ="Author_First_Name";
    public static final String AUTHOR_LAST_NAME ="Author_Last_Name";
    public static final String AUTHOR_ADDRESS="AU_Address";
    public static final String AUTHOR_TABLE="author";

    /** borrow_by Table Attribute*/
    //public static final String BOOK_ID ="Book_ID";
    public static final String MEMBER_ID ="Member_ID";
    public static final String DUE_DATE = "Due_Date";
    public static final String RETURN_DATE = "Return_Date";
    public static final String ISSUE= "Issue";
    public static final String BORROW_TABLE = "borrow_by";

    /** member Table Attribute*/
    //public static final String MEMBER_ID ="Member_ID";
    public static final String MEMBER_FIRST_NAME="Member_First_name";
    public static final String MEMBER_LAST_NAME="Member_Last_name";
    public static final String MEMBER_ADDRESS ="Member_address";
    public static final String MEMBER_TYPE="Member_type";
    public static final String MEMBER_DATE="Member_date";
    public static final String EXPIRED_DATE="Expired_date";
    public static final String MEMBER_TABLE="member";
    /**Database Name and Version*/
    private static final String DATABASE_NAME = "Diabetes";
    private static final String DATABASE_TABLE="publisher";
    private static final int  DATABASE_VERSION =1;
    public static final String KEY_NAME ="Pub_name";
    public static final String KEY_HOTNESS ="Pub_Address";
    /**Diabetes Database KEYS*/
    public static final String KEY_ROWID ="Row_ID";
    public static final String KEY_DATE ="Date";
    public static final String KEY_TIME ="Time";
    public static final String KEY_GLUCOSE_LEVEL ="Glucose_Level";
    public static final String KEY_UNIT ="Unit";
    public static final String KEY_STATUS ="Status";
    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;
    private static class DbHelper extends SQLiteOpenHelper{

        public DbHelper(Context context)
        {
            //Here the Data base Is Created with it's name
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        //Write SQL Code Here to create Table Column etc
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                             KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             KEY_DATE + " TEXT NOT NULL, " +
                             KEY_TIME + " TEXT NOT NULL, " +
                             KEY_GLUCOSE_LEVEL + " TEXT NOT NULL, " +
                             KEY_UNIT + " TEXT NOT NULL, " +
                             KEY_STATUS + " TEXT NOT NULL);"


             );


               /* db.execSQL("CREATE TABLE " + GROUP_TABLE + " (" +
                             GROUP_ID + " TEXT , " +
                             AUTHOR_ID + " TEXT );"

             );

            db.execSQL("CREATE TABLE " + BOOK_TABLE + " (" +
                            BOOK_ID + " TEXT , " +
                            TITLE + " TEXT , " +
                            AUTHOR_GROUP + " TEXT , " +
                            PUB_ID + " TEXT , " +
                            TOTAL_BOOK + " INTEGER, " +
                            AVAILABLE + " INTEGER , " +
                            PRICE + " INTEGER );"

            );
            */

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);

        }


    }


    //Constructor
    public DataBaseHelper(Context c){
        ourContext = c;
    }

    public DataBaseHelper open() throws SQLException{
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourHelper.close();
    }

    public long createEntry(String name, String hotness) {

        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,name);
        cv.put(KEY_HOTNESS,hotness);
        //Insert Content
       return ourDatabase.insert(DATABASE_TABLE,null,cv);


    }

    public void add_Data(String Date , String Time, String level, String Unit ,String Measured){

        /**INSERT CODE*/
        String query2 ="INSERT INTO "+DATABASE_TABLE+" ( "+KEY_DATE+" , "+KEY_TIME+" , "+KEY_GLUCOSE_LEVEL+" , "+KEY_UNIT+" , "+KEY_STATUS+" ) VALUES ( '"+Date+"' , '"+Time+"' , '"+level+"' , '"+Unit+"' , '"+Measured+"' );";

        ourDatabase.execSQL(query2);

    }

    public String getLastCheckData() {

        String ID ="1111";

        /** SELECT Code*/
        String query = "SELECT * FROM " +DATABASE_TABLE;

        /**UPDATE code*/
        //String query4 = "UPDATE "+BOOK_TABLE +" SET "+PRICE+ " = "+PRICE+" + 100 " + " WHERE  "+ BOOK_ID +" = '1111';";
       // ourDatabase.execSQL(query);

        //ourDatabase.rawQuery(update,null);
        Cursor c =ourDatabase.rawQuery(query,null);

        String result ="";
        // int iRow = c.getColumnIndex(KEY_ROWID);
        //int iName = c.getColumnIndex(KEY_NAME);
        int iglucoselevel = c.getColumnIndex(KEY_GLUCOSE_LEVEL);
        int iunit = c.getColumnIndex(KEY_UNIT);
        int idate = c.getColumnIndex(KEY_DATE);
        c.moveToLast();
        result = result + c.getString(iglucoselevel) +" "+c.getString(iunit) + " "+ c.getString(idate);
        // for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        //  {


        // }


        return result;

    }
    public String getDayDifference(String pastDay)
    {
        String Days ;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            Date pastDate = sdf.parse(pastDay);
            Date currentDate = new Date();
            String date = sdf.format(currentDate);

            // tv.setText( "" + String.format("%02d",Days); //
            if (!pastDate.after(currentDate)) {
                long diff = currentDate.getTime()
                        - pastDate.getTime();

                long days = diff / (24 * 60 * 60 * 1000);

                Days =  String.format("%02d", days);
                return Days;
            }
        }catch(ParseException e)
        {
            e.printStackTrace();

        }
        return null;

    }

    public Cursor getRecyclerData_table(){

        /** How to use  where Clause */
        //String query = "SELECT * FROM " + AUTHOR_TABLE +" WHERE " + AUTHOR_TABLE+"."+AUTHOR_ID+ " = '"+author_group+"';";

        //String query = "SELECT * FROM " + PUBLISHER_TABLE +" NATURAL JOIN "+BOOK_TABLE +" WHERE " + BOOK_TABLE+"."+BOOK_ID+ " = '"+book_id+"';";
        // String query = "SELECT * FROM " + DATABASE_TABLE +" WHERE " + KEY_STATUS +" = "+ ;

        String query = "SELECT * FROM " + DATABASE_TABLE  ;

        Cursor c =ourDatabase.rawQuery(query,null);
        return c;

    }
    public Cursor getGraphData_table(int when){

        /** How to use  where Clause */
        String when_measered[] = {  "All",
                "Before breakfast",
                "After breakfast",
                "Before lunch",
                "After lunch",
                "Before dinner",
                "After dinner",
                "Bedtime",
                "Other"
        };
        String query ;
        //String query = "SELECT * FROM " + AUTHOR_TABLE +" WHERE " + AUTHOR_TABLE+"."+AUTHOR_ID+ " = '"+author_group+"';";

        //String query = "SELECT * FROM " + PUBLISHER_TABLE +" NATURAL JOIN "+BOOK_TABLE +" WHERE " + BOOK_TABLE+"."+BOOK_ID+ " = '"+book_id+"';";

        if(when ==-1 || when == 0)
        {
            query = "SELECT * FROM " + DATABASE_TABLE;
        }else {
            query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_STATUS + " = '" + when_measered[when] + "';";
        }
        //String query = "SELECT * FROM " + DATABASE_TABLE  ;

        Cursor c =ourDatabase.rawQuery(query,null);
        return c;

    }
    public Cursor getRecyclerData(){
        /*String [] columns = new String[]{ PUBLISHER_ID, PUBLISHER_NAME, PUB_ADDRESS};
        Cursor c = ourDatabase.query(PUBLISHER_TABLE,columns,null,null,null,null,null);
        */
         String [] columns = new String[]{ BOOK_ID, TITLE,AUTHOR_GROUP,PUB_ID,TOTAL_BOOK,AVAILABLE,PRICE};
        Cursor c = ourDatabase.query(BOOK_TABLE, columns, null, null, null, null, null);


        return c;

    }
    public String getAllAuthorName(String author_group){

        /** How to use  where Clause */
        //String query = "SELECT * FROM " + AUTHOR_TABLE +" WHERE " + AUTHOR_TABLE+"."+AUTHOR_ID+ " = '"+author_group+"';";

        String query = "SELECT * FROM " + AUTHOR_TABLE +" NATURAL JOIN "+GROUP_TABLE +" WHERE " + GROUP_TABLE+"."+AUTHOR_GROUP+ " = '"+author_group+"';";
        Cursor c =ourDatabase.rawQuery(query,null);
        String result = "";
        int iFirstName = c.getColumnIndex(AUTHOR_FIRST_NAME);
        int iLastName = c.getColumnIndex(AUTHOR_LAST_NAME);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result = result + c.getString(iFirstName)+" "+c.getString(iLastName)+ ", ";
        }


        return result;

    }
    public String getPublicationName(String book_id){

        /** How to use  where Clause */
        //String query = "SELECT * FROM " + AUTHOR_TABLE +" WHERE " + AUTHOR_TABLE+"."+AUTHOR_ID+ " = '"+author_group+"';";

        String query = "SELECT * FROM " + PUBLISHER_TABLE +" NATURAL JOIN "+BOOK_TABLE +" WHERE " + BOOK_TABLE+"."+BOOK_ID+ " = '"+book_id+"';";
        Cursor c =ourDatabase.rawQuery(query, null);
        String result = "";
        int iFirstName = c.getColumnIndex(PUBLISHER_NAME);
        //int iLastName = c.getColumnIndex(AUTHOR_LAST_NAME);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result = result + c.getString(iFirstName) ;
        }


        return result;

    }
    public String getBorrow_By_table(){

        /** How to use  where Clause */
        //String query = "SELECT * FROM " + AUTHOR_TABLE +" WHERE " + AUTHOR_TABLE+"."+AUTHOR_ID+ " = '"+author_group+"';";

        //String query = "SELECT * FROM " + PUBLISHER_TABLE +" NATURAL JOIN "+BOOK_TABLE +" WHERE " + BOOK_TABLE+"."+BOOK_ID+ " = '"+book_id+"';";

        String query = "SELECT * FROM " + BORROW_TABLE ;

        Cursor c =ourDatabase.rawQuery(query,null);
        String result = "";
        int iBook_ID = c.getColumnIndex(BOOK_ID);
        int iMember_ID = c.getColumnIndex(MEMBER_ID);
        int iDue_Date = c.getColumnIndex(DUE_DATE);
        int iReturn_Date = c.getColumnIndex(RETURN_DATE);
        int iIssue = c.getColumnIndex(ISSUE);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result = result + c.getString(iBook_ID) + " "+ c.getString(iMember_ID)+" "+
            c.getString(iDue_Date)+" "+c.getString(iReturn_Date)+ " "+ c.getString(iIssue)+"\n" ;
        }


        return result;

    }
    public void  borrow_book(String book_id ,String member_id){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date currentDate = new Date();
        String date = sdf.format(currentDate);

        /**INSERT CODE*/
        String query2 ="INSERT INTO "+BORROW_TABLE+" ("+BOOK_ID+" , "+MEMBER_ID+","+DUE_DATE+","+ISSUE+" ) VALUES ( '"+book_id+"' , '"+member_id+"' , '"+date+"' , 'Borrowed' );";

        ourDatabase.execSQL(query2);

        String query4 = "UPDATE "+BOOK_TABLE +" SET "+AVAILABLE+ " = "+AVAILABLE+" - 1 " +  " WHERE  "+ BOOK_ID +" = '1111';";

        ourDatabase.execSQL(query4);


    }
    public void add_Book(String Book_ID, String Title ,String Author_Group_ID, String Pub_ID, String total,String price ){
        /**INSERT CODE*/
        String query ="INSERT INTO "+BOOK_TABLE+" ( "+BOOK_ID+" , "+TITLE+" , "+AUTHOR_GROUP+" , "+PUB_ID+" , "+TOTAL_BOOK+" , "+AVAILABLE+" , "+PRICE+" ) VALUES ( '"+Book_ID+"' , '"+Title+"' , '"+Author_Group_ID+"' , '"+ Pub_ID+"' , '"+total+"' , '"+total+"' , '"+ price+"' );";


        ourDatabase.execSQL(query);


    }
    public String  return_book(String book_id ,String member_id){


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date currentDate = new Date();
        String date = sdf.format(currentDate);

        String Difference = getDayDifference("1-Apr-2016") ;

        /**Update*/
        String query4 = "UPDATE "+BORROW_TABLE +" SET "+ RETURN_DATE+" = '"+date+"' , "+ISSUE+ " = 'Returned' "+ " WHERE  "+ BOOK_ID +" = '"+book_id+"' AND "+MEMBER_ID+" = '"+member_id+"' ;";
        try {
        ourDatabase.execSQL(query4);
            String query = "UPDATE "+BOOK_TABLE +" SET "+AVAILABLE+ " = "+AVAILABLE+" + 1 " +  " WHERE  "+ BOOK_ID +" = '" + book_id+"';";

            ourDatabase.execSQL(query);

        } catch (Exception e) {   //SQLException e
            e.printStackTrace();


        }





       /* String query = "SELECT * FROM " + BORROW_TABLE +" WHERE " +BOOK_ID+ " = '"+book_id+"' AND "+MEMBER_ID +" = '"+member_id+"' AND "+ISSUE + " 'Borrowed' " +" ;";
        Cursor c =ourDatabase.rawQuery(query,null);

        String result = "";
        int iDue_Date = c.getColumnIndex(DUE_DATE);
        //int iLastName = c.getColumnIndex(AUTHOR_LAST_NAME);

        if(c != null){
            c.moveToFirst();
            result  = c.getString(iDue_Date); //Select First Column




            Difference = getDayDifference(result);

            return Difference;


        }
        else
        {
            return "Error";
        }


        */

            return Difference;

    }
    public String  FineCalculate(String book_id ,String member_id){


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date currentDate = new Date();
        String date = sdf.format(currentDate);

        String Difference ;



       String query = "SELECT * FROM " + BORROW_TABLE +" WHERE " +BOOK_ID+ " = '"+book_id+"' AND "+MEMBER_ID +" = '"+member_id+"' AND "+ISSUE + " = 'Borrowed' ;";
        Cursor c =ourDatabase.rawQuery(query,null);

        String result = "";
        int iDue_Date = c.getColumnIndex(DUE_DATE);
        //int iLastName = c.getColumnIndex(AUTHOR_LAST_NAME);

        if(c != null){
            c.moveToFirst();
            result  = c.getString(iDue_Date); //Select First Column


            Difference = getDayDifference(result);


            return Difference;


        }
        else
        {
            return "Error";
        }


    }
    public String  BookStatus(String book_id ,String member_id){


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date currentDate = new Date();
        String date = sdf.format(currentDate);


        String query = "SELECT * FROM " + BORROW_TABLE +" WHERE " +BOOK_ID+ " = '"+book_id+"' AND "+MEMBER_ID +" = '"+member_id+"' ;";
        Cursor c =ourDatabase.rawQuery(query,null);

        String result = "";
        int ibookIssue = c.getColumnIndex(ISSUE);
        //int iLastName = c.getColumnIndex(AUTHOR_LAST_NAME);

        //if(c!=null && c.getCount()>0)
        if(c != null && c.getCount()>0){
            c.moveToFirst();
            result  = c.getString(ibookIssue); //Select First Column
            return result;

        }
        return "Not Found";

    }
    public Cursor getRecyclerBorrow_By_table(){

        /** How to use  where Clause */
        //String query = "SELECT * FROM " + AUTHOR_TABLE +" WHERE " + AUTHOR_TABLE+"."+AUTHOR_ID+ " = '"+author_group+"';";

        //String query = "SELECT * FROM " + PUBLISHER_TABLE +" NATURAL JOIN "+BOOK_TABLE +" WHERE " + BOOK_TABLE+"."+BOOK_ID+ " = '"+book_id+"';";

        String query = "SELECT * FROM " + BORROW_TABLE +" WHERE "+ ISSUE+ " = 'Borrowed' " ;

        Cursor c =ourDatabase.rawQuery(query,null);
        String result = "";
        //int iBook_ID = c.getColumnIndex(BOOK_ID);
        //int iMember_ID = c.getColumnIndex(MEMBER_ID);
        //int iDue_Date = c.getColumnIndex(DUE_DATE);
        //int iReturn_Date = c.getColumnIndex(RETURN_DATE);
        //int iIssue = c.getColumnIndex(ISSUE);

       // for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        //{
          //  result = result + c.getString(iBook_ID) + " "+ c.getString(iMember_ID)+" "+
        //            c.getString(iDue_Date)+" "+c.getString(iReturn_Date)+ " "+ c.getString(iIssue)+"\n" ;
       // }


        return c;

    }
    public String getMemberName(String member_id){

        /** How to use  where Clause */
        //String query = "SELECT * FROM " + AUTHOR_TABLE +" WHERE " + AUTHOR_TABLE+"."+AUTHOR_ID+ " = '"+author_group+"';";

        String query = "SELECT * FROM " + MEMBER_TABLE  +" WHERE " +MEMBER_ID+ " = '"+member_id+"';";
        Cursor c =ourDatabase.rawQuery(query,null);
        String result = "";
        int iFirstName = c.getColumnIndex(MEMBER_FIRST_NAME);
        int iLastName = c.getColumnIndex(MEMBER_LAST_NAME);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result = result + c.getString(iFirstName)+" "+c.getString(iLastName)+ ", ";
        }


        return result;

    }
    public String getBookTitle(String book_id){

        /** How to use  where Clause */
        //String query = "SELECT * FROM " + AUTHOR_TABLE +" WHERE " + AUTHOR_TABLE+"."+AUTHOR_ID+ " = '"+author_group+"';";

        String query = "SELECT * FROM " + BOOK_TABLE +" WHERE " +BOOK_ID+ " = '"+book_id+"';";
        Cursor c =ourDatabase.rawQuery(query,null);
        String result = "";
        int iFirstName = c.getColumnIndex(TITLE);
        //int iLastName = c.getColumnIndex(AUTHOR_LAST_NAME);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result = result + c.getString(iFirstName)+ ". ";
        }


        return result;

    }
    public String getMember_table(){

        /** How to use  where Clause */
        //String query = "SELECT * FROM " + AUTHOR_TABLE +" WHERE " + AUTHOR_TABLE+"."+AUTHOR_ID+ " = '"+author_group+"';";

        //String query = "SELECT * FROM " + PUBLISHER_TABLE +" NATURAL JOIN "+BOOK_TABLE +" WHERE " + BOOK_TABLE+"."+BOOK_ID+ " = '"+book_id+"';";

        String query = "SELECT * FROM " + MEMBER_TABLE ;

        Cursor c =ourDatabase.rawQuery(query,null);
        String result = "";
        int iMember_ID = c.getColumnIndex(MEMBER_ID);
        int iMember_First_name = c.getColumnIndex(MEMBER_FIRST_NAME);
        int iMember_Last_name = c.getColumnIndex(MEMBER_LAST_NAME);
        int iMember_Address = c.getColumnIndex(MEMBER_ADDRESS);
        int iMember_Type = c.getColumnIndex(MEMBER_TYPE);
        int iMember_Date = c.getColumnIndex(MEMBER_DATE);
        int iExpire_Date = c.getColumnIndex(EXPIRED_DATE);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result = result + c.getString(iMember_ID) + " "+ c.getString(iMember_First_name)+" "+
                    c.getString(iMember_Last_name)+" "+c.getString(iMember_Address)+ " "+c.getString(iMember_Type)+ " "+ c.getString(iMember_Date)+c.getString(iExpire_Date)+"\n" ;
        }


        return result;

    }
    public String getName(long l) {

        String [] columns = new String[]{ KEY_ROWID, KEY_NAME, KEY_HOTNESS};

        //Cursor c = ourDatabase.query(Table,columns,selection,selectioArgs,groupby,having,orderBy);
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);

        if(c != null){
            c.moveToFirst();
            String name = c.getString(1); //Select First Column
            return name;

        }

        return null;
    }
    public void delete_member(String member_id)
    {
        /**DELETE row code*/
        String query= "DELETE FROM "+MEMBER_TABLE+" WHERE "+ MEMBER_ID+" = " +"'"+member_id+"';";

        ourDatabase.execSQL(query);
    }
    public String getHotness(long l) {

        String [] columns = new String[]{ KEY_ROWID, KEY_NAME, KEY_HOTNESS};

        //Cursor c = ourDatabase.query(Table,columns,selection,selectioArgs,groupby,having,orderBy);
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);

        if(c != null){
            c.moveToFirst();
            String hotness = c.getString(2); //Select First Column
            return hotness;

        }
        return null;

    }
    public void updateEntry(long lRow, String mName, String mHotness) {

        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(KEY_NAME, mName);
        cvUpdate.put(KEY_HOTNESS,mHotness);
        ourDatabase.update(DATABASE_TABLE,cvUpdate,KEY_ROWID + "=" + lRow,null);
    }
    public void deleteEntry(long lRow1) {
        //ourDatabase.delete(DATABASE_TABLE,whereClause, whereArgs);
        ourDatabase.delete(DATABASE_TABLE,KEY_ROWID+ "=" +lRow1 , null);
    }
}
