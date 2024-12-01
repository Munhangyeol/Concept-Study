package com.example;

import com.example.Oop.chef.Utensil.Utensil;
import com.example.Oop.chef.steak.SteakPart;
import com.example.Oop.chef.steak.TBornSteackPart;

import static com.example.Oop.chef.Utensil.Utensil.PAN;
import static com.example.Oop.chef.Utensil.Utensil.POT;

public class ConceptApplication {
	public static void main(String[] args) {
		SteakPart steakPart=new TBornSteackPart("T-Born");
		steakPart.readyToCook(new Utensil[]{PAN,POT},"butter");
		steakPart.cook();
	}

}
