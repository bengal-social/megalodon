package org.joinmastodon.android.events;

public class NotificationDeletedEvent{
	public final String id;

	public NotificationDeletedEvent(String id){
		this.id=id;
  }
}
