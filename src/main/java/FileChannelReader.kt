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

        /**
         * read bytes from the channel into a buffer
         */
        var nrOfBytesRead: Int = channel.read(readBuffer)

        while (nrOfBytesRead != -1) {
            println("Nr of bytes read: $nrOfBytesRead, ReadBuffer: $readBuffer")
            // prepare for reading
            readBuffer.flip()
            println("ReadBuffer prepared for reading: $readBuffer")
            while (readBuffer.hasRemaining()) {
                println("Reading: ${readBuffer.get().toChar()}, ReadBuffer: $readBuffer")
            }
            readBuffer.clear()
            nrOfBytesRead = channel.read(readBuffer)
        }
        channel.close()
    }
}

fun main() {
    FileChannelReader().read()
}

