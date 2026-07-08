package com.projeto.modelo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region:us-east-1}")
    private String region;

    @Value("${aws.s3.public-endpoint:}")
    private String publicEndpoint;

    public String uploadArquivo(MultipartFile arquivo, String path) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(path)
                    .contentType(arquivo.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(arquivo.getInputStream(), arquivo.getSize()));

            // Agora retornamos apenas o 'path' (chave) para o banco de dados.
            return path;
        } catch (IOException e) {
            log.error("Erro ao fazer upload do arquivo para S3", e);
            throw new RuntimeException("Falha ao enviar arquivo para armazenamento.", e);
        }
    }

    public void deletarArquivo(String path) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(path)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("Erro ao deletar arquivo do S3: {}", path, e);
            throw new RuntimeException("Falha ao excluir arquivo do armazenamento.", e);
        }
    }

    public String gerarUrlAssinada(String path) {
        // Se já for uma URL completa (arquivos legados), extrai a chave
        if (path != null && path.startsWith("http")) {
            try {
                java.net.URI uri = new java.net.URI(path);
                String uriPath = uri.getPath(); // Ex: "/tickets/123.png" ou "/devhub-prod/tickets/123.png"
                if (uriPath != null) {
                    if (uriPath.startsWith("/")) {
                        uriPath = uriPath.substring(1);
                    }
                    if (uriPath.startsWith(bucket + "/")) {
                        uriPath = uriPath.substring(bucket.length() + 1);
                    }
                    path = uriPath;
                }
            } catch (Exception e) {
                log.warn("Falha ao fazer parse da URL legada: {}", path);
            }
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // URL expira em 60 minutos
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toExternalForm();
    }
}
