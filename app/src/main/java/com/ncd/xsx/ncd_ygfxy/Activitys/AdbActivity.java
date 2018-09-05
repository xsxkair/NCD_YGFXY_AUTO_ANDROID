package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ncd.xsx.ncd_ygfxy.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class AdbActivity extends MyActivity {

    private static final String TAG = "ServerThread";
    ServerThread serverThread;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Toast.makeText(getApplicationContext(), msg.getData().getString("MSG", "Toast"), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        super.MyActivityCommonInit();

        serverThread = new ServerThread();
        serverThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverThread.setIsLoop(false);
    }

    class ServerThread extends Thread {
        boolean isLoop = true;

        public void setIsLoop(boolean isLoop)
        {
            this.isLoop = isLoop;
        }

        @Override
        public void run()
        {
            Log.d(TAG, "running");
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(9000);
                byte[] buf = new byte[1024];

                while (isLoop)
                {
                    Socket socket = serverSocket.accept();
                    Log.d(TAG, "accept");
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    int len = inputStream.read(buf);
                    String msg = new String(buf, 0, len);
                    Log.d(TAG, "adb recv: "+msg);

                    outputStream.writeBytes("read ok\r");
                    /*Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("MSG", msg);
                    message.setData(bundle);
                    handler.sendMessage(message);*/
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Log.d(TAG, "destory");
                if (serverSocket != null)
                {
                    try {
                        serverSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
