package com.leezp.decorator;

import com.leezp.decorator.original.Hero;

//Decorator ������
public class Skills implements Hero {
	//����һ��Ӣ�۶���ӿ�
	private Hero hero;
	
	public Skills(Hero hero) {
		this.hero = hero;
	}

	@Override
	public void learnSkills() {
		if (hero != null) {
			hero.learnSkills();
		}
	}
}
