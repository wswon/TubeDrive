![image/Untitled.png](image/Untitled.png)

# TubeDriver

튜브는 앰뷸런스 운전사 입니다. 

응급환자가 앰뷸런스에 탔을 때 가장 가까운 병원, 약국의 위치를 확인하고 상세정보를 빠르게 확인하는 애플리케이션 입니다.

![image/Untitled%201.png](image/Untitled%201.png)

## Architecture

---

![image/Untitled%202.png](image/Untitled%202.png)

MVVM + Clean Architecture 구조를 사용하였습니다.

DataLayer는 Repository Pattern을 사용했습니다.

앱을 보다 테스트 가능하게 만들고 여러 지점에서 개체 인스턴스화를 하지 않아도 되도록 하기 위해 Hilt 라이브러리를 사용해 처리했습니다.

## Presentation Layer

![image/Untitled%203.png](image/Untitled%203.png)

### MapViewModel

- 데이터를 가져오고 가공합니다.
- 페이징 처리를 합니다.
- 선택된 카테고리 등 상태를 관리합니다.

### MapActivity

- MapViewModel의 event들을 observe하여 MapView를 변경합니다.
- MapView 기능에 대한 직접 참조를 하지 않고 MapMarkerManager를 통해서 하도록 구성했습니다.

### MapMarkerManager

- 지도위치이동, 마커 등 MapView에 대한 처리를 담당하는 클래스 입니다.

### MapEventProvider

- 카카오맵 sdk의 기본 interface MapView.MapViewEventListener, MapView.CurrentLocationEventListener, MapView.POIItemEventListener 3가지 리스너에서 필요한 callback만 사용하기 위한 wrapping 클래스 입니다.
