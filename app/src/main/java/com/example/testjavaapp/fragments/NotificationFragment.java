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
import android.widget.EditText;
import android.widget.Toast;

import com.example.testjavaapp.MainActivity;
import com.example.testjavaapp.R;
import com.example.testjavaapp.util.CustomAnimator;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationFragment extends Fragment {

    private Button addNotificationBtn;
    private EditText et_NotificationTitle;
    private EditText et_NotificationBody;

    private static final String CHANNEL_ID = "CUSTOM_ID";
    private static final int NOTIFICATION_ID = 123;
    private NotificationManagerCompat notificationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        et_NotificationTitle = view.findViewById(R.id.et_notification_title);
        et_NotificationBody = view.findViewById(R.id.et_notification_body);

        addNotificationBtn = view.findViewById(R.id.btn_notification);
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
    private void sendNotification() {

        if (isNotificationVisible()) {
            CustomAnimator.pressAnimation(addNotificationBtn);
            notificationManager.cancel(NOTIFICATION_ID);
            addNotificationBtn.setText(R.string.send_notification);
            return;
        }

        String notificationTitle = et_NotificationTitle.getText().toString();
        String notificationBody = et_NotificationBody.getText().toString();

        if (inputIsWrong(notificationTitle) || inputIsWrong(notificationBody)) {
            Toast.makeText(getActivity(), "Input is wrong", Toast.LENGTH_SHORT).show();
            return;
        }

        CustomAnimator.pressAnimation(addNotificationBtn);
        createNotificationChannel();

        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        notificationIntent.putExtra("menuFragment", "favoritesMenuItem");
        notificationIntent.setAction("OPEN_NOTIFICATION_FRAGMENT");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        addNotificationBtn.setText(R.string.cancel_notification);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
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
            if (notification.getId() == NOTIFICATION_ID) {
                return true;
            }
        }
        return false;
    }

    private boolean inputIsWrong(String text) {
        return (text.trim().isEmpty());
    }
}
