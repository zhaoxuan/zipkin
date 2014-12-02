package com.twitter.zipkin.kafka

import java.nio.charset.Charset

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
    val kafkaTopic = genTopic(span).getOrElse(topic)
    val keyMsg = new KeyedMessage[String, String](kafkaTopic, msg)

    Future {
      kafka.send(keyMsg)
    } onSuccess { (_) =>
      //println("send to kafka success")
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
      "product" -> getProduct(span.serviceName),
      "service" -> getModule(span).getOrElse("service"),
      "module" -> getModule(span).getOrElse("service"),
      "page_view" -> "1",
      "response_time" -> response_time.toString,
      "event_time" -> System.currentTimeMillis,
      "zipkin_time" -> (span.firstAnnotation.get.timestamp / 1000)
    )

    var binaryMap: Map[String, Any] = Map()

    span.binaryAnnotations.foreach( t => {
      val s = Charset.forName("UTF-8").newDecoder().decode(t.value)
      val key = t.key.toString
      val subfix = key.split('.').lastOption match {
        case Some(s) => s
        case None => "log"
        case _ => "log"
      }

      subfix match {
        case "log" => ""
        case _ => binaryMap += key -> s.toString
      }

    })

    jsonGen(binaryMap ++ mapData).toString()
  }

  def genTopic(span: Span): Option[String] = {
    val product = getProduct(span.serviceName)
    val service = "dtrace"
    Some("%s_%s_topic".format(product, service).toString)
  }

  def getProduct(serviceName: Option[String]): String = {
    serviceName.getOrElse("default").split(":", 2)(0)
  }

  def getModule(span: Span): Option[String] = {
    val service = span.serviceName.getOrElse("service").split(":", 2)
    val name = service.size match {
      case 2 => service(1)
      case _ => "service"
    }
    Some(name)
  }


}
