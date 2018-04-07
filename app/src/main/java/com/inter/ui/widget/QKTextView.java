package com.inter.ui.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.inter.R;
import com.inter.face.LiveView;
import com.inter.ui.setting.SettingsFragment;
import com.inter.ui.util.ThemeManager;
import com.inter.util.FontManager;
import com.inter.util.LiveViewManager;
import com.inter.util.QKPreference;

import java.util.regex.Pattern;

public class QKTextView extends AppCompatTextView {
    private final String TAG = "QKTextView";

    private Context mContext;
    private int mType = FontManager.TEXT_TYPE_PRIMARY;
    private boolean mOnColorBackground = false;

    public QKTextView(Context context) {
        this(context,null);
    }

    public QKTextView(Context context, AttributeSet attrs) {
        this(context, attrs,android.R.attr.textViewStyle);
    }

    public QKTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QKTextView);
            mType = array.getInt(R.styleable.QKTextView_type, FontManager.TEXT_TYPE_PRIMARY);
            array.recycle();
        }

        setTextColor(FontManager.getTextColor(mContext, mType));
        setText(getText());

        setType(mType);
    }

    public void setType(final int type) {
        mType = type;

        // Register for theme updates if we're text that changes color dynamically.
        if (mType == FontManager.TEXT_TYPE_CATEGORY) {
            LiveViewManager.registerView(QKPreference.THEME, this, new LiveView() {
                @Override
                public void refresh(String key) {
                    setTextColor(FontManager.getTextColor(mContext, mType));
                }
            });
        }

        LiveViewManager.registerView(QKPreference.FONT_FAMILY, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setTypeface(FontManager.getFont(mContext, type));
            }
        });

        LiveViewManager.registerView(QKPreference.FONT_WEIGHT, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setTypeface(FontManager.getFont(mContext, type));
            }
        });

        LiveViewManager.registerView(QKPreference.FONT_SIZE, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, FontManager.getTextSize(mContext, mType));

            }
        });

        LiveViewManager.registerView(QKPreference.BACKGROUND, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setTextColor(FontManager.getTextColor(mContext, mType));

            }
        });

        LiveViewManager.registerView(QKPreference.TEXT_FORMATTING, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setText(getText(), BufferType.NORMAL);

            }
        });
    }

    public void setOnColorBackground(boolean onColorBackground) {
        if (onColorBackground != mOnColorBackground) {
            mOnColorBackground = onColorBackground;

            if (onColorBackground) {
                if (mType == FontManager.TEXT_TYPE_PRIMARY) {
                    setTextColor(ThemeManager.getTextOnColorPrimary());
                    setLinkTextColor(ThemeManager.getTextOnColorPrimary());
                } else if (mType == FontManager.TEXT_TYPE_SECONDARY ||
                        mType == FontManager.TEXT_TYPE_TERTIARY) {
                    setTextColor(ThemeManager.getTextOnColorSecondary());
                }
            } else {
                if (mType == FontManager.TEXT_TYPE_PRIMARY) {
                    setTextColor(ThemeManager.getTextOnBackgroundPrimary());
                    setLinkTextColor(ThemeManager.getColor());
                } else if (mType == FontManager.TEXT_TYPE_SECONDARY ||
                        mType == FontManager.TEXT_TYPE_TERTIARY) {
                    setTextColor(ThemeManager.getTextOnBackgroundSecondary());
                }
            }
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (mType == FontManager.TEXT_TYPE_DIALOG_BUTTON) {
            text = text.toString().toUpperCase();
        }

        if (prefs.getBoolean(SettingsFragment.MARKDOWN_ENABLED, false)) {
            text = styleText(text);
            if (text == null || text.length() <= 0) {
                super.setText(text, BufferType.EDITABLE);
                return;
            }

            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            super.setText(builder, BufferType.EDITABLE);
        } else {
            super.setText(text, BufferType.NORMAL);
        }

    }

    public static CharSequence styleText(CharSequence text) {
        if (text == null || text.toString().isEmpty() || (!text.toString().contains("*") && !text.toString().contains("_")))
            return text; // Do nothing if there's nothing to be styled

        boolean bool;

        text = Html.toHtml(new SpannedString(text));

        // bold text
        if (text.toString().contains("*")) {
            int doubleStars = 0;
            bool = true;
            for (int i = 0; i < text.length() - 1; i++) {
                if (text.subSequence(i, i + 2).equals("**")) {
                    doubleStars++;
                }
            }
            if (doubleStars >= 2) {
                if (doubleStars % 2 != 0) {
                    doubleStars--;
                }
                for (int i = 0; i < doubleStars; i++) {
                    text = text.toString().replaceFirst(Pattern.quote("**"), bool ? "<b>" : "</b>");
                    bool = !bool;
                }
            }
        }

        // italic text
        if (text.toString().contains("*")) {
            int singleStars = 0;
            bool = true;
            for (int i = 0; i < text.length(); i++) {
                if (text.subSequence(i, i + 1).equals("*")) {
                    singleStars++;
                }
            }
            if (singleStars >= 2) {
                if (singleStars % 2 != 0) {
                    singleStars--;
                }
                for (int i = 0; i < singleStars; i++) {
                    text = text.toString().replaceFirst(Pattern.quote("*"), bool ? "<i>" : "</i>");
                    bool = !bool;
                }
            }
        }

        // underlined text
        if (text.toString().contains("_")) {
            int underscores = 0;
            bool = true;
            for (int i = 0; i < text.length(); i++) {
                if (text.subSequence(i, i + 1).equals("_")) {
                    underscores++;
                }
            }
            if (underscores >= 2) {
                if (underscores % 2 != 0) {
                    underscores--;
                }
                for (int i = 0; i < underscores; i++) {
                    text = text.toString().replaceFirst(Pattern.quote("_"), bool ? "<u>" : "</u>");
                    bool = !bool;
                }
            }
        }

        text = text.toString().replaceAll(Pattern.quote("<p dir=\"ltr\">"), "").replaceAll(Pattern.quote("</p>"), "");

        return Html.fromHtml(text.toString());
    }
}
