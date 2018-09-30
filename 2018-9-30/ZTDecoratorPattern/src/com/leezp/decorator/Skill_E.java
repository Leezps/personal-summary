package com.leezp.decorator;

import com.leezp.decorator.original.Hero;

//ConreteDecorator ���ܣ�E
public class Skill_E extends Skills {
	private String skillName;

	public Skill_E(Hero hero, String skillName) {
		super(hero);
		this.skillName = skillName;
	}
	
	@Override
	public void learnSkills() {
		System.out.println("ѧϰ�˼���E:"+skillName);
		super.learnSkills();
	}
}
