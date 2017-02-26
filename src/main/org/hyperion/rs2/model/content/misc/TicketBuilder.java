package org.hyperion.rs2.model.content.misc;

public class TicketBuilder {
	private String reason;
	
	public String getReason() {
		return reason;
	}
	
	private long enteredTime;
	
	public long startTime() {
		return enteredTime;
	}
	
	private boolean answered = false;
	
	public void answerTicket() {
		answered = true;
		reason = "@str@"+reason;
	}
	
	public boolean isAnswered() {
		return answered;
	}
	
	public TicketBuilder(String reason, long enteredTime) {
		this.reason = reason;
		this.enteredTime = enteredTime;
	}
	
}
