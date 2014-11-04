package com.twitter.zipkin.kafka

import com.twitter.zipkin.builder.Builder
import com.twitter.zipkin.storage.Storage
import com.twitter.zipkin.storage.kafka.KafkaStorage

/**
 * Created by john on 11/3/14.
 */
case class StorageBuilder(
  host: String,
  port: Int,
  topic: String
) extends Builder[Storage] { self =>

  def apply() = {
    new KafkaStorage {
      val host: String = host
      val port: Int = port
      val topic: String = topic
    }
  }
}
