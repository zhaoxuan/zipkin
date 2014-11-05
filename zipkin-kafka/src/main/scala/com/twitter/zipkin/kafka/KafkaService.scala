package com.twitter.zipkin.kafka

import com.twitter.finagle.Service
import com.twitter.util.{Future, Time}
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
      println("sended to kafka success")
    }

  }

  override def close(deadline: Time = Time.now): Future[scala.Unit] = {
    Future {
      kafka.close
    } onSuccess { (_) =>
      println("close kafka service success")
    }
  }

  def spanFormat(span: Span): String = {
//    TODO:john
//    format span to json for kafka
    val data: String = "test data"
    data
  }
}
