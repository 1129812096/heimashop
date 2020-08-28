package com.me.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.me.doman.Category;
import com.me.util.DataSourceUtils;

public class CategoryDao {

	public List<Category> findAllCategory() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "SELECT * FROM category";
		List<Category> list = qr.query(sql, new BeanListHandler<Category>(
				Category.class));
		return list;
	}

}
