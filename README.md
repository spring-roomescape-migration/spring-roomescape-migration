

## **1. AWS 아키텍처 & CI/CD**
본 서비스는 AWS의 서비스 및 자원을 활용하여 구축 및 운영하고 있습니다. AWS CodePipeline을 사용하여 CI/CD 파이프라인을 구성하고, VPC 엔드포인트를 통해 프라이빗 서브넷의 EC2에 애플리케이션을 배포하고, 데이터베이스를 별도의 프라이빗 서브넷에 배치했습니다. 사용자와의 통신은 Route 53과 ALB 로드밸런서를 통해 관리합니다. 또한 보안을 위해 HTTPS를 구축했고 앞으로 사용자 트래픽을 고려한 Auto-Scaling 적용할 계획입니다.

<br>

![Untitled](https://github.com/user-attachments/assets/6edd2b1f-cbfd-40c8-8026-6997cdccb655)
  

<br>
<br>

## **2. Test Coverage**
  <img alt="image" src="https://github.com/user-attachments/assets/85090c15-2c63-439f-b0f2-53f81783ccbe" max-width= 100 />

