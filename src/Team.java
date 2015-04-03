public class Team {
	private FantasyChampion[] champions;
	private long id;
	
	public Team() {
		champions = new FantasyChampion[5];
	}
	
	public FantasyChampion getChampion(int i) {
		return champions[i];
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public int getPoints() {
		int total = 0;
		for (int i = 0; i < champions.length; i++) {
			total += champions[i].getTotalPoints();
		}
		return total;
	}
}
