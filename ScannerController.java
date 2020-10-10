package cn.sunline.insd.cut.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

import cn.sunline.insd.cut.api.protocol.req.BoltCustReq;
import cn.sunline.insd.cut.api.protocol.req.notice.NewNoticecountReq;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/**
**  接口文档工具类
*/

public class ScannerController {
	
	//1、打印出bean实体类的名字、数值类型、注释等
	@Test
	public void scannerClass() {
		for(Field field : NewNoticecountReq.class.getDeclaredFields()) {
			if(!field.isAnnotationPresent(ApiModelProperty.class)) continue ;
			if(field.getType().getSimpleName().equals("List")) {
				System.out.println("此处需要单独获取: "+field.getName());
				continue;
			}
			System.out.println(field.getName()+"\t"+field.getType().getSimpleName()
					+"\t"+field.getAnnotation(ApiModelProperty.class).required()
					+"\t"+field.getAnnotation(ApiModelProperty.class).value());
		}
	}
	
	//打印controller中方法
	@Test
	public void scannerFunction() {
		for(Method method : NoticeMsgController.class.getMethods()) {
			if(!method.isAnnotationPresent(RequestMapping.class)) continue ;
			System.out.println(
					method.getAnnotation(ApiOperation.class).value()
					+"("+method.getAnnotation(RequestMapping.class).value()[0]+")");
			System.out.println("接口请求地址： ");
			System.out.println("http://192.144.139.88:9012/cut/"+method.getAnnotation(RequestMapping.class).value()[0]);
			System.out.println();
		}
	}
	
	
	// 拼接实体类报文
	@Test
	public void baoWen() throws Exception {
		baoWenFun(BoltCustReq.class);
	}
	
	public void baoWenFun(Class class1) throws Exception {
		Object obj = class1.getConstructor().newInstance();
		
		for(Field field : obj.getClass().getDeclaredFields()) {
			if(!field.isAnnotationPresent(ApiModelProperty.class)) continue ;
				switch(field.getType().getSimpleName()) {
					case "String":
						field.setAccessible(true);
						if(field.getAnnotation(ApiModelProperty.class).required()) {
							field.set(obj,"测试String");
						}else {
							field.set(obj,"");
						}
						break;
				}
		}
		System.out.println(JSON.toJSONString(obj));
	}
}
