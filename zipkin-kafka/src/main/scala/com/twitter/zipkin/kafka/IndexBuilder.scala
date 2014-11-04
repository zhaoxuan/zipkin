package com.twitter.zipkin.kafka

import com.twitter.zipkin.builder.Builder
import com.twitter.zipkin.storage.Index
import com.twitter.zipkin.storage.kafka.KafkaIndex

/**
 * Created by john on 11/3/14.
 */
case class IndexBuilder() extends Builder[Index] { self =>
  def apply() = {
    new KafkaIndex {}
  }
}
