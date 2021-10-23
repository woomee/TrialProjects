package proto.greeting.custom;

public class GreetingCustom {

    private final long id;
    private final String content;
    private final String dateTime;

	public GreetingCustom(long id, String content, String dateTime) {
        this.id = id;
        this.content = content;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }


    public String getDateTime() {
		return dateTime;
	}

}