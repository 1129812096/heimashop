package com.me.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.me.dao.adminDao;
import com.me.doman.Category;
import com.me.doman.Order;
import com.me.doman.Product;

public class adminService {

	public List<Category> findAllCategory() {
		// TODO Auto-generated method stub
		List<Category> categoryList = null;
		try {
			adminDao dao = new adminDao();
			categoryList = dao.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryList;
	}

	public void saveProduct(Product product) {
		// TODO Auto-generated method stub
		adminDao dao = new adminDao();
		try {
			dao.saveProduct(product);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Order> findAllOrders() {
		List<Order> orderList = null;
		adminDao dao = new adminDao();
		try {
			orderList = dao.findAllOrders();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return orderList;
	}

	public List<Map<String, Object>> findOrderInfoByOid(String oid) {
		adminDao dao = new adminDao();
		List<Map<String, Object>> mapList = null;
		try {
			mapList = dao.findOrderInfoByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapList;
	}
}
