package com.lz.filter;

import com.lz.jwt.JWTToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 乐。
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter implements Filter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            return executeLogin(request,response);
        }catch (Exception e){
            log.debug("invoke======>JwtFilter过滤验证失败");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Token");
       /* if (token == null){
            return true;
        }*/
        JWTToken jwtToken = new JWTToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        try {
            getSubject(request, response).login(jwtToken);
            // 如果没有抛出异常则代表登入成功，返回true
            return true;
        } catch (AuthenticationException e) {
            /*ResponseData responseData = ResponseDataUtil.authorizationFailed( "没有访问权限，原因是:" + e.getMessage());
            //SerializerFeature.WriteMapNullValue为了null属性也输出json的键值对
            Object o = JSONObject.toJSONString(responseData, SerializerFeature.WriteMapNullValue);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(o);*/
            log.debug("invoke======>没有访问权限！");
            e.printStackTrace();
            return false;
        }
    }
        /**
         * 对跨域提供支持
         * @param request
         * @param response
         * @return
         * @throws Exception
         */
        @Override
        protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
            // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
            if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
                httpServletResponse.setStatus(HttpStatus.OK.value());
                return false;
            }
            return super.preHandle(request, response);
        }
}
