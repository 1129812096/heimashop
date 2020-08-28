package com.me.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.me.doman.Category;
import com.me.doman.Order;
import com.me.service.adminService;

public class admin extends baseServlet {

	/**
	 * Constructor of the object.
	 */
	public admin() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//获得oid
		String oid = request.getParameter("oid");
		
		//用解耦合的方式进行编码----解web层与service层的耦合
		//使用工厂+反射+配置文件
		 
		adminService service =new adminService();
		List<Map<String,Object>> mapList = service.findOrderInfoByOid(oid);
		
		Gson gson = new Gson();
		String json = gson.toJson(mapList);
		 
		/*[
		 * 	{"shop_price":4499.0,"count":2,"pname":"联想（Lenovo）小新V3000经典版","pimage":"products/1/c_0034.jpg","subtotal":8998.0},
		 *  {"shop_price":2599.0,"count":1,"pname":"华为 Ascend Mate7","pimage":"products/1/c_0010.jpg","subtotal":2599.0}
		 *]*/
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().write(json);
		
	}
	/*
	 * 
	 * findAllOrders
	 */
	public void findAllOrders(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		adminService service = new adminService();
		List<Order> orderList = service.findAllOrders();

		request.setAttribute("orderList", orderList);

		request.getRequestDispatcher("/admin/order/list.jsp").forward(request,
				response);
	}

	/**
	 * 显示分类
	 * 
	 * @param request
	 * @param response
	 */
	public void findAllCategory(HttpServletRequest request,
			HttpServletResponse response) {
		adminService service = new adminService();
		List<Category> categoryList = service.findAllCategory();
		Gson gson = new Gson();
		String json = gson.toJson(categoryList);

		try {
			response.setContentType("text/json; charset=UTF-8");
			response.getWriter().write(json);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
