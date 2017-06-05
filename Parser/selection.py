# -*- coding: utf-8 -*-
from datetime import datetime
import matplotlib.pyplot as plt
import pandas as pd
import csv
import warnings
import numpy
warnings.simplefilter(action = "ignore", category = RuntimeWarning)

index = 0
column = []         #header 정보
tempData = []
ACC_ACT_Data = []
ACC_EQUIP_Data = []
ACC_ObjDist_Data = []
ACC_ObjLatPos_Data = []
ACC_ObjRelSpd_Data = []
ACC_ObjStatus_Data = []
ACC_REQ_Data = []
ACCFailInfo_Data = []
ACCMode_Data = []
AliveCounterACC_Data = []
CF_Gway_PassiveAccessLock_Data = []
LAT_ACCEL_Data = []
LAT_ACCEL_DIAG_Data = []
LAT_ACCEL_STAT_Data = []
LONG_ACCEL_Data = []
LONG_ACCEL_DIAG_Data = []
LONG_ACCEL_STAT_Data = []
MainMode_ACC_Data = []
QECACC_Data = []
TQI_ACC_Data = []
VS_Data = []

timeData = []
tata = []
distributionData = []
scopeData = []
winSize = 200
dn = 100
type = "ntu"
filePath = "/home/6546788/MDF/ImgAna/uncaved.dat.csv"
print (">>> start: " + datetime.today().strftime("%Y-%m-%d %H:%M:%S"))

# 파일 가져오기
#with open("/home/6546788/MDF/test2.txt", 'r') as file:
with open(filePath, 'r') as file:
    for line in file:
        fields = line.strip().split(',')
        if index == 0:
		column.append(fields.index('ACC_ACT'))
		column.append(fields.index('ACC_EQUIP'))
		column.append(fields.index('ACC_ObjDist'))
		column.append(fields.index('ACC_ObjLatPos'))
		column.append(fields.index('ACC_ObjRelSpd'))
		column.append(fields.index('ACC_ObjStatus'))
		column.append(fields.index('ACC_REQ'))
		column.append(fields.index('ACCFailInfo'))
		column.append(fields.index('ACCMode'))
		column.append(fields.index('AliveCounterACC'))
		column.append(fields.index('CF_Gway_PassiveAccessLock'))
		column.append(fields.index('LAT_ACCEL'))
		column.append(fields.index('LAT_ACCEL_DIAG'))
		column.append(fields.index('LAT_ACCEL_STAT'))
		column.append(fields.index('LONG_ACCEL'))
		column.append(fields.index('LONG_ACCEL_DIAG'))
		column.append(fields.index('LONG_ACCEL_STAT'))
		column.append(fields.index('MainMode_ACC'))
		column.append(fields.index('QECACC'))
		column.append(fields.index('TQI_ACC'))
		column.append(fields.index('VS'))

	  #hearder 부분 제거
        if index >= 2:
            #인덱스 위치로 데이터가져오기
            for i in column:
                tempData.append(fields[i])
            tempData.insert(0, index-1)                     
            ACC_ACT_Data.append(float(tempData[1]))
            ACC_EQUIP_Data.append(float(tempData[2]))
            ACC_ObjDist_Data.append(float(tempData[3]))
            ACC_ObjLatPos_Data.append(float(tempData[4]))
            ACC_ObjRelSpd_Data.append(float(tempData[5]))
            ACC_ObjStatus_Data.append(float(tempData[6]))
            ACC_REQ_Data.append(float(tempData[7]))
            ACCFailInfo_Data.append(float(tempData[8]))
            ACCMode_Data.append(float(tempData[9]))
            AliveCounterACC_Data.append(float(tempData[10]))
            CF_Gway_PassiveAccessLock_Data.append(float(tempData[11]))
            LAT_ACCEL_Data.append(float(tempData[12]))
            LAT_ACCEL_DIAG_Data.append(float(tempData[13]))
            LAT_ACCEL_STAT_Data.append(float(tempData[14]))
            LONG_ACCEL_Data.append(float(tempData[15]))
            LONG_ACCEL_DIAG_Data.append(float(tempData[16]))
            LONG_ACCEL_STAT_Data.append(float(tempData[17]))
            MainMode_ACC_Data.append(float(tempData[18]))
            QECACC_Data.append(float(tempData[19]))
            TQI_ACC_Data.append(float(tempData[20]))
            VS_Data.append(float(tempData[21]))
	     #시간정보
            timeData.append(tempData[0])
            
            
        tempData = []
        index += 1

df = pd.DataFrame({'time':timeData, 'ACC_ACT' : ACC_ACT_Data, 'ACC_EQUIP' : ACC_EQUIP_Data, 'ACC_ObjDist' : ACC_ObjDist_Data, \
                    'ACC_ObjLatPos' : ACC_ObjLatPos_Data, 'ACC_ObjRelSpd' : ACC_ObjRelSpd_Data, 'ACC_ObjStatus' : ACC_ObjStatus_Data, \
                    'ACC_REQ' : ACC_REQ_Data, 'ACCFailInfo' : ACCFailInfo_Data, 'ACCMode' : ACCMode_Data, 'AliveCounterACC' : AliveCounterACC_Data, \
                    'CF_Gway_PassiveAccessLock' : CF_Gway_PassiveAccessLock_Data, 'LAT_ACCEL' : LAT_ACCEL_Data, \
                    'LAT_ACCEL_DIAG' : LAT_ACCEL_DIAG_Data, 'LAT_ACCEL_STAT' : LAT_ACCEL_STAT_Data, 'LONG_ACCEL' : LONG_ACCEL_Data,\
                    'LONG_ACCEL_DIAG' : LONG_ACCEL_DIAG_Data, 'LONG_ACCEL_STAT' : LONG_ACCEL_STAT_Data, 'MainMode_ACC' : MainMode_ACC_Data, \
                    'QECACC' : QECACC_Data, 'TQI_ACC' : TQI_ACC_Data, 'VS' : VS_Data})

df.to_csv('/home/6546788/MDF/ImgAna/uncaved_export.csv', sep=',', encoding='utf-8')
