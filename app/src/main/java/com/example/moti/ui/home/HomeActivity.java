package com.example.moti.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.moti.data.local.appconst.AppConst;
import com.example.moti.ui.nutrition.NutritionActivity;
import com.example.moti.ui.profile.ProfileActivity;
import com.example.moti.ui.progress.ProgressActivity;
import com.example.moti.ui.workout.WorkoutActivity;
import com.example.moti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.thefinestartist.finestwebview.FinestWebView;

public class HomeActivity extends AppCompatActivity {

    Intent workoutIntent;
    Intent progressIntent;
    Intent nutritionIntent;
    Intent profileIntent;
    SharedPreferences loginSP;
    TextView homeTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString();
        myToolbar.setTitle("WELCOME,  " + name);
        myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(myToolbar);

        //Inits
        workoutIntent = new Intent(this, WorkoutActivity.class);
        progressIntent = new Intent(this, ProgressActivity.class);
        nutritionIntent = new Intent(this, NutritionActivity.class);
        profileIntent = new Intent(this, ProfileActivity.class);
        loginSP = getSharedPreferences("Login", MODE_PRIVATE);
        String userName = loginSP.getString("Name", "");
        setTitle("WELCOME, " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_help:
                gotoHelpAddress();

                return true;

            default:
                return false;
        }
    }

    private void gotoHelpAddress() {
        FinestWebView.Builder webBuilder = new FinestWebView.Builder(this);
        webBuilder.statusBarColorRes(R.color.colorPrimaryDark);
        webBuilder.toolbarColorRes(R.color.colorPrimary);
        webBuilder.titleDefaultRes(R.string.action_help);
        webBuilder.showIconBack(true);
        webBuilder.showIconForward(true);
        webBuilder.showSwipeRefreshLayout(true);
        webBuilder.showProgressBar(true);
        webBuilder.setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit);

        webBuilder.show(AppConst.HELP_URL);
    }

    public void workoutButton(View view) {
        startActivity(workoutIntent);
    }

    public void progressButton(View view) {
        startActivity(progressIntent);
    }

    public void nutritionButton(View view) {
        startActivity(nutritionIntent);
    }

    public void profileButton(View view) {
        startActivity(profileIntent);
    }
}
