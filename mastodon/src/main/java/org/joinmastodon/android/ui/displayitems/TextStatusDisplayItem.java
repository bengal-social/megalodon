package org.joinmastodon.android.ui.displayitems;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joinmastodon.android.R;
import org.joinmastodon.android.api.requests.statuses.TranslateStatus;
import org.joinmastodon.android.api.session.AccountSession;
import org.joinmastodon.android.api.session.AccountSessionManager;
import org.joinmastodon.android.fragments.BaseStatusListFragment;
import org.joinmastodon.android.model.Instance;
import org.joinmastodon.android.model.Status;
import org.joinmastodon.android.model.StatusPrivacy;
import org.joinmastodon.android.model.TranslatedStatus;
import org.joinmastodon.android.ui.text.HtmlParser;
import org.joinmastodon.android.ui.utils.CustomEmojiHelper;
import org.joinmastodon.android.ui.views.LinkedTextView;

import me.grishka.appkit.api.Callback;
import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.imageloader.ImageLoaderViewHolder;
import me.grishka.appkit.imageloader.MovieDrawable;
import me.grishka.appkit.imageloader.requests.ImageLoaderRequest;

public class TextStatusDisplayItem extends StatusDisplayItem{
	private CharSequence text;
	private CustomEmojiHelper emojiHelper=new CustomEmojiHelper(), spoilerEmojiHelper;
	private CharSequence parsedSpoilerText;
	public boolean textSelectable;
	public final Status status;
	public boolean translated = false;
	public TranslatedStatus translation = null;

	private AccountSession session;
	private Instance instanceInfo;
	private boolean translateEnabled;

	public TextStatusDisplayItem(String parentID, CharSequence text, BaseStatusListFragment parentFragment, Status status){
		super(parentID, parentFragment);
		this.text=text;
		this.status=status;
		emojiHelper.setText(text);
		if(!TextUtils.isEmpty(status.spoilerText)){
			parsedSpoilerText=HtmlParser.parseCustomEmoji(status.spoilerText, status.emojis);
			spoilerEmojiHelper=new CustomEmojiHelper();
			spoilerEmojiHelper.setText(parsedSpoilerText);
		}
		session = AccountSessionManager.getInstance().getAccount(parentFragment.getAccountID());
		instanceInfo = AccountSessionManager.getInstance().getInstanceInfo(session.domain);
		translateEnabled = instanceInfo.v2 != null && instanceInfo.v2.configuration.translation != null && instanceInfo.v2.configuration.translation.enabled;
	}

	@Override
	public Type getType(){
		return Type.TEXT;
	}

	@Override
	public int getImageCount(){
		if(spoilerEmojiHelper!=null && !status.spoilerRevealed)
			return spoilerEmojiHelper.getImageCount();
		return emojiHelper.getImageCount();
	}

	@Override
	public ImageLoaderRequest getImageRequest(int index){
		if(spoilerEmojiHelper!=null && !status.spoilerRevealed)
			return spoilerEmojiHelper.getImageRequest(index);
		return emojiHelper.getImageRequest(index);
	}

	public static class Holder extends StatusDisplayItem.Holder<TextStatusDisplayItem> implements ImageLoaderViewHolder{
		private final LinkedTextView text;
		private final TextView spoilerTitle, translateInfo;
		private final View spoilerOverlay, textWrap, translateWrap;
		private final Button translateButton;

		public Holder(Activity activity, ViewGroup parent){
			super(activity, R.layout.display_item_text, parent);
			text=findViewById(R.id.text);
			spoilerTitle=findViewById(R.id.spoiler_title);
			spoilerOverlay=findViewById(R.id.spoiler_overlay);
			textWrap=findViewById(R.id.text_wrap);
			translateWrap=findViewById(R.id.translate_wrap);
			translateButton=findViewById(R.id.translate_btn);
			translateInfo=findViewById(R.id.translate_info);
			itemView.setOnClickListener(v->item.parentFragment.onRevealSpoilerClick(this));
		}

		@Override
		public void onBind(TextStatusDisplayItem item){
			text.setText(item.translated
							? HtmlParser.parse(item.translation.content, item.status.emojis, item.status.mentions, item.status.tags, item.parentFragment.getAccountID())
							: item.text);
			text.setTextIsSelectable(item.textSelectable);
			text.setInvalidateOnEveryFrame(false);

			if(!TextUtils.isEmpty(item.status.spoilerText)){
				spoilerTitle.setText(item.parsedSpoilerText);
				if(item.status.spoilerRevealed){
					spoilerOverlay.setVisibility(View.GONE);
					textWrap.setVisibility(View.VISIBLE);
					itemView.setClickable(false);
				}else{
					spoilerOverlay.setVisibility(View.VISIBLE);
					textWrap.setVisibility(View.INVISIBLE);
					itemView.setClickable(true);
				}
			}else{
				spoilerOverlay.setVisibility(View.GONE);
				textWrap.setVisibility(View.VISIBLE);
				itemView.setClickable(false);
			}

			translateWrap.setVisibility(item.textSelectable && item.translateEnabled &&
					!item.status.visibility.isLessVisibleThan(StatusPrivacy.UNLISTED) &&
					(item.session.preferences == null || !item.status.language.equalsIgnoreCase(item.session.preferences.postingDefaultLanguage))
					? View.VISIBLE : View.GONE);
			translateButton.setText(item.translated ? R.string.translate_show_original : R.string.translate_post);
			translateInfo.setText(item.translated ? itemView.getResources().getString(R.string.translated_using, item.translation.provider) : "");
			translateButton.setOnClickListener(v->{
				if (item.translation == null) {
					new TranslateStatus(item.status.id).setCallback(new Callback<>() {
						@Override
						public void onSuccess(TranslatedStatus translatedStatus) {
							item.translation = translatedStatus;
							item.translated = true;
							rebind();
						}

						@Override
						public void onError(ErrorResponse error) {
							error.showToast(itemView.getContext());
						}
					}).exec(item.parentFragment.getAccountID());
				} else {
					item.translated = !item.translated;
					rebind();
				}
			});
		}

		@Override
		public void setImage(int index, Drawable image){
			getEmojiHelper().setImageDrawable(index, image);
			text.invalidate();
			spoilerTitle.invalidate();
			if(image instanceof Animatable){
				((Animatable) image).start();
				if(image instanceof MovieDrawable)
					text.setInvalidateOnEveryFrame(true);
			}
		}

		@Override
		public void clearImage(int index){
			getEmojiHelper().setImageDrawable(index, null);
			text.invalidate();
		}

		private CustomEmojiHelper getEmojiHelper(){
			return item.spoilerEmojiHelper!=null && !item.status.spoilerRevealed ? item.spoilerEmojiHelper : item.emojiHelper;
		}
	}
}
