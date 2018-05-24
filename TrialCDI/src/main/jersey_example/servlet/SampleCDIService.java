package servlet;

import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;

//@RequestScoped
@ApplicationScoped
@ManagedBean
//@SessionScoped   //NG
public class SampleCDIService {

    private int counter = 0;

    public void countUp() {
        this.counter++;
    }

    public int getCounter() {
        return this.counter;
    }
}
