package com.vegvisir.app.annotativemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by jiangyi on 14/07/2017.
 */

public class send extends AppCompatActivity implements View.OnClickListener{
    private Button send_content, done1 = null;
    private TextView listening;
    private EditText from_port, to_ip_address, to_port;
    private String to_ip = "10.131.179.75";
    private int from_port_num = 4567;
    private int to_port_num = 4567;
    private String message = "";
    private int ver = 0;
    private int user_id = 1;

    public DatagramSocket socket;
    public InetAddress serverAddress;
    private byte b[] = new byte[8192];
    private DatagramPacket packet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);

        Intent intent = getIntent();
        message = intent.getStringExtra("CONTENT");

        listening = findViewById(R.id.listening);
        send_content = findViewById(R.id.send_content);
        done1 = findViewById(R.id.done1);
        from_port = findViewById(R.id.from_port);
        to_ip_address = findViewById(R.id.to_ip_address);
        to_port = findViewById(R.id.to_port);

//        from_ip_address.setText(getHostAddress());

//        listen();

        send_content.setOnClickListener(this);
        done1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_content:
                if (!TextUtils.isEmpty(to_ip_address.getText().toString())) {
                    to_ip = to_ip_address.getText().toString();
                }
                if(!TextUtils.isEmpty(from_port.getText().toString())) {
                    from_port_num = Integer.parseInt(from_port.getText().toString());
                }
                if(!TextUtils.isEmpty(to_port.getText().toString())) {
                    to_port_num = Integer.parseInt(to_port.getText().toString());
                }

                if (socket == null) {
                    try {
                        socket = new DatagramSocket(from_port_num);
                        serverAddress = InetAddress.getByName(to_ip);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(message);

                send("user_id"+" "+user_id,to_port_num);
                send(message, to_port_num);
                String pc = "0" + " " + to_ip + " " + to_port_num + " " + "1";
//                send("Address", pc, to_port_num);
//                String mobile = "1" + " " + "mobileIP" + " " + from_port_num + " " + "2";
//                send("Address", mobile,to_port_num);
                Toast.makeText(getApplicationContext(), "Send over", Toast.LENGTH_SHORT).show();
                break;
            case R.id.done1:
                socket.close();
                Intent intent1 = new Intent(send.this, picture.class);
                setResult(2, intent1);
                finish();
                break;
            default:
                break;
        }
    }

    private void send(final String msg, final int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    String str = "Hello World";
//                    byte data[] = str.getBytes();
//                    packet = new DatagramPacket(data, data.length, serverAddress, port);
//                    socket.send(packet);

                    byte sendMsg[] = (msg).getBytes();
                    packet = new DatagramPacket(sendMsg,sendMsg.length,serverAddress,port);
                    socket.send(packet);
                    System.out.println(msg); //
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte o[] = new byte[8192];
                    DatagramPacket packet = new DatagramPacket(o, o.length);
                    socket.receive(packet);
                    String od = new String(packet.getData(),packet.getOffset(),packet.getLength());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
