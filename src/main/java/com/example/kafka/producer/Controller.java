package com.example.kafka.producer;

import com.example.kafka.producer.common.Foo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.kafka.core.KafkaTemplate;


@RestController
public class Controller {
  
  @Autowired
  private KafkaTemplate<Object, Object> template;

  @GetMapping(path = "/send/foo/{what}")
	public void sendFoo(@PathVariable String what) {
		this.template.send("topic1", new Foo1(what));
	}
}
