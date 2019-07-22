package com.topica.dao;

public interface TeacherDao {
	public void doTransfer(int id_from, int id_to, double salary);
	public String doTransfer2(int id_from, int id_to, double salary);
}
