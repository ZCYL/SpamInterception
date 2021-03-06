package com.inter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.drm.DrmManagerClient;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;

import com.inter.ui.util.ThemeManager;

/**
 * Created by ZCYL on 2018/4/5.
 */
public class MyApplication extends MultiDexApplication {
    public static final int LOADER_CONVERSATIONS = 0;
    public static final int LOADER_MESSAGES = 1;

    private SearchRecentSuggestions mRecentSuggestions;
    private TelephonyManager mTelephonyManager;
    private String mCountryIso;
    private static MyApplication sQKSMSApp = null;
//    private static RequestQueue sRequestQueue;
//    private PduLoaderManager mPduLoaderManager;
//    private ThumbnailManager mThumbnailManager;
    private DrmManagerClient mDrmManagerClient;
//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

//        if (Log.isLoggable(LogTag.STRICT_MODE_TAG, Log.DEBUG)) {
            // Log tag for enabling/disabling StrictMode violation log. This will dump a stack
            // in the log that shows the StrictMode violator.
            // To enable: adb shell setprop log.tag.Mms:strictmode DEBUG
//            StrictMode.setThreadPolicy(
//                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//        }

        sQKSMSApp = this;

        loadDefaultPreferenceValues();

        // Initialize analytics, leakcanary, and crittercism
//        AnalyticsManager.getInstance().init(this);
//        refWatcher = LeakCanary.install(this);

        // Figure out the country *before* loading contacts and formatting numbers
//        Country country = new Country(Locale.getDefault().getCountry(), Country.COUNTRY_SOURCE_LOCALE);
//        mCountryIso = country.getCountryIso();

        Context context = getApplicationContext();
//        mPduLoaderManager = new PduLoaderManager(context);
//        mThumbnailManager = new ThumbnailManager(context);
//
//        registerActivityLifecycleCallbacks(new LifecycleHandler());

        ThemeManager.init(this);
//        MmsConfig.init(this);
//        Contact.init(this);
//        DraftCache.init(this);
//        Conversation.init(this);
//        DownloadManager.init(this);
//        RateController.init(this);
//        LayoutManager.init(this);
//        NotificationManager.init(this);
//        LiveViewManager.init(this);
//        QKPreferences.init(this);
//
//        activePendingMessages();
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        QKSMSAppBase application = (QKSMSAppBase) context.getApplicationContext();
//        return application.refWatcher;
//    }

    @SuppressLint("CommitPrefEdits")
    private void loadDefaultPreferenceValues() {
        // Load the default values
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    /**
     * Try to process all pending messages(which were interrupted by user, OOM, Mms crashing,
     * etc...) when Mms app is (re)launched.
     */
//    private void activePendingMessages() {
//        // For Mms: try to process all pending transactions if possible
//        MmsSystemEventReceiver.wakeUpService(this);
//
//        // For Sms: retry to send smses in outbox and queued box
//        //sendBroadcast(new Intent(SmsReceiverService.ACTION_SEND_INACTIVE_MESSAGE, null, this, SmsReceiver.class));
//    }

     public static MyApplication getApplication() {
        return sQKSMSApp;
    }

//    public RequestQueue getRequestQueue() {
//        if (sRequestQueue == null) {
//            sRequestQueue = Volley.newRequestQueue(this);
//        }
//
//        return sRequestQueue;
//    }


//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//
//        mPduLoaderManager.onLowMemory();
//        mThumbnailManager.onLowMemory();
//    }
//
//    public PduLoaderManager getPduLoaderManager() {
//        return mPduLoaderManager;
//    }
//
//    public ThumbnailManager getThumbnailManager() {
//        return mThumbnailManager;
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        LayoutManager.getInstance().onConfigurationChanged(newConfig);
//    }

    /**
     * @return Returns the TelephonyManager.
     */
    public TelephonyManager getTelephonyManager() {
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }
        return mTelephonyManager;
    }

    /**
     * Returns the content provider wrapper that allows access to recent searches.
     *
     * @return Returns the content provider wrapper that allows access to recent searches.
     */
    public SearchRecentSuggestions getRecentSuggestions() {
        return mRecentSuggestions;
    }

    // This function CAN return null.
//    public String getCurrentCountryIso() {
//        if (mCountryIso == null) {
//            Country country = new Country(Locale.getDefault().getCountry(), Country.COUNTRY_SOURCE_LOCALE);
//            mCountryIso = country.getCountryIso();
//        }
//        return mCountryIso;
//    }

    public DrmManagerClient getDrmManagerClient() {
        if (mDrmManagerClient == null) {
            mDrmManagerClient = new DrmManagerClient(getApplicationContext());
        }
        return mDrmManagerClient;
    }
}
