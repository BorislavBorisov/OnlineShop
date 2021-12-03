package project.onlinestore.service;

public interface PagesViewCountService {

    void onRequest(String reqUrl);

    Integer getPageViews(String reqUrl);
}
