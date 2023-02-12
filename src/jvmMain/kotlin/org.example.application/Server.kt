package org.example.application

import DependencyVersionsRequest
import DependencyVersionsResponse
import MetadataRequest
import MetadataResponse
import MetadataXml
import Project
import RepositoriesStore
import RepositoryId
import RepositoryRequest
import RepositoryInfo
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.net.URL

fun HTML.index() {
    body {
        script(src = "/static/HW14.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::myApplicationModule).start(wait = true)
}

var repo = RepositoriesStore(mutableListOf())

fun Application.myApplicationModule() {

    val repoMavenApache = RepositoryInfo(0, "repo.maven.apache.org", "https://repo.maven.apache.org/maven2")
    val repoMavenGoogle = RepositoryInfo(1, "dl.google.com", "https://dl.google.com/dl/android/maven2")
    repo.repositories.add(repoMavenApache)
    repo.repositories.add(repoMavenGoogle)

    install(CallLogging)
    install(ContentNegotiation) {
        jackson()
    }

    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }

        addRepositoryUrl()

        getRepositoriesUrls()

        deleteRepositoryUrl()

        getDependencyMetadata()

        getDependencyVersions()

        static("/static") {
            resources()
        }
    }
}

fun Routing.addRepositoryUrl(){
    post("/api/v1/repository/add") {
        val request = call.receive<RepositoryRequest>()
        val response = RepositoryInfo(repo.repositories.size, request.name, request.url)
        repo.repositories.add(response)

        call.respond(response)
    }
}

fun Routing.getRepositoriesUrls(){
    get("/api/v1/repository/list") {
        call.respond(repo)
    }
}

fun Routing.deleteRepositoryUrl(){
    post("/api/v1/repository/delete") {
        val request = call.receive<RepositoryId>()
        repo.repositories.removeAt(request.id)
        call.respond(request)
    }
}

fun Routing.getDependencyMetadata(){
    get("/api/v1/dependency/metadata") {
        val request = call.receive<MetadataRequest>()
        val group = request.group
        val artifact = request.artifact
        val version = request.version

        val pomUrl = "https://repo.maven.apache.org/maven2/${group.replace('.', '/')}/$artifact/$version/$artifact-$version.pom"

        var stringToParse = URL(pomUrl).readText()

        val xmlDeserializer = XmlMapper(JacksonXmlModule().apply {
            setDefaultUseWrapper(false)
        }).registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        if (stringToParse.startsWith("<?xml")) {
            stringToParse = stringToParse.substring(stringToParse.indexOf("?>") + 2);
        }

        val project = xmlDeserializer.readValue(stringToParse, Project::class.java)
        val name = project.name
        val url = project.url
        val license = project.licenses.license[0].name

        val response = MetadataResponse(name, url, license)
        call.respond(response)
    }
}

fun Routing.getDependencyVersions(){
    get("/api/v1/dependency/versions") {
        val request = call.receive<DependencyVersionsRequest>()
        val group = request.group
        val artifact = request.artifact

        val metadataUrl = "https://repo.maven.apache.org/maven2/${group.replace('.', '/')}/$artifact/maven-metadata.xml"

        val stringToParse = URL(metadataUrl).readText()

        val xmlDeserializer = XmlMapper(JacksonXmlModule().apply {
            setDefaultUseWrapper(false)
        }).registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val metadata = xmlDeserializer.readValue(stringToParse, MetadataXml::class.java)
        val name = metadata.artifactId
        val versions = metadata.versioning.versions.version

        val response = DependencyVersionsResponse(name, versions)
        call.respond(response)
    }
}