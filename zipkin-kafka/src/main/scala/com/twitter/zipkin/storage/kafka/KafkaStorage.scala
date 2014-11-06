package com.twitter.zipkin.storage.kafka

import com.twitter.finagle.Service
import com.twitter.zipkin.common.Span
import com.twitter.zipkin.storage.Storage
import com.twitter.util.Future
import com.twitter.util.Duration

/**
 * Created by john on 11/3/14.
 */

trait KafkaStorage extends Storage {
  val service: Service[Span, Unit]

  override def close() = {
    service.close()
  }

  override def storeSpan(span: Span): Future[Unit] = {
    service(span)
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
