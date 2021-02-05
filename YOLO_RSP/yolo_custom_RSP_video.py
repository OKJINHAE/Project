import numpy as np
import cv2
import time

min_confidence = 0.5
width = 960
height = 0
show_ratio = 1.0
title_name = 'RSP Custom Yolo'

# Load Yolo
net = cv2.dnn.readNet("model/custom-train-yolo_12000.weights", "model/custom-train-yolo_12000.cfg")
classes = []

with open("model/classes.names", "r") as f:
    classes = [line.strip() for line in f.readlines()]
color_lists = np.random.uniform(0, 255, size=(len(classes), 3))

layer_names = net.getLayerNames()
output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]

tie_label = 'TIE!'
win_label = 'WIN!'
lose_label = 'LOSE!'
font = cv2.FONT_HERSHEY_PLAIN


def RSP_flag_function(rsp_flag, names):
    if names == 'Rock':
        rsp_flag[0] = 1
    elif names == 'Scissors':
        rsp_flag[1] = 1
    elif names == 'Paper':
        rsp_flag[2] = 1
    return rsp_flag


def RSP_output(win, lose, img, indexes, names, boxes, font, colors):
    for idx in indexes:
        x, y, w, h = boxes[idx[0]]
        color = colors[idx[0]]
        if names[idx[0]] == win:
            cv2.putText(img, win_label, (x, y+h), font, 2, color, 2)
        if names[idx[0]] == lose:
            cv2.putText(img, lose_label, (x, y+h), font, 2, color, 2)


def detectAndDisplay(image):
    h, w = image.shape[:2]
    height = int(h * width / w)
    img = cv2.resize(image, (width, height))

    blob = cv2.dnn.blobFromImage(img, 0.00392, (416, 416), swapRB=True, crop=False)

    net.setInput(blob)
    outs = net.forward(output_layers)
    
    confidences = []
    names = []
    boxes = []
    colors = []

    for out in outs:
        for detection in out:
            scores = detection[5:]
            class_id = np.argmax(scores)
            confidence = scores[class_id]
            if confidence > min_confidence:
                center_x = int(detection[0] * width)
                center_y = int(detection[1] * height)
                w = int(detection[2] * width)
                h = int(detection[3] * height)

                x = int(center_x - w / 2)
                y = int(center_y - h / 2)

                boxes.append([x, y, w, h])
                confidences.append(float(confidence))
                names.append(classes[class_id])
                colors.append(color_lists[class_id])

    indexes = cv2.dnn.NMSBoxes(boxes, confidences, min_confidence, 0.4)

    rsp_flag = [0, 0, 0]
    print("R_flag {}, S_flag {}, P_flag {}".format(rsp_flag[0], rsp_flag[1], rsp_flag[2]))

    for i in range(len(boxes)):
        if i in indexes:
            x, y, w, h = boxes[i]
            label = '{} {:,.2%}'.format(names[i], confidences[i])
            color = colors[i]
            print(i, label, x, y, w, h)
            cv2.rectangle(img, (x, y), (x + w, y + h), color, 2)
            cv2.putText(img, label, (x, y - 10), font, 1, color, 2)
            rsp_flag = RSP_flag_function(rsp_flag, names[i])
            print("R_flag {}, S_flag {}, P_flag {}".format(rsp_flag[0], rsp_flag[1], rsp_flag[2]))

    if len(indexes) > 1:
        if sum(rsp_flag) != 2:
            color = np.random.uniform(0, 255, size=(3,))
            cv2.putText(img, tie_label, (int(width/2)-150, int(height/2)+50), font, 10, color, 7)

        else:
            # 묵vs찌
            if rsp_flag[0] == 1 and rsp_flag[1] == 1 and rsp_flag[2] == 0:
                win = 'Rock'
                lose = 'Scissors'
                RSP_output(win, lose, img, indexes, names, boxes, font, colors)
                
            # 묵vs빠
            elif rsp_flag[0] == 1 and rsp_flag[1] == 0 and rsp_flag[2] == 1:
                win = 'Paper'
                lose = 'Rock'
                RSP_output(win, lose, img, indexes, names, boxes, font, colors)
            
            # 찌vs빠
            elif rsp_flag[0] == 0 and rsp_flag[1] == 1 and rsp_flag[2] == 1:
                win = 'Scissors'
                lose = 'Paper'
                RSP_output(win, lose, img, indexes, names, boxes, font, colors)

    cv2.imshow(title_name, img)
    
capture = cv2.VideoCapture(0)
time.sleep(2.0)
if not capture.isOpened:
    print('### Error opening video ###')
    exit(0)
    
while True:
    ret, frame = capture.read()
    if frame is None:
        print('### No more frame ###')
        capture.release()
        break
    detectAndDisplay(frame)

    # 'q'를 누르면 카메라 종료
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

capture.release()
cv2.destroyAllWindows()
