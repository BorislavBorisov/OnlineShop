package project.onlinestore.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.onlinestore.service.RequestsStatsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestsStatsInterceptor implements HandlerInterceptor {

    private final RequestsStatsService requestsStatsService;

    public RequestsStatsInterceptor(RequestsStatsService requestsStatsService) {
        this.requestsStatsService = requestsStatsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        requestsStatsService.onRequest();
        return true;
    }
}
