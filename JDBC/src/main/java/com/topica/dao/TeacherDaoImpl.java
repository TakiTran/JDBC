package com.topica.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

import com.topica.connection.MysqlConnection;

public class TeacherDaoImpl implements TeacherDao {

	@Override
	public void doTransfer(int id_from, int id_to, double salary) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int checkExistsFrom = 0;
		int checkExistsTo = 0;
		double salaryFrom = 0;
		double salaryTo = 0;
		boolean checkTransfer = false;

		try {
			connection = MysqlConnection.getConnection();
			connection.setAutoCommit(false);
			String checkExists = "select count(*) from teacher where id = ?";
			String getSalary = "select salary from teacher where id = ?";
			String update = "update teacher set salary = ? where id = ?";

			preparedStatement = connection.prepareStatement(checkExists);
			preparedStatement.setInt(1, id_from);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				checkExistsFrom = resultSet.getInt(1);
			}

			preparedStatement.setInt(1, id_to);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				checkExistsTo = resultSet.getInt(1);
			}

			if (checkExistsFrom != 0 && checkExistsTo != 0) {
				preparedStatement = connection.prepareStatement(getSalary);

				preparedStatement.setInt(1, id_from);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					salaryFrom = resultSet.getDouble(1);
				}

				preparedStatement.setInt(1, id_to);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					salaryFrom = resultSet.getDouble(1);
				}

				Savepoint pointStart = connection.setSavepoint();

				preparedStatement = connection.prepareStatement(update);
				preparedStatement.setDouble(1, salaryTo + salary);
				preparedStatement.setInt(2, id_to);
				preparedStatement.execute();

				if (salaryFrom > salary) {
					preparedStatement.setDouble(1, salaryFrom - salary);
					preparedStatement.setInt(2, id_from);
					preparedStatement.execute();
					checkTransfer = true;
				} else {
					connection.rollback(pointStart);
				}
				if (checkTransfer) {
					System.out.println("Transfer success.");
				} else {
					System.out.println(
							"Transaction failed. The amount of money to trade is greater than the amount allowed.");
				}
			} else {
				if (checkExistsFrom == 0) {
					System.out.println("not exists teacher has id = " + id_from);
				}
				if (checkExistsTo == 0) {
					System.out.println("not exists teacher has id = " + id_to);
				}
			}
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	@Override
	public String doTransfer2(int id_from, int id_to, double salary) {
		Connection connection = null;
		CallableStatement callableStatement = null;
		String result = "";
		try {
			connection = MysqlConnection.getConnection();
			String sql = "{Call doTransfer(?,?,?)}";
			callableStatement = connection.prepareCall(sql);
			callableStatement.setInt(1, id_from);
			callableStatement.setInt(2, id_to);
			callableStatement.setDouble(3, salary);
			ResultSet resultSet = callableStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getString(1);
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
		return result;
	}
}
