package us.wetpaws.essentialnotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> notesListItems;
    ArrayAdapter<String> notesAdapter;
    Boolean hasAppRunBefore;
    SharedPreferences appIntroHint = null;
    customEditText userNoteEditTextField;
    ListView userNoteListView;
    FloatingActionButton fab;

    public void closeKeyboardHideFab() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        userNoteEditTextField.clearFocus();
        fab.setVisibility(View.INVISIBLE);
        userNoteEditTextField.setText("");
        userNoteEditTextField.getText().clear();
    }

    public void openDeleteNoteDialogBox(int oneOrTwo) {

        AlertDialog.Builder build = new AlertDialog.Builder(this);

        if (oneOrTwo == 1) {
            build.setIcon(R.drawable.android_5_black_full_trash);
            build.setTitle(R.string.delete_all_title);
            build.setMessage(R.string.delete_all_message);
            build.setCancelable(true);
            build.setPositiveButton(R.string.option_choice_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_confirmation), Toast.LENGTH_SHORT).show();
                }
            });
            build.setNegativeButton(R.string.option_choice_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        } else if (oneOrTwo == 2) {
            build.setIcon(R.drawable.android_5_black_full_trash);
            build.setTitle(R.string.delete_single_title);
            build.setMessage(R.string.delete_single_message);
            build.setCancelable(true);
            build.setPositiveButton(R.string.option_choice_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_confirmation), Toast.LENGTH_SHORT).show();
                }
            });
            build.setNegativeButton(R.string.option_choice_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        AlertDialog displayMessage = build.create();
        displayMessage.show();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("notes", "System is paused.");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.i("notes", "Back Pressed");

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("notes", "System has stopped");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("notes", "System has resumed");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Check if app is running for the very first time.
        appIntroHint = getSharedPreferences("hasRunBefore_appIntroHint", 0); // Load the preferences.
        hasAppRunBefore = appIntroHint.getBoolean("hasRun_appIntroHint", false); // See if it's been run before with the default set as no.

        fab = (FloatingActionButton) findViewById(R.id.newNoteButton);
        userNoteEditTextField = (customEditText) findViewById(R.id.noteInputField);
        userNoteListView = (ListView) findViewById(R.id.notesListView);

        if (userNoteEditTextField != null) {
            userNoteEditTextField.setHorizontallyScrolling(false);
            userNoteEditTextField.setMaxLines(4);
        }

        userNoteEditTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    closeKeyboardHideFab();

                    return  true;
                }

                return false;
            }
        });

        userNoteEditTextField.setKeyImeChangeListener(new customEditText.KeyImeChange() {
            @Override
            public boolean onKeyIme(int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    closeKeyboardHideFab();

                    Log.i("notes", "The back key works if this message shows");

                }

                return false;
            }
        });
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("notes", userNoteEditTextField.getText().toString());

            }
        });

        fab.setVisibility(View.INVISIBLE);

        notesListItems = new ArrayList<>();

        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesListItems);
        userNoteListView.setAdapter(notesAdapter);
        userNoteListView.setClickable(true);
        userNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDeleteNoteDialogBox(2);
            }
        });

        if (!hasAppRunBefore) {

            // Save information that shows the app has now already run first time.
            SharedPreferences settings = getSharedPreferences("hasRunBefore_appIntroHint", 0);
            SharedPreferences.Editor edit = settings.edit();
            edit.putBoolean("hasRun_appIntroHint", true);
            edit.apply();

            notesListItems.add(getResources().getString(R.string.note_hint_text));
            notesListItems.add(getResources().getString(R.string.note_delete_text));

        }

        notesListItems.add("Adding a second note to this application is as easy as clicking the + sign.");
        notesListItems.add("This is the length of a note, this is the size of the text that will be held in this text field, the length is constricted by the parameter.");

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
        if (id == R.id.clearAllNotes) {

            openDeleteNoteDialogBox(1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
