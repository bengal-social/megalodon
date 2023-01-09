package org.joinmastodon.android.model;

import org.joinmastodon.android.api.RequiredField;
import org.parceler.Parcel;

import java.time.Instant;
import java.util.List;

@Parcel
public class Announcement extends BaseModel implements DisplayItemsParent {
    @RequiredField
    public String id;
    @RequiredField
    public String content;
    public Instant startsAt;
    public Instant endsAt;
    public boolean published;
    public boolean allDay;
    public Instant publishedAt;
    public Instant updatedAt;
    public boolean read;
    public List<Emoji> emojis;
    public List<Mention> mentions;
    public List<Hashtag> tags;

    @Override
    public String toString() {
        return "Announcement{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", startsAt=" + startsAt +
                ", endsAt=" + endsAt +
                ", published=" + published +
                ", allDay=" + allDay +
                ", publishedAt=" + publishedAt +
                ", updatedAt=" + updatedAt +
                ", read=" + read +
                ", emojis=" + emojis +
                ", mentions=" + mentions +
                ", tags=" + tags +
                '}';
    }

    public Status toStatus() {
        Status s = new Status();
        s.id = id;
        s.mediaAttachments = List.of();
        s.createdAt = startsAt != null ? startsAt : publishedAt;
        if (updatedAt != null) s.editedAt = updatedAt;
        s.content = s.text = content;
        s.spoilerText = "";
        s.visibility = StatusPrivacy.PUBLIC;
        s.mentions = List.of();
        s.tags = List.of();
        s.emojis = List.of();
        return s;
    }

    @Override
    public String getID() {
        return id;
    }
}
