package my.typekannada.ashwin.customkeyboard;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private InterstitialAd interstitial;
    final String PREFS_NAME = "MyPrefsFile";
    final String FONT_PREFS_KEY = "selected_font_style";
    CardView promo;
    private RewardedVideoAd mRewardedVideoAd;
    private ScrollView scroll;
    private FirebaseAnalytics mFirebaseAnalytics;
    LinearLayout ll1;
    Button fontStyleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        scroll = findViewById(R.id.scrollView);
        promo = findViewById(R.id.promos);
        fontStyleButton = findViewById(R.id.fontStyleButton);

        // Setup Font Style Button
        fontStyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFontStyleDialog();
            }
        });

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1924436259631090/6298664761");

        //Interstitial Ad Space
        AdRequest adRequests = new AdRequest.Builder()
//                .addTestDevice("91BCA0B98362AF53D5488A3F87FA1614")
                .build();
        interstitial = new InterstitialAd(MainActivity.this);
        interstitial.setAdUnitId(getString(R.string.home_interstitial_id));
        interstitial.loadAd(adRequests);
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                scroll.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            displayInterstitial();
                        }
                        return false;
                    }
                });
            }
        });
        // Interstetial ad Finished


        //Banner Ad Space

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E1C583B224120C3BEF4A3DB0177A7A37")
                .build();
        mAdView.loadAd(adRequest);

        //Banner Ad Finished






        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=vadeworks.news.duniya"));
                startActivity(i);
            }
        });



    }

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//Code For First TIme Opening The App.
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    if (settings.getBoolean("my_first_time", true)) {
                        //the app is being launched for first time, do something
                        //Notification Tray as Soon as User Installs
                        ///Test Notification Code
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                        mBuilder.setContentTitle("Mysore Sandal Soap ");
                        mBuilder.setContentText("Click here to Read more!");
                        mBuilder.setAutoCancel(true);
                        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                        Intent resultIntent = new Intent(getApplicationContext(), NotifTray.class);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                        stackBuilder.addParentStack(NotifHandler.class);


                        // Adds the Intent that starts the Activity to the top of the stack
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(resultPendingIntent);
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        // notificationID allows you to update the notification later on.
                        mNotificationManager.notify(0, mBuilder.build());
                        ///Test Notification code ends here
                        //Notification Tray Code Ends Here
                        // record the fact that the app has been started at least once
                        settings.edit().putBoolean("my_first_time", false).commit();
                    }
                    //Code for First TIme opening the App Ends Here
                }
            }, 3000);



            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }


    public void langset(View view) {

        Intent j = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        j.setClassName("com.android.settings", "com.android.settings.LanguageSettings");
        startActivity(j);

    }

    private void showFontStyleDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_font_selector, null);

        // Get references to dialog components
        final TextView previewText = dialogView.findViewById(R.id.fontPreviewText);
        final Spinner fontSpinner = dialogView.findViewById(R.id.fontStyleSpinner);
        Button applyButton = dialogView.findViewById(R.id.applyFontButton);

        // Get font names and values from resources
        final String[] fontNames = getResources().getStringArray(R.array.font_style_names);
        final String[] fontValues = getResources().getStringArray(R.array.font_style_values);

        // Setup spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, fontNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(adapter);

        // Load current font selection
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentFont = prefs.getString(FONT_PREFS_KEY, "default");

        // Set spinner to current selection
        for (int i = 0; i < fontValues.length; i++) {
            if (fontValues[i].equals(currentFont)) {
                fontSpinner.setSelection(i);
                break;
            }
        }

        // Create the dialog
        final AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(dialogView)
            .create();

        // Handle font preview when spinner selection changes
        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFont = fontValues[position];
                applyFontToTextView(previewText, selectedFont);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Handle apply button click
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = fontSpinner.getSelectedItemPosition();
                String selectedFont = fontValues[selectedPosition];

                // Save to SharedPreferences
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(FONT_PREFS_KEY, selectedFont);
                editor.apply();

                Toast.makeText(MainActivity.this,
                    getString(R.string.font_changed_success),
                    Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void applyFontToTextView(TextView textView, String fontName) {
        if (fontName.equals("default")) {
            // Use system default font
            textView.setTypeface(Typeface.DEFAULT);
        } else {
            try {
                // Try to load custom font from res/font/
                int fontId = getResources().getIdentifier(fontName, "font", getPackageName());
                if (fontId != 0) {
                    Typeface typeface = ResourcesCompat.getFont(this, fontId);
                    textView.setTypeface(typeface);
                } else {
                    // Fallback to default if font not found
                    textView.setTypeface(Typeface.DEFAULT);
                }
            } catch (Exception e) {
                Log.e("FontError", "Error loading font: " + fontName, e);
                textView.setTypeface(Typeface.DEFAULT);
            }
        }
    }
}
