package org.joinmastodon.android.events;

import org.joinmastodon.android.model.ListTimeline;

public class ListUpdatedCreatedEvent {
	public final String id;
	public final String title;
	public final ListTimeline.RepliesPolicy repliesPolicy;

	public ListUpdatedCreatedEvent(String id, String title, ListTimeline.RepliesPolicy repliesPolicy) {
		this.id = id;
		this.title = title;
		this.repliesPolicy = repliesPolicy;
	}
}
