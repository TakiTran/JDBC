package com.topica.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.topica.dao.StudentDaoImpl;
import com.topica.dao.TeacherDaoImpl;
import com.topica.model.Student;

public class Main {
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		StudentDaoImpl studentDaoImpl = new StudentDaoImpl();
		List<Student> students = new ArrayList<Student>();
		students = studentDaoImpl.getStudents();
		for (Student student : students) {
			System.out.println(student);
		}
	
		TeacherDaoImpl teacherDaoImpl = new TeacherDaoImpl();
		teacherDaoImpl.doTransfer(1, 2, 1000000);
	}
}
