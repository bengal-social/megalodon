package org.joinmastodon.android.api.requests.accounts;

import com.google.gson.reflect.TypeToken;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.Account;
import org.joinmastodon.android.model.FollowSuggestion;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public class GetFollowRequests extends MastodonAPIRequest<List<Account>>{
	private String maxId;

	public GetFollowRequests(String maxID, String minID, int limit){
		super(HttpMethod.GET, "/follow_requests", new TypeToken<>(){});
		if(maxID!=null)
			addQueryParameter("max_id", maxID);
		if(minID!=null)
			addQueryParameter("min_id", minID);
		if(limit>0)
			addQueryParameter("limit", ""+limit);
	}

	@Override
	public void validateAndPostprocessResponse(List<Account> respObj, Response httpResponse) throws IOException {
		super.validateAndPostprocessResponse(respObj, httpResponse);
		// <https://mastodon.social/api/v1/follow_requests?max_id=268962>; rel="next",
		// <https://mastodon.social/api/v1/follow_requests?min_id=268981>; rel="prev"
		String link=httpResponse.header("link");
		// parsing link header by hand; using a library would be cleaner
		// (also, the functionality should be part of the max id logics and implemented in MastodonAPIRequest)
		if(link==null) return;
		String maxIdEq="max_id=";
		for(String s : link.split(",")) {
			if(s.contains("rel=\"next\"")) {
				int start=s.indexOf(maxIdEq)+maxIdEq.length();
				int end=s.indexOf('>');
				if(start<0 || start>end) return;
				this.maxId=s.substring(start, end);
			}
		}
	}

	public String getMaxId() {
		return maxId;
	}
}
