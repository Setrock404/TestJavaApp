package com.example.testjavaapp.fragments;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDeepLinkBuilder;

import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testjavaapp.MainActivity;
import com.example.testjavaapp.R;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    private static final String CHANNEL_ID = "CUSTOM_ID";
    private static final String TAG = "Notification";
    private NotificationManagerCompat notificationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button addNotificationBtn = view.findViewById(R.id.btn_notification);
        notificationManager = NotificationManagerCompat.from(getActivity());

        addNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendNotification() {

        if(isNotificationVisible()){
            notificationManager.cancel(1);
            Log.d(TAG, "sendNotification: "+notificationManager.getNotificationChannels());
            return;
        }



        createNotificationChannel();

        Intent notificationIntent = new Intent(getActivity(),MainActivity.class);
        notificationIntent.putExtra("menuFragment", "favoritesMenuItem");
        notificationIntent.setAction("OPEN_NOTIFICATION_FRAGMENT");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);

        //Navigation way to redirect to Current Fragment
//        PendingIntent pendingIntent2 =  new NavDeepLinkBuilder(getActivity())
//                .setComponentName(MainActivity.class)
//                     .setGraph(R.navigation.main)
//                .setDestination(R.id.notificationScreen)
//                .createPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        notificationManager.notify(1, builder.build());

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isNotificationVisible() {
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = mNotificationManager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == 1) {
                return true;
            }
        }
        return false;
    }



}
