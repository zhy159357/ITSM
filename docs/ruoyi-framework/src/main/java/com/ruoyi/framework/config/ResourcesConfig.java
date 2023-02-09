package com.ruoyi.framework.config;

import com.ruoyi.common.config.BizFileConfig;
import com.ruoyi.framework.interceptor.AppMobileInterceptor;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.framework.interceptor.RepeatSubmitInterceptor;

import javax.annotation.Resource;

/**
 * 通用配置
 *
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    /**
     * 首页地址
     */
    @Value("${shiro.user.indexUrl}")
    private String indexUrl;
    @Autowired
    private BizFileConfig bizFileConfig;

    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Autowired
    private AppMobileInterceptor appMobileInterceptor;

    @Resource
    private CustomerBizInterceptor customerBizInterceptor;

    /**
     * 默认首页的设置，当输入域名是可以自动跳转到默认指定的网页
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("forward:" + indexUrl);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + RuoYiConfig.getProfile() + "/");
        registry.addResourceHandler(this.bizFileConfig.getPrefix() + "/**").addResourceLocations("file:" + this.bizFileConfig.getPath() + "/");
        /** swagger配置 */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
        // 先暂时只拦截app登录加密
        registry.addInterceptor(appMobileInterceptor).addPathPatterns("/appMobile/getToken/**");
        registry.addInterceptor(customerBizInterceptor).addPathPatterns("/customerForm/**").addPathPatterns("/system/work/incident/**");
    }
    @Bean
    public FilterRegistrationBean customCorsfilterRegistrationBean() {
        FilterRegistrationBean<CustomCorsFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CustomCorsFilter());
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}