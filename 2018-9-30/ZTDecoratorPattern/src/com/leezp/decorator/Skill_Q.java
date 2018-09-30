package com.leezp.decorator;

import com.leezp.decorator.original.Hero;

//ConreteDecorator ���ܣ�Q
public class Skill_Q extends Skills {
	private String skillName;

	public Skill_Q(Hero hero, String skillName) {
		super(hero);
		this.skillName = skillName;
	}
	
	@Override
	public void learnSkills() {
		System.out.println("ѧϰ�˼���Q: "+skillName);
		super.learnSkills();
	}
}
