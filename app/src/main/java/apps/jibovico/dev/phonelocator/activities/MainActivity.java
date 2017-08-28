package apps.jibovico.dev.phonelocator.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import apps.jibovico.dev.phonelocator.fragments.HomeFragment;
import apps.jibovico.dev.phonelocator.R;
import apps.jibovico.dev.phonelocator.fragments.RecorderFragment;
import apps.jibovico.dev.phonelocator.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String ARG_FRAGMENT = "fragment";
    private static final String ARG_CURRENT_PAGE = "currentPage";
    private static final String ARG_NAV_IDX = "navItemIndex";

    private static final String TAG_HOME = "home";
    private static final String TAG_RECORDER = "recorder";
    private static final String TAG_SETTINGS = "settings";

    private final int PERMISSIONS_REQUEST = 0;

    private String[] mViewTitles;

    public static String CURRENT_TAG = TAG_HOME;
    public static int mNavItemIndex = 0;

    private Toolbar mToolbar;
    private Fragment mFragment;
    private Handler mHandler;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mHandler = new Handler();
        mViewTitles = getResources().getStringArray(R.array.viewTitles);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            mNavItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            mFragment = getCurrentFragment();
        }else {
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, ARG_FRAGMENT);
            CURRENT_TAG = savedInstanceState.getString(ARG_CURRENT_PAGE);
            mNavItemIndex = savedInstanceState.getInt(ARG_NAV_IDX);
        }

        loadHomeFragment();
        requestPermission();
        //getSupportFragmentManager().beginTransaction().add()
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void loadHomeFragment() {

        setToolbarTitle();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            return;
        }

        showFragment(mFragment);
    }

    private void showFragment(final Fragment fragment){
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        mHandler.post(mPendingRunnable);
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, ARG_FRAGMENT, mFragment);
        outState.putString(ARG_CURRENT_PAGE, CURRENT_TAG);
        outState.putInt(ARG_NAV_IDX, mNavItemIndex);
    }

    /**
     * Request user's permission to get location
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission(){
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            requestPermissions(new String[] {Manifest.permission.INTERNET}, PERMISSIONS_REQUEST);
//        }
    }

    private Fragment getCurrentFragment() {
        switch (mNavItemIndex) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return RecorderFragment.newInstance();
            case 2:
                return SettingsFragment.newInstance();
            default:
                return HomeFragment.newInstance();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(mViewTitles[mNavItemIndex]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                }else {
//                    Toast.makeText(getApplicationContext(), "Internet Permission Required", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mNavItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                    break;
                case R.id.navigation_recorder:
                    mNavItemIndex = 1;
                    CURRENT_TAG = TAG_RECORDER;
                    break;
                case R.id.navigation_settings:
                    mNavItemIndex = 2;
                    CURRENT_TAG = TAG_SETTINGS;
                    break;
                default:
                    mNavItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
            }

            mFragment = getCurrentFragment();
            loadHomeFragment();
            return true;
        }
    };
}
