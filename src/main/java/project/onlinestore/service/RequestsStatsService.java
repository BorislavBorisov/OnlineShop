package project.onlinestore.service;

import project.onlinestore.domain.view.RequestsStatsView;

public interface RequestsStatsService {

    void onRequest();

    RequestsStatsView getReqStats();
}
