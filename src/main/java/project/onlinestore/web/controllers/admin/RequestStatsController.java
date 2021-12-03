package project.onlinestore.web.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.onlinestore.service.RequestsStatsService;

@Controller
public class RequestStatsController {

    private final RequestsStatsService requestsStatsService;


    public RequestStatsController(RequestsStatsService requestsStatsService) {
        this.requestsStatsService = requestsStatsService;
    }

    @GetMapping("/stats")
    public String stats(Model model){
        model.addAttribute("stats", requestsStatsService.getReqStats());
        return "/admin/request-stats";
    }
}
