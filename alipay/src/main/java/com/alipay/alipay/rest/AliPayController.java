package com.alipay.alipay.rest;

import com.alipay.alipay.util.AliPayUtil;
import com.alipay.alipay.util.ParamUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 支付宝支付接口类
 *
 * @author changzhenzhen
 * @date 2018-08-24
 */
@Controller
@RequestMapping("/alipay")
public class AliPayController {

    private static final Logger logger = LoggerFactory.getLogger(AliPayController.class);

    @GetMapping("/alipay")
    public void aplipay(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        try {
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(ParamUtil.GATE_WAY, ParamUtil.APP_ID, ParamUtil.PRIVATE_KEY, ParamUtil.PARAM_FORMAT, ParamUtil.CHARTSET, ParamUtil.ALIPAY_PUBLIC_KEY, ParamUtil.SIGN_TYPE);
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
            alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址
            //SDK已经封装掉了公共参数，这里只需要传入业务参数
            //此次只是参数展示，未进行字符串转义，实际情况下请转义
            alipayRequest.setBizContent("{" +
                    " \"out_trade_no\":\"20150320010101002\"," +
                    " \"total_amount\":\"88.88\"," +
                    " \"subject\":\"Iphone6 16G\"," +
                    " \"product_code\":\"QUICK_WAP_PAY\"" +
                    " }");//填充业务参数
            String form = "";

            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单

            httpResponse.setContentType("text/html;charset=" + ParamUtil.CHARTSET);
            httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } catch (AlipayApiException e) {
            logger.info("支付时抛出了异常" + e.getMessage());
            e.printStackTrace();
        }
    }
}
