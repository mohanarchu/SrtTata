package cbots.b_to_c.team_leader.team;

public class TeamArray {

    String teamName, Id;
    boolean setActive;

    public TeamArray(String teamName,String id){
        this.teamName  = teamName;
        this.Id = id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public boolean isActive() {
        return setActive;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setActive(boolean array) {
        this.setActive = array;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

}
