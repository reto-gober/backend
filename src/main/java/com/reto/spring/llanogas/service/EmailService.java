package com.reto.spring.llanogas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmail(String destinatario, String asunto, String cuerpo) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);
            mensaje.setFrom("noreply@sistemareportes.com");
            
            mailSender.send(mensaje);
            log.info("Email enviado exitosamente a: {}", destinatario);
        } catch (Exception e) {
            log.error("Error al enviar email a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar email", e);
        }
    }
}
