package com.fred.n0568843.mpd;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //DeadlinesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeadlinesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeadlinesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    DatabaseReference dref;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listValues = new ArrayList<>();
    ArrayList<Deadline> deadlineList = new ArrayList<>();
    //ArrayAdapter<String> adapter;
    DeadlineList adapter;
    Calendar myCalendar;
    private String DATE_FORMAT = "dd-MM-yyy HH:mm";
    private FirebaseAuth mAuth;
    private Runnable runnable;
    private Handler handler = new Handler();

    public DeadlinesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeadlinesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeadlinesFragment newInstance(String param1, String param2) {
        DeadlinesFragment fragment = new DeadlinesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("Deadlines");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deadlines, container,false);
        //Add Floating action button action
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //Table stuff
        //final TableLayout tableLayout = view.findViewById(R.id.deadlinesTableLayout);
        //Table stuff end
        myCalendar = Calendar.getInstance();
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("New Deadline");
                final EditText deadlineName = new EditText(getActivity());
                final Button deadlineDate = new Button(getActivity());
                final Button deadlineTime = new Button(getActivity());
                final EditText deadlineModule = new EditText(getActivity());
                deadlineName.setHint("Enter Deadline Name");
                deadlineDate.setHint("Choose Deadline Date");
                deadlineDate.setFocusable(false);
                deadlineTime.setHint("Choose Deadline Time");
                deadlineTime.setFocusable(false);
                LinearLayout lp = new LinearLayout(getActivity());
                lp.setOrientation(LinearLayout.VERTICAL);
                lp.addView(deadlineName);
                lp.addView(deadlineDate);
                lp.addView(deadlineTime);
                builder.setView(lp);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addNewDeadline(deadlineName, deadlineDate, deadlineTime, deadlineModule);
                    }
                });
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                        deadlineDate.setText(sdf.format(myCalendar.getTime()));

                    }
                };
                deadlineDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                deadlineTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                        int minute = myCalendar.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                deadlineTime.setText( selectedHour + ":" + selectedMinute);
                            }
                    }, hour, minute, true);
                    mTimePicker.show();}
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                //Toast.makeText(getActivity(), "Add Module Button", Toast.LENGTH_SHORT).show();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        ListView deadlinesListView = (ListView) view.findViewById(R.id.deadlinesList);
        //adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, list);
        adapter = new DeadlineList(getActivity(),deadlineList);
        deadlinesListView.setAdapter(adapter);
        dref = FirebaseDatabase.getInstance().getReference("UserID/"+mAuth.getUid()+"/Deadlines");
        Log.d("Hello mate" , mAuth.getUid());
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey().toString();
                Log.d("Dave", key);
                deadlineList.add(dataSnapshot.getValue(Deadline.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                deadlineList.remove(dataSnapshot.getValue(Deadline.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                adapter.notifyDataSetChanged();
            }
        });
        //deadlinesListView.setClickable(true);
        //Open notes fragment
        deadlinesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //countdownStart(deadlineList.get(i).deadlineDate, deadlineList.get(i).deadlineTime);
            }
        });
        //Open options menu
        deadlinesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                CharSequence options[] = new CharSequence[] {"Edit "+deadlineList.get(i).deadlineName+" Name", "Delete "+deadlineList.get(i).deadlineName+" Deadline"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose an option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) //Edit name
                        {

                        }
                        if (which == 1) //Delete Deadline
                        {
                            dref.child(deadlineList.get(i).deadlineName).removeValue();
                            deadlineList.remove(i);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
        return view;
    }

    private void addNewDeadline(EditText name, Button date, Button time, EditText module) {
        String deadlineName = name.getText().toString();
        String deadlineDate = date.getText().toString();
        String deadlineTime = time.getText().toString();
        String deadlineModule = module.getText().toString();
        Deadline deadline = new Deadline(deadlineName, deadlineModule, deadlineDate, deadlineTime);
        dref.child(deadlineName).setValue(deadline);
    }

    //    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume() {
        getActivity().setTitle("Deadlines");
        super.onResume();
    }
}
