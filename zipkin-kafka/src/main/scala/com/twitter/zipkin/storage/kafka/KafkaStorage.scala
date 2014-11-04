package com.twitter.zipkin.storage.kafka

import com.twitter.zipkin.common.Span
import com.twitter.zipkin.storage.Storage
import com.twitter.util.Future
import com.twitter.util.Duration
import kafka.producer.Producer

/**
 * Created by john on 11/3/14.
 */

trait KafkaStorage extends Storage {
  val producer: Producer[String, String]

  override def close() = {}

  override def storeSpan(span: Span): Future[Unit] = {
    println(producer)
    println("output span into kafka")
//    TODO:john
//    send span to kafka
    Future.Unit
  }

  override def setTimeToLive(traceId: Long, ttl: Duration): Future[Unit] = {
    Future.Unit
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
