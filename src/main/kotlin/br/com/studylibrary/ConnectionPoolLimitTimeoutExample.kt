package br.com.studylibrary

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.time.Instant.now

fun main() {
    val dataSource = HikariDataSource(HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        username = "postgres"
        password = "test_postgres"
    })

    // Executa o getConnection() do HikariCP 10 vezes, consumindo todas as conexões
    repeat(times = 10) { dataSource.connection }

    printlnWithTime("Aguardando conexão")
    try {
        // A conexão ficará parada na linha abaixo aguardando uma conexão disponível no pool,
        // pois o limite padrão é de 10 conexões simultâneas, que já foram atingidas acima.
        // Após 30s é lançada uma Exception com a descrição:
        //   Exception in thread "main" java.sql.SQLTransientConnectionException:
        //   HikariPool-1 - Connection is not available, request timed out after 30000ms.
        dataSource.connection
    } finally {
        printlnWithTime("Fim da execução")
    }
}

private fun printlnWithTime(text: String) = println("[${now()}] $text")
