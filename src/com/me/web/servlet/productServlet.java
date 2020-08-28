package com.me.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.me.dao.ProductDao;
import com.me.doman.Cart;
import com.me.doman.CartItem;
import com.me.doman.Category;
import com.me.doman.Order;
import com.me.doman.OrderItem;
import com.me.doman.PageBean;
import com.me.doman.Product;
import com.me.doman.User;
import com.me.service.ProductService;
import com.me.util.CommonUtils;
import com.me.util.PaymentUtil;

@SuppressWarnings(value = "all")
public class productServlet extends baseServlet {

	/**
	 * Constructor of the object.
	 */
	public productServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/*
	 * 我的订单查看
	 */
	public void myOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 判断用户是否登录
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		ProductService service = new ProductService();
		// 查询该订单订单项
		List<Order> orderList = service.findAllOrders(user.getUid());
		if (orderList != null) {
			for (Order order : orderList) {
				String oid = order.getOid();
				List<Map<String, Object>> mapOrderList = service
						.findAllOrderItemByOid(user.getUid());
				for (Map<String, Object> map : mapOrderList) {
					OrderItem item = new OrderItem();
					try {
						BeanUtils.populate(item, map);
						Product product = new Product();
						BeanUtils.populate(product, map);
						item.setProduct(product);
						order.getOrderItems().add(item);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/order_list.jsp").forward(request,
				response);

	}

	/*
	 * 提交订单
	 */

	public void submitOrder(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 判断用户是否登录
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		Order order = new Order();
		// 1.private String oid;// 该订单的订单号
		String oid = CommonUtils.getUUid();
		order.setOid(oid);
		// 1.private Date ordertime;// 下单时间
		order.setOrdertime(new Date());
		// 1.private double total;// 该订单的总金额
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart != null) {
			order.setTotal(cart.getSumTotol());
		}
		// 1.private int state;// 订单支付状态 1代表已付款 0代表未付款
		order.setState(0);
		// 1.private String address;// 收货地址
		order.setAddress(null);
		// 1.private String name;// 收货人
		order.setTelephone(null);
		// 1.private String telephone;// 收货人电话
		order.setUser(user);
		// 1.private User user;// 该订单属于哪个用户
		// // 该订单中有多少订单项List<OrderItem> orderItems = new ArrayList<OrderItem>();
		Map<String, CartItem> cartItems = cart.getCartItems();
		for (Map.Entry<String, CartItem> entry : cartItems.entrySet()) {
			CartItem cartItem = entry.getValue();
			OrderItem orderitem = new OrderItem();
			/*
			 * private String itemid;//订单项的id private int count;//订单项内商品的购买数量
			 * private double subtotal;//订单项小计 private Product
			 * product;//订单项内部的商品 private Order order;//该订单项属于哪个订单
			 */
			orderitem.setItemid(CommonUtils.getUUid());
			orderitem.setCount(cartItem.getBuyNum());
			orderitem.setSubtotal(cartItem.getSubtotol());
			orderitem.setProduct(cartItem.getProduct());
			orderitem.setOrder(order);
			order.getOrderItems().add(orderitem);
		}
		ProductService service = new ProductService();
		service.submitOrder(order);
		session.setAttribute("order", order);
		response.sendRedirect(request.getContextPath() + "/order_info.jsp");
	}

	/*
	 * 菜单
	 */
	public void categoryList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		List<Category> category = service.findAllCategory();
		response.setContentType("text/html;charset=utf-8");
		Gson gs = new Gson();
		response.getWriter().write(gs.toJson(category));
	}

	// 商品显示列表
	public void productList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");
		String currentPageStr = request.getParameter("currentPage");
		if (currentPageStr == null)
			currentPageStr = "1";
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 12;
		ProductService service = new ProductService();
		PageBean pagebean = service.findMethodProduct(cid, currentPage,
				currentCount);
		request.setAttribute("pagebean", pagebean);
		request.setAttribute("cid", cid);
		List<Product> historyProductList = new ArrayList<Product>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] split = pids.split("-");
					for (String pid : split) {
						Product product = service.findProductinfo(pid);
						historyProductList.add(product);
					}
				}
			}
		}
		request.setAttribute("historyProductList", historyProductList);
		request.getRequestDispatcher("/productlist.jsp").forward(request,
				response);
	}

	/*
	 * 添加购物车
	 */
	public void addcart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		CartItem cartItem = new CartItem();
		String pid = request.getParameter("pid");
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		int buyNum_x = buyNum;
		ProductService productinfo = new ProductService();
		Product product = productinfo.findProductinfo(pid);
		cartItem.setProduct(product);
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
		} else {
			if (cart.getCartItems().containsKey(product.getPid())) {
				buyNum = buyNum
						+ cart.getCartItems().get(product.getPid()).getBuyNum();
			}
		}
		double subtotol = product.getShop_price() * buyNum;
		cartItem.setBuyNum(buyNum);
		cartItem.setSubtotol(subtotol);

		double sumTotol = cart.getSumTotol() + buyNum_x
				* product.getShop_price();
		cart.getCartItems().put(product.getPid(), cartItem);
		cart.setSumTotol(sumTotol);
		session.setAttribute("cart", cart);

		// request.getRequestDispatcher("/cart.jsp").forward(request, response);
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}

	public void delAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		session.invalidate();
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}

	/*
	 * 删除某一项
	 */
	public void delCartItem(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		String pid = request.getParameter("pid");
		Map<String, CartItem> cartItems = cart.getCartItems();
		double jinE = 0.0;
		if (cartItems.containsKey(pid)) {
			jinE = cartItems.get(pid).getSubtotol();
			cartItems.remove(pid);
		}
		cart.setSumTotol(cart.getSumTotol() - jinE);
		cart.setCartItems(cartItems);
		session.setAttribute("cart", cart);
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}

	/*
	 * 商品详细信息
	 */
	public void productInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String formPage = request.getParameter("formpage");

		if (formPage == null)
			formPage = "1";
		if (formPage.equals("0")) {
			request.setAttribute("formpage", "0");
		} else {
			request.setAttribute("formpage", "1");
		}
		String currentPage = request.getParameter("currentPage");
		String cid = request.getParameter("cid");
		String pid = request.getParameter("pid");
		ProductService productinfo = new ProductService();
		Product product = productinfo.findProductinfo(pid);
		request.setAttribute("product", product);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("cid", cid);
		Cookie[] cookies = request.getCookies();
		String pids = pid;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids = cookie.getValue();
					String[] split = pids.split("-");
					List<String> list = Arrays.asList(split);
					LinkedList<String> linklist = new LinkedList<String>(list);
					if (linklist.contains(pid)) {
						linklist.remove(pid);
					}
					linklist.addFirst(pid);
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < linklist.size() && i < 7; i++) {
						sb.append(linklist.get(i));
						sb.append("-");
					}
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}
		Cookie cookie_pids = new Cookie("pids", pids);
		response.addCookie(cookie_pids);
		request.getRequestDispatcher("/product_info.jsp").forward(request,
				response);
	}

	/**
	 * 详细信息添加
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void orderAddXiangxi(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			IllegalAccessException, InvocationTargetException {

		Map<String, String[]> map = request.getParameterMap();
		Order order = new Order();
		BeanUtils.populate(order, map);
		ProductDao dao = new ProductDao();
		dao.orderAddXiangxi(order);

		// 获得 支付必须基本数据
		String orderid = order.getOid();
		// String money = request.getParameter(order.getTotal()+"");
		String money = "0.01";
		// 银行
		String pd_FrpId = request.getParameter("pd_FrpId");

		// 发给支付公司需要哪些数据
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString(
				"p1_MerId");
		String p2_Order = orderid;
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString(
				"callback");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);

		String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="
				+ pd_FrpId + "&p0_Cmd=" + p0_Cmd + "&p1_MerId=" + p1_MerId
				+ "&p2_Order=" + p2_Order + "&p3_Amt=" + p3_Amt + "&p4_Cur="
				+ p4_Cur + "&p5_Pid=" + p5_Pid + "&p6_Pcat=" + p6_Pcat
				+ "&p7_Pdesc=" + p7_Pdesc + "&p8_Url=" + p8_Url + "&p9_SAF="
				+ p9_SAF + "&pa_MP=" + pa_MP + "&pr_NeedResponse="
				+ pr_NeedResponse + "&hmac=" + hmac;

		// 重定向到第三方支付平台
		response.sendRedirect(url);

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
