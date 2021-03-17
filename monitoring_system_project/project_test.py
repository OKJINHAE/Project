import Adafruit_DHT
from time import *
import sys
from PyQt5.QtWidgets import *
from PyQt5 import uic
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtCore import *
from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
from threading import Timer
import datetime
from PyQt5.QtCore import QTimer, QTime
import serial
from PMS7003 import PMS7003
from mq import *
import math
from MCP3008 import MCP3008

form_class = uic.loadUiType("test_window.ui")[0]
pin = 4
sensor = Adafruit_DHT.DHT11
dust = PMS7003()
Speed = 9600
UART = '/dev/ttyAMA0'
SERIAL_PORT = UART
ser = serial.Serial(SERIAL_PORT, Speed, timeout = 1)


#mq = MQ();
#perc = mq.MQPercentage()
#sys.stdout.write("LPG: %g ppm, CO: %g ppm, Smoke: %g ppm" % (perc["GAS_LPG"], perc["CO"], perc["SMOKE"]))


class MyWindow(QMainWindow, form_class):
    def __init__(self):
        super().__init__()
        self.setupUi(self)
        self.on_set.clicked.connect(self.on_set_clicked)
        self.off_set.clicked.connect(self.off_set_clicked)
        self.timer = QTimer(self)
        self.timer.setInterval(1000)
        self.timer.timeout.connect(self.displayTime)
        self.timer.start()
        
    def displayTime(self):
        h, t = Adafruit_DHT.read_retry(sensor, pin)
        buffer = ser.read(1024)
        data = dust.unpack_data(buffer)
        d = (data[dust.DUST_PM2_5_ATM])
        now = datetime.datetime.now()
        nowDate = now.strftime('%Y-%m-%d %H:%M:%S')
        mq = MQ();
        perc = mq.MQPercentage()
#sys.stdout.write("LPG: %g ppm, CO: %g ppm, Smoke: %g ppm" % (perc["GAS_LPG"], perc["CO"], perc["SMOKE"]))
        self.time.setText(str(nowDate))        
        self.h_label.setText(str(h))
        self.t_label.setText(str(t))
        self.d_label.setText(str(d))
        self.g_label.setText(str(perc["GAS_LPG"]))
        if(self.on_set_clicked == True):
            set_list1 = set_on_clicked()
            set_hum1 = set_list1[1]
            set_tem1 = set_list1[0]
            if(set_hum1 < h) | (set_tem1 < t):
                print("온도가 높거나 습도가 높습니다.")
                if(d < 15):
                    self.d_image.setText("좋음")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 50):
                    self.d_image.setText("보통")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 100):
                    self.d_image.setText("나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                else:
                    self.d_image.setText("매우나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고")                
            else:
                print("온도와 습도가 적당합니다.")
                if(d < 15):
                    self.d_image.setText("좋음")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 50):
                    self.d_image.setText("보통")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 100):
                    self.d_image.setText("나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                else:
                    self.d_image.setText("매우나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
        elif(self.off_set_clicked == True):
            set_list2 = set_on_clicked()
            set_hum2 = set_list2[1]
            set_tem2 = set_list2[0]
            if(set_hum2 < h) | (set_tem2 < t):
                print("온도가 낮거나 습도가 낮습니다.")
                if(d < 15):
                    self.d_image.setText("좋음")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 50):
                    self.d_image.setText("보통")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 100):
                    self.d_image.setText("나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                else:
                    self.d_image.setText("매우나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
            else:
                print("온도와 습도가 적당합니다.")
                if(d < 15):
                    self.d_image.setText("좋음")

        else:
            set_hum1 = 80
            set_tem1 = 25
            if(set_hum1 < h) | (set_tem1 < t):
                print("온도가 높거나 습도가 높습니다.")
                if(d < 15):
                    self.d_image.setText("좋음")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 50):
                    self.d_image.setText("보통")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 100):
                    self.d_image.setText("나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                else:
                    self.d_image.setText("매우나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
            else:
                print("온도와 습도가 적당합니다.")
                if(d < 15):
                    self.d_image.setText("좋음")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 50):
                    self.d_image.setText("보통")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                elif(d < 100):
                    self.d_image.setText("나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 
                else:
                    self.d_image.setText("매우나쁨")
                    if(perc["GAS_LPG"] < 0.01):
                        self.g_image.setText("정상")
                    else:
                        self.d_image.setText("경고") 



    def on_set_clicked(self):
        tem = self.t_on_set_spinbox_on.value()
        hum = self.h_on_set_spinbox_on.value()
        set_list1 = [tem, hum]
        return set_list1
    
    def off_set_clicked(self):
        tem1 = self.t_off_set_spinbox_off.value()
        hum1 = self.h_off_set_spinbox_off.value()
        set_list2 = [tem1, hum1]
        return set_list2

        

        
if __name__ =="__main__":  
    app = QApplication(sys.argv)
    mywindow = MyWindow()
    mywindow.show()
    app.exec_()


