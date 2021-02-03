package com.example.automiccarapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    ImageButton btnOff, btnCheck, btn1, btn2, btn3, btn4, btn5, btn6;
    Button btnCancel;
    TextView textView, textSend, textAngle, textDistance, textBack;

    public static String CMD = "0";

    RelativeLayout layout_joystick;

    JoyStickClass js;

    //서버주소
    public static final String sIP = "192.168.0.27";
    //사용할 통신 포트
    public static final int sPORT = 8011;
    //데이터 보낼 클랙스
    public SendData mSendData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버튼 부분
        btnOff = findViewById(R.id.btnOff);
        btnCheck = findViewById(R.id.btnCheck);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);

        btnCancel = findViewById(R.id.btnCancel);

        textView = findViewById(R.id.textView);
        textSend = findViewById(R.id.textSend);
        textAngle = findViewById(R.id.textAngle);
        textDistance = findViewById(R.id.textDistance);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Automic Car");
        btnCancel = dialog.findViewById(R.id.btnCancel);
        textBack = dialog.findViewById(R.id.textBack);


        //조이스틱 부분
        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);
        js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.image_button);

        //눌렀을 때 나오는 스틱 사이즈
        js.setStickSize(150, 150);
        //스틱 움직이는 범위가 되는 배경
        js.setLayoutSize(600, 600);
        js.setLayoutAlpha(80);
        js.setStickAlpha(200);
        js.setOffset(90);
        js.setMinimumDistance(50);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(sIP);
                mSendData = new SendData();
                CMD = "check," + sIP;
                //보내기 시작
                mSendData.start();
            }
        });

        //종료 버튼
        btnOff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnOff.setBackgroundResource(R.drawable.button33);
                    textView.setText("종료되었습니다.");
//                    Toast.makeText(getApplication(),"종료합니다",Toast.LENGTH_SHORT).show();
                    mSendData = new SendData();
                    CMD = "joystick,PP,0,0";
                    //보내기 시작
                    mSendData.start();
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnOff.setBackgroundResource(R.drawable.button3);
                }
                return false;
            }
        });

        //지정한 곳에 이동하는 버튼
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textBack.setText("1번으로 운행중");
                mSendData = new SendData();
                CMD = "line,1";
                //보내기 시작
                mSendData.start();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textBack.setText("2번으로 운행중");
                mSendData = new SendData();
                CMD = "line,2";
                //보내기 시작
                mSendData.start();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textBack.setText("3번으로 운행중");
                mSendData = new SendData();
                CMD = "line,3";
                //보내기 시작
                mSendData.start();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textBack.setText("4번으로 운행중");
                mSendData = new SendData();
                CMD = "line,4";
                //보내기 시작
                mSendData.start();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textBack.setText("5번으로 운행중");
                mSendData = new SendData();
                CMD = "line,5";
                //보내기 시작
                mSendData.start();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textBack.setText("6번으로 운행중");
                mSendData = new SendData();
                CMD = "line,6";
                //보내기 시작
                mSendData.start();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //조이스틱 속도조절
//        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                textView.setText("조이스틱  사용중");
//                js.drawStick(event);
//                //textX.setText("X : " + String.valueOf(js.getX()));
//                //textY.setText("Y : " + String.valueOf(js.getY()));
//                textAngle.setText("Angle : " + String.valueOf(Math.round(js.getAngle())));
//                textDistance.setText("Distance : " + String.valueOf(Math.round(js.getDistance())));
//                int valueX =js.getX(), valueY = js.getY();
//                Float angle = js.getAngle();
//                //int distance = 0; //js.getDistance();
//                int distance = Math.round(js.getDistance());
//                String L = "", R = "";
//                double numL, numR;
//
//
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    //int direction = js.get8Direction();
//                    String direction = js.get2Direction();
//                    //textDirection.setText("Direction : " + String.valueOf(js.get2Direction()));
//                    //if (distance >360){distance=360;}
//                    if (valueY <= 0) {
//                        if (valueX >= 0) { //F_R
//                            numL = (distance / 3);
//                            L = String.valueOf(Math.round(numL));
//                            numR = ((360 - angle) * 1.11 * (distance / 280));
//                            R = String.valueOf(Math.round(numR));
//                        } else { //F_L
//                            numL = ((angle - 180) * 1.11 * (distance / 280));
//                            L = String.valueOf(Math.round(numL));
//                            numR = (distance / 3);
//                            R = String.valueOf(Math.round(numR));
//                        }
//                    }
//                    else{
//                        if (valueX >= 0) { //Back_R
//                            numL = (distance / 3);
//                            L = String.valueOf(Math.round(numL));
//                            numR = (angle * 1.11 * (distance / 280));
//                            R = String.valueOf(Math.round(numR));
//                        } else { //Back_L
//                            numL = ((180-angle) * 1.11 * (distance / 280));
//                            L = String.valueOf(Math.round(numL));
//                            numR =  (distance / 3);
//                            R = String.valueOf(Math.round(numR));
//                        }
//                    }
//                    textSend.setText("L:  "+L + " R: " + R);
//                    mSendData = new SendData();
//                    Log.e("MainActivity", "Send L, R");
//                    CMD = "joystick"+","+direction+","+L+","+R;
//                    //보내기 시작
//                    mSendData.start();
//                }
//                if (valueX == 0 && valueY ==0){
//                    //event.getAction() == MotionEvent.ACTION_UP){
//                    textView.setText("");
//                    mSendData = new SendData();
//                    Log.e("MainActivity", "Send S");
//                    textSend.setText("L: 0" + " R: 0");
//                    CMD = "joystick"+","+"S"+",0, 0";
//                    //보내기 시작
//                    mSendData.start();
//                }
//                return true;
//            }
//        });
        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    int direction = js.get8Direction();
                    textView.setText("조이스틱  사용중");
                    if (direction == JoyStickClass.STICK_UP) {
                        mSendData = new SendData();
                        CMD = "joystick,FF,100";
                        textSend.setText(CMD);
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                        mSendData = new SendData();
                        CMD = "joystick,FR,100";
                        textSend.setText(CMD);
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_RIGHT) {
                        mSendData = new SendData();
                        CMD = "joystick,RR,100";
                        textSend.setText(CMD);
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                        mSendData = new SendData();
                        CMD = "joystick,BR,100";
                        textSend.setText(CMD);
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_DOWN) {
                        mSendData = new SendData();
                        CMD = "joystick,BB,100";
                        textSend.setText(CMD);
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        mSendData = new SendData();
                        CMD = "joystick,BL,100";
                        textSend.setText(CMD);
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_LEFT) {
                        mSendData = new SendData();
                        CMD = "joystick,LL,100";
                        textSend.setText(CMD);
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_UPLEFT) {
                        mSendData = new SendData();
                        CMD = "joystick,FL,100";
                        textSend.setText(CMD);
                        mSendData.start();
                    } else if (direction == JoyStickClass.STICK_NONE) {
                        mSendData = new SendData();
                        CMD = "joystick,SS,00";
                        textSend.setText(CMD);
                        mSendData.start();
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    mSendData = new SendData();
                    CMD = "joystick,SS,00";
                    textSend.setText("");
                    mSendData.start();
                }
                return true;
            }
        });
    }

    //데이터 보내는 쓰레드 클래스
    class SendData extends Thread {
        public void run() {
            try {
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

                //데이터 수신 대기
                socket.receive(packet);

//                //데이터 수신되었다면 문자열로 변환
//                String msg = new String(packet.getData());
//
//                //txtView에 표시
//                txtView.setText(msg);

            } catch (Exception e) {

            }
        }
    }
}