package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var radioGroup: RadioGroup
    private lateinit var loadingButton: LoadingButton
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        radioGroup = findViewById(R.id.radioGroup)
        loadingButton = findViewById(R.id.custom_button)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        //creating notification channel
        createNotificationChannel()

        custom_button.setOnClickListener {
            custom_button.setButtonStatue(ButtonState.Clicked) //temporary for testing
            when (radioGroup.checkedRadioButtonId) {
                -1 -> Toast.makeText(
                    this,
                    getString(R.string.please_select_the_file_to_downlaod),
                    Toast.LENGTH_LONG
                ).show()
                R.id.glide -> {
                    download(
                        "https://github.com/bumptech/glide",
                        getString(R.string.glide_image_loading_library_by_bumptech)
                    );custom_button.setButtonStatue(ButtonState.Clicked)
                }
                R.id.loadApp -> {
                    download(
                        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter",
                        getString(R.string.loadapp_current_repository_by_udacity)
                    ); custom_button.setButtonStatue(ButtonState.Clicked)
                }
                R.id.retrofit -> {
                    download(
                        "https://github.com/square/retrofit",
                        getString(R.string.retrofit_type_safe_http_client_for_android_and_java_by_sequence_inc)
                    );custom_button.setButtonStatue(ButtonState.Clicked)
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                loadingButton.setButtonStatue(ButtonState.Completed)
                //also creating notification
                buildNotification()

            }


/*
            val extras = intent?.extras;
            val q =  DownloadManager.Query();
            val downloadedID = extras?.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)
            if(downloadedID == downloadID ){ // so it is my file that has been completed
                q.setFilterById(downloadedID);
                val manager =context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val c:Cursor = manager.query(q);
                if (c.moveToFirst()) {
                    val status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // do any thing here
                    }
                }
                c.close();


        }
*/
        }
    }

    private fun buildNotification() {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, DetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
         pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
       action= NotificationCompat.Action(R.drawable.ic_assistant_black_24dp,getString(R.string.notification_action),pendingIntent)


        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(getString(R.string.loadapp))
            .setContentText(getString(R.string.notification_title))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.notification_description)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(action)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun download(URL: String, file: String) {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

        custom_button.setButtonStatue(ButtonState.Loading)

        val prefs = getSharedPreferences("download", 0)
        val editor = prefs.edit()
        editor.putLong("download id", downloadID)
        editor.putString("download file", file)
        editor.apply()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
           notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
        private const val NOTIFICATION_ID = 48918
    }

}
