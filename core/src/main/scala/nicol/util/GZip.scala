package nicol.util

object GZip {
  import java.io.ByteArrayOutputStream
  import java.util.zip.GZIPOutputStream

  def compress(bytes: Seq[Byte]) = {
    val out = new ByteArrayOutputStream()
    val gzip = new GZIPOutputStream(out)
    gzip.write(bytes.toArray)
    gzip.close()
    out.toByteArray
  }

  import java.io.{InputStreamReader, BufferedReader, ByteArrayInputStream}
  import java.util.zip.GZIPInputStream

  def decompress(bytes: Seq[Byte]) = {
    val r = new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(bytes.toArray))))

    Iterator.continually(r.readLine()).takeWhile(_ != null).mkString.getBytes.toSeq
  }
}
