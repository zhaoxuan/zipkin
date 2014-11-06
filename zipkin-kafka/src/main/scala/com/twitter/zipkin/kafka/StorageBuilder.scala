package com.twitter.zipkin.kafka

import com.twitter.zipkin.builder.Builder
import com.twitter.zipkin.storage.Storage
import com.twitter.zipkin.storage.kafka.KafkaStorage
import kafka.javaapi.producer.Producer
import kafka.producer.ProducerConfig
import java.util.Properties
import com.twitter.zipkin.{kafka => outKafka}

/**
 * Created by john on 11/3/14.
 */
case class StorageBuilder(
  host: String,
  port: Int,
  topic: String = "topic"
) extends Builder[Storage] { self =>

  def apply() = {
    val kafkaBroker = "%s:%d".format(host, port)

    val properties = new Properties

    properties.put("metadata.broker.list", kafkaBroker)
    properties.put("producer.type", "async")
    properties.put("serializer.class", "kafka.serializer.StringEncoder")
    properties.put("request.required.acks", "0")

    val producerConfig = new ProducerConfig(properties)
    val producerClient = new Producer[String, String](producerConfig)
    val kafkaService = new outKafka.KafkaService(producerClient, topic)

    new KafkaStorage {
      val service = kafkaService
    }
  }
}
