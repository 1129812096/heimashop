package com.me.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.me.doman.Category;
import com.me.doman.Order;
import com.me.doman.Product;
import com.me.util.DataSourceUtils;

public class adminDao {

	public List<Category> findAllCategory() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String  sql ="select * from category";
		List<Category> categoryList= qr.query(sql, new BeanListHandler<Category>(Category.class));
		return categoryList;
	}

	public void saveProduct(Product product) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		runner.update(sql, product.getPid(),product.getPname(),product.getMarket_price(),
				product.getShop_price(),product.getPimage(),product.getPdate(),
				product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCategory().getCid());
	}

	public List<Order> findAllOrders() throws SQLException {
		
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders";
		// TODO Auto-generated method stub
		return runner.query(sql, new BeanListHandler<Order>(Order.class));
	}
	
	public List<Map<String, Object>> findOrderInfoByOid(String oid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select p.pimage,p.pname,p.shop_price,i.count,i.subtotal "+
					" from orderitem i,product p "+
					" where i.pid=p.pid and i.oid=? ";
		return runner.query(sql, new MapListHandler(), oid);
	}
	
	
}
