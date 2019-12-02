package com.example.serborll.examentipo1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;
    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void lanzarSegundaActivity(View view){
        Intent i = new Intent(this, segundaActivity.class);
        startActivity(i);
    }

    public void llamarTelefono(View view) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:5555"));
            startActivity(intent);
        } else {
            solicitarPermiso(Manifest.permission.CALL_PHONE, "Sin el permiso ->"+
                            " No puedo llamar si no das el permiso.",
                    SOLICITUD_PERMISO_WRITE_CALL_LOG, this);
        }
    }

    public static void solicitarPermiso(final String permiso, String
            justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }})
                    .show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    public void lanzarNotificacion(View view) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "Mis Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(MainActivity.this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notificacion")
                        .setContentText("Esto es una notificacion.");
        PendingIntent intencionPendiente = PendingIntent.getActivity(
                this, 0, new Intent(this, segundaActivity.class), 0);
        notificacion.setContentIntent(intencionPendiente);
        notificationManager.notify(NOTIFICACION_ID, notificacion.build());
    }




}
