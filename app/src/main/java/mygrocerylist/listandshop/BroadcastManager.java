package mygrocerylist.listandshop;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class BroadcastManager extends BroadcastReceiver {

    String state;
    TypedArray imgs;

    @Override
    public void onReceive(Context context, Intent intent) {
/*        String action = intent.getAction();
        if(action.equals("my.action.string")){
            state = intent.getExtras().getString("extra");}





            try {
            String yourDate = "20/07/2018";
            String yourHour = "18:32:01";
            Date d = new Date();
            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            if (date.equals(yourDate) && hour.equals(yourHour)) {
                Intent it = new Intent(context, MainActivity.class);
                createNotification(context, it, "new mensage", "body!", "this is a mensage");
            }
        } catch (Exception e) {
            Log.i("date", "error == " + e.getMessage());
        }*/

        imgs = context.getResources().obtainTypedArray(R.array.icons);
        String name = intent.getExtras().getString("name");
        Integer icon = intent.getExtras().getInt("icon");
        Integer random = intent.getExtras().getInt("random");
        createNotification(context, icon, name, random);
    }


    public void createNotification(Context context, Integer icon, String text, Integer random) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), "Main");
        Intent ii = new Intent(context.getApplicationContext(), CalendarClass.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), imgs.getResourceId(icon, -1)); //huh

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
/*        bigText.bigText("omg");
        bigText.setBigContentTitle("Today's Bible Verse");
        bigText.setSummaryText("Text in detail");*/


        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setLargeIcon(bm);
        mBuilder.setContentTitle("List and Shop reminder:");
        mBuilder.setContentText("Remember to buy more " + text + "!");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Main",
                    "Notifications for List and Shop items",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(random, mBuilder.build());
    }
}
