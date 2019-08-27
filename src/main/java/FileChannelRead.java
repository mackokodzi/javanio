import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Source -> Channel -> Buffer
 */
public class FileChannelRead {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("source.txt");
        FileChannel channel = FileChannel.open(path); //getting a channel
        ByteBuffer buf = ByteBuffer.allocate(10); //allocate a buffer - 10 bytes long
        int nrBytes = channel.read(buf); //read bytes from the channel into a buffer
        while (nrBytes != -1) {
            System.out.println("Read " + nrBytes);
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.println((char) buf.get());
            }
            buf.clear();
            nrBytes = channel.read(buf);
        }
        channel.close();
    }
}
