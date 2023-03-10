package org.example.application
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ServerKtTest {
    @Test
    fun addRepositoryUrl() = testApplication {
        application {
            myApplicationModule()
        }

        val response = client.post("/api/v1/repository/add") {
            setBody("""{"name": "atlassian-public","url": "packages.atlassian.com/mvn/maven-atlassian-external"}""")
            header("Content-Type", "application/json")
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val newRepo = repo.repositories.find { it.name == "atlassian-public" && it.url == "packages.atlassian.com/mvn/maven-atlassian-external" }
        val mapper = jacksonObjectMapper()
        val result = mapper.writeValueAsString(newRepo)

        assertEquals(result, response.bodyAsText())
    }

    @Test
    fun getRepositoriesUrls() = testApplication {
        application {
            myApplicationModule()
        }

        val response = client.get("/api/v1/repository/list"){}

        assertEquals(HttpStatusCode.OK, response.status)

        val mapper = jacksonObjectMapper()
        val result = mapper.writeValueAsString(repo)

        assertEquals(result, response.bodyAsText())
    }

    @Test
    fun deleteRepositoryUrl() = testApplication {
        application {
            myApplicationModule()
        }

        val response = client.post("/api/v1/repository/delete") {
            setBody("""{"id":1}""")
            header("Content-Type", "application/json")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"id":1}""", response.bodyAsText())
    }

    @Test
    fun getDependencyMetadata() = testApplication {
        application {
            myApplicationModule()
        }

        val response = client.get("/api/v1/dependency/metadata"){
            setBody("""{"group":"org.jetbrains.kotlin","artifact":"kotlin-reflect","version":"1.7.20"}""")
            header("Content-Type", "application/json")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"name":"Kotlin Reflect","url":"https://kotlinlang.org/","license":"The Apache License, Version 2.0"}""", response.bodyAsText())
    }

    @Test
    fun getDependencyVersions() = testApplication {
        application {
            myApplicationModule()
        }

        val response = client.get("/api/v1/dependency/versions"){
            setBody("""{"group":"org.jetbrains.kotlin","artifact":"kotlin-reflect"}""")
            header("Content-Type", "application/json")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"name":"kotlin-reflect","versions":["0.9.0-native","0.9.1-native","0.11.91","0.11.91.1","0.11.91.2","0.11.91.4","0.12.200","0.12.213","0.12.412","0.12.1218","0.12.1230","0.13.1513","0.13.1514","0.13.1516","0.14.449","0.14.451","1.0.0-beta-1038","1.0.0-beta-1103","1.0.0-beta-2417","1.0.0-beta-2422","1.0.0-beta-2423","1.0.0-beta-3593","1.0.0-beta-3594","1.0.0-beta-3595","1.0.0-beta-4583","1.0.0-beta-4584","1.0.0-beta-4589","1.0.0-rc-1036","1.0.0","1.0.1","1.0.1-1","1.0.1-2","1.0.2","1.0.2-1","1.0.3","1.0.4","1.0.5","1.0.5-2","1.0.5-3","1.0.6","1.0.7","1.1.0","1.1.1","1.1.2","1.1.2-2","1.1.2-3","1.1.2-4","1.1.2-5","1.1.3","1.1.3-2","1.1.4","1.1.4-2","1.1.4-3","1.1.50","1.1.51","1.1.60","1.1.61","1.2.0","1.2.10","1.2.20","1.2.21","1.2.30","1.2.31","1.2.40","1.2.41","1.2.50","1.2.51","1.2.60","1.2.61","1.2.70","1.2.71","1.3.0-rc-190","1.3.0-rc-198","1.3.0","1.3.10","1.3.11","1.3.20","1.3.21","1.3.30","1.3.31","1.3.40","1.3.41","1.3.50","1.3.60","1.3.61","1.3.70","1.3.71","1.3.72","1.4.0-rc","1.4.0","1.4.10","1.4.20-M1","1.4.20-M2","1.4.20-RC","1.4.20","1.4.21","1.4.21-2","1.4.30-M1","1.4.30-RC","1.4.30","1.4.31","1.4.32","1.5.0-M1","1.5.0-M2","1.5.0-RC","1.5.0","1.5.10","1.5.20-M1","1.5.20-RC","1.5.20","1.5.21","1.5.30-M1","1.5.30-RC","1.5.30","1.5.31","1.5.32","1.6.0-M1","1.6.0-RC","1.6.0-RC2","1.6.0","1.6.10-RC","1.6.10","1.6.20-M1","1.6.20-RC","1.6.20-RC2","1.6.20","1.6.21","1.7.0-Beta","1.7.0-RC","1.7.0-RC2","1.7.0","1.7.10","1.7.20-Beta","1.7.20-RC","1.7.20","1.7.21","1.7.22","1.8.0-Beta","1.8.0-RC","1.8.0-RC2","1.8.0","1.8.10","1.8.20-Beta"]}""", response.bodyAsText())

    }

}