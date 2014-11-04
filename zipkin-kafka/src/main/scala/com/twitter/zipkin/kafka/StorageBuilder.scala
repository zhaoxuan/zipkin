package com.twitter.zipkin.kafka

import java.util.Properties

import com.twitter.zipkin.builder.Builder
import com.twitter.zipkin.storage.Storage
import com.twitter.zipkin.storage.kafka.KafkaStorage
import kafka.producer.{Producer, ProducerConfig}

/**
 * Created by john on 11/3/14.
 */
case class StorageBuilder(
  host: String,
  port: Int,
  topic: String
) extends Builder[Storage] { self =>

  def apply() = {

    val zkConnectString = host + ":" + port.toString
    val properties = new Properties
    properties.put("zk.connect", zkConnectString)
    properties.put("producer.type", "sync")
    val producerConfig = new ProducerConfig(properties)

    val producerClient = new Producer[String, String](producerConfig)
    //  new bKafka.KafkaService(producer, topic)
    new KafkaStorage {
      val producer = producerClient
    }
  }
}
