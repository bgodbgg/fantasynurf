import java.util.ArrayList;


public class FantasyMatch {

	private ArrayList<Team> teams;
	private boolean finished = false;
	
	public FantasyMatch() {
		this.teams = new ArrayList<Team>();
	}
	
	public void addTeam(Team t) {
		teams.add(t);
	}
	
	public Team getTeam(long id) {
		for (int i = 0; i < teams.size(); i++) {
			if (teams.get(i).getId() == id) {
				return teams.get(i);
			}
		}
		return null;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public long getWinnerId() {
		int max = 0;
		int index = -1;
		for (int i = 0; i < teams.size(); i++) {
			if (teams.get(i).getPoints() > max) {
				max = teams.get(i).getPoints();
				index = i;
			}
		}
		return teams.get(index).getId();
	}
	
	
}
