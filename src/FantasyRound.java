import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class FantasyRound {
	private FantasyMatch[] rounds;
	
	public FantasyRound() {
		rounds = new FantasyMatch[6];
		for (int i = 0; i < 6; i++) {
			rounds[i] = new FantasyMatch();
		}
	}
	
	public long getWinnerId() {
		if (rounds[5] != null && rounds[5].isFinished() == true) {
			HashMap<Long, Integer> hm = new HashMap<Long, Integer>();
			for (int i = 0; i < 6; i++) {
				if (hm.containsKey(rounds[i].getWinnerId())) {
					hm.put(rounds[i].getWinnerId(), hm.get(rounds[i].getWinnerId()) + 1);
				} else {
					hm.put(rounds[i].getWinnerId(), 1);
				}
			}
			int occurrences = 0;
			long id = -1;
			Iterator it = hm.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        if ((int) pair.getValue() > occurrences) {
		        	id = (long) pair.getKey();
		        	occurrences = (int) pair.getValue();
		        }
		        it.remove();
		    }
		    return id;
		} else {
			return -1;
		}
	}
	
	public void setFantasyMatch(FantasyMatch fm, int n) {
		this.rounds[n] = fm;
	}
	
	public FantasyMatch getFantasyMatch(int n) {
		return this.rounds[n];
	}

	public FantasyMatch[] getRounds() {
		return rounds;
	}

	public void setRounds(FantasyMatch[] rounds) {
		this.rounds = rounds;
	}
}
