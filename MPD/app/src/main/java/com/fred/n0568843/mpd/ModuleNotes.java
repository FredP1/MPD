package com.fred.n0568843.mpd;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ModuleNotes extends AppCompatActivity {

    DatabaseReference dref;
    ArrayList<String> list = new ArrayList<>();
    ListWithImage adapter;
    private FirebaseAuth mAuth;
    ArrayList<Note> noteList = new ArrayList<>();
    int imageID = R.drawable.paper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_notes);

        mAuth = FirebaseAuth.getInstance();
        String ModuleName = getIntent().getStringExtra("MODULE_NAME");
        setTitle(ModuleName + " Notes");

        //Add Floating action button action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ModuleNotes.this, NewNote.class);
                startActivity(myIntent);
                //Toast.makeText(getActivity(), "Add Note button", Toast.LENGTH_SHORT).show();
            }
        });

        ListView moduleListView = findViewById(R.id.moduleNotesListView);
        adapter=new ListWithImage(this, list, imageID);
        moduleListView.setAdapter(adapter);
        dref = FirebaseDatabase.getInstance().getReference("UserID/"+mAuth.getUid()+"/Modules/"+ModuleName);
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey().toString();
                Log.d("ModuleNotes", key);
                list.add(key);
                adapter.notifyDataSetChanged();
                Note note = dataSnapshot.getValue(Note.class);
                noteList.add(note);
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
        moduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Dave", list.get(i));;
                Intent myIntent = new Intent(ModuleNotes.this, NewNote.class);
                myIntent.putExtra("NoteTitle", noteList.get(i).title);
                myIntent.putExtra("NoteContents", noteList.get(i).noteContents);
                startActivity(myIntent);
            }
        });
    }

}
