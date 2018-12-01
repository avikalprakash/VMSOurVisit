package lueorganisation.winmall.via.ourvisitor.model;

public class Branch {
    int org_id;
    String branch;

    public Branch(int org_id,String branch) {
        this.org_id = org_id;
        this.branch=branch;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
