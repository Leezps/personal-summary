package com.leezp.test;

import com.leezp.mvc.Student;
import com.leezp.mvc.StudentController;
import com.leezp.mvc.StudentView;

public class MVCPatternDemo {
	public static void main(String[] args) {
		//�����ݿ��ȡѧ����¼
		Student model = retriveStudentFromDatabase();
		
		//����һ����ͼ����ѧ����ϸ��Ϣ���������̨
		StudentView view = new StudentView();
		
		StudentController controller = new StudentController(model, view);
		controller.updateView();
		
		//����ģ������
		controller.setStudentName("John");
		controller.updateView();
	}

	private static Student retriveStudentFromDatabase() {
		Student student = new Student();
		student.setName("Robert");
		student.setRollNo("10");
		return student;
	}
}
