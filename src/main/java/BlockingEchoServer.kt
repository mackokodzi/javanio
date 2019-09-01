import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class BlockingEchoServer {
    fun start() {
        println("Server started on port: $PORT")

        val serverSocket = ServerSocket(PORT)

        serverSocket.use { server ->
            while (true) {

                /**
                 * Listens for a client to make a connection and when it does, creates a new socket
                 */
                val clientSocket: Socket = server.accept()
                println("Threads count: ${Thread.activeCount()}")

                /**
                 * Handling each connection in a new thread
                 */
                Thread(ConnectionHandler(clientSocket)).start()
            }
        }
    }

    companion object {
        const val PORT = 10550
    }
}

class ConnectionHandler(
    private val clientSocket: Socket
) : Runnable {

    override fun run() {
        /**
         * Get input and output stream from the socket
         */
        val outputWriter = PrintWriter(clientSocket.getOutputStream(), true)
        val inputReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

        /**
         * Process request and echo it
         */
        for (request: String in inputReader.lines()) {
            println("Server input message: $request")
            outputWriter.println(request)
            if ("Done" == request) {
                break
            }
        }
        clientSocket.close()
    }
}

fun main() {
    BlockingEchoServer().start()
}