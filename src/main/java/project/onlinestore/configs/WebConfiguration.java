package project.onlinestore.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.onlinestore.web.interceptors.PagesViewsCountInterceptor;
import project.onlinestore.web.interceptors.RequestsStatsInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final PagesViewsCountInterceptor pagesViewsCountInterceptor;
    private final RequestsStatsInterceptor requestsStatsInterceptor;

    public WebConfiguration(PagesViewsCountInterceptor pagesViewsCountInterceptor, RequestsStatsInterceptor requestsStatsInterceptor) {
        this.pagesViewsCountInterceptor = pagesViewsCountInterceptor;
        this.requestsStatsInterceptor = requestsStatsInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pagesViewsCountInterceptor);
        registry.addInterceptor(requestsStatsInterceptor);
    }
}
