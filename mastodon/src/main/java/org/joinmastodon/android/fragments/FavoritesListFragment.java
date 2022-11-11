package org.joinmastodon.android.fragments;

import android.os.Bundle;
import org.joinmastodon.android.R;
import org.joinmastodon.android.api.requests.accounts.GetFavourites;
import org.joinmastodon.android.api.session.AccountSession;
import org.joinmastodon.android.api.session.AccountSessionManager;
import org.joinmastodon.android.model.Status;

import java.util.List;

import me.grishka.appkit.api.SimpleCallback;

public class FavoritesListFragment extends StatusListFragment{

    private String accountID;
    private String lastMaxId=null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        accountID=getArguments().getString("account");
        AccountSession session=AccountSessionManager.getInstance().getAccount(accountID);
        setTitle(R.string.favorited_posts);
    }

    @Override
    protected void onShown(){
        super.onShown();
        if(!getArguments().getBoolean("noAutoLoad") && !loaded && !dataLoading)
            loadData();
    }

    @Override
    protected void doLoadData(int offset, int count) {
        GetFavourites b=new GetFavourites(offset>0 ? lastMaxId : null, null, count);
        currentRequest=b.setCallback(new SimpleCallback<>(this){
            @Override
            public void onSuccess(List<Status> result){
                onDataLoaded(result, b.getMaxId()!=null);
                lastMaxId=b.getMaxId();
            }
        })
                .exec(accountID);
    }
}
