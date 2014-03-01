package com.example.calorieconverter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.calorieconverter.Classes.Dish;
import com.example.calorieconverter.Classes.Product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.view.View.OnClickListener;

//класс формы "Новое блюдо"
public class NewDishActivity extends Activity implements OnClickListener
{
	private EditText nameEditText;			//поле "Название"
	private Button buttonAdd;				//кнопка "Добавить продукт"
	private ListView productsListView;		//таблица продуктов
	private Button buttonOK;				//кнопка "OK"
	private Button buttonCancel;			//кнопка "Отмена"
	
	private Database helper;	//объект работы с базой
	
	private Dish dish;	//текущее блюдо
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//создание формы
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_newdish);
	    
	    helper = new Database(this);

	    //инициализация элементов управления
	    nameEditText = (EditText)findViewById(R.id.newDish_nameEditText);
	    buttonAdd = (Button)findViewById(R.id.newDish_buttonAdd);
	    productsListView = (ListView)findViewById(R.id.newDish_productsListView);
	    buttonOK = (Button)findViewById(R.id.newDish_buttonOK);
	    buttonCancel = (Button)findViewById(R.id.newDish_buttonCancel);
	    
	    //получение идентификатора текущего блюда
	    try
	    {
	    	dish = helper.getDish(getIntent().getExtras().getInt("Id"));
	    	nameEditText.setText(dish.Name);
	    	fillProductTable();
	    	//форма открыта для редактирования существующего блюда
	    }
	    catch (Exception e)
	    {
	    	dish = new Dish();
	    	//форма открыта для добавления нового блюда
	    }
	    
	    buttonAdd.setOnClickListener(this);
	    
	    //обработчик нажатия кнопки "OK"
	    buttonOK.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				dish.Name = nameEditText.getText().toString();
				if (dish.Id != -1)
				{
					//редактирование существеющего блюда
					helper.updateDish(dish);
				}
				else
				{
					//добавление нового блюда
					helper.insertDish(dish);
				}
				finish();
			}
	    });
	    
	    //обработчик нажатия кнопки ""Отмена
	    buttonCancel.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v)
			{
				//закрытие формы
				finish();
			}
	    });
	}

	//обработчик нажатия кнопки "Добавить продукт"
	@Override
	public void onClick(View v) 
	{
		//открытие формы "Продукты" в режиме добавления продукта в блюдо
		Intent intent = new Intent(NewDishActivity.this, ProductsActivity.class);
		intent.putExtra("Mode", true);
		startActivityForResult(intent, 1);
	}

	//добавление продукта в блюдо
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (data != null)
		{
			Product product = helper.getProduct(Integer.parseInt(data.getStringExtra("Id")));
			dish.Products.add(product);
			fillProductTable();
		}
	}
	
	//отображение списка продуктов в составе блюда
	private void fillProductTable() 
	{
		ArrayList<HashMap<String, Object>> products = new ArrayList<HashMap<String, Object>>();
	    
	    for (int i = 0; i < dish.Products.size(); i++) 
	    {
    		HashMap<String, Object> hm = new HashMap<String, Object>();
    		hm.put("ID", dish.Products.get(i).Id);
    		hm.put("NAME", dish.Products.get(i).Name);
    		hm.put("TYPE", dish.Products.get(i).Type);
    		hm.put("CALORIES", dish.Products.get(i).Calories);
    		hm.put("PROTEINS", dish.Products.get(i).Proteins);
    		hm.put("FATS", dish.Products.get(i).Fats);
    		hm.put("CARBOHYDRATES", dish.Products.get(i).Carbohydrates);
    		products.add(hm);
	    }
			    
	    productsListView.setAdapter(new SimpleAdapter(NewDishActivity.this, products, R.layout.product_row, 
    		new String[] { "ID", "NAME", "TYPE", "CALORIES", "PROTEINS", "FATS", "CARBOHYDRATES" },
    		new int[] { R.id.ProductColumn0, R.id.ProductColumn1, R.id.ProductColumn2, R.id.ProductColumn3, R.id.ProductColumn4, R.id.ProductColumn5, R.id.ProductColumn6 }));
	}
}
