package project.onlinestore.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.view.RequestsStatsView;
import project.onlinestore.service.RequestsStatsService;

@Service
public class RequestsStatsServiceImpl implements RequestsStatsService {

    private int authenticatedReq, anonymousReq;

    @Override
    public void onRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && (authentication.getPrincipal()) instanceof UserDetails) {
            authenticatedReq++;
        } else {
            anonymousReq++;
        }
    }

    @Override
    public RequestsStatsView getReqStats() {
        return new RequestsStatsView(authenticatedReq, anonymousReq);
    }
}
