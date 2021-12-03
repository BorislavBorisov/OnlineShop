package project.onlinestore.service.impl;

import org.springframework.stereotype.Service;
import project.onlinestore.service.PagesViewCountService;

import java.util.HashMap;
import java.util.Map;

@Service
public class PagesViewCountServiceImpl implements PagesViewCountService {

    private Map<String, Integer> pagesViewsCount;

    public PagesViewCountServiceImpl() {
        this.pagesViewsCount = new HashMap<>();
    }

    @Override
    public void onRequest(String reqUrl) {
        pagesViewsCount.put(reqUrl, pagesViewsCount.getOrDefault(reqUrl, 0) + 1);
    }

    @Override
    public Integer getPageViews(String reqUrl) {
        return pagesViewsCount.get(reqUrl);
    }
}
