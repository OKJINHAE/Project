# local DB : kaggle1에 업로드
# collection 목록
# - bson : 이미지 이진화
# - blosc : numpy배열을 업로드 하는 방식
# - fs.chunks, fs.files : gridfs 방식 ( 16MB 이상의 파일 업로드 가능, 자동 생성 )

# pip install pymongo
# pip install pandas , matplotlib, pillow

import gridfs
import numpy as np
from PIL import Image
import blosc
import base64
import os
import zipfile
from pymongo import MongoClient
from glob import glob # 원하는 파일 리스트를 뽑아 올 때 사용가능


# 지역 db 접근
client = MongoClient('localhost', 27017) # 지역 local로만 접속함
# 경로 지정
photo_test='C:\\Users\\w\\jupyter\\kaggle\\input\\ranzcr-clip-catheter-line-classification\\test' # upload할 dir
zip_dir='C:\\Users\\w\\Desktop\\kaggle\\datasample\\zip' # zip한 파일 저장할 dir
file_list = os.listdir(photo_test)
zip_list=os.listdir(zip_dir)

# 간단 binary 형식=========================================================
# 파일 내에 있는 모든 리스트 업로드
def fileup():
    for file in file_list:
        img_name=photo_test+'\\'+file # 출력하는 파일 주소
        photo=open(img_name, 'rb') # binary형식으로 읽기
        encoded_string=base64.b64encode(photo.read()) #이미지 파일 2진화형식으로
        xrayimg={"imagenum":file, "photo":encoded_string}
        client.kaggle1.bson.insert_one(xrayimg)

# 파일 내에 있는 모든 리스트 download
def filedown():
    document=client.kaggle1.bson.find()
    count=0
    for doc in document:
        # os.chdir('C:\\Users\\w\\Desktop\\kaggle\\datasample\\down') 원하는 위치에 저장가능
        # open에다가 경로 써줘도 상관은 없음
        img=base64.b64decode(doc['photo'])
        with open(doc['imagenum'], mode='wb') as e:
            e.write(img)

# python-blosc 형식===========================
def blosc_up():
    for file in file_list:
        img_name=photo_test+'\\'+file
        img=Image.open(img_name)
        # numpy화
        # numpy.asarry() 이미지->array
        image_array=np.asarray(img)
        #  pack_array 배열 압축
        compressed_bytes = blosc.pack_array(image_array)
        xrayimg={"imagenum":file, "photo":compressed_bytes}
        client.kaggle1.blosc.insert_one(xrayimg)

def blosc_down():
    pass
#=============================================
# 1. 파일 기본 압축
def file_zip():
    os.chdir(photo_test) # 이미지가 저장되어 있는 베이스로이동!
    for file in file_list:
        myzip_w = zipfile.ZipFile(zip_dir+'\\'+file[:-4] + '.zip', 'w')
        myzip_w.write(file)
        myzip_w.close()

# 2. 압축파일 몽고 upload중이었으나 하다가 알고리즘으로 넘어감
def fileup_zip():
    for file in zip_list:
        img_name=zip+'\\'+file
        xrayimg={"imagenum":file, "zip":img_name} # 파일
        client.kaggle1.blosc.insert_one(xrayimg)
def Gridup():
    db = client.kaggle1
    path = photo_test
    fs = gridfs.GridFSBucket(db)
    file_list = os.listdir(photo_test)
    # file_list = [os.path.basename(x) for x in glob(path + './*.jpg')] ## 경로 안 파일의 이름만 리스트로 넣음
    for file in file_list:
        with open(path + '\\'+file, 'rb') as jpg:
            fs.upload_from_stream(file, jpg) # fs로 업로드

def Griddown():
    db = client.kaggle1
    # 저장할 위치
    path = 'C:\\Users\\w\\jupyter\\kaggle\\input\\ranzcr-clip-catheter-line-classification\\downtest'
    fs = gridfs.GridFSBucket(db)
    for data in db.fs.files.find({}, {'filename':True}):
        filename = data['filename']
        with open(path +'\\' +filename, 'wb') as jpg:
            fs.download_to_stream_by_name(data['filename'], jpg)
    print('Done')

while True:
    print('1: bin up, 2: bin down, 3: blosc up, 4: blosc down, 6: gridup, 7: griddown, 9: 종료')
    choice=int(input('숫자를 선택하세요 : '))
    if choice==1:
        fileup()
    if choice==2:
        filedown()
    if choice==3:
        blosc_up()
    if choice==5:
        file_zip()
    if choice==6:
        Gridup()
    if choice==7:
        Griddown()
    if choice==9:
        exit(0)