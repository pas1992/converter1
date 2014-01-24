package com.example.calorieconverter.Classes;

import java.util.ArrayList;

// класс блюда
public class Dish 
{
	//идентификатор блюда
	public int Id = -1;
	
	//название блюда
	public String Name = "";
	
	//список продуктов в составе блюда
	public ArrayList<Product> Products = new ArrayList<Product>();
}
