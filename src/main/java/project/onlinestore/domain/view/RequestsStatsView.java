package project.onlinestore.domain.view;

public class RequestsStatsView {

    private int authenticatedRequests;
    private int anonymousRequests;

    public RequestsStatsView(int authenticatedRequests, int anonymousRequests) {
        this.authenticatedRequests = authenticatedRequests;
        this.anonymousRequests = anonymousRequests;
    }

    public int getTotalRequests() {
        return authenticatedRequests + anonymousRequests;
    }

    public int getAuthenticatedRequests() {
        return authenticatedRequests;
    }


    public int getAnonymousRequests() {
        return anonymousRequests;
    }
}
