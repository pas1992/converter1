package com.example.calorieconverter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.calorieconverter.Classes.*;

//класс формы "Меню"
public class MenuActivity extends Activity 
{
	private EditText searchEditText;	//поле поиска по названию
	private ListView listView;		//перечень продуктов в меню
	private Button buttonWeight;		//кнопка "Масса"
	private Button buttonDelete;		//кнопка "Удалить"
	private Button buttonConvert;		//кнопка "Конвертер"
	private Button buttonBack;		//кнопка "Назад"
	
	private Database helper;	//объект работы с базой
	
	private View currentRow;	//выбранная строка в перечне
	private int currentId;		//идентификатор текущего продукта в меню
	
	private ArrayList<Menu> menuItems;	//список продуктов в меню
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//создание формы
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_menu);
	    
	    //инициализация элементов управления
	    searchEditText = (EditText)findViewById(R.id.menu_searchEditText);
	    listView = (ListView)findViewById(R.id.menu_listView);
	    buttonWeight = (Button)findViewById(R.id.menu_buttonWeight);
	    buttonDelete = (Button)findViewById(R.id.menu_buttonDelete);
	    buttonConvert = (Button)findViewById(R.id.menu_buttonConvert);
	    buttonBack = (Button)findViewById(R.id.menu_buttonBack);
	    
	    helper = new Database(this);
	    
	    //получение списка продуктов в меню
	    menuItems = helper.getMenu();
	    
	    currentRow = null;
	    currentId = -1;
	    
	  //обработчик изменения текста в поле поиска по названию
	    searchEditText.addTextChangedListener(new TextWatcher() 
	    {
			@Override
			public void afterTextChanged(Editable arg0) { fillMenuTable(arg0.toString()); }

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
	    });
	    
	    //обработчик нажатия строки в перечне продуктов
	    listView.setOnItemClickListener(new OnItemClickListener() 
	    {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) 
			{
				//снятие выделения с предыдущей строки
				if (currentRow != null)
				{
					currentRow.setBackgroundColor(Color.BLACK);
				}
				
				//выделение текущей строки
				currentRow= view;
				currentRow.setBackgroundColor(Color.BLUE);
				currentId = Integer.parseInt(((HashMap<String, Object>)adapter.getItemAtPosition(position)).get("ID").toString());
				
				//установка доступности кнопок
				buttonWeight.setEnabled(true);
				buttonDelete.setEnabled(true);
			}
	    });
	    
	    //обработчик нажатия кнопки "Масса"
	    buttonWeight.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//отображение формы ввода массы
				final AlertDialog.Builder dialog = new AlertDialog.Builder(MenuActivity.this);
				dialog.setTitle("������� ����� �������� � �������");
				final EditText weightEditText = new EditText(MenuActivity.this);
				weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
				dialog.setView(weightEditText);
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						//задание массы продукта в меню
						try
						{
							helper.setProductWeight(currentId, Double.parseDouble(weightEditText.getText().toString()));
						}
						catch (Exception e)
						{
							
						}
						menuItems = helper.getMenu();
						fillMenuTable(searchEditText.getText().toString());
						dialog.cancel();
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
	    });
	    
	    //обработчик нажатия кнопки "Удалить"
	    buttonDelete.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//запрос подтверждения удаления
				AlertDialog.Builder dialog = new AlertDialog.Builder(MenuActivity.this);
    			dialog.setTitle("Удаление продукта");
    			dialog.setMessage("Вы действительно хотите удалить этот продукт?");
    			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
    			{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						//удаление продукта из меню
						helper.deleteFromMenu(currentId);
						currentRow = null;
						menuItems = helper.getMenu();
						fillMenuTable(searchEditText.getText().toString());
						
						//установка недосутпности кнопок
						buttonWeight.setEnabled(false);
		    			buttonDelete.setEnabled(false);
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
	    });
	    
	    //обработчик нажатия кнопки "Ковертер"
	    buttonConvert.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//расчет энергетической ценности меню
				double calories = 0.0;
				double proteins = 0.0;
				double fats = 0.0;
				double carbohydrates = 0.0;
				for (Menu item : menuItems)
				{
					Product product = helper.getProduct(item.Product);
					calories += product.Calories * item.Weight / 100.0;
					proteins += product.Proteins * item.Weight / 100.0;
					fats += product.Fats * item.Weight / 100.0;
					carbohydrates += product.Carbohydrates * item.Weight / 100.0;
				}
				
				//вывод энергетической ценности
				AlertDialog.Builder dialog = new AlertDialog.Builder(MenuActivity.this);
				dialog.setTitle("Энергетическая ценность");
				dialog.setMessage(String.format("Ккал:\t\t\t%.2f\nБелки:\t\t\t%.2f\nЖиры:\t\t\t%.2f\nУглеводы:\t%.2f\n", teins, fats, carbohydrates));
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
	    
	    //обработчик нажатия кнопки "Назад"
	    buttonBack.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//закрытие формы
				finish();
			}
	    });
	    
	    fillMenuTable("");
	}
	
	//отображение продуктов с искомым названием
	private void fillMenuTable(String query) 
	{
		final Handler handler = new Handler();
		final String _query = query; //строка поиска по названию
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{	
				//фильтрация списка
				ArrayList<HashMap<String, Object>> menu = new ArrayList<HashMap<String, Object>>();
			    for (int i = 0; i < menuItems.size(); i++) 
			    {
			    	String productName = helper.getProduct(menuItems.get(i).Product).Name;
			    	if (_query.isEmpty() || productName.toUpperCase().indexOf(_query.toUpperCase()) == 0)
			    	{
			    		HashMap<String, Object> hm = new HashMap<String, Object>();
			    		hm.put("ID", menuItems.get(i).Id);
			    		hm.put("NAME", productName);
			    		hm.put("WEIGHT", String.format(""Масса: %.2f г", nuItems.get(i).Weight));
			    		menu.add(hm);
		    		}
			    }
			    final ArrayList<HashMap<String, Object>> _menu = menu;
			    
			    ////отображение отфильтрованного списка продуктовотображение отфильтрованного списка продуктов
			    handler.post(new Runnable() 
			    {
					@Override
					public void run() 
					{
					    listView.setAdapter(new SimpleAdapter(MenuActivity.this, _menu, android.R.layout.simple_list_item_2, 
				    		new String[] { "NAME", "WEIGHT" },
				    		new int[] { android.R.id.text1, android.R.id.text2 }));
					}
			    	
			    }); 
			}
		});
		thread.start();
		
		//отображение отфильтрованного списка продуктов
		buttonWeight.setEnabled(false);
		buttonDelete.setEnabled(false);
	}
}
