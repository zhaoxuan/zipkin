package com.twitter.zipkin.storage.kafka

import java.util.Properties

import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import com.twitter.zipkin.common.Span
import com.twitter.zipkin.storage.Storage
import com.twitter.util.Future
import com.twitter.util.Duration
import kafka.producer.Producer
import kafka.producer.ProducerConfig
import com.twitter.zipkin.{kafka => bKafka}

/**
 * Created by john on 11/3/14.
 */
trait KafkaStorage extends Storage {
  val host: String
  val port: Int
  val topic: String
  var statsReceiver: StatsReceiver = NullStatsReceiver

  val zkConnectString = host + ":" + port.toString
  val properties = new Properties
  properties.put("zk.connect", zkConnectString)
  properties.put("producer.type", "sync")
  val producerConfig = new ProducerConfig(properties)

  val producer = new Producer[String, String](producerConfig)
  new bKafka.KafkaService(producer, topic)

  override def close() = {}

  override def storeSpan(span: Span): Future[Unit] = {
    println("output span into kafka")
    Future(Unit)
  }

  override def setTimeToLive(traceId: Long, ttl: Duration): Future[Unit] = {
    Future(Unit)
  }

  override def getTimeToLive(traceId: Long): Future[Duration] = {
    Future(Duration.Zero)
  }

  override def getSpansByTraceId(traceId: Long) : Future[Seq[Span]] = {
    Future(Seq.empty[Span])
  }

  override def getSpansByTraceIds(traceIds: Seq[Long]): Future[Seq[Seq[Span]]] = {
    Future(Seq(Seq.empty[Span]))
  }

  override def getDataTimeToLive: Int = {
    Int.MaxValue
  }

  override def tracesExist(traceIds: Seq[Long]): Future[Set[Long]] = {
    Future(Set.empty[Long])
  }



}
