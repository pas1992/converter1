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

//����� ����� "����"
public class MenuActivity extends Activity 
{
	private EditText searchEditText;	//���� ������ �� ��������
	private ListView listView;			//�������� ��������� � ����
	private Button buttonWeight;		//������ "�����"
	private Button buttonDelete;		//������ "�������"
	private Button buttonConvert;		//������ "���������"
	private Button buttonBack;			//������ "�����"
	
	private Database helper;	//������ ������ � �����
	
	private View currentRow;	//��������� ������ � �������
	private int currentId;		//������������� �������� �������� � ����
	
	private ArrayList<Menu> menuItems;	//������ ��������� � ����
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//�������� �����
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_menu);
	    
	    //������������� ��������� ����������
	    searchEditText = (EditText)findViewById(R.id.menu_searchEditText);
	    listView = (ListView)findViewById(R.id.menu_listView);
	    buttonWeight = (Button)findViewById(R.id.menu_buttonWeight);
	    buttonDelete = (Button)findViewById(R.id.menu_buttonDelete);
	    buttonConvert = (Button)findViewById(R.id.menu_buttonConvert);
	    buttonBack = (Button)findViewById(R.id.menu_buttonBack);
	    
	    helper = new Database(this);
	    
	    //��������� ������ ��������� � ����
	    menuItems = helper.getMenu();
	    
	    currentRow = null;
	    currentId = -1;
	    
	  //���������� ��������� ������ � ���� ������ �� ��������
	    searchEditText.addTextChangedListener(new TextWatcher() 
	    {
			@Override
			public void afterTextChanged(Editable arg0) { fillMenuTable(arg0.toString()); }

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
	    });
	    
	    //���������� ������� ������ � ������� ���������
	    listView.setOnItemClickListener(new OnItemClickListener() 
	    {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) 
			{
				//������ ��������� � ���������� ������
				if (currentRow != null)
				{
					currentRow.setBackgroundColor(Color.BLACK);
				}
				
				//��������� ������� ������
				currentRow= view;
				currentRow.setBackgroundColor(Color.BLUE);
				currentId = Integer.parseInt(((HashMap<String, Object>)adapter.getItemAtPosition(position)).get("ID").toString());
				
				//��������� ����������� ������
				buttonWeight.setEnabled(true);
				buttonDelete.setEnabled(true);
			}
	    });
	    
	    //���������� ������� ������ "�����"
	    buttonWeight.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//����������� ����� ����� �����
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
						//������� ����� �������� � ����
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
	    });
	    
	    //���������� ������� ������ "�������"
	    buttonDelete.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//������ ������������� ��������
				AlertDialog.Builder dialog = new AlertDialog.Builder(MenuActivity.this);
    			dialog.setTitle("�������� ��������");
    			dialog.setMessage("�� ������������� ������ ������� ���� �������?");
    			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
    			{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						//�������� �������� �� ����
						helper.deleteFromMenu(currentId);
						currentRow = null;
						menuItems = helper.getMenu();
						fillMenuTable(searchEditText.getText().toString());
						
						//��������� ������������� ������
						buttonWeight.setEnabled(false);
		    			buttonDelete.setEnabled(false);
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
	    });
	    
	    //���������� ������� ������ "��������"
	    buttonConvert.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//������ �������������� �������� ����
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
				
				//����� �������������� ��������
				AlertDialog.Builder dialog = new AlertDialog.Builder(MenuActivity.this);
				dialog.setTitle("�������������� ��������");
				dialog.setMessage(String.format("����:\t\t\t%.2f\n�����:\t\t\t%.2f\n����:\t\t\t%.2f\n��������:\t%.2f\n", calories, proteins, fats, carbohydrates));
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
	    
	    fillMenuTable("");
	}
	
	//����������� ��������� � ������� ���������
	private void fillMenuTable(String query) 
	{
		final Handler handler = new Handler();
		final String _query = query; //������ ������ �� ��������
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{	
				//���������� ������
				ArrayList<HashMap<String, Object>> menu = new ArrayList<HashMap<String, Object>>();
			    for (int i = 0; i < menuItems.size(); i++) 
			    {
			    	String productName = helper.getProduct(menuItems.get(i).Product).Name;
			    	if (_query.isEmpty() || productName.toUpperCase().indexOf(_query.toUpperCase()) == 0)
			    	{
			    		HashMap<String, Object> hm = new HashMap<String, Object>();
			    		hm.put("ID", menuItems.get(i).Id);
			    		hm.put("NAME", productName);
			    		hm.put("WEIGHT", String.format("�����: %.2f �", menuItems.get(i).Weight));
			    		menu.add(hm);
		    		}
			    }
			    final ArrayList<HashMap<String, Object>> _menu = menu;
			    
			    ////����������� ���������������� ������ ���������
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
		
		//��������� ������������� ������
		buttonWeight.setEnabled(false);
		buttonDelete.setEnabled(false);
	}
}
