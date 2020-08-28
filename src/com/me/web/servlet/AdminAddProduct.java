package com.me.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
import java.lang.reflect.InvocationTargetException;
 
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.me.doman.Category;
import com.me.doman.Product;
import com.me.service.adminService;
import com.me.util.CommonUtils;

public class AdminAddProduct extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AdminAddProduct() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Product product = new Product();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 创建磁盘文件项工程
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 创建文件上唇核心对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解析request文件
			List<FileItem> list = upload.parseRequest(request);

			for (FileItem item : list) {
				// 判断是否是普通表单项
				boolean formField = item.isFormField();
				if (formField) {
					// 普通表单项
					String fieldName = item.getFieldName();
					String fieldValue = item.getString("utf-8");
					map.put(fieldName, fieldValue);

				} else {
					//
					String fileName = item.getName();
					String path = this.getServletContext()
							.getRealPath("upload");
					InputStream input = item.getInputStream();
					OutputStream output = new FileOutputStream(path + "/" + fileName);
					IOUtils.copy(input, output);
					map.put("pimage", "upload"+ "/" + fileName);
					output.close();
					input.close();
					item.delete();
				}
			}
			BeanUtils.populate(product, map);
			product.setPid(CommonUtils.getUUid());
			
			product.setPdate(new Date());
			product.setPflag(0);
			Category category= new Category();
			category.setCid(map.get("cid").toString());
			product.setCategory(category);
			//将product传递给service
			adminService service= new adminService();
			System.out.println(product);
			service.saveProduct(product);
			
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
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
