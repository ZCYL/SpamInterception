package com.inter.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.inter.face.LiveView;
import com.inter.ui.util.ThemeManager;
import com.inter.util.FontManager;
import com.inter.util.LiveViewManager;
import com.inter.util.QKPreference;


public class QKEditText extends AppCompatEditText {
    public static final String TAG = "QKEditText";

    public interface TextChangedListener {
        void onTextChanged(CharSequence s);
    }

    private Context mContext;
    private boolean mTextChangedListenerEnabled = true;

    public QKEditText(Context context) {
        super(context);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public QKEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public QKEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            init(context);
        }
    }

    private void init(Context context) {
        mContext = context;

        LiveViewManager.registerView(QKPreference.FONT_FAMILY, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setTypeface(FontManager.getFont(mContext));
            }
        });

        LiveViewManager.registerView(QKPreference.FONT_WEIGHT, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setTypeface(FontManager.getFont(mContext));
            }
        });

        LiveViewManager.registerView(QKPreference.FONT_SIZE, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, FontManager.getTextSize(mContext, FontManager.TEXT_TYPE_PRIMARY));
            }
        });

        LiveViewManager.registerView(QKPreference.BACKGROUND, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setTextColor(ThemeManager.getTextOnBackgroundPrimary());
                setHintTextColor(ThemeManager.getTextOnBackgroundSecondary());
            }
        });

        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text) || Build.VERSION.SDK_INT < 19) {
            text = new SpannableStringBuilder(text);
        }
        super.setText(text, type);
    }

    public void setTextChangedListenerEnabled(boolean textChangedListenerEnabled) {
        mTextChangedListenerEnabled = textChangedListenerEnabled;
    }

    public void setTextChangedListener(final TextChangedListener listener) {
        if (listener != null) {
            addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mTextChangedListenerEnabled) {
                        listener.onTextChanged(s);
                    }
                }
            });
        }
    }
}
