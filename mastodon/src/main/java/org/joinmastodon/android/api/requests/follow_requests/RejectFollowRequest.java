package org.joinmastodon.android.api.requests.follow_requests;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.Relationship;

public class RejectFollowRequest extends MastodonAPIRequest<Relationship>{
    public RejectFollowRequest(String id){
        super(HttpMethod.POST, "/follow_requests/"+id+"/reject", Relationship.class);
        setRequestBody(new Object());
    }
}
