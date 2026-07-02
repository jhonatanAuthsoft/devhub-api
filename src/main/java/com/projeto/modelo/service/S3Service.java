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

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

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

            String originalUrl = s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(path)
                    .build()).toExternalForm();

            // Override URL se houver um public endpoint configurado (útil para MinIO local)
            if (publicEndpoint != null && !publicEndpoint.trim().isEmpty()) {
                return publicEndpoint + "/" + bucket + "/" + path;
            }

            return originalUrl;
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
}
