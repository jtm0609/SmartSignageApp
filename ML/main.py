import cv2, glob, dlib, requests


age_list = ['(0, 2)','(4, 6)','(8, 12)','(15, 20)','(25, 32)','(38, 43)','(48, 53)','(60, 100)']
gender_list = ['Male', 'Female']

detector = dlib.get_frontal_face_detector()

age_net = cv2.dnn.readNetFromCaffe(
          'models/deploy_age.prototxt', 
          'models/age_net.caffemodel')
gender_net = cv2.dnn.readNetFromCaffe(
          'models/deploy_gender.prototxt', 
          'models/gender_net.caffemodel')

#비디오작업
capture = cv2.VideoCapture(0)
capture.set(cv2.CAP_PROP_FRAME_WIDTH, 1000)
capture.set(cv2.CAP_PROP_FRAME_HEIGHT, 1000)

#사용자의 성별연령 실시간으로 check

while True:
  ret, frame = capture.read()
  faces=detector(frame)
  for face in faces:
    x1,y1,x2,y2=face.left(), face.top(), face.right(), face.bottom()
    #cv2.rectangle(frame,(x1,y1),(x2,y2),(0,0,255),thickness=2)
    face_img = frame[y1:y2, x1:x2].copy()

    blob = cv2.dnn.blobFromImage(face_img, scalefactor=1, size=(227, 227),
      mean=(78.4263377603, 87.7689143744, 114.895847746),
      swapRB=False, crop=False)

    # predict gender
    gender_net.setInput(blob)
    gender_preds = gender_net.forward()
    gender = gender_list[gender_preds[0].argmax()]

    # predict age
    age_net.setInput(blob)
    age_preds = age_net.forward()
    age = age_list[age_preds[0].argmax()]

    # visualize
    cv2.rectangle(frame, (x1, y1), (x2, y2), (255,255,255), 2)
    overlay_text = '%s %s' % (gender, age)
    cv2.putText(frame, overlay_text, org=(x1, y1), fontFace=cv2.FONT_HERSHEY_SIMPLEX,
      fontScale=1, color=(0,0,0), thickness=10)
    cv2.putText(frame, overlay_text, org=(x1, y1),
      fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(255,255,255), thickness=2)


  cv2.imshow("VideoFrame", frame)
  #사용자가 버튼누르면
  if cv2.waitKey(1) > 0: 
    resize_img=cv2.resize(frame,(1000,1000))
    #캡처됨
    cv2.imwrite("img/"+"capture.jpg",resize_img)
    print(age)
    print(gender)

    ##서버측에서 요구한 사항
    if gender=='Male'  :
        #0~6세 남자
      if age_list.index(age)==0 and age_list.index(age)==1:
        data=0

        #8~12세 남자
      elif age_list.index(age)==2:
        data=1
        
        #15~20세 남자
      elif age_list.index(age)==3:
        data=2

        #25~32세 남자
      elif age_list.index(age)==4:
        data=3

        #38~43세 남자
      elif age_list.index(age)==5:
        data=4

        #48~53세 남자
      elif age_list.index(age)==6:
        data=5

        #60~100세 남자
      elif age_list.index(age)==7:
        data=6

    elif gender=='Female' :
          #0~6세 여자
      if age_list.index(age)==0 and age_list.index(age)==1:
        data=7

        #8~12세 여자
      elif age_list.index(age)==2:
        data=8
        
        #15~20세 여자
      elif age_list.index(age)==3:
        data=9

        #25~32세 여자
      elif age_list.index(age)==4:
        data=10

        #38~43세 여자
      elif age_list.index(age)==5:
        data=11

        #48~53세 여자
      elif age_list.index(age)==6:
        data=12

        #60~100세 여자
      elif age_list.index(age)==7:
        data=13

    
    print(data)

    #성별연령 데이터를 서버로보냄
    r=requests.post('http://localhost:4050/index',{'data':daa}).text
    print(r)
    break

#일시멈춤-> esc누르면 종료
k=cv2.waitKey(0)
if k==27:
  cv2.destroyAllWindows()      

#capture.release()

    
