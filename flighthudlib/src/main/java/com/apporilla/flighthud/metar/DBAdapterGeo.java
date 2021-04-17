package com.apporilla.flighthud.metar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/*

apx distance:

((( ABS(lat-xlat)*ABS(lat-xlat) +ABS(long-xlong)*ABS(long-xlong))/(ABS(lat-xlat)+ABS(long-xlong)))+(ABS(lat-xlat)+ABS(long-xlong)))/2

((( ABS(lat-3400)*ABS(lat-3400) +ABS(long- -11834)*ABS(long- -11834))/(ABS(lat-3400)+ABS(long- -11834)))+(ABS(lat-3400)+ABS(long- -11834)))/2
 
sqlite> select _id,((( ABS(lat-3400)*ABS(lat-3400) +ABS(long- -11834)*ABS(long- -11834))/(ABS(lat-3400)+ABS(long- -11834)))+(ABS(lat-3400)+ABS(long- -11834)))/2 from geo order by 2 limit 3;
KSMO|7
KVNY|14
KHHR|48 
 
*/

public class DBAdapterGeo {
	
	private static final String DATABASE_NAME = "metargeo.db";
	private String DATABASE_PATH;	

	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "DBGeo";
	
	private Context context;
	private SQLiteDatabase db;
	private DatabaseHelper DBHelper;	
	private SQLiteStatement dbstatement;	
	
	public DBAdapterGeo(Context context)
	{
		this.context = context;
		DATABASE_PATH = "/data/data/" + context.getPackageName() +"/databases/";		
		DBHelper = new DatabaseHelper(context);
	}	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
	
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		}
			
	
	}
	
	public DBAdapterGeo open()
	{
		String myPath = DATABASE_PATH + DATABASE_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		return this;
	}
	
	public void close()
	{		
		db.close();
		DBHelper.close();
	}	
	
	public void createDataBase() throws IOException
	{
		boolean dbExists = checkDataBase();
		if (!dbExists)
		{
			//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
			DBHelper.getReadableDatabase();
			
			try 
			{
				copyDataBase();
			} catch(IOException e)
			{
				throw new Error("Error copying database");
			}
			DBHelper.close();
		}
	}
	
	private boolean checkDataBase()
	{
		 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DATABASE_PATH + DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;			
	}
	
	private void copyDataBase() throws IOException
	{
    	//Open your local db as the input stream
		
    	InputStream myInput = context.getAssets().open(DATABASE_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DATABASE_PATH + DATABASE_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();			
	}			


	
	public ArrayList<GeoData> getNearby(final double longitude, final double latitude, final int count)
	{
		ArrayList<GeoData> nearby = new ArrayList<GeoData>();
		
		final int lng = new Double( longitude*10000).intValue();
		final int lat = new Double( latitude*10000).intValue();
		Log.d(TAG, "geting nearby: " + lng + "," + lat);	
		
		final String q = "select _id,lat,long,((( ABS(lat- " + lat + ")*ABS(lat- " + lat + ") +ABS(long- " + lng +")*ABS(long- " + lng + "))/(ABS(lat- " + lat + ")+ABS(long- " + lng +")))+(ABS(lat- " + lat + ")+ABS(long- " + lng + ")))/2 from geo order by 4 limit " + count + " ";
		Cursor cursor = db.rawQuery(q, null);
		
		if (cursor != null)
		{
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount(); i++)
			{
				final String retId=cursor.getString(0);
				final double retLat=0.0001*cursor.getDouble(1);
				final double retLong=0.0001*cursor.getDouble(2);
				nearby.add(new GeoData(retId,retLat,retLong));
				cursor.moveToNext();
			}
				
			cursor.close();
		}
		
		return nearby;
	}

	
	
}
