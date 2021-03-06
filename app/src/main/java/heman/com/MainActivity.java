package heman.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import heman.com.API.gitapi;
import heman.com.model.gitmodel;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {

    Button click;
    TextView tv;
    EditText edit_user;
    ProgressBar pbar;
    String API = "https://api.github.com";                         //BASE URL

    View.OnClickListener mOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        click = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.tv);
        edit_user = (EditText) findViewById(R.id.edit);
        pbar = (ProgressBar) findViewById(R.id.pb);
        pbar.setVisibility(View.INVISIBLE);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("");
            }

        };

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Snaker
                Snackbar.make(findViewById(R.id.root), "Had a snack at Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Undo", mOnClickListener)
                        .setActionTextColor(Color.RED)
                        .show();
                //snaker

                String user = edit_user.getText().toString();
                pbar.setVisibility(View.VISIBLE);

                //Retrofit section start from here...
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(API).build();                                        //create an adapter for retrofit with base url

                gitapi git = restAdapter.create(gitapi.class);                            //creating a service for adapter with our GET class

                //Now ,we need to call for response
                //Retrofit using gson for JSON-POJO conversion

                git.getFeed(user, new Callback<gitmodel>() {
                    @Override
                    public void success(gitmodel gitmodel, Response response) {
                        //we get json object from github server to our POJO or model class

                        tv.setText("Github Name :" + gitmodel.getName() + "\nWebsite :" + gitmodel.getBlog() + "\nCompany Name :" + gitmodel.getCompany());

                        pbar.setVisibility(View.INVISIBLE);                               //disable progressbar
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tv.setText(error.getMessage());
                        pbar.setVisibility(View.INVISIBLE);                               //disable progressbar
                    }
                });
            }
        });
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
}