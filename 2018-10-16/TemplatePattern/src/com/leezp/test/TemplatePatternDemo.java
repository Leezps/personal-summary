package com.leezp.test;

import com.leezp.template.Cricket;
import com.leezp.template.Football;
import com.leezp.template.Game;

public class TemplatePatternDemo {
	public static void main(String[] args) {
		Game game = new Cricket();
		game.play();
		System.out.println();
		game = new Football();
		game.play();
	}
}
