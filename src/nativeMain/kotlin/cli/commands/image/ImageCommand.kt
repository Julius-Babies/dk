@file:OptIn(ExperimentalTime::class)

package cli.commands.image

import cli.commands.image.pull.PullCommand
import cli.commands.image.remove.RemoveCommand
import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.core.subcommands
import es.jvbabi.docker.kt.api.image.ImageApi
import es.jvbabi.docker.kt.docker.DockerClient
import es.jvbabi.tui.table.buildTable
import es.jvbabi.tui.table.components.BorderStyle
import util.human_readable.formatFileSize
import util.human_readable.formatTimeAgo
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class ImageCommand : SuspendingCliktCommand("image") {
    override val invokeWithoutSubcommand: Boolean = true

    override suspend fun run() {
        val subcommand = currentContext.invokedSubcommand
        if (subcommand != null) return
        DockerClient().use { client ->
            val images = client.images.getImages()
            val table = buildTable {
                border = BorderStyle.Borderless
                cellPadding = 2
                row {
                    cell { +"REPOSITORY" }
                    cell { +"TAG" }
                    cell { +"IMAGE ID" }
                    cell { +"CREATED" }
                    cell { +"SIZE" }
                }

                if (images.isEmpty()) {
                    row {
                        cell {
                            centered = true
                            colspan = 5
                            +"No images found"
                        }
                    }
                } else images.forEach { image ->
                    row {
                        cell repositories@{
                            +image
                                .repoDigests
                                .map { digest -> digest.substringBefore("@sha256") }
                                .distinct()
                                .joinToString()
                        }
                        cell tags@{
                            +image
                                .repoTags
                                .map { image -> ImageApi.tagFromImage(image) }
                                .distinct()
                                .joinToString()
                        }
                        cell imageId@{ +image.id.substringAfter("sha256:").take(12) }
                        cell createdAt@{
                            +formatTimeAgo(
                                instant = Instant.fromEpochSeconds(image.created),
                                baseInstant = Clock.System.now()
                            )
                        }
                        cell size@{
                            +formatFileSize(image.size, 1)
                        }
                    }
                }
            }

            println(table)
        }
    }

    init {
        this.subcommands(PullCommand(), RemoveCommand())
    }
}