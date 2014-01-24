package com.example.calorieconverter;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.calorieconverter.Classes.*;

//����� ������ � ����� ������
public class Database extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "Converter";	//�������� ����� ����
	private static final int DATABASE_VERSION = 1;				//������ ����
	

	public Database(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//�������� ����
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		//�������� ������� ����� ���������
		db.execSQL("CREATE TABLE ProductTypes (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL)");
		
		//�������� ������� ��������
		db.execSQL("CREATE TABLE Products (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL, Type INTEGER NOT NULL, Calories REAL NOT NULL, Proteins REAL NOT NULL, Fats REAL NOT NULL, Carbohydrates REAL NOT NULL)");
		
		//�������� ������� ����
		db.execSQL("CREATE TABLE Dishes (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL, Products TEXT)");
		
		//�������� ������� ����
		db.execSQL("CREATE TABLE Menu (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Product INTEGER NOT NULL, Weight REAL NOT NULL)");
		
		//���������� ������� ����� ��������
		String insertProductQuery = "INSERT INTO ProductTypes (Name) VALUES ('%s')";
		db.execSQL(String.format(insertProductQuery, "��������"));
		db.execSQL(String.format(insertProductQuery, "�������"));
		db.execSQL(String.format(insertProductQuery, "�����"));
		db.execSQL(String.format(insertProductQuery, "��������"));
		db.execSQL(String.format(insertProductQuery, "������� �� ����"));
		db.execSQL(String.format(insertProductQuery, "������������ �������"));
		db.execSQL(String.format(insertProductQuery, "�����"));
		db.execSQL(String.format(insertProductQuery, "�������� �������"));
		db.execSQL(String.format(insertProductQuery, "������ ��������"));
		db.execSQL(String.format(insertProductQuery, "�������"));
		db.execSQL(String.format(insertProductQuery, "�����"));
		db.execSQL(String.format(insertProductQuery, "�����"));
		db.execSQL(String.format(insertProductQuery, "�����"));
		db.execSQL(String.format(insertProductQuery, "���� � ������������"));
		db.execSQL(String.format(insertProductQuery, "������"));
		db.execSQL(String.format(insertProductQuery, "�����"));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		

	}
	
	//��������� ������ ����� ��������� �� ����
	public ArrayList<ProductType> getProductTypes()
	{
		ArrayList<ProductType> types = new ArrayList<ProductType>();
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM ProductTypes", null);
		while (cursor.moveToNext()) {
			ProductType type = new ProductType();
			type.Id = cursor.getInt(cursor.getColumnIndex("Id"));
			type.Name = cursor.getString(cursor.getColumnIndex("Name"));
			types.add(type);
		}
		cursor = null;
		return types;
	}
	
	//���������� �������� � ����
	public void insertProduct(Product product) {
		String insertProductQuery = "INSERT INTO Products (Name, Type, Calories, Proteins, Fats, Carbohydrates) VALUES ('%s', %d, %f, %f, %f, %f)";
		this.getWritableDatabase().execSQL(String.format(insertProductQuery, product.Name, product.Type, product.Calories, product.Proteins, product.Fats, product.Carbohydrates));
	}
	
	//��������� ������ ��������� �� ����
	public ArrayList<Product> getProducts()
	{
		ArrayList<Product> products = new ArrayList<Product>();
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM Products", null);
		while (cursor.moveToNext()) 
		{
			Product product = new Product();
			product.Id = cursor.getInt(cursor.getColumnIndex("Id"));
			product.Name = cursor.getString(cursor.getColumnIndex("Name"));
			product.Type = cursor.getInt(cursor.getColumnIndex("Type"));
			product.Calories = cursor.getDouble(cursor.getColumnIndex("Calories"));
			product.Proteins = cursor.getDouble(cursor.getColumnIndex("Proteins"));
			product.Fats = cursor.getDouble(cursor.getColumnIndex("Fats"));
			product.Carbohydrates = cursor.getDouble(cursor.getColumnIndex("Carbohydrates"));
			products.add(product);
		}
		return products;
	}
	
	//��������� �������� �� ���� �� ��������������
	public Product getProduct(int id)
	{
		Cursor cursor = getReadableDatabase().rawQuery(String.format("SELECT * FROM Products WHERE Id = %d", id), null);
		if (cursor.moveToNext())
		{
			Product product = new Product();
			product.Id = cursor.getInt(cursor.getColumnIndex("Id"));
			product.Name = cursor.getString(cursor.getColumnIndex("Name"));
			product.Type = cursor.getInt(cursor.getColumnIndex("Type"));
			product.Calories = cursor.getInt(cursor.getColumnIndex("Calories"));
			product.Proteins = cursor.getDouble(cursor.getColumnIndex("Proteins"));
			product.Fats = cursor.getDouble(cursor.getColumnIndex("Fats"));
			product.Carbohydrates = cursor.getDouble(cursor.getColumnIndex("Carbohydrates"));
			return product;
		}
		return null;
	}
	
	//�������������� �������� � ����
	public void updateProduct(Product product)
	{
		String insertProductQuery = "UPDATE Products SET Name = '%s', Type = %d, Calories = %f, Proteins = %f, Fats = %f, Carbohydrates = %f WHERE Id = %d";
		getWritableDatabase().execSQL(String.format(insertProductQuery, product.Name, product.Type, product.Calories, product.Proteins, product.Fats, product.Carbohydrates, product.Id));
	}
	
	//�������� �������� �� ���� �� ��������������
	public void deleteProduct(int id) 
	{
		getWritableDatabase().execSQL("DELETE FROM Products WHERE Id = " + String.format("%d", id));
	}
	
	//���������� ����� � ����
	public void insertDish(Dish dish)
	{
		String insertDishQuery = "INSERT INTO Dishes (Name, Products) VALUES ('%s', '%s')";
		String products = "";
		for (Product p : dish.Products)
		{
			products += p.Id + ",";
		}
		if (!products.isEmpty())
		{
			StringBuilder sb = new StringBuilder(products);
			sb.deleteCharAt(products.length() - 1);
			products = sb.toString();
		}
		getWritableDatabase().execSQL(String.format(insertDishQuery, dish.Name, products));
	}
	
	//�������������� ����� � ����
	public void updateDish(Dish dish)
	{
		String updateDishQuery = "UPDATE Dishes SET Name = '%s', Products = '%s' WHERE Id = %d";
		String products = "";
		for (Product p : dish.Products)
		{
			products += p.Id + ",";
		}
		if (!products.isEmpty())
		{
			StringBuilder sb = new StringBuilder(products);
			sb.deleteCharAt(products.length() - 1);
			products = sb.toString();
		}
		getWritableDatabase().execSQL(String.format(updateDishQuery, dish.Name, products, dish.Id));
	}
	
	//��������� ������ ���� �� ����
	public ArrayList<Dish> getDishes()
	{
		ArrayList<Dish> dishes = new ArrayList<Dish>();
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM Dishes", null);
		while (cursor.moveToNext())
		{
			Dish dish = new Dish();
			dish.Id = cursor.getInt(cursor.getColumnIndex("Id"));
			dish.Name = cursor.getString(cursor.getColumnIndex("Name"));
			dish.Products = new ArrayList<Product>();
			String products = cursor.getString(cursor.getColumnIndex("Products"));
			try
			{
				for (String idProduct : products.split(","))
				{
					dish.Products.add(getProduct(Integer.parseInt(idProduct)));
				}
			}
			catch (Exception e)
			{
			
			}
			dishes.add(dish);
		}
		return dishes;
	}
	
	//��������� ����� �� ���� �� ��������������
	public Dish getDish(int id)
	{
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM Dishes", null);
		if (cursor.moveToNext())
		{
			Dish dish = new Dish();
			dish.Id = cursor.getInt(cursor.getColumnIndex("Id"));
			dish.Name = cursor.getString(cursor.getColumnIndex("Name"));
			dish.Products = new ArrayList<Product>();
			String products = cursor.getString(cursor.getColumnIndex("Products"));
			try
			{
				for (String idProduct : products.split(","))
				{
					dish.Products.add(getProduct(Integer.parseInt(idProduct)));
				}
			}
			catch (Exception e)
			{
				
			}
			return dish;
		}
		return null;
	}
	
	//�������� ����� �� ���� �� ��������������
	public void deleteDish(int id)
	{
		getWritableDatabase().execSQL("DELETE FROM Dishes WHERE Id = " + String.format("%d", id));
	}
	
	//��������� �������� � ���� �� ��������������
	public void addToMenu(int productId)
	{
		getWritableDatabase().execSQL(String.format("INSERT INTO Menu (Product, Weight) VALUES (%d, 0.0)", productId));
	}
	
	//������� ����� �������� � ����
	public void setProductWeight(int id, double weight)
	{
		getWritableDatabase().execSQL(String.format("UPDATE Menu SET Weight = %f WHERE Id = %d", weight, id));
	}
	
	//�������� �������� �� ����
	public void deleteFromMenu(int id)
	{
		getWritableDatabase().execSQL("DELETE FROM Menu WHERE Id = " + String.format("%d", id));
	}
	
	//��������� ���� �� ����
	public ArrayList<Menu> getMenu()
	{
		ArrayList<Menu> menu = new ArrayList<Menu>();
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM Menu", null);
		while (cursor.moveToNext())
		{
			Menu item = new Menu();
			item.Id = cursor.getInt(cursor.getColumnIndex("Id"));
			item.Product = cursor.getInt(cursor.getColumnIndex("Product"));
			item.Weight = cursor.getDouble(cursor.getColumnIndex("Weight"));
			menu.add(item);
		}
		return menu;
	}
}
