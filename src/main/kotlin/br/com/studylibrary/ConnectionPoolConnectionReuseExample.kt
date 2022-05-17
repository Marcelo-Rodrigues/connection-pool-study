package br.com.studylibrary

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.time.Instant.now
import java.util.LinkedList
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

fun main() {
    val dataSource = HikariDataSource(HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        username = "postgres"
        password = "test_postgres"
    })
    val connections = LinkedList<Connection>()

    val timer = Timer()
    repeat(times = 10) { index ->
        val currentExecution = index + 1
        timer.schedule((currentExecution) * 500L) { // Agenda a abertura de uma conexão a cada meio segundo
            connections.add(dataSource.connection) // Adiciona a conexão na fila em memória
            printlnWithTime("Conexão $currentExecution aberta")
        }
        timer.schedule((currentExecution + 10) * 500L) { // Agenda o fechamento de uma conexão a cada meio segundo após todas aberturas
            connections.remove().close() // Remove a conexão da fila e fecha
            printlnWithTime("Conexão $currentExecution fechada")
        }
    }

    timer.schedule(11000) {
        dataSource.connection // Depois de abrir 10 conexões e fechá-las, abre uma nova conexão
        // O pool permanece com 10 conexões abertas durante o processamento inteiro
        exitProcess(status = 0) // Termina o programa, para não esperar a thread do timer
    }

}

private fun printlnWithTime(text: String) = println("[${now()}] $text")
