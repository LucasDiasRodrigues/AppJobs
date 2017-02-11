/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teamappjobs.appjobs.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.ChatActivity;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.asyncTask.AtualizaMsgRecebidaTask;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.web.MensagemJson;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String tipo = remoteMessage.getData().get("tipo");
        String message = remoteMessage.getData().get("mensagem");
        //  String idNotificacao = data.getString("idNotificacao");
        Log.i("Get Mensagem: ", message);

        switch (tipo) {
            case "chat":
                onMessageChat(message);
                break;
            case "novaPromocao":
                onNovaPromocao(message);
                break;
            default:
                break;
        }


    }
    // [END receive_message]

    /**
     * Create and show notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void onNovaPromocao(String messageBody) {
        VitrineJson vitrineJson = new VitrineJson();
        Promocao promocao = vitrineJson.JsonToPromocao(messageBody);

        //  Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("From", "notifyFragSigo");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] v = {100, 500, 100, 500}; //Vibração
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.heart_notif)
                .setContentTitle(promocao.getNome())
                .setContentText(promocao.getDescricao())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(v)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    private void onMessageChat(String message) {

        boolean chatAtivo = false;
        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        SharedPreferences prefsChat = getSharedPreferences("Chat", MODE_PRIVATE);
        MensagemJson json = new MensagemJson();

        //Broadcast
        Intent broadcastIntent = new Intent("chat");
        //send broadcast
        this.sendBroadcast(broadcastIntent);

        List<Bundle> bundles = json.JsonToNotificacoes(message);

        for (Bundle bundle : bundles) {
            String corpoMsg;
            //Cria Notificacao
            String profPicRemetente = bundle.getString("profPic", "");
            String nomeRemetente = bundle.getString("nome");
            String emailRemetente = bundle.getString("email", "");
            int auxNumMsg = Integer.parseInt(bundle.getString("qtdMsg"));
            if (auxNumMsg > 1) {
                corpoMsg = getResources().getString(R.string.corpo_notificacao_chat1) + " " +
                        bundle.getString("qtdMsg") + " " + getResources().getString(R.string.corpo_notificacao_chat2_2);
            } else {
                corpoMsg = getResources().getString(R.string.corpo_notificacao_chat1) + " " +
                        bundle.getString("qtdMsg") + " " + getResources().getString(R.string.corpo_notificacao_chat2_1);
            }


            if (prefsChat.getBoolean("chatAtivo", false)) {

                if (!prefsChat.getString("chatDestinatario", "").equals(emailRemetente)) {

                    //Sem som

                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("origem", "notificacao");
                    intent.putExtra("emailRemetente", emailRemetente);
                    intent.putExtra("nomeRemetente", nomeRemetente);
                    intent.putExtra("profPicRemetente", profPicRemetente);
                    //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    String aux = emailRemetente + nomeRemetente;
                    int auxId = aux.length();

                    PendingIntent pendingIntent = PendingIntent.getActivity(this, auxId, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);


                    sendNotificationNoSound(nomeRemetente, corpoMsg, emailRemetente, pendingIntent, this.getResources().getColor(R.color.wallet_holo_blue_light), R.drawable.ic_chat);
                } else {
                    //Atualizar Lista
                    chatAtivo = true;
                    //updateChatActivity(this, null);
                }
            } else {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("origem", "notificacao");
                intent.putExtra("emailRemetente", emailRemetente);
                intent.putExtra("nomeRemetente", nomeRemetente);
                intent.putExtra("profPicRemetente", profPicRemetente);
                //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                String aux = emailRemetente + nomeRemetente;
                int auxId = aux.length();

                PendingIntent pendingIntent = PendingIntent.getActivity(this, auxId, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                sendNotification(nomeRemetente, corpoMsg, emailRemetente, pendingIntent, this.getResources().getColor(R.color.wallet_holo_blue_light), R.drawable.ic_chat);
            }
        }

        //Atualiza servidor para "Recebido"
        AtualizaMsgRecebidaTask task = new AtualizaMsgRecebidaTask(this, prefs.getString("email", ""), "treinador", chatAtivo);
        task.execute();


    }


    private void sendNotification(String titulo, String message, String tag, PendingIntent pendingIntent, int cor, int icone) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] v = {100, 500, 100, 500}; //Vibração
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(icone)
                .setColor(cor)
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(v)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(tag, 0, notificationBuilder.build());

    }

    private void sendNotificationNoSound(String titulo, String message, String tag, PendingIntent pendingIntent, int cor, int icone) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(icone)
                .setColor(cor)
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(null)
                .setVibrate(null)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(tag, 0, notificationBuilder.build());

    }


}
