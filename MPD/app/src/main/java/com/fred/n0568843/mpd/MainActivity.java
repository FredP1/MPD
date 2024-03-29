package com.fred.n0568843.mpd;

import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ModuleFragment.OnFragmentInteractionListener,
        NotesFragment.OnFragmentInteractionListener,
        RevisionFragment.OnFragmentInteractionListener,
        DeadlinesFragment.OnFragmentInteractionListener {

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {

            setTitle("FNote Home");
            //Default to Notes Fragment
            Fragment notesFragment = new NotesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, notesFragment);
            transaction.commit();
            navigationView.setCheckedItem(R.id.nav_notes);

            //Sets username and pic for navigation view
            View headerView = navigationView.getHeaderView(0);
            TextView navName = headerView.findViewById(R.id.navName);
            ImageView navPic = headerView.findViewById(R.id.navPicture);
            mAuth = FirebaseAuth.getInstance();
            String userName = mAuth.getCurrentUser().getDisplayName();
            navName.setText(userName);
            //This changes the icon in navigation menu to the profile pic
            //Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).into(navPic);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.app.Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            setTitle("All Notes");
            navigationView.setCheckedItem(R.id.nav_notes);
            Fragment notesFragment = new NotesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, notesFragment, "NOTES");
            transaction.commit();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            LoginManager.getInstance().logOut();
            FirebaseAuth.getInstance().signOut();
            System.out.println(AccessToken.getCurrentAccessToken());
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
            finish();
        }
        if (id == R.id.action_exit){
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_modules) {
            Fragment moduleFragment = new ModuleFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, moduleFragment);
            transaction.commit();
        } else if (id == R.id.nav_deadlines) {
            Fragment deadlinesFragment = new DeadlinesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, deadlinesFragment);
            transaction.commit();
        } else if (id == R.id.nav_revision) {
            Intent i = new Intent();
            ComponentName cn;
            cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
            i.setComponent(cn);
            startActivity(i);
//            Fragment revisionFragment = new RevisionFragment();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, revisionFragment);
//            transaction.commit();
        } else if (id == R.id.nav_notes) {
            Fragment notesFragment = new NotesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, notesFragment);
            transaction.commit();
        } else if (id == R.id.nav_pictures) {
            Fragment pictureFragment = new PictureFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, pictureFragment);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
