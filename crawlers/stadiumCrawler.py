import json
import requests
import time

def postStadium(data,i):
    url = 'http://13.209.73.252:8080/stadium'  # API 엔드포인트 URL

    # POST 요청 보내기
    response = requests.post(url, data=json.dumps(data), headers={'Content-Type': 'application/json'})

    # 응답 확인
    if response.status_code == 200:  # 성공적인 응답을 받았을 때
        result = response.json()  # JSON 형태로 응답 데이터 가져오기
    else:
        print("API 요청 실패:", response.text)
        print(i)

def getStadiumDataFromPublicApi():
    url = 'http://openapi.seoul.go.kr:8088/4b51536b76726b6438387544446862/json/ListPublicReservationSport/1/500/'

    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    else:
        return {}

if __name__ == "__main__":
    data = getStadiumDataFromPublicApi()
    dataLength = data['ListPublicReservationSport']['list_total_count']
    resultData = data['ListPublicReservationSport']['row']

    # 읽어온 데이터 삽입
    for i in range(dataLength):
        body = dict()
        body["name"] = resultData[i]["SVCNM"]
        body["location"] = resultData[i]["AREANM"]
        body["contactPhone"] = resultData[i]["TELNO"].replace("-","")
        body["address"] = resultData[i]["PLACENM"]
        body["comment"] = resultData[i]["DTLCONT"][:255]
        body["price"] = 0
        body["image"] = resultData[i]["IMGURL"]
        body["externalUrl"] = resultData[i]["SVCURL"]
        postStadium(body,i)
        time.sleep(2)
