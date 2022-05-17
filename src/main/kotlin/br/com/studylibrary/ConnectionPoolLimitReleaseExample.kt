package br.com.studylibrary

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.time.Instant.now
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

fun main() {
    val dataSource = HikariDataSource(HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        username = "postgres"
        password = "test_postgres"
    })
    repeat(times = 9) { dataSource.connection }

    val lastConnection = dataSource.connection // Abre a última conexão e obtém a referência

    Timer().schedule(2000) { // Fecha a última conexão após 2s
        lastConnection.close()
        printlnWithTime("Conexão liberada após 2s")
        exitProcess(status = 0) // Termina o programa, para não esperar a thread do timer
    }

    printlnWithTime("Aguardando conexão")
    // A conexão ficará parada na linha abaixo até que a última execução seja liberada após 2s
    dataSource.connection
}

private fun printlnWithTime(text: String) = println("[${now()}] $text")
