package edu.neu.madcourse.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class chooseRecipientActivity extends AppCompatActivity {

    private User user;
    private String username;
    private String SERVER_KEY;
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<String> active_user_list = new ArrayList<>();;
    private ArrayAdapter<String> adapter;
    private String selectedUserName = "";
    //    private final Map<String, Integer> sendHistory = new HashMap<>();
    private final String TAG = "chooseRecipientActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_recipient);

        // init
        SERVER_KEY = getIntent().getStringExtra("SERVER_KEY");
        username = getIntent().getStringExtra("username");
        Button btn_choose = findViewById(R.id.btn_choose);



        // listview
        ListView usersListView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,
                active_user_list);
        usersListView.setAdapter(adapter);
        usersListView.setOnItemClickListener((parent, view, position, id)
                -> selectedUserName = (String) parent.getItemAtPosition(position));
//        int i = 0;
//        Log.d(TAG, selectedUserName.toString());
        TextView display = findViewById(R.id.textView2);


        // database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                user = snapshot.getValue(User.class);
                assert user != null;
                if (!user.username.equals(username)) {
                    users.add(user);
                    int i = 0;
                    Log.d(TAG, "onChildAdded: "+user.username);
                    active_user_list.add(user.username);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_choose.setOnClickListener(v -> {
            Intent SentActivity = new Intent(getApplicationContext(), SentStickerActivity.class);
            //add more data to intent
            SentActivity.putExtra("SERVER_KEY", SERVER_KEY);
            SentActivity.putExtra("username", username);
            SentActivity.putExtra("CLIENT_REGISTRATION_TOKEN", user.CLIENT_REGISTRATION_TOKEN);
            SentActivity.putExtra("selectedUserName", selectedUserName);

            startActivity(SentActivity);
        });
    }
}