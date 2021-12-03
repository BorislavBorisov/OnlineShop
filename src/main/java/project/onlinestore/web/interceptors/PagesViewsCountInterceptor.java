package project.onlinestore.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.onlinestore.service.PagesViewCountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PagesViewsCountInterceptor implements HandlerInterceptor {

    private final PagesViewCountService pagesViewCountService;

    public PagesViewsCountInterceptor(PagesViewCountService pagesViewCountService) {
        this.pagesViewCountService = pagesViewCountService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        pagesViewCountService.onRequest(request.getRequestURI());
        return true;
    }
}
