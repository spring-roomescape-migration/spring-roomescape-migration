#!/bin/bash
NEW_JAR_FILE=$(ls /home/ec2-user/build/src*.jar)
JAR_FILE_NAME=$(basename $NEW_JAR_FILE)
DEFAULT_CHANGE_JAR_FILE_NAME='spring-roomescape-migration-0.0.1-SNAPSHOT.jar'
DEPLOY_PATH=/home/ec2-user/build/

echo "> 새로운 build 파일명 확인 ==> $JAR_FILE_NAME" >> $DEPLOY_PATH/deploy.log

echo "> 기존 build 파일 삭제"
rm -f $DEFAULT_CHANGE_JAR_FILE_NAME

mv "$DEPLOY_PATH$JAR_FILE_NAME" "$DEPLOY_PATH$DEFAULT_CHANGE_JAR_FILE_NAME"
NEW_JAR_FILE=$(ls /home/ec2-user/build/*.jar)
NEW_JAR_FILE_NAME=$(basename $NEW_JAR_FILE)
echo "> build 파일명 수정 ==> $NEW_JAR_FILE_NAME" >> $DEPLOY_PATH/deploy.log

CURRENT_PID=$(pgrep -f spring)

echo "> 현재 실행 중인 애플리케이션 pid 확인  ==> $CURRENT_PID" >> $DEPLOY_PATH/deploy.log

if [ -z $CURRENT_PID ]
then
  echo "> 현재 실행 중인 애플리케이션이 없으므로 종료하지 않습니다." >> $DEPLOY_PATH/deploy.log
else
  echo "> 현재 실행 중인 애플리케이션을 종료합니다." >> $DEPLOY_PATH/deploy.log
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$NEW_JAR_FILE_NAME
echo "> 배포 작업을 실시합니다." >> $DEPLOY_PATH/deploy.log
nohup java -jar $DEPLOY_JAR >> $DEPLOY_PATH/deploy.log 2>$DEPLOY_PATH/deploy_err.log &

CURRENT_PID=$(pgrep -f $NEW_JAR_FILE_NAME)
echo "> 현재 실행 중인 애플리케이션의 포트는 다음과 같습니다. ==> $CURRENT_PID" >> $DEPLOY_PATH/deploy.log
