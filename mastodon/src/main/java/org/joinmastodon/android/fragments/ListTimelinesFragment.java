package org.joinmastodon.android.fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.joinmastodon.android.R;
import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.api.requests.lists.AddAccountsToList;
import org.joinmastodon.android.api.requests.lists.CreateList;
import org.joinmastodon.android.api.requests.lists.GetLists;
import org.joinmastodon.android.api.requests.lists.RemoveAccountsFromList;
import org.joinmastodon.android.model.ListTimeline;
import org.joinmastodon.android.ui.M3AlertDialogBuilder;
import org.joinmastodon.android.ui.views.ListTimelineEditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.grishka.appkit.Nav;
import me.grishka.appkit.api.Callback;
import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.api.SimpleCallback;
import me.grishka.appkit.fragments.BaseRecyclerFragment;
import me.grishka.appkit.utils.BindableViewHolder;
import me.grishka.appkit.views.UsableRecyclerView;

public class ListTimelinesFragment extends BaseRecyclerFragment<ListTimeline> implements ScrollableToTop {
    private static final int LIST_DELETED_RESULT = 987;

    private String accountId;
    private String profileAccountId;
    private String profileDisplayUsername;
    private HashMap<String, Boolean> userInListBefore = new HashMap<>();
    private HashMap<String, Boolean> userInList = new HashMap<>();
    private int inProgress = 0;
    private ListsAdapter adapter;

    public ListTimelinesFragment() {
        super(10);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        accountId=args.getString("account");

        if(args.containsKey("profileAccount")){
            profileAccountId=args.getString("profileAccount");
            profileDisplayUsername=args.getString("profileDisplayUsername");
            setTitle(getString(R.string.sk_lists_with_user, profileDisplayUsername));
            setHasOptionsMenu(true);
        }
    }

    @Override
    protected void onShown(){
        super.onShown();
        if(!getArguments().getBoolean("noAutoLoad") && !loaded && !dataLoading)
            loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create) {
            ListTimelineEditor editor = new ListTimelineEditor(getContext());
            new M3AlertDialogBuilder(getActivity())
                    .setTitle(R.string.sk_create_list_title)
                    .setView(editor)
                    .setPositiveButton(R.string.sk_create, (d, which) -> {
                        new CreateList(editor.getTitle(), editor.getRepliesPolicy()).setCallback(new Callback<>() {
                            @Override
                            public void onSuccess(ListTimeline list) {
                                saveListMembership(list.id, true);
                                data.add(0, list);
                                adapter.notifyItemRangeInserted(0, 1);
                            }

                            @Override
                            public void onError(ErrorResponse error) {
                                error.showToast(getContext());
                            }
                        }).exec(accountId);
                    })
                    .setNegativeButton(R.string.cancel, (d, which) -> {})
                    .show();
        }
        return true;
    }

    private void saveListMembership(String listId, boolean isMember) {
        userInList.put(listId, isMember);
        List<String> accountIdList = Collections.singletonList(profileAccountId);
        MastodonAPIRequest<Object> req = isMember ? new AddAccountsToList(listId, accountIdList) : new RemoveAccountsFromList(listId, accountIdList);
        req.setCallback(new SimpleCallback<>(this) {
            @Override
            public void onSuccess(Object o) {}
        }).exec(accountId);
    }

    @Override
    protected void doLoadData(int offset, int count){
        userInListBefore.clear();
        userInList.clear();
        currentRequest=(profileAccountId != null ? new GetLists(profileAccountId) : new GetLists())
                .setCallback(new SimpleCallback<>(this) {
                    @Override
                    public void onSuccess(List<ListTimeline> lists) {
                        for (ListTimeline l : lists) userInListBefore.put(l.id, true);
                        userInList.putAll(userInListBefore);
                        if (profileAccountId == null || !lists.isEmpty()) onDataLoaded(lists, false);
                        if (profileAccountId == null) return;

                        currentRequest=new GetLists().setCallback(new SimpleCallback<>(ListTimelinesFragment.this) {
                            @Override
                            public void onSuccess(List<ListTimeline> allLists) {
                                List<ListTimeline> newLists = new ArrayList<>();
                                for (ListTimeline l : allLists) {
                                    if (lists.stream().noneMatch(e -> e.id.equals(l.id))) newLists.add(l);
                                    if (!userInListBefore.containsKey(l.id)) {
                                        userInListBefore.put(l.id, false);
                                    }
                                }
                                userInList.putAll(userInListBefore);
                                onDataLoaded(newLists, false);
                            }
                        }).exec(accountId);
                    }
                })
                .exec(accountId);
    }

    @Override
    public void onFragmentResult(int reqCode, boolean listDeleted, Bundle result){
        if (reqCode == LIST_DELETED_RESULT && listDeleted) {
            String listID = result.getString("listID");

            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).id.equals(listID)) {
                    data.remove(i);
                    adapter.notifyItemRemoved(i);
                    break;
                }
            }
        }
    }

    @Override
    protected RecyclerView.Adapter<ListViewHolder> getAdapter() {
        return adapter = new ListsAdapter();
    }

    @Override
    public void scrollToTop() {
        smoothScrollRecyclerViewToTop(list);
    }

    private class ListsAdapter extends RecyclerView.Adapter<ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            return new ListViewHolder();
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            holder.bind(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class ListViewHolder extends BindableViewHolder<ListTimeline> implements UsableRecyclerView.Clickable{
        private final TextView title;
        private final CheckBox listToggle;

        public ListViewHolder(){
            super(getActivity(), R.layout.item_text, list);
            title=findViewById(R.id.title);
            listToggle=findViewById(R.id.list_toggle);
        }

        @Override
        public void onBind(ListTimeline item) {
            title.setText(item.title);
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(itemView.getContext().getDrawable(R.drawable.ic_fluent_people_community_24_regular), null, null, null);
            if (profileAccountId != null) {
                Boolean checked = userInList.get(item.id);
                listToggle.setVisibility(View.VISIBLE);
                listToggle.setChecked(userInList.containsKey(item.id) && checked != null && checked);
                listToggle.setOnClickListener(this::onClickToggle);
            } else {
                listToggle.setVisibility(View.GONE);
            }
        }

        private void onClickToggle(View view) {
            saveListMembership(item.id, listToggle.isChecked());
        }

        @Override
        public void onClick() {
            Bundle args=new Bundle();
            args.putString("account", accountId);
            args.putString("listID", item.id);
            args.putString("listTitle", item.title);
            Nav.goForResult(getActivity(), ListTimelineFragment.class, args, LIST_DELETED_RESULT, ListTimelinesFragment.this);
        }
    }
}
