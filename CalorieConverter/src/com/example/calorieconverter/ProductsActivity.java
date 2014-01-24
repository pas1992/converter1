package com.example.calorieconverter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.calorieconverter.Classes.Product;
import com.example.calorieconverter.Classes.ProductType;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

//����� ����� "��������"
public class ProductsActivity extends Activity implements OnClickListener
{
	private int currentTypeId;						//������������ ���������� ���� ���������
	private LinearLayout currentRow;				//��������� ������ � ������� ���������
	private ArrayList<Product> productItems;		//������ ���������
	private ArrayList<ProductType> productTypes;	//������ ����� ���������
	
	private Database helper;	//������ ������ � �����
	
	private Spinner productTypesSpinner;	//���������� ������ ����� ���������
	private ListView productsListView;		//������� ���������
	private EditText searchEditText;		//���� ������ �� ��������
	private Button buttonToMenu;			//������ "� ����"
	private Button buttonUpdate;			//������ "�������������"
	private Button buttonDelete;			//������ "�������"
	private Button buttonBack;				//������ "�����"
	private Button buttonOK;				//������ "OK" (����� � ������ ���������� �������� � �����)
	private Button buttonCancel;			//������ "������" (����� � ������ ���������� �������� � �����)
	
	private boolean mode;	//true - ����� ���������� �������� � �����
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//�������� �����
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_products);
	    
	    //������������� ��������� ���������
	    productTypesSpinner = (Spinner)findViewById(R.id.products_productTypesSpinner);
	    productsListView = (ListView)findViewById(R.id.products_productsListView);
	    searchEditText = (EditText)findViewById(R.id.products_searchEditText);
	    buttonToMenu = (Button)findViewById(R.id.products_buttonToMenu);
	    buttonUpdate = (Button)findViewById(R.id.products_buttonUpdate);
	    buttonDelete = (Button)findViewById(R.id.products_buttonDelete);
	    buttonBack = (Button)findViewById(R.id.products_buttonBack);
	    buttonOK = (Button)findViewById(R.id.products_buttonOK);
	    buttonCancel = (Button)findViewById(R.id.products_buttonCancel);
	    
	    //�������� ������
	    try
	    {
	    	mode = getIntent().getExtras().getBoolean("Mode");
	    	//����� ������� � ������ ���������� �������� � �����
	    }
	    catch (Exception e)
	    {
	    	mode = false;
	    	//����� ������� � ������ �� ���������
	    }
	    
	    //��������� ������� ������
	    if (mode)
	    {
	    	buttonToMenu.setVisibility(View.GONE);
	    	buttonUpdate.setVisibility(View.GONE);
	    	buttonDelete.setVisibility(View.GONE);
	    	buttonBack.setVisibility(View.GONE);
	    	buttonOK.setVisibility(View.VISIBLE);
	    	buttonCancel.setVisibility(View.VISIBLE);
	    }
	    else
	    {
	    	buttonToMenu.setVisibility(View.VISIBLE);
	    	buttonUpdate.setVisibility(View.VISIBLE);
	    	buttonDelete.setVisibility(View.VISIBLE);
	    	buttonBack.setVisibility(View.VISIBLE);
	    	buttonOK.setVisibility(View.GONE);
	    	buttonCancel.setVisibility(View.GONE);
	    }
	    
	    //��������� ������������� ������
	    buttonToMenu.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonUpdate.setEnabled(false);
		buttonOK.setEnabled(false);
	    
	    helper = new Database(this);
		currentRow = null;
		
		//��������� ������ ����� ��������� �� ����
		productTypes = helper.getProductTypes();
		productTypes.add(0, new ProductType("���"));
		
		currentTypeId = 0;
	    
		//����������� ����� ��������� � ���������� ������
	    ArrayAdapter<ProductType> spinnerArrayAdapter = new ArrayAdapter<ProductType>(this, android.R.layout.simple_spinner_item, productTypes);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productTypesSpinner.setAdapter(spinnerArrayAdapter);
		
		//���������� ������ ���� ��������� � ���������� ������
		productTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{
				ProductType type = (ProductType)parent.getItemAtPosition(position);
				currentTypeId = type.Id;
				fillProductTable(currentTypeId, searchEditText.getText().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				
			}
		});
	    
		//��������� ������ ��������� �� ����
	    productItems = new Database(this).getProducts();
	    
	    //���������� ������� �� ������ � ������� ���������
	    productsListView.setOnItemClickListener(new OnItemClickListener() 
	    {
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	    	{
	    		//������ ��������� � ���������� ������
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
    			buttonOK.setEnabled(true);
	    	}
	    });
	    
	    //���������� ������� ������ "� ����"
	    buttonToMenu.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//���������� �������� � ����
				helper.addToMenu(Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText()));
				
				//����� ��������� � ���������� �������� � ����
				AlertDialog.Builder dialog = new AlertDialog.Builder(ProductsActivity.this);
				dialog.setMessage("������� ������� �������� � ����");
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
	    			AlertDialog.Builder dialog = new AlertDialog.Builder(ProductsActivity.this);
	    			dialog.setTitle("�������� ��������");
	    			dialog.setMessage("�� ������������� ������ ������� ���� �������?");
	    			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
	    			{
    					@Override
    					public void onClick(DialogInterface dialog, int which) 
    					{
    						//�������� �������� �� ����
    						helper.deleteProduct(Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText()));
    						
    						//���������� ������� ���������
    						currentRow = null;
    						productItems = helper.getProducts();
    						fillProductTable(currentTypeId, searchEditText.getText().toString());
    						
    						//��������� ������������� ������
    						buttonToMenu.setEnabled(false);
    		    			buttonDelete.setEnabled(false);
    		    			buttonUpdate.setEnabled(false);
    		    			buttonOK.setEnabled(false);
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
	    
	    //���������� ������� ������ "��" (����� ���������� �������� � �����)
	    buttonOK.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//�������� �������������� ���������� �������� ����� "����� �����"
				Intent intent = new Intent();
				intent.putExtra("Id", ((TextView)currentRow.getChildAt(0)).getText().toString());
				setResult(RESULT_OK, intent);
				
				//�������� �����
				finish();
			}
	    });
	    
	    //���������� ������� ������ "������" (����� ���������� �������� � �����)
	    buttonCancel.setOnClickListener(new OnClickListener() 
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
			public void afterTextChanged(Editable arg0) 
			{ 
				fillProductTable(currentTypeId, arg0.toString()); 
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
	    });
	    
	    fillProductTable(currentTypeId, "");
	}
	
	//����������� ��������� ���������� ���� � ������� ���������
	private void fillProductTable(int typeId, String query) 
	{
		final Handler handler = new Handler();
		final int _typeId = typeId;		//��� ��������
		final String _query = query;	//������ ������ �� ��������
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				//����������� � ������������ �����
				
				//���������� ������ ���������
				ArrayList<HashMap<String, Object>> products = new ArrayList<HashMap<String, Object>>();
			    for (int i = 0; i < productItems.size(); i++) 
			    {
			    	if ((_typeId == 0 || _typeId == productItems.get(i).Type) 
			    			&& (_query.isEmpty() || productItems.get(i).Name.toUpperCase().indexOf(_query.toUpperCase()) == 0)) 
			    	{
			    		HashMap<String, Object> hm = new HashMap<String, Object>();
			    		hm.put("ID", productItems.get(i).Id);
			    		hm.put("NAME", productItems.get(i).Name);
			    		hm.put("TYPE", productItems.get(i).Type);
			    		hm.put("CALORIES", productItems.get(i).Calories);
			    		hm.put("PROTEINS", productItems.get(i).Proteins);
			    		hm.put("FATS", productItems.get(i).Fats);
			    		hm.put("CARBOHYDRATES", productItems.get(i).Carbohydrates);
			    		products.add(hm);
		    		}
			    }
			    final ArrayList<HashMap<String, Object>> _products = products;
			    
			    //����������� ���������������� ������ ���������
			    handler.post(new Runnable() 
			    {
					@Override
					public void run() 
					{
					    productsListView.setAdapter(new SimpleAdapter(ProductsActivity.this, _products, R.layout.product_row, 
				    		new String[] { "ID", "NAME", "TYPE", "CALORIES", "PROTEINS", "FATS", "CARBOHYDRATES" },
				    		new int[] { R.id.ProductColumn0, R.id.ProductColumn1, R.id.ProductColumn2, R.id.ProductColumn3, R.id.ProductColumn4, R.id.ProductColumn5, R.id.ProductColumn6 }));
					}
			    	
			    }); 
			}
		});
		thread.start();
		
		//��������� ������������� ������
		buttonToMenu.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonUpdate.setEnabled(false);
		buttonOK.setEnabled(false);
	}

	//���������� ������� ������ "�������������"
	@Override
	public void onClick(View arg0) 
	{
		//�������� �������������� �������� �������� ����� "����� �������"
		int id = Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText());
		Intent intent = new Intent(ProductsActivity.this, NewProductActivity.class);
		intent.putExtra("Id", id);
		startActivityForResult(intent, 0);
	}
	
	//���������� ������ � �������� ����� ��������������
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		productItems = helper.getProducts();
		Product product = helper.getProduct(Integer.parseInt((String)((TextView)currentRow.getChildAt(0)).getText()));
		((TextView)currentRow.getChildAt(1)).setText(product.Name);
		((TextView)currentRow.getChildAt(2)).setText(Integer.toString(product.Type));
		((TextView)currentRow.getChildAt(3)).setText(Double.toString(product.Calories));
		((TextView)currentRow.getChildAt(4)).setText(Double.toString(product.Proteins));
		((TextView)currentRow.getChildAt(5)).setText(Double.toString(product.Fats));
		((TextView)currentRow.getChildAt(6)).setText(Double.toString(product.Carbohydrates));
	}
}