package org.joinmastodon.android.ui.displayitems;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.joinmastodon.android.GlobalUserPreferences;
import org.joinmastodon.android.R;
import org.joinmastodon.android.api.session.AccountSessionManager;
import org.joinmastodon.android.fragments.BaseStatusListFragment;
import org.joinmastodon.android.fragments.ComposeFragment;
import org.joinmastodon.android.model.Status;
import org.joinmastodon.android.model.StatusPrivacy;
import org.joinmastodon.android.ui.utils.UiUtils;
import org.parceler.Parcels;

import me.grishka.appkit.Nav;
import me.grishka.appkit.utils.CubicBezierInterpolator;
import me.grishka.appkit.utils.V;

public class FooterStatusDisplayItem extends StatusDisplayItem{
	public final Status status;
	private final String accountID;
	public boolean hideCounts;

	public FooterStatusDisplayItem(String parentID, BaseStatusListFragment parentFragment, Status status, String accountID){
		super(parentID, parentFragment);
		this.status=status;
		this.accountID=accountID;
	}

	@Override
	public Type getType(){
		return Type.FOOTER;
	}

	public static class Holder extends StatusDisplayItem.Holder<FooterStatusDisplayItem>{
		private final TextView reply, boost, favorite, bookmark;
		private final ImageView share;
		private static final Animation opacityOut, opacityIn;

		private final View.AccessibilityDelegate buttonAccessibilityDelegate=new View.AccessibilityDelegate(){
			@Override
			public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info){
				super.onInitializeAccessibilityNodeInfo(host, info);
				info.setClassName(Button.class.getName());
				info.setText(item.parentFragment.getString(descriptionForId(host.getId())));
			}
		};

		static {
			opacityOut = new AlphaAnimation(1, 0.5f);
			opacityOut.setDuration(200);
			opacityOut.setInterpolator(CubicBezierInterpolator.DEFAULT);
			opacityOut.setFillAfter(true);
			opacityIn = new AlphaAnimation(0.5f, 1);
			opacityIn.setDuration(150);
			opacityIn.setInterpolator(CubicBezierInterpolator.DEFAULT);
		}

		public Holder(Activity activity, ViewGroup parent){
			super(activity, R.layout.display_item_footer, parent);
			reply=findViewById(R.id.reply);
			boost=findViewById(R.id.boost);
			favorite=findViewById(R.id.favorite);
			bookmark=findViewById(R.id.bookmark);
			share=findViewById(R.id.share);
			if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
				UiUtils.fixCompoundDrawableTintOnAndroid6(reply);
				UiUtils.fixCompoundDrawableTintOnAndroid6(boost);
				UiUtils.fixCompoundDrawableTintOnAndroid6(favorite);
				UiUtils.fixCompoundDrawableTintOnAndroid6(bookmark);
			}
			View reply=findViewById(R.id.reply_btn);
			View boost=findViewById(R.id.boost_btn);
			View favorite=findViewById(R.id.favorite_btn);
			View share=findViewById(R.id.share_btn);
			View bookmark=findViewById(R.id.bookmark_btn);
			reply.setOnTouchListener(this::onButtonTouch);
			reply.setOnClickListener(this::onReplyClick);
			reply.setAccessibilityDelegate(buttonAccessibilityDelegate);
			boost.setOnTouchListener(this::onButtonTouch);
			boost.setOnClickListener(this::onBoostClick);
			boost.setAccessibilityDelegate(buttonAccessibilityDelegate);
			favorite.setOnTouchListener(this::onButtonTouch);
			favorite.setOnClickListener(this::onFavoriteClick);
			favorite.setAccessibilityDelegate(buttonAccessibilityDelegate);
			bookmark.setOnTouchListener(this::onButtonTouch);
			bookmark.setOnClickListener(this::onBookmarkClick);
			bookmark.setAccessibilityDelegate(buttonAccessibilityDelegate);
			share.setOnTouchListener(this::onButtonTouch);
			share.setOnClickListener(this::onShareClick);
			share.setAccessibilityDelegate(buttonAccessibilityDelegate);
		}

		@Override
		public void onBind(FooterStatusDisplayItem item){
			bindButton(reply, item.status.repliesCount);
			bindButton(boost, item.status.reblogsCount);
			bindButton(favorite, item.status.favouritesCount);
			boost.setSelected(item.status.reblogged);
			favorite.setSelected(item.status.favourited);
			bookmark.setSelected(item.status.bookmarked);
			boost.setEnabled(item.status.visibility==StatusPrivacy.PUBLIC || item.status.visibility==StatusPrivacy.UNLISTED
					|| (item.status.visibility==StatusPrivacy.PRIVATE && item.status.account.id.equals(AccountSessionManager.getInstance().getAccount(item.accountID).self.id)));
		}

		private void bindButton(TextView btn, long count){
			if(GlobalUserPreferences.showInteractionCounts && count>0 && !item.hideCounts){
				btn.setText(UiUtils.abbreviateNumber(count));
				btn.setCompoundDrawablePadding(V.dp(8));
			}else{
				btn.setText("");
				btn.setCompoundDrawablePadding(0);
			}
		}

		private void onReplyClick(View v){
			Bundle args=new Bundle();
			args.putString("account", item.accountID);
			args.putParcelable("replyTo", Parcels.wrap(item.status));
			Nav.go(item.parentFragment.getActivity(), ComposeFragment.class, args);
		}

		private boolean onButtonTouch(View v, MotionEvent event){
			int action = event.getAction();
			// 20dp to center in middle of icon, because: (icon width = 24dp) / 2 + (paddingStart = 8dp)
			v.setPivotX(V.dp(20));
			if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
				v.animate().scaleX(1).scaleY(1).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(100).start();
				if (action == MotionEvent.ACTION_UP) v.performClick();
			} else if (action == MotionEvent.ACTION_DOWN) {
				v.animate().scaleX(0.9f).scaleY(0.9f).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(50).start();
			}
			return true;
		}

		private void onBoostClick(View v){
			v.startAnimation(opacityOut);
			boost.setSelected(!item.status.reblogged);
			AccountSessionManager.getInstance().getAccount(item.accountID).getStatusInteractionController().setReblogged(item.status, !item.status.reblogged, r->{
				v.startAnimation(opacityIn);
				bindButton(boost, r.reblogsCount);
			});
		}

		private void onFavoriteClick(View v){
			v.startAnimation(opacityOut);
			favorite.setSelected(!item.status.favourited);
			AccountSessionManager.getInstance().getAccount(item.accountID).getStatusInteractionController().setFavorited(item.status, !item.status.favourited, r->{
				v.startAnimation(opacityIn);
				bindButton(favorite, r.favouritesCount);
			});
		}

		private void onBookmarkClick(View v){
			v.startAnimation(opacityOut);
			bookmark.setSelected(item.status.bookmarked);
			AccountSessionManager.getInstance().getAccount(item.accountID).getStatusInteractionController().setBookmarked(item.status, !item.status.bookmarked, r->{
				v.startAnimation(opacityIn);
			});
		}

		private void onShareClick(View v){
			Intent intent=new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, item.status.url);
			v.getContext().startActivity(Intent.createChooser(intent, v.getContext().getString(R.string.share_toot_title)));
		}

		private int descriptionForId(int id){
			if(id==R.id.reply_btn)
				return R.string.button_reply;
			if(id==R.id.boost_btn)
				return R.string.button_reblog;
			if(id==R.id.favorite_btn)
				return R.string.button_favorite;
			if(id==R.id.bookmark_btn)
				return R.string.add_bookmark;
			if(id==R.id.share_btn)
				return R.string.button_share;
			return 0;
		}
	}
}
