package com.example.calorieconverter;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.calorieconverter.Classes.*;

//класс работы с базой данных
public class Database extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "Converter";	//название файла базы
	private static final int DATABASE_VERSION = 1;				//версия базы
	

	public Database(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//создание базы
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		//создание таблицы типов продуктов
		db.execSQL("CREATE TABLE ProductTypes (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL)");
		
		//создание таблицы продутов
		db.execSQL("CREATE TABLE Products (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL, Type INTEGER NOT NULL, Calories REAL NOT NULL, Proteins REAL NOT NULL, Fats REAL NOT NULL, Carbohydrates REAL NOT NULL)");
		
		//создание таблицы блюд
		db.execSQL("CREATE TABLE Dishes (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL, Products TEXT)");
		
		//создание таблицы меню
		db.execSQL("CREATE TABLE Menu (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Product INTEGER NOT NULL, Weight REAL NOT NULL)");
		
		//заполнение таблицы типов продутов
		String insertProductQuery = "INSERT INTO ProductTypes (Name) VALUES ('%s')";
		db.execSQL(String.format(insertProductQuery, "Бахчевые"));
		db.execSQL(String.format(insertProductQuery, "Бобовые"));
		db.execSQL(String.format(insertProductQuery, "Грибы"));
		db.execSQL(String.format(insertProductQuery, "Зерновые"));
		db.execSQL(String.format(insertProductQuery, "Изделия из муки"));
		db.execSQL(String.format(insertProductQuery, "Кондитерские изделия"));
		db.execSQL(String.format(insertProductQuery, "Крупы"));
		db.execSQL(String.format(insertProductQuery, "Молочные изделия"));
		db.execSQL(String.format(insertProductQuery, "Мясные продукты"));
		db.execSQL(String.format(insertProductQuery, "Напитки"));
		db.execSQL(String.format(insertProductQuery, "Овощи"));
		db.execSQL(String.format(insertProductQuery, "Орехи"));
		db.execSQL(String.format(insertProductQuery, "Птица"));
		db.execSQL(String.format(insertProductQuery, "Рыба и морепродукты"));
		db.execSQL(String.format(insertProductQuery, "Фрукты"));
		db.execSQL(String.format(insertProductQuery, "Ягоды"));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		

	}
	
	//получение списка типов продуктов из базы
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
	
	//добавление продукта в базу
	public void insertProduct(Product product) {
		String insertProductQuery = "INSERT INTO Products (Name, Type, Calories, Proteins, Fats, Carbohydrates) VALUES ('%s', %d, %f, %f, %f, %f)";
		this.getWritableDatabase().execSQL(String.format(insertProductQuery, product.Name, product.Type, product.Calories, product.Proteins, product.Fats, product.Carbohydrates));
	}
	
	//получение списка продуктов из базы
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
	
	//получение продукта из базы по идентификатору
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
	
	//редактирование продукта в базе
	public void updateProduct(Product product)
	{
		String insertProductQuery = "UPDATE Products SET Name = '%s', Type = %d, Calories = %f, Proteins = %f, Fats = %f, Carbohydrates = %f WHERE Id = %d";
		getWritableDatabase().execSQL(String.format(insertProductQuery, product.Name, product.Type, product.Calories, product.Proteins, product.Fats, product.Carbohydrates, product.Id));
	}
	
	//удаление продукта из базы по идентификатору
	public void deleteProduct(int id) 
	{
		getWritableDatabase().execSQL("DELETE FROM Products WHERE Id = " + String.format("%d", id));
	}
	
	//добавление блюда в базу
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
	
	//редактирование блюда в базе
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
	
	//получение списка блюд из базы
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
	
	//получение блюда из базы по идентификатору
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
	
	//удаление блюда из базы по идентификатору
	public void deleteDish(int id)
	{
		getWritableDatabase().execSQL("DELETE FROM Dishes WHERE Id = " + String.format("%d", id));
	}
	
	//добаление продукта в меню по идентификатору
	public void addToMenu(int productId)
	{
		getWritableDatabase().execSQL(String.format("INSERT INTO Menu (Product, Weight) VALUES (%d, 0.0)", productId));
	}
	
	//задание массы продукта в меню
	public void setProductWeight(int id, double weight)
	{
		getWritableDatabase().execSQL(String.format("UPDATE Menu SET Weight = %f WHERE Id = %d", weight, id));
	}
	
	//удаление продукта из меню
	public void deleteFromMenu(int id)
	{
		getWritableDatabase().execSQL("DELETE FROM Menu WHERE Id = " + String.format("%d", id));
	}
	
	//полючение меню из базы
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
