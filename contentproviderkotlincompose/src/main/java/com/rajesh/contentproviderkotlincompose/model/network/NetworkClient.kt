package com.rajesh.contentproviderkotlincompose.model.network

import com.rajesh.contentproviderkotlincompose.model.security.EncryptionUtil
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.File

object NetworkClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }

    }

    suspend fun uploadMedia(file: File): HttpResponse {
        val encryptedData = EncryptionUtil.encryptFile(file)
        return client.post("https://your-free-server.com/upload") {
            setBody(MultiPartFormDataContent(
                formData {
                    append("media", encryptedData, Headers.build {
                        append(HttpHeaders.ContentType, "application/octet-stream")
                        append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                    })
                }
            ))
        }
    }
}
