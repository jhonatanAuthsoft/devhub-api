package com.projeto.modelo.service.imp;


import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.UsuarioStatus;
import com.projeto.modelo.repository.EmailService;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.util.TemplateUtils;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailServiceImp implements EmailService {

    @Value("${email.smtp.host}")
    private String smtpHost;

    @Value("${email.smtp.port}")
    private String smtpPort;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${email.smtp.auth}")
    private String smtpAuth;

    @Value("${email.smtp.starttls.enable}")
    private String starttlsEnable;

    private Properties mailProperties;

    private final UsuarioRepository usuarioRepository;

    private final Random random = new Random();


    private static String cadastraUsuario = "templates/cadastroUsuario.html";
    private static String esqueceuSenha = "templates/esqueceuSenha.html";

    public EmailServiceImp(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostConstruct
    public void init() {
        // Configurando as propriedades de e-mail
        mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", smtpAuth);
        mailProperties.put("mail.smtp.starttls.enable", starttlsEnable);
        mailProperties.put("mail.smtp.host", smtpHost);
        mailProperties.put("mail.smtp.port", smtpPort);
    }


    @Async
    @Override
    public void cadastraUsuario(String toEmail, String senha) {
        try {
            String corpoEmail = this.corpoCadastroUsuario(senha);
            this.enviaEmail(toEmail, corpoEmail, "NOME DO SISTEMA - Cadastro de usuário");
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail de Cadastro de usuário", e);
        }
    }

    @Override
    public void enviarEmailEsqueceuSenha(String toEmail) {
        try {
            int codigoVerificador = this.geraCodigoVerificador();
            Optional<Usuario> emailUsuario = this.usuarioRepository.findByEmailAndStatus(toEmail, UsuarioStatus.ATIVO);
            if (emailUsuario.isPresent()) {
                emailUsuario.get().setCodigoTrocaSenha(codigoVerificador);
                this.usuarioRepository.save(emailUsuario.get());
            }
            String corpoEmail = this.corpoEsqueceuSenha(codigoVerificador);
            this.enviaEmail(toEmail, corpoEmail, "SeuLarMS - Recuperação de senha");
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail de recuperação de senha", e);
        }
    }

    private String corpoEsqueceuSenha(int codigoVerificador) throws IOException {
        return TemplateUtils.htmlToString(esqueceuSenha).replace("#codigo#", String.valueOf(codigoVerificador));
    }

    private String corpoCadastroUsuario(String senha) throws IOException {
        return  TemplateUtils.htmlToString(cadastraUsuario)
                .replace("#senha#", String.valueOf(senha));
    }

    private void enviaEmail(String toEmail, String htmlContent, String titulo) {
        try {
            Session session = Session.getInstance(mailProperties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(titulo);

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");

            MimeBodyPart imagePart = new MimeBodyPart();
            ClassPathResource resource = new ClassPathResource("logo.png");

            try (InputStream inputStream = resource.getInputStream()) {
                DataSource dataSource = new ByteArrayDataSource(inputStream, "image/png");
                imagePart.setDataHandler(new DataHandler(dataSource));
                imagePart.setHeader("Content-ID", "<logoImage>");
                imagePart.setDisposition(Part.INLINE);
            }

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            Transport.send(message);

            log.info("E-mail enviado com sucesso para " + toEmail);
        } catch (Exception e) {
            log.error("[EmailServiceImp -> enviaEmail] - Erro ao enviar e-mail: ", e);
        }
    }

    private int geraCodigoVerificador() {
        return 1000 + this.random.nextInt(9000);
    }

}
