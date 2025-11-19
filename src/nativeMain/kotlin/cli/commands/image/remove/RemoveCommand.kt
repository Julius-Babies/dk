package cli.commands.image.remove

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import es.jvbabi.docker.kt.docker.DockerClient

class RemoveCommand : SuspendingCliktCommand(name = "remove") {

    val imageToRemove by argument(
        name = "image",
        help = "Image to remove, e.g., 'alpine:latest', 'docker.io/library/ubuntu:22.04', hash, ...",
    )

    override suspend fun run() {
        DockerClient().use { client ->
            val response = client.images.removeImage(imageToRemove)
            response.forEach {
                println("${it.type.name} ${it.id}")
            }
        }
    }
}