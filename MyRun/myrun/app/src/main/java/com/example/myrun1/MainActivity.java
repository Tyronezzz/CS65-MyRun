/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-03
 */

package com.example.myrun1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {            // Inflate the menu
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;

            case R.id.action_edit_profile:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
