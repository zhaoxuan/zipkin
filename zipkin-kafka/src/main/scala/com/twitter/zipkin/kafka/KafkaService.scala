package com.twitter.zipkin.kafka

import com.twitter.finagle.Service
import com.twitter.util.Future
import com.twitter.zipkin.common.Span
import kafka.message.Message
import kafka.producer.{ProducerData, Producer}

/**
 * Created by john on 11/4/14.
 */

class KafkaService(
                    kafka: Producer[String, String],
                    topic: String
                    ) extends Service[Span, Unit] {

  def apply(data: Span): Future[Unit] = {
    val producerData = new ProducerData[String, String](topic, Seq("123"))

//    Future {
//      kafka.send(m)
//    } onSuccess { (_) =>
//      println("send to kafka success")
//    }
    Future.Unit
  }
}
