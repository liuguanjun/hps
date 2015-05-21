package com.my.hps.webapp.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.my.hps.webapp.controller.vo.ResponceError;

public class HpsSimpleMappingExceptionResolver extends
		SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		try {
			PrintWriter writer = response.getWriter();
			ResponceError error = new ResponceError();
			String msg = ex.getMessage();
			if (StringUtils.isEmpty(msg)) {
				msg = "服务器运行时发生错误(" + ex.getClass().getSimpleName() + ")";
			}
			error.setErrorMsg(msg);
			JSONObject jsonObject = JSONObject.fromObject(error);
			writer.write(jsonObject.toString());
			writer.flush();
		} catch (IOException e) {
		}
		return null;
	}
}