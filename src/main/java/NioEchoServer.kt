import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.WeakHashMap

class NioEchoServer {

    private val server: ServerSocketChannel = ServerSocketChannel.open().also {
        it.socket().bind(InetSocketAddress(PORT))
        /**
         * Configure serverSocket non-blocking so it can be used with the Selector
         */
        it.configureBlocking(false)
    }
    private val selector: Selector = Selector.open()
    private val messages: WeakHashMap<SocketChannel, String> = WeakHashMap()

    fun start() {
        println("Server starting on port $PORT")
        /**
         * Registers this channel (ServerSocketChannel) with the given selector, returning a selection
         * key.
         * Selection key OP_ACCEPT waits for socket-accept operations
         * Listening only for incoming connections, not for regular read/write data
         */
        server.register(selector, SelectionKey.OP_ACCEPT)

        /**
         * EVENT LOOP 🚀🚀🚀
         */
        while (true) {
            /**
             * Selector.select() -> WAIT FOR EVENTS
             * It returns only after at least one channel is selected,
             * this selector's {@link #wakeup wakeup} method is invoked, or the current
             * thread is interrupted, whichever comes first
             */
            println("Thread active count: ${Thread.activeCount()}")
            val keysCount: Int = selector.select()
            println("Selected nr of keys: $keysCount")
            handleAllActions()
        }
    }

    private fun handleAllActions() {
        val selectionKeysIterator = selector.selectedKeys().iterator()
        while (selectionKeysIterator.hasNext()) {
            val selectionKey: SelectionKey = selectionKeysIterator.next()
            selectionKeysIterator.remove()
            selectionKey.handleAction()
        }
    }

    /**
     * A SelectionKey 🔑 represents the registration of a single Channel with a single Selector.
     * A SelectionKey 🔑 is created each time a channel is registered with a selector.
     */
    private fun SelectionKey.handleAction() {
        when {
            isAcceptable -> {
                println("Key is acceptable: $this")
                acceptConnection()
            }
            isReadable   -> {
                println("Key is readable: $this")
                this.readData()
            }
            isWritable   -> {
                println("Key is writable: $this")
                this.writeData()
            }
        }
    }

    private fun acceptConnection() {
        val socketChannel: SocketChannel = server.accept()
        socketChannel.configureBlocking(false)
        // creating new OP_READ and OP_WRITE SelectionKeys due to registering socketChannel with the selector
        socketChannel.register(selector, SelectionKey.OP_READ + SelectionKey.OP_WRITE)

        println("Accepted connection from: ${socketChannel.socket().inetAddress}:${socketChannel.socket().port}")
    }

    /**
     * Reading data from channel into buffer
     */
    private fun SelectionKey.readData() {
        val channel: SocketChannel = this.channel() as SocketChannel
        channel.configureBlocking(false)

        val readBuffer : ByteBuffer = ByteBuffer.allocate(256)
        val state: Int = channel.read(readBuffer)

        if (isClosed(state)) {
            close(this, channel)
        } else {
            val stringOfByteArray = String(readBuffer.array())
            messages[channel] = stringOfByteArray
            println("${channel.getAddress()} -> Received: $stringOfByteArray")
            this.interestOps(SelectionKey.OP_WRITE)
        }
    }

    private fun isClosed(state: Int) = state == -1

    private fun close(key: SelectionKey, channel: SocketChannel) {
        key.cancel()
        channel.close()
        messages.remove(channel)
    }

    /**
     * Writing data back from buffer to channel
     */
    private fun SelectionKey.writeData() {
        val channel: SocketChannel = this.channel() as SocketChannel
        val messageToWrite: String = getMessage(channel)

        val writeBuffer : ByteBuffer = ByteBuffer.wrap(messageToWrite.toByteArray())

        channel.write(writeBuffer)
        println("${channel.getAddress()} <- Writing: $messageToWrite")

        this.interestOps(SelectionKey.OP_READ)
    }

    private fun getMessage(channel: SocketChannel): String = when {
        messages.containsKey(channel) -> messages[channel]!!
        else                          -> welcome
    }

    private fun SocketChannel.getAddress() = "${this.socket().inetAddress}:${this.socket().port}"

    companion object {
        const val welcome: String = "Connection successful!\n"
        const val PORT = 10220
    }

}

fun main() {
    NioEchoServer().start()
}