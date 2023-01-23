package org.joinmastodon.android.events;

public class ListDeletedEvent {
	public final String id;

	public ListDeletedEvent(String id) {
		this.id = id;
	}
}
