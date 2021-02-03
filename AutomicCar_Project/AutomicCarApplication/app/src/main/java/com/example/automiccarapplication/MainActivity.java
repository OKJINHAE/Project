package com.example.automiccarapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    Button btnForward, btnLeft, btnRight, btnBackward, btnStop, btnOff;
    EditText ipAddress;
    public static String CMD = "0";

    RelativeLayout layout_joystick;
    TextView textDirection;

    JoyStickClass js;

    //서버주소
    public static final String sIP = "192.168.0.11";
    //사용할 통신 포트
    public static final int sPORT = 8011;
    //데이터 보낼 클랙스
    public SendData mSendData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버튼 부분
        btnForward = findViewById(R.id.btnForward);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnBackward = findViewById(R.id.btnBackward);
        ipAddress = findViewById(R.id.ipAddress);
        btnStop = findViewById(R.id.btnStop);
        btnOff = findViewById(R.id.btnOff);

        //조이스틱 부분
        textDirection = (TextView) findViewById(R.id.textDirection);
        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);
        js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);

        // 버튼 조작(시계방향으로 위오른쪽아래왼쪽)
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendData = new SendData();
                CMD="FF,80";
                //보내기 시작
                mSendData.start();

            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendData = new SendData();
                CMD="RR,80";
                //보내기 시작
                mSendData.start();
            }
        });
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendData = new SendData();
                CMD="BB,80";
                //보내기 시작
                mSendData.start();
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendData = new SendData();
                CMD="LL,80";
                //보내기 시작
                mSendData.start();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendData = new SendData();
                CMD="SS,00";
                //보내기 시작
                mSendData.start();
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendData = new SendData();
                CMD="PP,00";
                //보내기 시작
                mSendData.start();
            }
        });

        //조이스틱 8등분
        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    int direction = js.get8Direction();
                    if (direction == JoyStickClass.STICK_UP) {
                        textDirection.setText("Direction : Up");
                        mSendData = new SendData();
                        CMD="FF,80";
                        //보내기 시작
                        mSendData.start();

                    } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                        textDirection.setText("Direction : Up Right");
                        mSendData = new SendData();
                        CMD="FR,80";
                        //보내기 시작
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_RIGHT) {
                        textDirection.setText("Direction : Right");
                        mSendData = new SendData();
                        CMD="RR,80";
                        //보내기 시작
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textDirection.setText("Direction : Down Right");
                        mSendData = new SendData();
                        CMD="BR,80";
                        //보내기 시작
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_DOWN) {
                        textDirection.setText("Direction : Down");
                        mSendData = new SendData();
                        CMD="BB,80";
                        //보내기 시작
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        textDirection.setText("Direction : Down Left");
                        mSendData = new SendData();
                        CMD="BL,80";
                        //보내기 시작
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_LEFT) {
                        textDirection.setText("Direction : Left");
                        mSendData = new SendData();
                        CMD="LL,80";
                        //보내기 시작
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_UPLEFT) {
                        textDirection.setText("Direction : Up Left");
                        mSendData = new SendData();
                        CMD="FL,80";
                        //보내기 시작
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_NONE) {
                        textDirection.setText("Direction : Center");
                        mSendData = new SendData();
                        CMD="SS,00";
                        //보내기 시작
                        mSendData.start();
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    textDirection.setText("Direction :");
                    mSendData = new SendData();
                    CMD="SS,00";
                    //보내기 시작
                    mSendData.start();
                }
                return true;
            }
        });
    }

    //데이터 보내는 쓰레드 클래스
    class SendData extends Thread{
        public void run(){
            try{
                //UDP 통신용 소켓 생성
                DatagramSocket socket = new DatagramSocket();
                //서버 주소 변수
                InetAddress serverAddr = InetAddress.getByName(sIP);

                //보낼 데이터 생성
                byte[] buf = CMD.getBytes();

                //패킷으로 변경
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);

                //패킷 전송!
                socket.send(packet);

//                //데이터 수신 대기
//                socket.receive(packet);
//                //데이터 수신되었다면 문자열로 변환
//                String msg = new String(packet.getData());
//                //txtView에 표시
//                txtView.setText(msg);
            }catch (Exception e){

            }
        }
    }
}