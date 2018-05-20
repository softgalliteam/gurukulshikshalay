package softgalli.gurukulshikshalay.chatting;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;

public class ChattingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mChatListView)
    ListView mChatListView;
    @BindView(R.id.messageInput)
    EditText messageInput;
    @BindView(R.id.sendButton)
    ImageButton sendButton;
    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    private String TAG = ChattingActivity.class.getSimpleName();
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_activity);
        ButterKnife.bind(this);
        mActivity = this;
        initToolbar();
        // Make sure we have a mUsername
        setupUsername();

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(ApiUrl.FIREBASE_URL).child("chat");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilz.isOnline(mActivity))
                    sendMessage();
                else
                    Toast.makeText(mActivity, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
            }
        });
        Utilz.showDailog(mActivity, getResources().getString(R.string.pleasewait));
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Open Discussion");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        //final mChatListView mChatmChatListView = getmChatListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limitToLast(50), this, R.layout.chat_message, mUsername);
        mChatListView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mChatListView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               /* boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(ChattingActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChattingActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }*/
                Utilz.closeDialog();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        // Assign a random user name if we don't have one saved.
        mUsername = MyPreference.getUserName();
        if (TextUtils.isEmpty(mUsername))
            mUsername = ClsGeneral.getStrPreferences(AppConstants.NAME);
        if (AppConstants.STUDENT.equalsIgnoreCase(MyPreference.getLoginedAs())) {
            mUsername = mUsername + "(Student)";
        } else {
            mUsername = mUsername + "(" + ClsGeneral.getStrPreferences(AppConstants.DESIGNATION) + ")";
        }
    }

    private void sendMessage() {
        String input = "Welcome to " + mActivity.getResources().getString(R.string.app_name);
        if (messageInput != null)
            input = messageInput.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a ChatModel object
            ChatModel chat = new ChatModel(input, mUsername);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            if (messageInput != null)
                messageInput.setText("");
        }
    }
}
