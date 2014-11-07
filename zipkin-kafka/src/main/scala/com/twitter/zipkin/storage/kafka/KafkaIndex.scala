package com.twitter.zipkin.storage.kafka

import java.nio.ByteBuffer
import com.twitter.util.Future
import com.twitter.zipkin.common.Span
import com.twitter.zipkin.storage.{Index, IndexedTraceId, TraceIdDuration}

import scala.collection.Set

/**
 * Created by john on 11/4/14.
 */

trait KafkaIndex extends Index {
  override def close() = {}

  override def getTraceIdsByName(serviceName: String, spanName: Option[String],
                                 endTs: Long, limit: Int, startTs: Long = 0): Future[Seq[IndexedTraceId]] = {
    Future(Seq.empty[IndexedTraceId])
  }

  override def getTraceIdsByAnnotation(serviceName: String, annotation: String, value: Option[ByteBuffer],
                                        endTs: Long, limit: Int, startTs: Long = 0): Future[Seq[IndexedTraceId]] = {
    Future(Seq.empty[IndexedTraceId])
  }

  override def getTracesDuration(traceIds: Seq[Long]): Future[Seq[TraceIdDuration]] = Future(Seq.empty[TraceIdDuration])

  override def getServiceNames: Future[Set[String]] = Future(Set.empty[String])

  override def getSpanNames(service: String): Future[Set[String]] = Future(Set.empty[String])

  override def indexTraceIdByServiceAndName(span: Span) : Future[Unit] = Future.Unit

  override def indexSpanByAnnotations(span: Span) : Future[Unit] = Future.Unit

  override def indexServiceName(span: Span) : Future[Unit] = Future.Unit

  override def indexSpanNameByService(span: Span) : Future[Unit] = Future.Unit

  override def indexSpanDuration(span: Span): Future[Unit] = Future.Unit
}
