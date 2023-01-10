package org.joinmastodon.android.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import org.joinmastodon.android.R;
import org.joinmastodon.android.model.ListTimeline;

public class ListTimelineEditor extends LinearLayout {
    private ListTimeline.RepliesPolicy policy = null;
    private TextInputFrameLayout input;
    private Button button;

    @SuppressLint("ClickableViewAccessibility")
    public ListTimelineEditor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.list_timeline_editor, this);

        button = findViewById(R.id.button);
        input = findViewById(R.id.input);

        PopupMenu popupMenu = new PopupMenu(context, button, Gravity.CENTER_HORIZONTAL);
        popupMenu.inflate(R.menu.list_reply_policies);
        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);

        button.setOnTouchListener(popupMenu.getDragToOpenListener());
        button.setOnClickListener(v->popupMenu.show());
        input.getEditText().setHint(context.getString(R.string.sk_list_name_hint));

        setRepliesPolicy(ListTimeline.RepliesPolicy.LIST);
    }

    public void applyList(ListTimeline list) {
        policy = list.repliesPolicy;
        input.getEditText().setText(list.title);
        setRepliesPolicy(list.repliesPolicy);
    }

    public String getTitle() {
        return input.getEditText().getText().toString();
    }

    public ListTimeline.RepliesPolicy getRepliesPolicy() {
        return policy;
    }

    public void setRepliesPolicy(ListTimeline.RepliesPolicy policy) {
        this.policy = policy;
        switch (policy) {
            case FOLLOWED -> button.setText(R.string.sk_list_replies_policy_followed);
            case LIST -> button.setText(R.string.sk_list_replies_policy_list);
            case NONE -> button.setText(R.string.sk_list_replies_policy_none);
        }
    }

    private boolean onMenuItemClick(MenuItem i) {
        if (i.getItemId() == R.id.reply_policy_none) {
            setRepliesPolicy(ListTimeline.RepliesPolicy.NONE);
        } else if (i.getItemId() == R.id.reply_policy_followed) {
            setRepliesPolicy(ListTimeline.RepliesPolicy.FOLLOWED);
        } else if (i.getItemId() == R.id.reply_policy_list) {
            setRepliesPolicy(ListTimeline.RepliesPolicy.LIST);
        }
        return true;
    }

    public ListTimelineEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ListTimelineEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListTimelineEditor(Context context) {
        this(context, null);
    }
}
