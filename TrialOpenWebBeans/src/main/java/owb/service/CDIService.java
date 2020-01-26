package owb.service;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class CDIService {

	private int count = 0;
	public int countUp() {
		count++;
		return count;
	}

}