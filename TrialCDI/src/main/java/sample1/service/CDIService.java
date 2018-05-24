package sample1.service;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CDIService {

	private int count = 0;
	public int countUp() {
		count++;
		return count;
	}

}