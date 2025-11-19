import cli.commands.App
import cli.commands.image.ImageCommand
import cli.commands.image.pull.PullCommand
import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.subcommands
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        App()
            .subcommands(ImageCommand())
            .subcommands(PullCommand())
            .main(args)
    }
}