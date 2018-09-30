package com.leezp.test;

import com.leezp.decorator.Skill_E;
import com.leezp.decorator.Skill_Q;
import com.leezp.decorator.Skill_R;
import com.leezp.decorator.Skill_W;
import com.leezp.decorator.Skills;
import com.leezp.decorator.original.BlindMonk;
import com.leezp.decorator.original.Hero;

public class Player {
	public static void main(String[] args) {
		//ѡ��Ӣ��
		Hero hero = new BlindMonk("����");
		
		Skills skills = new Skills(hero);
		Skills r = new Skill_R(skills, "������β");
		Skills e = new Skill_E(r, "������/�ݽ�Ϲ�");
		Skills w = new Skill_W(e, "������/������");
		Skills q = new Skill_Q(w, "������/������");
		//ѧϰ����
		q.learnSkills();
	}
}
