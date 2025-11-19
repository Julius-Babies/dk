package cli.commands

import com.github.ajalt.clikt.command.SuspendingCliktCommand

class App : SuspendingCliktCommand() {
    override fun aliases(): Map<String, List<String>> = mapOf(
        "i" to listOf("image"),
    )
    override suspend fun run() {

    }
}