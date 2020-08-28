package com.me.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;

import com.me.dao.CategoryDao;
import com.me.dao.ProductDao;
import com.me.doman.Category;
import com.me.doman.Order;
import com.me.doman.PageBean;
import com.me.doman.Product;
import com.me.util.DataSourceUtils;

@SuppressWarnings(value = "all")
public class ProductService {

	public List<Product> findHotProductList() {
		List<Product> HotProduct = null;
		ProductDao dao = new ProductDao();
		try {
			HotProduct = dao.findHotProductList();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return HotProduct;

		// TODO Auto-generated method stub

	}

	public List<Product> findNewProductList() {
		// TODO Auto-generated method stub
		List<Product> NewProduct = null;
		ProductDao dao = new ProductDao();
		try {
			NewProduct = dao.findNewProductList();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NewProduct;

		// TODO Auto-generated method stub

	}

	public List<Category> findAllCategory() {
		// TODO Auto-generated method stub
		CategoryDao category = new CategoryDao();

		List<Category> list = null;
		try {
			list = category.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public PageBean findMethodProduct(String cid, int currentPage,
			int currentCount) {
		PageBean<Product> pagebean = new PageBean<Product>();

		pagebean.setCurrentPage(currentPage);
		pagebean.setCurrentCount(currentCount);
		ProductDao dao = new ProductDao();
		int totalCount = dao.getCount(cid);
		pagebean.setTotalCount(totalCount);
		int totalPage = (int) Math.ceil(1.0 * totalCount / currentCount);
		pagebean.setTotalPage(totalPage);
		int index = (currentPage - 1) * currentCount;
		List<Product> list = null;
		try {
			list = dao.findProductByPage(cid, index, currentCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pagebean.setList(list);
		return pagebean;

	}

	public Product findProductinfo(String pid) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		Product product = dao.findProductinfo(pid);
		return product;
	}

	/**
	 * 
	 * @param order
	 */
	public void submitOrder(Order order) {
		// TODO Auto-generated method stub
		// 1.开启事务
		ProductDao dao = new ProductDao();
		try {
			DataSourceUtils.startTransaction();
			dao.addOrders(order);
			dao.addOdderItems(order);
		} catch (SQLException e) {
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public List<Order> findAllOrders(String uid) {
		ProductDao dao = new ProductDao();
		List<Order> orderList = null;
		try {
			orderList = dao.findAllOrders(uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return orderList;
	}

	public void updateOrderState(String r6_Order) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		dao.updateOrderState(r6_Order);
	}

	public List<Map<String, Object>> findAllOrderItemByOid(String uid) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		List<Map<String, Object>> mapOrderList = null;
		try {
			mapOrderList = dao.findAllOrderItemByOid(uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return mapOrderList;
	}

}
