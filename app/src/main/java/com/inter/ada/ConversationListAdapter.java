package com.inter.ada;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inter.R;
import com.inter.bean.Contact;
import com.inter.bean.Conversation;
import com.inter.convers.ConversationPrefsHelper;
import com.inter.face.LiveView;
import com.inter.ui.base.BaseActivity;
import com.inter.ui.setting.SettingsFragment;
import com.inter.ui.util.ThemeManager;
import com.inter.util.DateFormatter;
import com.inter.util.FontManager;
import com.inter.util.LiveViewManager;
import com.inter.util.QKPreference;
import com.inter.util.QKPreferences;
import com.inter.util.emoji.EmojiRegistry;


public class ConversationListAdapter extends RecyclerCursorAdapter<ConversationListViewHolder, Conversation> {


    private final SharedPreferences mPrefs;

    public ConversationListAdapter(BaseActivity context) {
        super(context);
        mPrefs = mContext.getPrefs();
    }

    protected Conversation getItem(int position) {
        mCursor.moveToPosition(position);
        return Conversation.from(mContext, mCursor);
    }

    @Override
    public ConversationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_conversation, null);

        final ConversationListViewHolder holder = new ConversationListViewHolder(mContext, view);
        holder.mutedView.setImageResource(R.drawable.ic_notifications_muted);
        holder.unreadView.setImageResource(R.drawable.ic_unread_indicator);
        holder.errorIndicator.setImageResource(R.drawable.ic_error);

        LiveViewManager.registerView(QKPreference.THEME, this, new LiveView() {
            @Override
            public void refresh(String key) {
                holder.mutedView.setColorFilter(ThemeManager.getColor());
                holder.unreadView.setColorFilter(ThemeManager.getColor());
                holder.errorIndicator.setColorFilter(ThemeManager.getColor());
            }
        });

        LiveViewManager.registerView(QKPreference.BACKGROUND, this, new LiveView() {
            @Override
            public void refresh(String key) {
                holder.root.setBackground(ThemeManager.getRippleBackground());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ConversationListViewHolder holder, int position) {
        final Conversation conversation = getItem(position);

        holder.mData = conversation;
        holder.mContext = mContext;
        holder.mClickListener = mItemClickListener;
        holder.root.setOnClickListener(holder);
        holder.root.setOnLongClickListener(holder);

        holder.mutedView.setVisibility(new ConversationPrefsHelper(mContext, conversation.getThreadId())
                .getNotificationsEnabled() ? View.GONE : View.VISIBLE);

        holder.errorIndicator.setVisibility(conversation.hasError() ? View.VISIBLE : View.GONE);

        final boolean hasUnreadMessages = conversation.hasUnreadMessages();
        if (hasUnreadMessages) {
            holder.unreadView.setVisibility(View.VISIBLE);
            holder.snippetView.setTextColor(ThemeManager.getTextOnBackgroundPrimary());
            holder.dateView.setTextColor(ThemeManager.getColor());
            holder.fromView.setType(FontManager.TEXT_TYPE_PRIMARY_BOLD);
            holder.snippetView.setMaxLines(5);
        } else {
            holder.unreadView.setVisibility(View.GONE);
            holder.snippetView.setTextColor(ThemeManager.getTextOnBackgroundSecondary());
            holder.dateView.setTextColor(ThemeManager.getTextOnBackgroundSecondary());
            holder.fromView.setType(FontManager.TEXT_TYPE_PRIMARY);
            holder.snippetView.setMaxLines(1);
        }

        LiveViewManager.registerView(QKPreference.THEME, this, new LiveView() {
            @Override
            public void refresh(String key) {
                holder.dateView.setTextColor(hasUnreadMessages ? ThemeManager.getColor() : ThemeManager.getTextOnBackgroundSecondary());
            }
        });

        if (isInMultiSelectMode()) {
            holder.mSelected.setVisibility(View.VISIBLE);
            if (isSelected(conversation.getThreadId())) {
                holder.mSelected.setImageResource(R.drawable.ic_selected);
                holder.mSelected.setColorFilter(ThemeManager.getColor());
                holder.mSelected.setAlpha(1f);
            } else {
                holder.mSelected.setImageResource(R.drawable.ic_unselected);
                holder.mSelected.setColorFilter(ThemeManager.getTextOnBackgroundSecondary());
                holder.mSelected.setAlpha(0.5f);
            }
        } else {
            holder.mSelected.setVisibility(View.GONE);
        }

        LiveViewManager.registerView(QKPreference.HIDE_AVATAR_CONVERSATIONS, this, new LiveView() {
            @Override
            public void refresh(String key) {
                holder.mAvatarView.setVisibility(QKPreferences.getBoolean(QKPreference.HIDE_AVATAR_CONVERSATIONS) ? View.GONE : View.VISIBLE);
            }
        });

        // Date
        holder.dateView.setText(DateFormatter.getConversationTimestamp(mContext, conversation.getDate()));

        // Subject
        String emojiSnippet = conversation.getSnippet();
        if (mPrefs.getBoolean(SettingsFragment.AUTO_EMOJI, false)) {
            emojiSnippet = EmojiRegistry.parseEmojis(emojiSnippet);
        }
        holder.snippetView.setText(emojiSnippet);

        Contact.addListener(holder);

        // Update the avatar and name
        holder.onUpdate(conversation.getRecipients().size() == 1 ? conversation.getRecipients().get(0) : null);
    }
}
