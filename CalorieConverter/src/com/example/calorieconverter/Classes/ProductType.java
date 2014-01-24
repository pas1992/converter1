package com.example.calorieconverter.Classes;

//класс типа продукта
public class ProductType 
{
	//идентификатор типа продукта
	public int Id;
	
	//название типа продукта
	public String Name;
	
	//конструктор по умолчанию
	public ProductType() 
	{
		Id = 0;
		Name = "";
	}
	
	//конструктор с параметром "название"
	public ProductType(String name) 
	{
		Id = 0;
		Name = name;
	}
	
	//преобразование к строковому виду
	public String toString() 
	{
		return Name;
	}
}
