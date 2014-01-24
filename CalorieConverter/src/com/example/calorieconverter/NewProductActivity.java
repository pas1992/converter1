package com.example.calorieconverter;

import com.example.calorieconverter.Classes.Product;
import com.example.calorieconverter.Classes.ProductType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View.OnClickListener;

//форма "Новый продукт"
public class NewProductActivity extends Activity
{
	private int currentId;	//идентификатор текущего продукта
	
	private Spinner productTypesSpinner;		//выпадающий список "Типы продуктов"
	private EditText nameEditText;			//поле "Название продукта"
	private EditText caloriesEditText;		//поле "Ккал"
	private EditText proteinsEditText;		//поле "Белки"
	private EditText fatsEditText;			//поле "Жиры"
	private EditText carbohydratesEditText;		//поле "Углеводы"
	private Button buttonOK;			//кнопка "OK"
	private Button buttonCancel;			//кнопка "Отмена"
	
	private Database helper;	//объект работы с базой
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//создание формы
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_newproduct);
	    
	    //получение идентификатора текущего продукта
	    try
	    {
	    	currentId = getIntent().getExtras().getInt("Id");
	    	//форма открыта для редактирования существующего продукта
	    }
	    catch (Exception e)
	    {
	    	currentId = -1;
	    	//орма открыта для добавления нового продукта
	    }
	    
	    helper = new Database(this);
	    
	    //инициализация элементов управления
	    productTypesSpinner = (Spinner)findViewById(R.id.newProduct_productTypesSinner);
	    nameEditText = (EditText)findViewById(R.id.newProduct_nameEditText);
	    caloriesEditText = (EditText)findViewById(R.id.newProduct_caloriesEditText);
	    proteinsEditText = (EditText)findViewById(R.id.newProduct_proteinsEditText);
	    fatsEditText = (EditText)findViewById(R.id.newProduct_fatsEditText);
	    carbohydratesEditText = (EditText)findViewById(R.id.newProduct_carbohydratesEditText);
	    buttonOK = (Button)findViewById(R.id.newProduct_buttonOK);
	    buttonCancel = (Button)findViewById(R.id.newProduct_buttonCancel);
	    
	    //заполнения выпадающего списка типами продуктов из базы
	    ArrayAdapter<ProductType> spinnerArrayAdapter = new ArrayAdapter<ProductType>(this, android.R.layout.simple_spinner_item, helper.getProductTypes());
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productTypesSpinner.setAdapter(spinnerArrayAdapter);
		
		if (currentId != -1)
		{
			//отображение параметров редактируемого продукта
			Product product = helper.getProduct(currentId);
			nameEditText.setText(product.Name);
			caloriesEditText.setText(Double.toString(product.Calories));
			proteinsEditText.setText(Double.toString(product.Proteins));
			fatsEditText.setText(Double.toString(product.Fats));
			carbohydratesEditText.setText(Double.toString(product.Carbohydrates));
		}
	    
		//обработчик нажатия кнопки "OK"
	    buttonOK.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//проверка введенныъ данных
				if (nameEditText.getText().toString().isEmpty() 
						|| !tryParseDouble(caloriesEditText.getText().toString()) 
						|| !tryParseDouble(proteinsEditText.getText().toString())
						|| !tryParseDouble(fatsEditText.getText().toString())
						|| !tryParseDouble(carbohydratesEditText.getText().toString()))
				{
					//отображение сообщения об ошибке
					AlertDialog.Builder dialog = new AlertDialog.Builder(NewProductActivity.this);
	    			dialog.setTitle("Ошибка");
	    			dialog.setMessage("Неверные входные данные");
	    			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
	    			{
    					@Override
    					public void onClick(DialogInterface dialogInterface, int which) { dialogInterface.cancel(); }
	    			});
	    			dialog.show();
				}
				else
				{
					//сохранение продукта
					Product product = new Product();
					product.Id = currentId;
					product.Name = nameEditText.getText().toString();
					product.Type = ((ProductType)productTypesSpinner.getSelectedItem()).Id;
					product.Calories = Double.parseDouble(caloriesEditText.getText().toString());
					product.Proteins = Double.parseDouble(proteinsEditText.getText().toString());
					product.Fats = Double.parseDouble(fatsEditText.getText().toString());
					product.Carbohydrates = Double.parseDouble(carbohydratesEditText.getText().toString());
				
					if (currentId != -1) 
					{ 
						//редактирование существующего продукта в базе
						helper.updateProduct(product);
						setResult(RESULT_OK);
					}
					else
					{
						//добавление нового продукта в базу
						helper.insertProduct(product);
					}
					finish();
				}
			}
	    });
	    
	    //обработчик нажатия кнопки "Отмена"
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
	
	//проверка, является ли строка числом
	private boolean tryParseDouble(String input)
	{
		try
		{
			Double.parseDouble(input);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
