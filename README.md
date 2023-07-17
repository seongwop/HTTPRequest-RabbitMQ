# HTTPRequest-RabbitMQ

RabbitMQ 메세지 브로커를 이용한 HTTP 요청 동시성 제어 테스트

최대 인원이 N명인 방에 다수의 유저가 동시 접근 시 N개 이상의 HTTP 요청이 수락되는 동시성 문제 발생

- 해결 방안
1. Producer가 HTTP 요청을 Queue에 적재
2. Consumer가 Queue로부터 하나씩 요청을 처리 후 SSE를 통해 Client에게 결과 전송
3. 최대 인원 초과 시 발생하는 예외의 경우 Exception Handler로 잡아서 SSE를 통해 Client에게 예외 메세지 전송

테스트 : Jmeter를 이용한 1초동안 500개의 HTTP 요청 송신

- 일반 요청 처리
  <br>![http](https://github.com/seongwop/HTTPRequest-RabbitMQ/assets/93995037/ee454903-c205-430d-a559-e1404d848b3d)<br>
-> 10개 이상의 요청 성공 (동시성 제어 실패)

- Queue에 의한 처리
  <br>![queue (1)](https://github.com/seongwop/HTTPRequest-RabbitMQ/assets/93995037/a6ebcb26-bee0-4581-bc0e-9457884ffd37)<br>
-> 10개 초과 시 예외 메세지 전송 (동시성 제어 성공)
