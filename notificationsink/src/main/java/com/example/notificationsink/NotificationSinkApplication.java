package com.example.notificationsink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
@EnableBinding(Sink.class)
public class NotificationSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationSinkApplication.class, args);
	}

		@StreamListener(target = Sink.INPUT)
		public void handler(UserNameChanged event) {
			System.out.println(event.getOccuredAt() + " NickName = " +  event.getNewNickName());
		}

//	@StreamListener(target = Sink.INPUT, condition = "headers['type']=='useractivated'")
//	public void handler(@Payload UserActivated event) {
//		System.out.println(event);
//	}
//
//	@StreamListener(target = Sink.INPUT, condition = "headers['type']=='userdeactivated'")
//	public void handler(@Payload UserDeactivated event) {
//		System.out.println(event);
//	}
}
