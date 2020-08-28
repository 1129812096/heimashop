package com.me.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.me.doman.User;
import com.me.util.DataSourceUtils;

public class UserDao {
	private int row = 0;

	public int regist(User user) throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "INSERT INTO user VALUES(?,?,?,?,?,?,?,?,?,?)";
		row = qr.update(sql, user.getUid(), user.getUsername(),
				user.getPassword(), user.getName(), user.getEmail(),
				user.getTelephone(), user.getBirthday(), user.getSex(),
				user.getState(), user.getCode());

		return row;

		// TODO Auto-generated method stub

	}
	public void active(String activeCode) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update user set state=? where code=?";
		runner.update(sql, 1,activeCode);
	}
	
	public int jiHuo(String activeCode) {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "UPDATE user set state = 1 where code=?";
		try {
			row = qr.update(sql, activeCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}

	public long checkUser(String username) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from user where username=?";
		long count = (Long) qr.query(sql, new ScalarHandler(), username);
		return count;

	}
	//用户登录的方法
	public User login(String username, String password) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from user where username=? and password=?";
		return runner.query(sql, new BeanHandler<User>(User.class), username,password);
	}

}
