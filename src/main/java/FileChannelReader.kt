import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Source -> Channel -> Buffer
 */
class FileChannelReader {

    fun read() {
        val path: Path = Paths.get("source.txt")

        val channel: FileChannel = FileChannel.open(path)
        val readBuffer: ByteBuffer = ByteBuffer.allocate(10) //allocate a buffer - 10 bytes long
        var bytesCount: Int = channel.read(readBuffer) //read bytes from the channel into a buffer
        while (bytesCount != -1) {
            println("BytesCount: $bytesCount, ReadBuffer: $readBuffer")
            // prepare for reading
            readBuffer.flip()
            println("ReadBuffer prepared for reading: $readBuffer")
            while (readBuffer.hasRemaining()) {
                println("Reading: ${readBuffer.get().toChar()}, ReadBuffer: $readBuffer")
            }
            readBuffer.clear()
            bytesCount = channel.read(readBuffer)
        }
        channel.close()
    }
}

fun main() {
    FileChannelReader().read()
}

