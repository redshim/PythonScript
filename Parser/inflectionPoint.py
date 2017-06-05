# -*- coding: utf-8 -*-
from datetime import datetime
import matplotlib
import matplotlib.patches as mpatches
matplotlib.use('Agg')
import matplotlib.pyplot as plt
matplotlib.use('Agg')
import pandas as pd
import csv
import warnings
import numpy
warnings.simplefilter(action = "ignore", category = RuntimeWarning)

index = 0
column = []         #header 정보
tempData = []
NTUData = []
NMOData = []
timeData = []
tata = []
distributionData = []
scopeData = []
winSize = 200
dn = 100
type = "ntu"
filePath = "/home/6546788/MDF/mdf2.txt"
print (">>> start: " + datetime.today().strftime("%Y-%m-%d %H:%M:%S"))

# 파일 가져오기
#with open("/home/6546788/MDF/test2.txt", 'r') as file:
with open(filePath, 'r') as file:
    for line in file:
        fields = line.strip().split(',')
        if index == 0:
            column.append(fields.index('NAB'))
            column.append(fields.index('HDK'))
            column.append(fields.index('mmm'))
            column.append(fields.index('RGA_CUR'))
            column.append(fields.index('NTU'))
            column.append(fields.index('NMO'))
            column.append(fields.index('vfz'))

	  #hearder 부분 제거
        if index >= 2:
            #인덱스 위치로 데이터가져오기
            for i in column:
                tempData.append(fields[i])
            tempData.insert(0, index-1)                     
            #ntu 정보
            NTUData.append(float(tempData[5]))
            #nmo 정보
            NMOData.append(float(tempData[6]))
	      #시간정보
            timeData.append(tempData[0])
            
            
        tempData = []
        index += 1

df = pd.DataFrame({'time':timeData, 'rpm': NTUData, 'nmo': NMOData})
timeDf = pd.stats.moments.rolling_mean(df['time'], winSize)
rpmDf = pd.stats.moments.rolling_mean(df['rpm'], winSize)
nmoDf = pd.stats.moments.rolling_mean(df['nmo'], winSize)

def point (pData, tData, nData, type):
    tempRpm1 = 0 
    tempRpm2 = 0
    slope = []
    avgArr = []
    returnPointData = [None]*2
    
    for i in range(len(pData)):
        if i > winSize:
	    if type == "ntu":
	        if((pData[i] - tempRpm1 < 0) and (tempRpm1 - tempRpm2 > 0)) or ((pData[i] - tempRpm1) > 0 and (tempRpm1 - tempRpm2 < 0 )):
                    slope.append({"time":tData[i-1], "rpm":pData[i-1], "nmo":nData[i-1] })
                tempRpm2 = tempRpm1
                tempRpm1 = pData[i]
	    else:
                if((nData[i] - tempRpm1 < 0) and (tempRpm1 - tempRpm2 > 0)) or ((nData[i] - tempRpm1) > 0 and (tempRpm1 - tempRpm2 < 0 )):
                    slope.append({"time":tData[i-1], "rpm":pData[i-1], "nmo":nData[i-1] })
                tempRpm2 = tempRpm1
                tempRpm1 = nData[i]
            avgArr.append([tData[i], pData[i], nData[i]])
    returnPointData[0] = slope
    returnPointData[1] = avgArr
    return returnPointData
    
    
def scope (sData, type):
    tempScopeData = []
    distributionData = []
    scatter = []    
    count = 0
    setRpm = 0
    bol = False
    returnScopeData = [None]*2
    
    for i,d in enumerate(sData[0]):
        for j, o in enumerate(sData[1]):
            if(o[0] > d['time'] - winSize and o[0] < d['time'] + (winSize - dn)):
                if(d['time'] - winSize > 0):
                    if(setRpm == 0):
                        setRpm = o[1] 
                    if type == "ntu":
                        if(float(d['rpm']) - float(o[1]) >= 0 and  count <= winSize):
                            tempScopeData.append([o[0], o[1], o[2]])
                        elif(float(o[1]) - float(d['rpm']) < 0 and (float(o[1]) - float(setRpm) < 0) and count > winSize and count <= (winSize + dn)):
                            tempScopeData.append([o[0], o[1], o[2]])
                        else:
                            bol = True
                        setRpm = o[1]
                    else:
                        if(float(d['nmo']) - float(o[2]) >= 0 and  count <= winSize):
                            tempScopeData.append([o[0], o[1], o[2]])
                        elif(float(o[2]) - float(d['nmo']) < 0 and (float(o[2]) - float(setRpm) < 0) and count > winSize and count <= (winSize + dn)):
                            tempScopeData.append([o[0], o[1], o[2]])
                        else:
                            bol = True
		  	setRpm = o[2] 
                    count += 1
                else:
                    bol = True
        if(bol == False):
            distributionData.append(tempScopeData)
            scatter.append([d['time'], d['rpm'], d['nmo']])
        bol = False
        tempScopeData = []
        count = 0
    returnScopeData[0] = distributionData
    returnScopeData[1] = scatter
    return returnScopeData

pointData = point(rpmDf, timeDf, nmoDf, type)
scopeData = scope(pointData, type)

#전체 기본 데이터 
fig = plt.figure(figsize=(10, 10), dpi=100)
red_patch = mpatches.Patch(color='red', label='NTU')
blue_patch = mpatches.Patch(color='blue', label='NMO')
plt.legend(handles=[red_patch, blue_patch])
plt.plot(timeData, NTUData, 'r-', timeData, NMOData, 'b-')
plt.xlabel('time')
plt.ylabel('rpm')
fig.savefig(type + '/defaultChartData.png')

#이동평균을 적용한 데이터
fig = plt.figure(figsize=(10, 10), dpi=100)
red_patch = mpatches.Patch(color='red', label='NTU')
blue_patch = mpatches.Patch(color='blue', label='NMO')
plt.legend(handles=[red_patch, blue_patch])
plt.plot(timeDf, rpmDf, 'r-', timeDf, nmoDf, 'b-')
plt.xlabel('time')
plt.ylabel('rpm')
fig.savefig(type + '/movingAverageData.png')


#그래프
for i in range(len(scopeData[0])):
    fig = plt.figure(figsize=(10, 10), dpi=100)
    rpmGraph = tuple(zip(*scopeData[0][i]))
    red_patch = mpatches.Patch(color='red', label='NTU')
    blue_patch = mpatches.Patch(color='blue', label='NMO')
    plt.legend(handles=[red_patch, blue_patch])

    #rpmGraph = zip(*te)
    plt.plot(rpmGraph[0], rpmGraph[1], 'r-', rpmGraph[0], rpmGraph[2], 'b-')
    plt.xlabel('time')
    plt.ylabel('rpm')
    plt.scatter(scopeData[1][i][0],scopeData[1][i][1], 50, color ='blue')        
    plt.scatter(scopeData[1][i][0],scopeData[1][i][2], 50, color ='red')        
    fig.savefig(type + "/" + type +'PointChart' + str(i+1) + '.png')

## ntu 데이터
with open(type + "/ntu.csv", "w") as csvfile:
    writer = csv.writer(csvfile , delimiter=',')
    for i in range(len(scopeData[0])):
        rpmGraph = tuple(zip(*scopeData[0][i]))
        writer.writerow(numpy.asarray(rpmGraph[1]))
## nmo 데이터
with open(type + "/nmo.csv", "w") as csvfile:
    writer = csv.writer(csvfile , delimiter=',')
    for i in range(len(scopeData[0])):
        rpmGraph = tuple(zip(*scopeData[0][i]))
        writer.writerow(numpy.asarray(rpmGraph[2]))


##테이블 데이터
csvData = []
csvTemp = []
for i, ds in enumerate(scopeData[0]):
    for j, ss in enumerate(ds):
        #기울기 및 스타트 시간 
        if j == 0:
            csvTemp.append(scopeData[1][i][1] - ss[1])
            csvTemp.append(scopeData[1][i][2] - ss[2])            
            csvTemp.append(int(ss[0]))
        if j == (len(ds) -1):
            csvTemp.append(int(ss[0]))
    #변곡점
    csvTemp.append(int(scopeData[1][i][0]))
    csvTemp.append(scopeData[1][i][1])
    csvTemp.append(scopeData[1][i][2])
    csvTemp.append(scopeData[1][i][1] - scopeData[1][i][2])
    csvData.append(csvTemp)
    csvTemp = []
    

with open(type + "/" + type + "Problem.csv", "w") as csvfile:
    fieldnames = ['number', 'ntu_slope', 'nmo_slope', 'time_start', 'time_end', 'point_time', 'ntu_point', 'nmo_point', 'ntu_nmo_point_gap']        
    writer = csv.DictWriter(csvfile, delimiter=',', fieldnames=fieldnames)
    writer.writerow(dict((fn,fn) for fn in fieldnames))
    for i, rg in enumerate(csvData):
        writer.writerow({'number': i+1, 'ntu_slope': rg[0], 'nmo_slope': rg[1],'time_start': rg[2], 'time_end': rg[3], 'point_time': rg[4], 'ntu_point': rg[5], 'nmo_point': rg[6], 'ntu_nmo_point_gap': rg[7]})


print (">>> end: " + datetime.today().strftime("%Y-%m-%d %H:%M:%S"))




