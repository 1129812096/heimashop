package com.me.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.me.doman.Order;
import com.me.doman.OrderItem;
import com.me.doman.PageBean;
import com.me.doman.Product;
import com.me.util.DataSourceUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

@SuppressWarnings(value = "all")
public class ProductDao {

	public List<Product> findHotProductList() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";
		List<Product> hotProduct = qr.query(sql, new BeanListHandler<Product>(
				Product.class), 1, 0, 9);
		return hotProduct;
	}

	public List<Product> findNewProductList() throws SQLException {

		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate desc limit ?,?";
		List<Product> NewProduct = qr.query(sql, new BeanListHandler<Product>(
				Product.class), 0, 9);
		return NewProduct;
	}

	public PageBean findMethodProduct(String cid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid =? limit ?,?";
		List<Product> productList = qr.query(sql, new BeanListHandler<Product>(
				Product.class), cid, 0, 12);
		return null;

	}

	public int getCount(String cid) {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product where cid =?";
		Long totalCount = 0L;
		try {
			totalCount = (Long) qr.query(sql, new ScalarHandler(), cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return totalCount.intValue();

	}

	public List<Product> findProductByPage(String cid, int index,
			int currentCount) throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid=? limit ?,?";
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(
				Product.class), cid, index, currentCount);
		return list;
	}

	public Product findProductinfo(String pid) {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid=?";
		Product product = null;
		try {
			product = qr.query(sql, new BeanHandler<Product>(Product.class),
					pid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product;
	}

	public void addOrders(Order order) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner();
		String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
		Connection con = DataSourceUtils.getConnection();
		qr.update(con, sql, order.getOid(), order.getOrdertime(),
				order.getTotal(), order.getState(), order.getAddress(),
				order.getName(), order.getTelephone(), order.getUser().getUid());
	}

	public void addOdderItems(Order order) throws SQLException {
		// TODO Auto-generated method stub
		List<OrderItem> orderItems = order.getOrderItems();
		QueryRunner qr = new QueryRunner();
		String sql = "insert into orderitem values(?,?,?,?,?)";
		Connection con = DataSourceUtils.getConnection();
		for (OrderItem item : orderItems) {
			qr.update(con, sql, item.getItemid(), item.getCount(), item
					.getSubtotal(), item.getProduct().getPid(), item.getOrder()
					.getOid());
		}

	}

	public void orderAddXiangxi(Order order) {

		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set address =?,name=?,telephone=? where oid=? ";
		try {
			qr.update(sql, order.getAddress(), order.getName(),
					order.getTelephone(), order.getOid());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Order> findAllOrders(String uid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders where uid=?";
		// String sql =
		// "SELECT  count,subtotal,pimage,pname,shop_price FROM product t1,orders t2,orderitem t3 where t2.oid=t3.oid and t3.pid=t1.pid and t2.uid=?";
		List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(
				Order.class), uid);
		return orderList;

	}

	public void updateOrderState(String r6_Order) {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set state =1 where oid=? ";
		try {
			qr.update(sql, r6_Order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Map<String, Object>> findAllOrderItemByOid(String uid)
			throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		// String sql="select * from orders where uid=?";
		String sql = "SELECT  count, subtotal,pimage,pname,shop_price FROM product t1,orders t2,orderitem t3 where t2.oid=t3.oid and t3.pid=t1.pid and t2.uid=?";
		List<Map<String, Object>> mapOrderList = qr.query(sql,
				new MapListHandler(), uid);
		return mapOrderList;
	}

}
