package io.getquill.generic

import io.getquill._
import scala.reflect.ClassTag
import scala.quoted._
import scala.deriving._
import scala.compiletime.{erasedValue, summonFrom}  

trait EncodingDsl {
  type PrepareRow
  type ResultRow
  //type Index = Int

  type EncoderMethod[T] = (Int, T, PrepareRow) => PrepareRow
  type DecoderMethod[T] = (Int, ResultRow) => T

  type Encoder[T]
  type Decoder[T]

  type BaseEncoder[T] = GenericEncoder[T, PrepareRow]
  type BaseDecoder[T] = GenericDecoder[ResultRow, T]
  type ColumnResolver = GenericColumnResolver[ResultRow]
  type RowTyper[T] = GenericRowTyper[ResultRow, T]

  // TODO Needed for mapped encoding? Need to change signature
  implicit def mappedEncoder[I, O](implicit mapped: MappedEncoding[I, O], encoder: Encoder[O]): Encoder[I]
  implicit def mappedDecoder[I, O](implicit mapped: MappedEncoding[I, O], decoder: Decoder[I]): Decoder[O]
  
  protected def mappedBaseEncoder[I, O](mapped: MappedEncoding[I, O], encoder: EncoderMethod[O]): EncoderMethod[I] =
    (index, value, row) => encoder(index, mapped.f(value), row)

  protected def mappedBaseDecoder[I, O](mapped: MappedEncoding[I, O], decoder: DecoderMethod[I]): DecoderMethod[O] =
    (index, row) => mapped.f(decoder(index, row))
}
