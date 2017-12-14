package br.com.ezvida.rst.email;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.ArrayUtils;


public class EmailTemplate implements Serializable {

	private static final long serialVersionUID = -2887479874328998971L;

	private String assunto;

    private final String nome;

    private final InternetAddress remetente;

    private final Map<String, Object> parametros = Collections.synchronizedMap(new HashMap<String, Object>());

    private final Map<RecipientType, InternetAddress[]> destinatarios = Collections.synchronizedMap(new HashMap<RecipientType, InternetAddress[]>());

    public EmailTemplate(String nome, InternetAddress remetente) {
        this.nome = nome;
        this.remetente = remetente;
    }

    public EmailTemplate(String nome) {
       this(nome, null);
    }

    public EmailTemplate assunto(String assunto) {
        setAssunto(assunto);
        return this;
    }

    public EmailTemplate destinatario(InternetAddress destinatario) {
        return destinatario(destinatario, RecipientType.TO);
    }

    public EmailTemplate destinatario(InternetAddress destinatario, RecipientType type) {

        if (destinatarios.get(type) == null) {
            destinatarios.put(type, (InternetAddress[]) ArrayUtils.add(new InternetAddress[0], destinatario));
        } else {
            destinatarios.put(type, (InternetAddress[]) ArrayUtils.add(destinatarios.get(type), destinatario));
        }

        return this;

    }

    public EmailTemplate parametro(String key, Object value) {

        parametros.put(key, value);

        return this;

    }

    public InternetAddress getRemetente() {
        return remetente;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public Map<RecipientType, InternetAddress[]> getDestinatarios() {
        return destinatarios;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getNome() {
        return nome;
    }

}
