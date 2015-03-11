package com.blurbook.blurbook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blurbook.blurbook.Controllers.InboxActivity;
import com.blurbook.blurbook.Controllers.LoginActivity;
import com.blurbook.blurbook.Controllers.ProfileActivity;
import com.blurbook.blurbook.Controllers.SettingActivity;
import com.blurbook.blurbook.Controllers.WishListActivity;
import com.blurbook.blurbook.Services.RecyclerViewAdapter;
import com.blurbook.blurbook.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
* A simple {@link Fragment} subclass.
*/
public class NavigationDrawerFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    int[] icons = {R.drawable.ic_mail, R.drawable.ic_person, R.drawable.ic_heart,R.drawable.ic_setting};
    String[] titles = {"Inbox", "Profile", "Wish List", "Setting"};

    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private View containerView;

    TextView tvEmail, tvUserName;
    CircleImageView circleProfileImageView;
    public static final String DEFAULT = "N/A";

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if(savedInstanceState!=null)
        {
            mFromSavedInstanceState=true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerViewAdapter(getActivity(), titles, icons);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener(){
            @Override
            public void onClick(View v, int position) {

                switch (position) {
                    case 0:
                        Intent intent = new Intent(v.getContext(), InboxActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(v.getContext(), ProfileActivity.class);
                        startActivity(intent1);
                        break;

                    case 2:
                        Intent intent2 = new Intent(v.getContext(), WishListActivity.class);
                        startActivity(intent2);
                        break;

                    case 3:
                        Intent intent3 = new Intent(v.getContext(), SettingActivity.class);
                        startActivity(intent3);
                        break;
                }

                Toast.makeText(getActivity(), titles[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        tvEmail = (TextView) layout.findViewById(R.id.email);
        tvUserName = (TextView) layout.findViewById((R.id.name));
        circleProfileImageView = (CircleImageView) layout.findViewById(R.id.circleProfilePicView);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("LoginSession", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", DEFAULT);
        String firsName = sharedPreferences.getString("firstName", DEFAULT);
        String lastName = sharedPreferences.getString("lastName", DEFAULT);
        String avatarLink = sharedPreferences.getString("avatarLink", DEFAULT);

        if(email.equals(DEFAULT) || firsName.equals(DEFAULT) || lastName.equals(DEFAULT))
        {
            tvEmail.setText("");
            tvUserName.setText("");
            circleProfileImageView.setImageResource(R.drawable.pic_profile);
        }
        else
        {
            tvEmail.setText(email);
            tvUserName.setText(firsName + " " + lastName);
            if(avatarLink.equals(DEFAULT))
            {
                circleProfileImageView.setImageResource(R.drawable.pic_profile);
            }
            else
            {
                circleProfileImageView.setImageBitmap(BitmapFactory.decodeFile(avatarLink));
            }
        }

        Button loginSignUpButton = (Button) layout.findViewById(R.id.login_sign_up_button);
        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return layout;
    }

    public void setUp(int fragementId, DrawerLayout drawerLayout, Toolbar toolbar)
    {
        containerView=getActivity().findViewById(fragementId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if(!mUserLearnedDrawer && !mFromSavedInstanceState)
        {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.commit();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defalutValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defalutValue);
    }

    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(final Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if(child!=null && clickListener!=null)
                    {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(), e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }
    }
}
