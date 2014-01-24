package com.example.calorieconverter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.calorieconverter.Classes.Dish;
import com.example.calorieconverter.Classes.Product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//класс формы "Блюда"
public class DishesActivity extends Activity implements OnClickListener
{
	private Database helper;	//объект работы с базой
	
	private LinearLayout currentRow;	//выбранная строка в таблице
	private ArrayList<Dish> dishItems;	//список блюд
	
	private ListView dishesListView;	//таблица блюд
	private EditText searchEditText;	//поле поиска по названию
	private Button buttonToMenu;		//кнопка "в меню"
	private Button buttonUpdate;		//кнопка "Редкатировать"
	private Button buttonDelete;		//кнопка "Удалить"
	private Button buttonBack;			//кнопка "Назад"

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//создание формы
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dishes);
	
	    //инициализация элементов управления
	    dishesListView = (ListView)findViewById(R.id.dishes_dishesListView);
	    searchEditText = (EditText)findViewById(R.id.dishes_searchEditText);
	    buttonToMenu = (Button)findViewById(R.id.dishes_buttonToMenu);
	    buttonUpdate = (Button)findViewById(R.id.dishes_buttonUpdate);
	    buttonDelete = (Button)findViewById(R.id.dishes_buttonDelete);
	    buttonBack = (Button)findViewById(R.id.dishes_buttonBack);
	    
	    //установка недоступности кнопок
	    buttonToMenu.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonUpdate.setEnabled(false);
		
		helper = new Database(this);
		currentRow = null;
		
		//получение списка блюд из базы
		dishItems = new Database(this).getDishes();
	    
		//обработчик нажатия строки в таблице
	    dishesListView.setOnItemClickListener(new OnItemClickListener() 
	    {
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	    	{
	    		//снятие выделения предыдущей строки
	    		if (currentRow != null) 
	    		{
	    			((TextView)currentRow.getChildAt(0)).setBackgroundColor(Color.WHITE);
	    			((TextView)currentRow.getChildAt(1)).setBackgroundColor(Color.WHITE);
	    			((TextView)currentRow.getChildAt(2)).setBackgroundColor(Color.WHITE);
	    			((TextView)currentRow.getChildAt(3)).setBackgroundColor(Color.WHITE);
	    			((TextView)currentRow.getChildAt(4)).setBackgroundColor(Color.WHITE);
	    			((TextView)currentRow.getChildAt(5)).setBackgroundColor(Color.WHITE);
	    			((TextView)currentRow.getChildAt(6)).setBackgroundColor(Color.WHITE);
	    		}
	    		
	    		//выделение текущей строки
	    		currentRow = (LinearLayout)view;
	    		((TextView)currentRow.getChildAt(0)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(1)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(2)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(3)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(4)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(5)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(6)).setBackgroundColor(Color.BLUE);
    			
    			//установка доступности кнопок
    			buttonToMenu.setEnabled(true);
    			buttonDelete.setEnabled(true);
    			buttonUpdate.setEnabled(true);
	    	}
	    });
	    
	    //обработчик нажатия кнопки "в меню"
	    buttonToMenu.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//добавление в меню продуктов из состава блюда
				for (Product product : helper.getDish(Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText())).Products)
				{
					helper.addToMenu(product.Id);
				}
				
				//вывод сообщения об успешном добавлении продуктов в меню
				AlertDialog.Builder dialog = new AlertDialog.Builder(DishesActivity.this);
				dialog.setMessage("Продукты блюда успешно добавлены в меню");
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
    			{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.cancel();
					}
    			});
				dialog.show();
			}
	    });
	    
	    buttonUpdate.setOnClickListener(this);
	    
	    //обработчик нажатия кнопки "удалить"
	    buttonDelete.setOnClickListener(new OnClickListener() 
	    {
	    	@Override
	    	public void onClick(View v) 
	    	{
	    		if (currentRow != null) 
	    		{
	    			//запрос подтверждения удаления
	    			AlertDialog.Builder dialog = new AlertDialog.Builder(DishesActivity.this);
	    			dialog.setTitle("Удаление блюда");
	    			dialog.setMessage("Вы действительно хотите удалить это блюдо?");
	    			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
	    			{
    					@Override
    					public void onClick(DialogInterface dialog, int which) 
    					{
    						//удаление блюда
    						helper.deleteDish(Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText()));
    						
    						//обновление списка блюд
    						currentRow = null;
    						dishItems = helper.getDishes();
    						fillDishTable(searchEditText.getText().toString());
    						
    						//установка недоступности кнопок
    						buttonToMenu.setEnabled(false);
    		    			buttonDelete.setEnabled(false);
    		    			buttonUpdate.setEnabled(false);
    					}
	    			});
	    			dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() 
	    			{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.cancel();
						}
					});
	    			dialog.show();
	    		}
	    	}
	    });
	    
	    //обработчик нажатия кнопки "Назад"
	    buttonBack.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{ 
				//закртыие формы
				finish(); 
			}
	    });
	    
	  //обработчик изменения текста в поле поиска по названию
	    searchEditText.addTextChangedListener(new TextWatcher() 
	    {
			@Override
			public void afterTextChanged(Editable arg0) { fillDishTable(arg0.toString()); }

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
	    });
	    
	    fillDishTable("");
	}
	
	//отображение блюд по названию
	private void fillDishTable(String query) 
	{
		final Handler handler = new Handler();
		final String _query = query;	//строка поиска по названию
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				//выполнение в параллельном потоке
				
				//фильтрация списка блюд
				ArrayList<HashMap<String, Object>> dishes = new ArrayList<HashMap<String, Object>>();
			    for (int i = 0; i < dishItems.size(); i++) 
			    {
			    	if (_query.isEmpty() || dishItems.get(i).Name.toUpperCase().indexOf(_query.toUpperCase()) == 0)
			    	{
			    		HashMap<String, Object> hm = new HashMap<String, Object>();
			    		hm.put("ID", dishItems.get(i).Id);
			    		hm.put("NAME", dishItems.get(i).Name);
			    		
			    		//расчет энергетической ценности блюда
			    		double calories = 0.0;
			    		double proteins = 0.0;
			    		double fats = 0.0;
			    		double carbohydrates = 0.0;
			    		for (Product p : dishItems.get(i).Products)
			    		{
			    			calories += p.Calories;
			    			proteins += p.Proteins;
			    			fats += p.Fats;
			    			carbohydrates += p.Carbohydrates;
			    		}
			    		
			    		hm.put("CALORIES", calories);
			    		hm.put("PROTEINS", proteins);
			    		hm.put("FATS", fats);
			    		hm.put("CARBOHYDRATES", carbohydrates);
			    		dishes.add(hm);
		    		}
			    }
			    final ArrayList<HashMap<String, Object>> _dishes = dishes;
			    
			    //отображение отфильтрованного списка блюд
			    handler.post(new Runnable() 
			    {
					@Override
					public void run() 
					{
					    dishesListView.setAdapter(new SimpleAdapter(DishesActivity.this, _dishes, R.layout.product_row, 
				    		new String[] { "ID", "NAME", "CALORIES", "PROTEINS", "FATS", "CARBOHYDRATES" },
				    		new int[] { R.id.ProductColumn0, R.id.ProductColumn1, R.id.ProductColumn3, R.id.ProductColumn4, R.id.ProductColumn5, R.id.ProductColumn6 }));
					}
			    	
			    }); 
			}
		});
		thread.start();
		
		//установка недоступности кнопко
		buttonToMenu.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonUpdate.setEnabled(false);
	}

	//обработчик нажатия кнопки "Редактировать"
	@Override
	public void onClick(View arg0) 
	{
		//передача форме "Новое блюдо" идентификатора выбранного блюда
		int id = Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText());
		Intent intent = new Intent(DishesActivity.this, NewDishActivity.class);
		intent.putExtra("Id", id);
		startActivityForResult(intent, 0);
	}
	
	//обновление данных о блюде после редактирования
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		dishItems = helper.getDishes();
		Dish dish = helper.getDish(Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText()));
		((TextView)currentRow.getChildAt(1)).setText(dish.Name);
		double calories = 0.0;
		double proteins = 0.0;
		double fats = 0.0;
		double carbohydrates = 0.0;
		
		for (Product p : dish.Products)
		{
			calories += p.Calories;
			proteins += p.Proteins;
			fats += p.Fats;
			carbohydrates += p.Carbohydrates;
		}
		((TextView)currentRow.getChildAt(3)).setText(Double.toString(calories));
		((TextView)currentRow.getChildAt(4)).setText(Double.toString(proteins));
		((TextView)currentRow.getChildAt(5)).setText(Double.toString(fats));
		((TextView)currentRow.getChildAt(6)).setText(Double.toString(carbohydrates));
	}

}
