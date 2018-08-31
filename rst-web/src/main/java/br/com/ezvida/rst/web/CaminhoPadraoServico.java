package br.com.ezvida.rst.web;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.io.Serializable;

@ApplicationPath("/api")
public class CaminhoPadraoServico extends Application implements Serializable {

  private static final long serialVersionUID = -1945163039888240244L;

}
