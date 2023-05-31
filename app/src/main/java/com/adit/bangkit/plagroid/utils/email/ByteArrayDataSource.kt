package com.adit.bangkit.plagroid.utils.email

import java.io.*
import java.sql.Connection
import java.util.logging.Logger
import javax.activation.DataContentHandler
import javax.sql.DataSource

class ByteArrayDataSource : DataSource  {
  private var data: ByteArray
  private var type: String? = null

  constructor(data: ByteArray, type: String?) : super() {
    this.data = data
    this.type = type
  }

  constructor(data: ByteArray) : super() {
    this.data = data
  }

  fun setType(type: String?) {
    this.type = type
  }

  val contentType: String
    get() = type ?: "application/octet-stream"

  @get:Throws(IOException::class)
  val inputStream: InputStream
    get() = ByteArrayInputStream(data)
  val name: String
    get() = "ByteArrayDataSource"

  @get:Throws(IOException::class)
  val outputStream: OutputStream
    get() {
      throw IOException("Not Supported")
    }

  override fun getLogWriter(): PrintWriter {
    TODO("Not yet implemented")
  }

  override fun setLogWriter(p0: PrintWriter?) {
    TODO("Not yet implemented")
  }

  override fun setLoginTimeout(p0: Int) {
    TODO("Not yet implemented")
  }

  override fun getLoginTimeout(): Int {
    TODO("Not yet implemented")
  }

  override fun getParentLogger(): Logger {
    TODO("Not yet implemented")
  }

  override fun <T : Any?> unwrap(p0: Class<T>?): T {
    TODO("Not yet implemented")
  }

  override fun isWrapperFor(p0: Class<*>?): Boolean {
    TODO("Not yet implemented")
  }

  override fun getConnection(): Connection {
    TODO("Not yet implemented")
  }

  override fun getConnection(p0: String?, p1: String?): Connection {
    TODO("Not yet implemented")
  }

  fun createDataContentHandler(mimeType: String?): DataContentHandler {
    TODO("Not yet implemented")
  }
}