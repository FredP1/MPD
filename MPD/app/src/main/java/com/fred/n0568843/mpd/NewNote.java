package com.fred.n0568843.mpd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewNote extends AppCompatActivity {
    DatabaseReference dref;
    DatabaseReference drefModule;
    String chosenModule;
    EditText noteTitle;
    EditText noteContents;
    private FirebaseAuth mAuth;

    ArrayList<String> moduleList = new ArrayList<>();
    ArrayList<String> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        noteTitle = findViewById(R.id.noteTitle);
        noteContents = findViewById(R.id.noteTextBox);

        mAuth = FirebaseAuth.getInstance();
        dref = FirebaseDatabase.getInstance().getReference("UserID/" + mAuth.getUid() + "/Modules");
        Spinner moduleSpinner = findViewById(R.id.moduleSpinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, moduleList);
        moduleSpinner.setAdapter(adapter);
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey().toString();
                moduleList.add(key);
                adapter.notifyDataSetChanged();
                //populates notes list
                for (DataSnapshot uniqueNote : dataSnapshot.getChildren()){
                    noteList.add(uniqueNote.getKey());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        moduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenModule = moduleList.get(i);
                drefModule = FirebaseDatabase.getInstance().getReference("UserID/" + mAuth.getUid() + "/Modules/" +chosenModule);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(noteTitle.getText().toString());
                if (noteTitle.getText().toString().matches(""))
                {
                    Toast.makeText(NewNote.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                }
                else if (noteList.contains(noteTitle.getText().toString()))
                {
                    Toast.makeText(NewNote.this, "Note with that title already exists", Toast.LENGTH_SHORT).show();
                }
                else{
                    drefModule.child(noteTitle.getText().toString()).setValue(noteContents.getText().toString());
                    Intent myIntent = new Intent(NewNote.this, MainActivity.class);
                    NewNote.this.startActivity(myIntent);
                    finish();
                }

            }
        });

    }
}
