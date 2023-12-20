import org.apache.tools.ant.taskdefs.condition.Os

plugins {
   `cpp-application`
}

val port: String = "4949"

// task to run cpp client
tasks.register("run", Exec::class) {
    group = "application"                // set task group
    standardInput = System.`in`    // enable commandline input
    val exeDir : String = "${buildDir}/exe/main/debug/"
    val exeFile: String

    when {
        Os.isFamily(Os.FAMILY_WINDOWS) -> {
            exeFile = "Ipv6_SocketClient.exe"
			commandLine("cmd", "/k", exeDir+exeFile, port)
        }
        Os.isFamily(Os.FAMILY_MAC) -> {
            exeFile = "Ipv6_SocketClient"
			commandLine("bash", "-c", exeDir+exeFile, port)
        }
        Os.isFamily(Os.FAMILY_UNIX) -> {
            exeFile = "Ipv6_SocketClient"
			commandLine("bash", "-c", exeDir+exeFile, port)
        }
        else -> { throw GradleException(":cpp:Ipv6_SocketClient run-target -> unknown OS family encountered")}
    }
}

/*
// task to run cpp client
tasks.register("kill", Exec::class) {
    group = "application"                // set task group
    val port = 4949
    when {
        Os.isFamily(Os.FAMILY_WINDOWS) -> {
            commandLine("cmd", "/k", "for /f \"tokens=5\" %a in ('netstat -aon ^| findstr ${port}') do taskkill /F /PID %a")
        }
        Os.isFamily(Os.FAMILY_MAC) -> {
            commandLine("bash", "-c", "lsof -nti:${port} | xargs kill -9")
        }
        Os.isFamily(Os.FAMILY_UNIX) -> {
            commandLine("bash", "-c", "lsof -nti:${port} | xargs kill -9")
            // commandLine("sh", "-c", "kill \"\$(lsof -t -i:$port)\"") // possible alternative
        }
        else -> { throw GradleException(":cpp:Ipv6_SocketClient kill-target -> unknown OS family encountered")}
    }
}
*/
