package com.twitter.zipkin.kafka

import com.twitter.finagle.Service
import com.twitter.util.Future
import com.twitter.zipkin.common.Span
import kafka.producer.KeyedMessage
import kafka.javaapi.producer.Producer


/**
 * Created by john on 11/4/14.
 */

class KafkaService(
                    kafka: Producer[String, String],
                    topic: String
                    ) extends Service[Span, Unit] {

  def apply(data: Span): Future[Unit] = {
    val msg = spanFormat(data)
    val keyMsg = new KeyedMessage[String, String](topic, msg)

    Future {
      kafka.send(keyMsg)
    } onSuccess { (_) =>
      println("send to kafka success")
    }

  }

  def spanFormat(span: Span): String = {
    val data: String = "test data"
    data
  }
}
