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

//����� ����� "�����"
public class DishesActivity extends Activity implements OnClickListener
{
	private Database helper;	//������ ������ � �����
	
	private LinearLayout currentRow;	//��������� ������ � �������
	private ArrayList<Dish> dishItems;	//������ ����
	
	private ListView dishesListView;	//������� ����
	private EditText searchEditText;	//���� ������ �� ��������
	private Button buttonToMenu;		//������ "� ����"
	private Button buttonUpdate;		//������ "�������������"
	private Button buttonDelete;		//������ "�������"
	private Button buttonBack;			//������ "�����"

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//�������� �����
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dishes);
	
	    //������������� ��������� ����������
	    dishesListView = (ListView)findViewById(R.id.dishes_dishesListView);
	    searchEditText = (EditText)findViewById(R.id.dishes_searchEditText);
	    buttonToMenu = (Button)findViewById(R.id.dishes_buttonToMenu);
	    buttonUpdate = (Button)findViewById(R.id.dishes_buttonUpdate);
	    buttonDelete = (Button)findViewById(R.id.dishes_buttonDelete);
	    buttonBack = (Button)findViewById(R.id.dishes_buttonBack);
	    
	    //��������� ������������� ������
	    buttonToMenu.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonUpdate.setEnabled(false);
		
		helper = new Database(this);
		currentRow = null;
		
		//��������� ������ ���� �� ����
		dishItems = new Database(this).getDishes();
	    
		//���������� ������� ������ � �������
	    dishesListView.setOnItemClickListener(new OnItemClickListener() 
	    {
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	    	{
	    		//������ ��������� ���������� ������
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
	    		
	    		//��������� ������� ������
	    		currentRow = (LinearLayout)view;
	    		((TextView)currentRow.getChildAt(0)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(1)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(2)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(3)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(4)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(5)).setBackgroundColor(Color.BLUE);
    			((TextView)currentRow.getChildAt(6)).setBackgroundColor(Color.BLUE);
    			
    			//��������� ����������� ������
    			buttonToMenu.setEnabled(true);
    			buttonDelete.setEnabled(true);
    			buttonUpdate.setEnabled(true);
	    	}
	    });
	    
	    //���������� ������� ������ "� ����"
	    buttonToMenu.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//���������� � ���� ��������� �� ������� �����
				for (Product product : helper.getDish(Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText())).Products)
				{
					helper.addToMenu(product.Id);
				}
				
				//����� ��������� �� �������� ���������� ��������� � ����
				AlertDialog.Builder dialog = new AlertDialog.Builder(DishesActivity.this);
				dialog.setMessage("�������� ����� ������� ��������� � ����");
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
	    
	    //���������� ������� ������ "�������"
	    buttonDelete.setOnClickListener(new OnClickListener() 
	    {
	    	@Override
	    	public void onClick(View v) 
	    	{
	    		if (currentRow != null) 
	    		{
	    			//������ ������������� ��������
	    			AlertDialog.Builder dialog = new AlertDialog.Builder(DishesActivity.this);
	    			dialog.setTitle("�������� �����");
	    			dialog.setMessage("�� ������������� ������ ������� ��� �����?");
	    			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
	    			{
    					@Override
    					public void onClick(DialogInterface dialog, int which) 
    					{
    						//�������� �����
    						helper.deleteDish(Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText()));
    						
    						//���������� ������ ����
    						currentRow = null;
    						dishItems = helper.getDishes();
    						fillDishTable(searchEditText.getText().toString());
    						
    						//��������� ������������� ������
    						buttonToMenu.setEnabled(false);
    		    			buttonDelete.setEnabled(false);
    		    			buttonUpdate.setEnabled(false);
    					}
	    			});
	    			dialog.setNegativeButton("������", new DialogInterface.OnClickListener() 
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
	    
	    //���������� ������� ������ "�����"
	    buttonBack.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{ 
				//�������� �����
				finish(); 
			}
	    });
	    
	  //���������� ��������� ������ � ���� ������ �� ��������
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
	
	//����������� ���� �� ��������
	private void fillDishTable(String query) 
	{
		final Handler handler = new Handler();
		final String _query = query;	//������ ������ �� ��������
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				//���������� � ������������ ������
				
				//���������� ������ ����
				ArrayList<HashMap<String, Object>> dishes = new ArrayList<HashMap<String, Object>>();
			    for (int i = 0; i < dishItems.size(); i++) 
			    {
			    	if (_query.isEmpty() || dishItems.get(i).Name.toUpperCase().indexOf(_query.toUpperCase()) == 0)
			    	{
			    		HashMap<String, Object> hm = new HashMap<String, Object>();
			    		hm.put("ID", dishItems.get(i).Id);
			    		hm.put("NAME", dishItems.get(i).Name);
			    		
			    		//������ �������������� �������� �����
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
			    
			    //����������� ���������������� ������ ����
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
		
		//��������� ������������� ������
		buttonToMenu.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonUpdate.setEnabled(false);
	}

	//���������� ������� ������ "�������������"
	@Override
	public void onClick(View arg0) 
	{
		//�������� ����� "����� �����" �������������� ���������� �����
		int id = Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText());
		Intent intent = new Intent(DishesActivity.this, NewDishActivity.class);
		intent.putExtra("Id", id);
		startActivityForResult(intent, 0);
	}
	
	//���������� ������ � ����� ����� ��������������
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
