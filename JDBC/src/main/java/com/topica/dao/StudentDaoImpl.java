package com.topica.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.topica.connection.MysqlConnection;
import com.topica.model.Student;

public class StudentDaoImpl implements StudentDao {

	public List<Student> getStudents(){
		Connection connection = null;
		CallableStatement callableStatement = null;
		List<Student> students = new ArrayList<Student>();
		try {
			connection = MysqlConnection.getConnection();
			String sql = "{Call getStudent()}";
			callableStatement = connection.prepareCall(sql);
			ResultSet resultSet = callableStatement.executeQuery();
			while (resultSet.next()) {
				Student student = new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3),
						resultSet.getString(4));
				students.add(student);
			}
			resultSet.close();
		} catch (SQLException | ClassNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
		} finally {
			if(callableStatement != null) {
				try {
					callableStatement.close();
				} catch (SQLException e) {
					System.err.println("Error: " +  e.getMessage());
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					System.err.println("Error: " +  e.getMessage());
				}
			}
		}
		return students;
	}

}
