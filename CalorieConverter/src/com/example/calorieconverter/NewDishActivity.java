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

//����� ����� "����� �����"
public class NewDishActivity extends Activity implements OnClickListener
{
	private EditText nameEditText;			//���� "��������"
	private Button buttonAdd;				//������ "�������� �������"
	private ListView productsListView;		//������� ���������
	private Button buttonOK;				//������ "OK"
	private Button buttonCancel;			//������ "������"
	
	private Database helper;	//������ ������ � �����
	
	private Dish dish;	//������� �����
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//�������� �����
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_newdish);
	    
	    helper = new Database(this);

	    //������������� ��������� ����������
	    nameEditText = (EditText)findViewById(R.id.newDish_nameEditText);
	    buttonAdd = (Button)findViewById(R.id.newDish_buttonAdd);
	    productsListView = (ListView)findViewById(R.id.newDish_productsListView);
	    buttonOK = (Button)findViewById(R.id.newDish_buttonOK);
	    buttonCancel = (Button)findViewById(R.id.newDish_buttonCancel);
	    
	    //��������� �������������� �������� �����
	    try
	    {
	    	dish = helper.getDish(getIntent().getExtras().getInt("Id"));
	    	nameEditText.setText(dish.Name);
	    	fillProductTable();
	    	//����� ������� ��� �������������� ������������� �����
	    }
	    catch (Exception e)
	    {
	    	dish = new Dish();
	    	//����� ������� ��� ���������� ������ �����
	    }
	    
	    buttonAdd.setOnClickListener(this);
	    
	    //���������� ������� ������ "OK"
	    buttonOK.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				dish.Name = nameEditText.getText().toString();
				if (dish.Id != -1)
				{
					//�������������� ������������� �����
					helper.updateDish(dish);
				}
				else
				{
					//���������� ������ �����
					helper.insertDish(dish);
				}
				finish();
			}
	    });
	    
	    //���������� ������� ������ ""������
	    buttonCancel.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v)
			{
				//�������� �����
				finish();
			}
	    });
	}

	//���������� ������� ������ "�������� �������"
	@Override
	public void onClick(View v) 
	{
		//�������� ����� "��������" � ������ ���������� �������� � �����
		Intent intent = new Intent(NewDishActivity.this, ProductsActivity.class);
		intent.putExtra("Mode", true);
		startActivityForResult(intent, 1);
	}

	//���������� �������� � �����
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
	
	//����������� ������ ��������� � ������� �����
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
