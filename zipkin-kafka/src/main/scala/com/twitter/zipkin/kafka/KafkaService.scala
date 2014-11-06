package com.twitter.zipkin.kafka

import com.twitter.finagle.Service
import com.twitter.util.{Future, Time}
import com.twitter.zipkin.common.Span
import kafka.producer.KeyedMessage
import kafka.javaapi.producer.Producer
import scala.util.parsing.json


/**
 * Created by john on 11/4/14.
 */

class KafkaService(
                    kafka: Producer[String, String],
                    topic: String
                    ) extends Service[Span, Unit] {

  def apply(span: Span): Future[Unit] = {
    val msg = spanFormat(span)
    val astreamTopic = genTopic(span).getOrElse(topic)
    val keyMsg = new KeyedMessage[String, String](astreamTopic, msg)

    Future {
      kafka.send(keyMsg)
    } onSuccess { (_) =>
      //println("sended to kafka success")
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
    val jsonGen = json.JSONObject
//    change millisecond to microsecond
    val response_time = (span.duration.getOrElse(0.toLong) / 1000)

    val mapData = Map(
      "page_view" -> "1",
      "response_time" -> response_time.toString,
      "event_time" -> System.currentTimeMillis.toLong,
      "zipkin_time" -> (span.firstAnnotation.get.timestamp / 1000).toLong
    )

    jsonGen(mapData).toString()
  }

  def genTopic(span: Span): Option[String] = {
    val product = span.serviceName.getOrElse("topic_default").toString.split(":")(0)
    val service = "zipkin"
    Some("%s_%s_topic".format(product, service).toString)
  }


}
