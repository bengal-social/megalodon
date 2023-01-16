package org.joinmastodon.android.fragments;

import androidx.recyclerview.widget.RecyclerView;

public interface IsOnTop {
    boolean isOnTop();

    default boolean isRecyclerViewOnTop(RecyclerView list) {
        return !list.canScrollVertically(-1);
    }
}
