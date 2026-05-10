package server_2026_b.server.responses;

import server_2026_b.server.entities.WorkingSite;

import java.util.List;

public class SiteListResponse extends BasicResponse{
    //endpoint שלנו מחזיר רשימה של אתרים
    private List<WorkingSite> workingSites;

    public SiteListResponse(boolean success , Integer errorCode ,List<WorkingSite> workingSites) {
        super(success , errorCode);
        this.workingSites = workingSites;
    }

    public List<WorkingSite> getWorkingSites() {
        return workingSites;
    }

    public void setWorkingSites(List<WorkingSite> workingSites) {
        this.workingSites = workingSites;
    }
}
