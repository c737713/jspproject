package com.example.jsp.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.example.jsp.commons.exception.ProjectException;
import com.example.jsp.commons.model.Transporter;
import com.example.jsp.pojo.Store;
import com.example.jsp.pojo.User;
import com.example.jsp.service.OrderService;
import com.example.jsp.service.ProductService;
import com.example.jsp.service.StoreService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 橙鼠鼠
 */
@RestController
@RequestMapping("/store")
public class StoreController {
	private StoreService storeService;
	private OrderService orderService;
	private ProductService productService;


	@Autowired
	public void setService (StoreService storeService) {
		this.storeService = storeService;
	}

	@Autowired
	private  void setOrderService(OrderService orderService){
		this.orderService=orderService;
	}

	@Autowired
	private void setProductService(ProductService productService){
		this.productService=productService;
	}

	@GetMapping("/enroll/{username}/{password}/{name}/{address}/{telephone}")
	@Transactional(rollbackFor = Exception.class)
	public Transporter enroll (@PathVariable("username") String username,
	                           @PathVariable("password") String password,
	                           @PathVariable("name") String name,
	                           @PathVariable("address") String address,
	                           @PathVariable("telephone") String telephone) throws ProjectException {
		final var user = new User()
				.setUsername(username)
				.setPassword(password);
		final var store = new Store()
				.setName(name)
				.setTelephone(telephone)
				.setAddress(address)
				.setUser(user);
		storeService.enroll(store, user);
		return new Transporter().setMsg("注册成功");
	}

	/**
	 *商家接单
	 */

	@SaCheckRole("store")
	@GetMapping("/take/{orderId}")
	@Transactional(rollbackFor = Exception.class)
	public Transporter takeOrder (@PathVariable("orderId") Integer orderId) throws ProjectException{
		Transporter transporter = new Transporter();
		val select=orderService.select(orderId);
		val status =select.getStatus();
		select.setStatus(status+1);

		transporter.addData("status",status+1)
				.setMsg("接单成功");
		return transporter;
	}


	/**
	 * 商品列表显示：
	 * 显示商品信息
	 */
	@SaCheckRole("store")
	@GetMapping("/showproduct/{storeId}")
	@Transactional(rollbackFor = Exception.class)
	public Transporter showProduct(@PathVariable("storeId") Integer storeId) throws ProjectException{
		Transporter transporter = new Transporter();
		val select = storeService.select();
		val product=productService.select();
		transporter.addData("store",select);
		transporter.addData("product",product);
		return transporter;
	}

	/**
	 * 管理商家页
	 * 编辑
	 */
	@SaCheckRole("store")
	@GetMapping("/edit/{storeid}/{storename}/{storetel}/{storeaddress}")
	@Transactional(rollbackFor = Exception.class)
	public Transporter edit(@PathVariable("storeid") Integer storeId,
							@PathVariable("storename") String storeName,
							@PathVariable("storetel") String storeTel,
							@PathVariable("storeaddress") String storeAddress) throws ProjectException{
		val select = storeService.select(storeId);
		select.setName(storeName)
				.setTelephone(storeTel)
				.setAddress(storeAddress);
		storeService.restore(select);
		return new Transporter().setMsg("编辑成功");
	}

	/**
	 * 管理商家页
	 * 删除
	 */
	@SaCheckRole("guest")
	@GetMapping("/delete/{storeid}")
	@Transactional(rollbackFor = Exception.class)
	public Transporter delete(@PathVariable("storeid") Integer storeId) throws ProjectException{
		storeService.delete(storeId);
		return new Transporter().setMsg("删除成功");
	}

}

