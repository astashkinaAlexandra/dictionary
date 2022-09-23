//AUTHORS: TAYLOR / SOCHAJ
//Mini-Projet L3 Info
//DATE: 04/21

package myPackage;

import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary {
	private String name;
	public HashMap<ArrayList<String>, ArrayList<String>> translationENRU;
	public HashMap<ArrayList<String>, ArrayList<String>> translationRUEN;

	Dictionary(String name) {
		this.name = name;
		this.translationENRU = new HashMap<ArrayList<String>, ArrayList<String>>();
		this.translationRUEN = new HashMap<ArrayList<String>, ArrayList<String>>();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addTranslation(ArrayList<String> fr, ArrayList<String> en) {
		translationENRU.put(fr, en);
		translationRUEN.put(en, fr);
	}

	public ArrayList<String> getTranslation(ArrayList<String> word){
		ArrayList<String> resultEN_FR = translationRUEN.get(word);
		ArrayList<String> resultFR_EN = translationENRU.get(word);
		ArrayList<String> temp = new ArrayList<String>();
		for(ArrayList<String> i : translationRUEN.keySet()) {
			if(i.contains(word.get(0))) {
				temp = i;
				resultEN_FR = translationRUEN.get(temp);
			}
		}
		if(resultEN_FR != null) {
				return resultEN_FR;
		}
		if(resultFR_EN !=null) {
				return resultFR_EN;
		}
		else return null;
	}

	public void showHashMap() {
		for(ArrayList<String> i : translationENRU.keySet()) {
			System.out.println("Английский: " + i + " Русский: " + translationENRU.get(i));
		}
	}
}