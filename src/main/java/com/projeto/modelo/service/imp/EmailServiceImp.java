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
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImp.class);

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
            this.enviarEmailHtml(toEmail, corpoEmail, "NOME DO SISTEMA - Cadastro de usuário");
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
            this.enviarEmailHtml(toEmail, corpoEmail, "SeuLarMS - Recuperação de senha");
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail de recuperação de senha", e);
        }
    }

    @Async
    @Override
    public void enviarAlertaHorasPendentes(String toEmail, String nomeColaborador, String nomeProjeto) {
        try {
            String corpoEmail = this.corpoAlertaHorasPendentes(nomeColaborador, nomeProjeto);
            this.enviarEmailHtml(toEmail, corpoEmail, "Aviso DevHub - Registro de Horas Pendente");
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail de alerta de horas pendentes", e);
        }
    }

    private String corpoEsqueceuSenha(int codigoVerificador) throws IOException {
        return TemplateUtils.htmlToString(esqueceuSenha).replace("#codigo#", String.valueOf(codigoVerificador));
    }

    private String corpoAlertaHorasPendentes(String nomeColaborador, String nomeProjeto) {
        return "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; color: #333;\">" +
               "<div style=\"background-color: #f8f9fa; padding: 20px; text-align: center; border-bottom: 3px solid #007bff;\">" +
               "<h2 style=\"color: #007bff; margin: 0;\">DevHub - Alerta de Horas</h2>" +
               "</div>" +
               "<div style=\"padding: 30px 20px;\">" +
               "<p style=\"font-size: 16px;\">Olá <strong>" + nomeColaborador + "</strong>,</p>" +
               "<p style=\"font-size: 16px; line-height: 1.5;\">Verificamos que não há registro de horas da sua parte no projeto <strong>" + nomeProjeto + "</strong> nos últimos dias.</p>" +
               "<p style=\"font-size: 16px; line-height: 1.5;\">Lembre-se de manter seus apontamentos atualizados na plataforma para o correto acompanhamento do projeto.</p>" +
               "<div style=\"text-align: center; margin: 30px 0;\">" +
               "<a href=\"http://localhost:3002/lancamento-horas\" style=\"background-color: #007bff; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;\">Registrar Horas Agora</a>" +
               "</div>" +
               "</div>" +
               "<div style=\"text-align: center; font-size: 12px; color: #777; padding-top: 20px; border-top: 1px solid #eee;\">" +
               "<p>Este é um e-mail automático enviado pelo sistema DevHub. Por favor, não responda.</p>" +
               "</div></div>";
    }

    private String corpoCadastroUsuario(String senha) throws IOException {
        return  TemplateUtils.htmlToString(cadastraUsuario)
                .replace("#senha#", String.valueOf(senha));
    }

    @Override
    public void enviarEmailHtml(String toEmail, String htmlContent, String titulo) {
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
