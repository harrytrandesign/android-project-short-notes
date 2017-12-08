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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> notesListItems;
    ArrayAdapter<String> notesAdapter;
    Boolean hasAppRunBefore;
    SharedPreferences appIntroHint = null;
    customEditText userNoteEditTextField;
    String userNoteInput;
    FloatingActionButton fab;
    Gson gson;

    public void saveTheNotesList() {

        String usersNotesToList = new Gson().toJson(notesListItems);

        SharedPreferences myPrefs = getSharedPreferences("us.wetpaws.essentialnotes", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myPrefs.edit();
        myEditor.putString("users_saved_notes_list", usersNotesToList);
        myEditor.apply();

        Log.i("notes", "Currently Saved List " + usersNotesToList);

    }

    public void clearTextField() {

        userNoteEditTextField.clearFocus();
        userNoteEditTextField.setText("");
        userNoteEditTextField.getText().clear();

    }

    public void closeKeyboardHideFab() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userNoteEditTextField.getWindowToken(), 0);
        fab.setVisibility(View.INVISIBLE);
        clearTextField();

    }

    public void insertNoteIntoTable() {
        userNoteInput = userNoteEditTextField.getText().toString();

        if (userNoteInput.equals("")) {

            Toast.makeText(getApplicationContext(), "Enter a note first.", Toast.LENGTH_SHORT).show();

        } else {

            if (notesListItems.size() < 100) {

                Log.i("notes", userNoteInput);

                notesListItems.add(userNoteInput);
                notesAdapter.notifyDataSetChanged();
                saveTheNotesList();
                closeKeyboardHideFab();

//                jsonAllNotes = gson.toJson(notesListItems);
//                Log.i("notes", "Currently Saved List " + jsonAllNotes);

//                saved_notes = getSharedPreferences("all_user_notes", 0);
//                saved_note = saved_notes.edit();
//                saved_note.putString("all_user_saved_notes", jsonAllNotes);
//                saved_note.apply();


            } else {

                Toast.makeText(getApplicationContext(), "Delete a note first.", Toast.LENGTH_SHORT).show();

            }

        }

    }

    public void openDeleteNoteDialogBox(int oneOrTwo, final int position) {

        AlertDialog.Builder build = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

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

                    Log.i("notes", "Position is at " + String.valueOf(position));
                    Log.i("notes", "Size of table is " + notesListItems.size());

                    notesListItems.clear();
                    notesAdapter.notifyDataSetChanged();
                    saveTheNotesList();

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

                    Log.i("notes", "Position is at " + String.valueOf(position));

                    notesListItems.remove(position);
                    notesAdapter.notifyDataSetChanged();
                    saveTheNotesList();

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

        gson = new Gson();

        ListView userNoteListView;

        SharedPreferences myPrefs = getSharedPreferences("us.wetpaws.essentialnotes", Context.MODE_PRIVATE);

        String usersNotesToList = myPrefs.getString("users_saved_notes_list", "");

        Log.i("notes", "Currently Saved List " + usersNotesToList);

        // Check if app is running for the very first time.
        appIntroHint = getSharedPreferences("hasRunBefore_appIntroHint", 0); // Load the preferences.
        hasAppRunBefore = appIntroHint.getBoolean("hasRun_appIntroHint", false); // See if it's been run before with the default set as no.

        userNoteEditTextField = (customEditText) findViewById(R.id.noteInputField);
        userNoteListView = (ListView) findViewById(R.id.notesListView);

        notesListItems = new ArrayList<>();

        if (!hasAppRunBefore) {

            // Save information that shows the app has now already run first time.
            SharedPreferences settings = getSharedPreferences("hasRunBefore_appIntroHint", 0);
            SharedPreferences.Editor edit = settings.edit();
            edit.putBoolean("hasRun_appIntroHint", true);
            edit.apply();

            notesListItems.add(getResources().getString(R.string.note_hint_text));
            notesListItems.add(getResources().getString(R.string.note_delete_text));
            saveTheNotesList();

        } else {

            notesListItems = new Gson().fromJson(usersNotesToList, new TypeToken<List<String>>() {}.getType());

        }

        notesAdapter = new ArrayAdapter<>(this, R.layout.my_row_layout, R.id.note, notesListItems);
        userNoteListView.setAdapter(notesAdapter);

        fab = (FloatingActionButton) findViewById(R.id.newNoteButton);
        fab.setVisibility(View.INVISIBLE);

        if (userNoteEditTextField != null) {
            userNoteEditTextField.setHorizontallyScrolling(false);
            userNoteEditTextField.setMaxLines(4);
        }

        // Hitting enter than the soft keyboard closes.
        userNoteEditTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    insertNoteIntoTable();

                    Log.i("notes", "Action done key works.");

                    return true;
                }

                return false;
            }
        });

        // Pressing the soft down key on hardware closes the soft keyboard.
        userNoteEditTextField.setKeyImeChangeListener(new customEditText.KeyImeChange() {
            @Override
            public boolean onKeyIme(int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    closeKeyboardHideFab();

                    Log.i("notes", "Soft down button works.");

                    return true;
                }

                return false;
            }
        });

        // On focus of the edit text we make fab visible.
        userNoteEditTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    fab.setVisibility(View.VISIBLE);

                }
            }
        });

        // Write the message into the array when fab is pressed.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertNoteIntoTable();

            }
        });

        userNoteListView.setClickable(true);
        userNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                openDeleteNoteDialogBox(2, position);

            }
        });

//        if (usersNotesToList != null) {
//
//            notesListItems = new Gson().fromJson(usersNotesToList, new TypeToken<List<String>>() {}.getType());
//            saveTheNotesList();
//
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
        if (id == R.id.clearAllNotes) {

            openDeleteNoteDialogBox(1, notesListItems.size());

            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
