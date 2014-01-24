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

//����� "����� �������"
public class NewProductActivity extends Activity
{
	private int currentId;	//������������� �������� ��������
	
	private Spinner productTypesSpinner;		//���������� ������ "���� ���������"
	private EditText nameEditText;				//���� "�������� ��������"
	private EditText caloriesEditText;			//���� "����"
	private EditText proteinsEditText;			//���� "�����"
	private EditText fatsEditText;				//���� "����"
	private EditText carbohydratesEditText;		//���� "��������"
	private Button buttonOK;					//������ "OK"
	private Button buttonCancel;				//������ "������"
	
	private Database helper;	//������ ������ � �����
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//�������� �����
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_newproduct);
	    
	    //��������� �������������� �������� ��������
	    try
	    {
	    	currentId = getIntent().getExtras().getInt("Id");
	    	//����� ������� ��� �������������� ������������� ��������
	    }
	    catch (Exception e)
	    {
	    	currentId = -1;
	    	//����� ������� ��� ���������� ������ ��������
	    }
	    
	    helper = new Database(this);
	    
	    //������������� ��������� ����������
	    productTypesSpinner = (Spinner)findViewById(R.id.newProduct_productTypesSinner);
	    nameEditText = (EditText)findViewById(R.id.newProduct_nameEditText);
	    caloriesEditText = (EditText)findViewById(R.id.newProduct_caloriesEditText);
	    proteinsEditText = (EditText)findViewById(R.id.newProduct_proteinsEditText);
	    fatsEditText = (EditText)findViewById(R.id.newProduct_fatsEditText);
	    carbohydratesEditText = (EditText)findViewById(R.id.newProduct_carbohydratesEditText);
	    buttonOK = (Button)findViewById(R.id.newProduct_buttonOK);
	    buttonCancel = (Button)findViewById(R.id.newProduct_buttonCancel);
	    
	    //���������� ����������� ������ ������ ��������� �� ����
	    ArrayAdapter<ProductType> spinnerArrayAdapter = new ArrayAdapter<ProductType>(this, android.R.layout.simple_spinner_item, helper.getProductTypes());
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productTypesSpinner.setAdapter(spinnerArrayAdapter);
		
		if (currentId != -1)
		{
			//����������� ���������� �������������� ��������
			Product product = helper.getProduct(currentId);
			nameEditText.setText(product.Name);
			caloriesEditText.setText(Double.toString(product.Calories));
			proteinsEditText.setText(Double.toString(product.Proteins));
			fatsEditText.setText(Double.toString(product.Fats));
			carbohydratesEditText.setText(Double.toString(product.Carbohydrates));
		}
	    
		//���������� ������� ������ "OK"
	    buttonOK.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				//�������� ��������� ������
				if (nameEditText.getText().toString().isEmpty() 
						|| !tryParseDouble(caloriesEditText.getText().toString()) 
						|| !tryParseDouble(proteinsEditText.getText().toString())
						|| !tryParseDouble(fatsEditText.getText().toString())
						|| !tryParseDouble(carbohydratesEditText.getText().toString()))
				{
					//����������� ��������� �� ������
					AlertDialog.Builder dialog = new AlertDialog.Builder(NewProductActivity.this);
	    			dialog.setTitle("������");
	    			dialog.setMessage("�������� ������� ������");
	    			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
	    			{
    					@Override
    					public void onClick(DialogInterface dialogInterface, int which) { dialogInterface.cancel(); }
	    			});
	    			dialog.show();
				}
				else
				{
					//���������� ��������
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
						//�������������� ������������� �������� � ����
						helper.updateProduct(product);
						setResult(RESULT_OK);
					}
					else
					{
						//���������� ������ �������� � ����
						helper.insertProduct(product);
					}
					finish();
				}
			}
	    });
	    
	    //���������� ������� ������ "������"
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
	
	//��������, �������� �� ������ ������
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
