package br.com.ezvida.rst.web.util;

import java.io.Serializable;

public class Response<T> implements Serializable{

    private static final long serialVersionUID = -9089370876110351820L;

    private T content;

    public Response(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
