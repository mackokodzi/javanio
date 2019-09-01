import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

/**
 * Buffer -> Channel -> Source
 */
class FileChannelWriter {
    fun write() {
        val input = "Some very important data"
        print("Input string: $input")

        val inputBytes: ByteArray = input.toByteArray()
        val writeBuffer: ByteBuffer = ByteBuffer.wrap(inputBytes)

        val fos = FileOutputStream("writesource.txt")
        val fileChannel: FileChannel = fos.channel

        /**
         * writes a sequence of bytes to this channel from the given buffer.
         */
        fileChannel.write(writeBuffer)
        fileChannel.close()
        fos.close()
    }
}

fun main() {
    FileChannelWriter().write()
}