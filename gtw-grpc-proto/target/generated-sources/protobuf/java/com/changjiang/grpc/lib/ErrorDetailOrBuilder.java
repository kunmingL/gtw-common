// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: gtw_service.proto

package com.changjiang.grpc.lib;

public interface ErrorDetailOrBuilder extends
    // @@protoc_insertion_point(interface_extends:gtw.ErrorDetail)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * 错误码
   * </pre>
   *
   * <code>string error_code = 1;</code>
   * @return The errorCode.
   */
  java.lang.String getErrorCode();
  /**
   * <pre>
   * 错误码
   * </pre>
   *
   * <code>string error_code = 1;</code>
   * @return The bytes for errorCode.
   */
  com.google.protobuf.ByteString
      getErrorCodeBytes();

  /**
   * <pre>
   * 错误消息
   * </pre>
   *
   * <code>string error_message = 2;</code>
   * @return The errorMessage.
   */
  java.lang.String getErrorMessage();
  /**
   * <pre>
   * 错误消息
   * </pre>
   *
   * <code>string error_message = 2;</code>
   * @return The bytes for errorMessage.
   */
  com.google.protobuf.ByteString
      getErrorMessageBytes();

  /**
   * <pre>
   * 错误堆栈（可选）
   * </pre>
   *
   * <code>string stack_trace = 3;</code>
   * @return The stackTrace.
   */
  java.lang.String getStackTrace();
  /**
   * <pre>
   * 错误堆栈（可选）
   * </pre>
   *
   * <code>string stack_trace = 3;</code>
   * @return The bytes for stackTrace.
   */
  com.google.protobuf.ByteString
      getStackTraceBytes();

  /**
   * <pre>
   * 额外错误信息
   * </pre>
   *
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */
  int getMetadataCount();
  /**
   * <pre>
   * 额外错误信息
   * </pre>
   *
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */
  boolean containsMetadata(
      java.lang.String key);
  /**
   * Use {@link #getMetadataMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String>
  getMetadata();
  /**
   * <pre>
   * 额外错误信息
   * </pre>
   *
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */
  java.util.Map<java.lang.String, java.lang.String>
  getMetadataMap();
  /**
   * <pre>
   * 额外错误信息
   * </pre>
   *
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */
  /* nullable */
java.lang.String getMetadataOrDefault(
      java.lang.String key,
      /* nullable */
java.lang.String defaultValue);
  /**
   * <pre>
   * 额外错误信息
   * </pre>
   *
   * <code>map&lt;string, string&gt; metadata = 4;</code>
   */
  java.lang.String getMetadataOrThrow(
      java.lang.String key);
}
