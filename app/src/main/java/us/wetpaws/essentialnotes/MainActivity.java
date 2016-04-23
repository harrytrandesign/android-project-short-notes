package us.wetpaws.essentialnotes;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> notesListItems;
    ArrayAdapter<String> notesAdapter;
    boolean showHintNote = true;
    EditText userNoteEditTextField;
    ListView userNoteListView;

    public void openDeleteNoteDialogBox(int oneOrTwo) {
        int option = oneOrTwo;

        AlertDialog.Builder build = new AlertDialog.Builder(this);

        if (option == 1) {
            build.setIcon(R.drawable.android_5_black_full_trash);
            build.setTitle(R.string.delete_all_title);
            build.setMessage(R.string.delete_all_message);
            build.setCancelable(true);
            build.setPositiveButton(R.string.option_choice_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            build.setNegativeButton(R.string.option_choice_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        } else if (option == 2) {
            build.setIcon(R.drawable.android_5_black_full_trash);
            build.setTitle(R.string.delete_single_title);
            build.setMessage(R.string.delete_single_message);
            build.setCancelable(true);
            build.setPositiveButton(R.string.option_choice_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
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

        userNoteEditTextField = (EditText) findViewById(R.id.noteInputField);
        userNoteListView = (ListView) findViewById(R.id.notesListView);

        if (userNoteEditTextField != null) {
            userNoteEditTextField.setHorizontallyScrolling(false);
            userNoteEditTextField.setMaxLines(4);
        }

        userNoteEditTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return  true;
                }

                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newNoteButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("notes", userNoteEditTextField.getText().toString());

            }
        });

        notesListItems = new ArrayList<>();

        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesListItems);
        userNoteListView.setAdapter(notesAdapter);
        userNoteListView.setClickable(true);

        if (showHintNote) {
            notesListItems.add(getResources().getString(R.string.note_hint_text));
        }

        notesListItems.add("Second Note is here");
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