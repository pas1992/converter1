package com.example.calorieconverter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//класс главной формы
public class MainActivity extends Activity 
{
	private Button buttonNewProduct;	//кнопка "Новый продукт"
	private Button buttonProducts;		//кнопка "Продукты"
	private Button buttonNewDish;		//кнопка "Новое блюдо"
	private Button buttonDishes;		//кнопка "Блюда"
	private Button buttonMenu;			//кнопка "Меню"
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//создание формы
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//инициализация элементов управления
		buttonNewProduct = (Button)findViewById(R.id.main_buttonNewProduct);
		buttonProducts = (Button)findViewById(R.id.main_buttonProducts);
		buttonNewDish = (Button)findViewById(R.id.main_buttonNewDish);
		buttonDishes = (Button)findViewById(R.id.main_buttonDishes);
		buttonMenu = (Button)findViewById(R.id.main_buttonMenu);
		
		//обработчик нажатия кнопки "Новый продукт"
		buttonNewProduct.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//переход на форму "Новый продукт"
				Intent intent = new Intent(MainActivity.this, NewProductActivity.class);
				startActivity(intent);
			}
		});
		
		//обработчик нажатия кнопки "Продукты"
		buttonProducts.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//переход на форму "Продукты"
				Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
				startActivity(intent);
			}
		});
		
		//обработчик нажатия кнопки "Новое блюдо"
		buttonNewDish.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//переход на форму "Новое блюдо"
				Intent intent = new Intent(MainActivity.this, NewDishActivity.class);
				startActivity(intent);
			}
		});
		
		//обработчик нажатия кнопки "Блюда"
		buttonDishes.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//переход на форму "Блюда"
				Intent intent = new Intent(MainActivity.this, DishesActivity.class);
				startActivity(intent);
			}
		});
		
		//обработчик нажатия кнопки "Меню"
		buttonMenu.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//переход на форму "Меню"
				Intent intent = new Intent(MainActivity.this, MenuActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
