package com.example.calorieconverter.Classes;

//����� ���� ��������
public class ProductType 
{
	//������������� ���� ��������
	public int Id;
	
	//�������� ���� ��������
	public String Name;
	
	//����������� �� ���������
	public ProductType() 
	{
		Id = 0;
		Name = "";
	}
	
	//����������� � ���������� "��������"
	public ProductType(String name) 
	{
		Id = 0;
		Name = name;
	}
	
	//�������������� � ���������� ����
	public String toString() 
	{
		return Name;
	}
}
