package com.example.calorieconverter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//����� ������� �����
public class MainActivity extends Activity 
{
	private Button buttonNewProduct;	//������ "����� �������"
	private Button buttonProducts;		//������ "��������"
	private Button buttonNewDish;		//������ "����� �����"
	private Button buttonDishes;		//������ "�����"
	private Button buttonMenu;			//������ "����"
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//�������� �����
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//������������� ��������� ����������
		buttonNewProduct = (Button)findViewById(R.id.main_buttonNewProduct);
		buttonProducts = (Button)findViewById(R.id.main_buttonProducts);
		buttonNewDish = (Button)findViewById(R.id.main_buttonNewDish);
		buttonDishes = (Button)findViewById(R.id.main_buttonDishes);
		buttonMenu = (Button)findViewById(R.id.main_buttonMenu);
		
		//���������� ������� ������ "����� �������"
		buttonNewProduct.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//������� �� ����� "����� �������"
				Intent intent = new Intent(MainActivity.this, NewProductActivity.class);
				startActivity(intent);
			}
		});
		
		//���������� ������� ������ "��������"
		buttonProducts.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//������� �� ����� "��������"
				Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
				startActivity(intent);
			}
		});
		
		//���������� ������� ������ "����� �����"
		buttonNewDish.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//������� �� ����� "����� �����"
				Intent intent = new Intent(MainActivity.this, NewDishActivity.class);
				startActivity(intent);
			}
		});
		
		//���������� ������� ������ "�����"
		buttonDishes.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//������� �� ����� "�����"
				Intent intent = new Intent(MainActivity.this, DishesActivity.class);
				startActivity(intent);
			}
		});
		
		//���������� ������� ������ "����"
		buttonMenu.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//������� �� ����� "����"
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
